package lazyfox16.shift.sort;

import lazyfox16.shift.enums.Order;
import lazyfox16.shift.enums.Type;
import lazyfox16.shift.helper.FileBuffer;
import lazyfox16.shift.utils.FileUtils;
import lazyfox16.shift.utils.StringSizeUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ExternalMergeSorter<T extends Comparable<? super T>> implements Sorter {

    // максимум байт, доступных для выгрузки в память с файла
    private static final int MAX_CHUNK_SIZE = 1024;

    private final Type type;
    private final String outputFileName;
    private final List<String> inputFileNames;
    private final Comparator<T> comparator;
    private final Comparator<FileBuffer<T>> bufferComparator;

    public ExternalMergeSorter(Order order, Type type, List<String> fileNames) {
        this.comparator = order.getComparator();
        this.bufferComparator = order.getComparator();
        this.outputFileName = fileNames.remove(0);
        this.inputFileNames = fileNames;
        this.type = type;
    }

    public void handle() {
        List<File> files = new ArrayList<>();
        File outputFile = new File(outputFileName);

        try {
            for (String fileName : inputFileNames) {

                try (
                        InputStream is = FileUtils.getFile(fileName);
                        InputStreamReader isr = new InputStreamReader(is);
                        BufferedReader br = new BufferedReader(isr)
                ) {

                    List<T> tmplist = new ArrayList<>();
                    String line = "";

                    while (line != null) {
                        int currentChunksize = 0;
                        while ((currentChunksize < MAX_CHUNK_SIZE) && ((line = br.readLine()) != null)) {
                            if (!line.isBlank()) {
                                T converted = type.convert(line);
                                if (converted != null) {
                                    tmplist.add(type.convert(line));
                                    currentChunksize += StringSizeUtils.getSize(line);
                                }
                            }
                        }

                        sortAndSave(files, tmplist);
                    }
                }
            }

            merge(files, outputFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void sortAndSave(List<File> files, List<T> tmplist) throws IOException {
        MergeSorter.sort(tmplist, comparator);
        File tmpFile = save(tmplist);
        files.add(tmpFile);
        tmplist.clear();
    }

    private File save(List<T> sortedList) throws IOException {

        File newtmpfile = File.createTempFile("inputChunk", null);
        newtmpfile.deleteOnExit();

        try (
                FileOutputStream fos = new FileOutputStream(newtmpfile);
                OutputStreamWriter osw = new OutputStreamWriter(fos);
                BufferedWriter bw = new BufferedWriter(osw)
        ) {

            for (T elem : sortedList) {
                bw.write(elem.toString());
                bw.newLine();
            }

        }

        return newtmpfile;
    }

    private void merge(List<File> files, File outputfile) throws IOException {
        List<FileBuffer<T>> buffers = new ArrayList<>();

        try (
                FileOutputStream fos = new FileOutputStream(outputfile);
                OutputStreamWriter osw = new OutputStreamWriter(fos);
                BufferedWriter bw = new BufferedWriter(osw)
        ) {

            for (File file : files) {
                InputStream is = new FileInputStream(file);
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                FileBuffer<T> buffer = new FileBuffer<>(br, type);

                if (buffer.isNotEmpty()) {
                    addAndSort(buffers, buffer);
                }
            }

            while (buffers.size() > 0) {

                // Вытаскиваем мин/макс элемент из файла
                FileBuffer<T> buffer = buffers.remove(0);
                bw.write(buffer.next().toString());
                bw.newLine();

                if (buffer.isNotEmpty()) {
                    addAndSort(buffers, buffer);
                } else {
                    buffer.close();
                }
            }

        } finally {
            for (FileBuffer<T> buffer : buffers) {
                buffer.close();
            }

            for (File file : files) {
                file.delete();
            }
        }
    }

    private void addAndSort(List<FileBuffer<T>> buffers, FileBuffer<T> buffer) {
        buffers.add(buffer);
        MergeSorter.sort(buffers, bufferComparator);
    }
}

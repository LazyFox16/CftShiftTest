package lazyfox16.shift.helper;

import lazyfox16.shift.enums.Type;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;

public class FileBuffer<T extends Comparable<? super T>> implements Comparable<FileBuffer<T>>, Closeable {

    private final BufferedReader reader;
    private final Type type;
    private T current;

    public FileBuffer(BufferedReader reader, Type type) throws IOException {
        this.reader = reader;
        this.type = type;
        this.current = type.convert(reader.readLine());
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }

    public T next() throws IOException {
        T prev = current;
        current = type.convert(reader.readLine());
        return prev;
    }

    public boolean isNotEmpty() {
        return current != null;
    }

    @Override
    public int compareTo(FileBuffer<T> o) {
        return current.compareTo(o.current);
    }
}

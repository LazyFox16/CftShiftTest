package lazyfox16.shift.utils;

import java.io.InputStream;

public class FileUtils {

    private FileUtils() {}

    public static InputStream getFile(String fileName) {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);

        if (is == null) {
            throw new IllegalArgumentException(String.format("File %s not found!", fileName));
        }

        return is;
    }
}

package lazyfox16.shift.init;

import lazyfox16.shift.enums.Order;
import lazyfox16.shift.enums.Type;
import lazyfox16.shift.sort.ExternalMergeSorter;
import lazyfox16.shift.sort.Sorter;

import java.util.ArrayList;
import java.util.List;

public class Initializer {

    // 1 - режим сортировки (-a, -d), опционально
    // 2 - тип данных (-s, -l)
    // 3 - имя выходного файла
    // 4 - имена входных файлов
    public Sorter init(String[] args) {

        Order order = null;
        Type type = null;
        List<String> fileNames = new ArrayList<>();

        for (String arg : args) {
            if (arg == null || arg.isBlank()) {
                throw new IllegalArgumentException("Missing keys");
            }

            if (arg.equals("-a") || arg.equals("-d")) {
                order = Order.of(arg);
            } else if (arg.equals("-s") || arg.equals("-i")) {
                type = Type.of(arg);
            } else {
                fileNames.add(arg);
            }
        }

        checkArgs(type, fileNames);

        // если не было задано, то устанавливаем порядок сортировки по умолчанию
        if (order == null) {
            order = Order.ASC;
        }

        return new ExternalMergeSorter<>(order, type, fileNames);
    }

    private void checkArgs(Type type, List<String> fileNames) {
        if (type == null) {
            throw new IllegalArgumentException("Missing type key");
        }

        if (fileNames == null || fileNames.size() < 2) {
            throw new IllegalArgumentException("Missing file names");
        }
    }
}

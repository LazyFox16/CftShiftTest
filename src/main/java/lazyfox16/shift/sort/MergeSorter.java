package lazyfox16.shift.sort;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MergeSorter {

    public static <T extends Comparable<? super T>> void sort(List<T> input, Comparator<T> comparator) {
        if (input == null) {
            throw new IllegalArgumentException("Input array for sort is null!");
        }

        int lenght = input.size();
        if (lenght < 2) {
            return;
        }

        List<T> aux = new ArrayList<>(input);
        sort(aux, input, 0, lenght - 1, comparator);
    }

// с помощью рекурсии делим и сортируем сначала левую,затем правую части и объединяем
    private static <T extends Comparable<? super T>> void sort(List<T> src, List<T> dest,
                                                               int low, int high, Comparator<T> comparator) {
        if (low >= high) {
            return;
        }

        int mid = (low + high) / 2;
        sort(dest, src, low, mid, comparator);
        sort(dest, src, mid + 1, high, comparator);
        merge(src, dest, low, mid, high, comparator);
    }

    private static <T extends Comparable<? super T>> void merge(List<T> src, List<T> dest,
                                                                int low, int mid, int high, Comparator<T> comparator) {
        for (int i = low, j = low, k = mid + 1; i <= high; i++) {
            dest.set(i, src.get(k <= high && (j > mid || comparator.compare(src.get(j), src.get(k)) > 0) ? k++ : j++));
        }
    }
}

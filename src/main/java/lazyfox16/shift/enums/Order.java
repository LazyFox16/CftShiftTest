package lazyfox16.shift.enums;

import java.util.Comparator;

public enum Order {

    ASC("-a") {
        @Override
        public <T extends Comparable<? super T>> Comparator<T> getComparator() {
            return Comparator.naturalOrder();
        }
    },
    DESC("-d") {
        @Override
        public <T extends Comparable<? super T>> Comparator<T> getComparator() {
            return Comparator.reverseOrder();
        }
    };

    private final String key;


    Order(String key) {
        this.key = key;
    }

    public static Order of(String key) {
        for (Order value : values()) {
            if (value.key.equals(key)) {
                return value;
            }
        }

        throw new IllegalArgumentException("Unsupported key for order " + key);
    }

    public abstract <T extends Comparable<? super T>> Comparator<T> getComparator();
}

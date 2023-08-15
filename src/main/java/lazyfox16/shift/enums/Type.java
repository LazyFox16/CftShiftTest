package lazyfox16.shift.enums;

public enum Type {

    INTEGER("-i") {
        @Override
        @SuppressWarnings("unchecked")
        public <T> T convert(String str) {
            try {
                return str == null ? null : (T) Integer.valueOf(str);
            } catch (NumberFormatException e) {
                System.err.printf("Can`t convert %s to int type%n", str);
                return null;
            }
        }
    },
    STRING("-s") {
        @Override
        @SuppressWarnings("unchecked")
        public <T> T convert(String str) {
            return (T) str;
        }
    };

    private final String key;

    Type(String key) {
        this.key = key;
    }

    public static Type of(String key) {
        for (Type value : values()) {
            if (value.key.equals(key)) {
                return value;
            }
        }

        throw new IllegalArgumentException("Unsupported key for type " + key);
    }

    public abstract <T> T convert(String str);
}

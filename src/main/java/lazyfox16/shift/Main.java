package lazyfox16.shift;

import lazyfox16.shift.init.Initializer;
import lazyfox16.shift.sort.Sorter;

public class Main {

    public static void main(String[] args) {
        Initializer initializer = new Initializer();
        Sorter sorter = initializer.init(args);
        sorter.handle();
    }
}

package carsharing;

import java.util.List;

class Printer {

    public static void printMenu(List<?> menu) {

        for (int i = 1; i < menu.size(); i++) {
            System.out.println(i + ". " + menu.get(i));
        }

        System.out.println("0. " + menu.getFirst());
    }
}

package carsharing;

import java.util.List;

class Validator {

    static int getValidMenuOption(String menuOption, List<?> menu) {
        int loginMenuNumber = 0;
        try {
            loginMenuNumber = Integer.parseInt(menuOption);
            if (loginMenuNumber < -1 || loginMenuNumber > menu.size()) {
                System.out.println("Selection is out of bounds.");
                return 0;
            }
        } catch (NumberFormatException n) {
            System.out.println("Must be a numeral.");
        }

        return loginMenuNumber;
    }
}

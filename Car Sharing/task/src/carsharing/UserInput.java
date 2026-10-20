package carsharing;

import java.util.List;
import java.util.Scanner;

class UserInput {

    private final Scanner in = new Scanner(System.in);

    public int getLoginIdentity(List<String> loginMenu) {
        String loginMenuNumber = in.nextLine();
        System.out.println();
        return Validator.getValidMenuOption(loginMenuNumber, loginMenu);
    }

    public int getMenuNum(List<?> menu) {
        String menuNum = in.nextLine();
        System.out.println();
        return Validator.getValidMenuOption(menuNum, menu);
    }

    public String getNewName() {
        return in.nextLine();
    }

    public void closeConnection() {
        in.close();
    }
}

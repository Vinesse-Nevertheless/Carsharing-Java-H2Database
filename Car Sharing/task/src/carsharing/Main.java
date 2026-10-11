package carsharing;

import java.util.List;
import java.util.Scanner;

public class Main {

    static CompanyDOA companyDOAImpl;

    public static void main(String[] args) {

        DbConfig.connectToDb(args);

        DbClient dbClient = new DbClient(DbConfig.DB_URL);
        dbClient.createTable();

        companyDOAImpl = new CompanyDOAImpl(dbClient);

        UserInput userInput = new UserInput();
        init(userInput);
        userInput.closeConnection();
    }

    static void init(UserInput userInput) {

        List<String> loginMenu = List.of("Exit",
                "Log in as a manager");

        List<String> managerOnlyMenu = List.of("Back",
                "Company list",
                "Create a company");

        int loginIdentity = -1;

        while (true) {
            Printer.printMenu(loginMenu);
            loginIdentity = userInput.getLoginIdentity(loginMenu);

            if (loginIdentity == 0) {
                break;
            }

            while (loginIdentity == 1) {
                Printer.printMenu(managerOnlyMenu);
                int managerChoice = userInput.getManagerOnlyOptions(managerOnlyMenu);
                switch (managerChoice) {
                    case 1 -> {
                        if (companyDOAImpl.findAll() == null || companyDOAImpl.findAll().isEmpty()) {
                            System.out.println("The company list is empty!");
                            System.out.println();
                        } else {
                            System.out.println("Company list:");
                            companyDOAImpl.findAll().forEach(company -> System.out.println(company.getId() + ". " + company.getName()));
                            System.out.println();
                        }
                    }
                    case 2 -> {
                        String name = userInput.getNewName();
                        if (companyDOAImpl.add(new Company(name))) {
                            System.out.println("The company was created!");
                            System.out.println();
                        }
                    }
                    case 0 -> loginIdentity = -1;
                }
            }
        }
    }
}

class UserInput {

    private final Scanner in;

    public UserInput() {
        in = new Scanner(System.in);
    }

    public int getLoginIdentity(List<String> loginMenu) {
        String loginMenuNumber = in.nextLine();
        System.out.println();
        return Validator.getValidMenuOption(loginMenuNumber, loginMenu);
    }

    public int getManagerOnlyOptions(List<String> managerOnlyMenu) {
        String managerOnlyNumber = in.nextLine();
        System.out.println();
        return Validator.getValidMenuOption(managerOnlyNumber, managerOnlyMenu);
    }

    public String getNewName() {
        System.out.println("Enter company name:");
        return in.nextLine();
    }

    public void closeConnection() {
        in.close();
    }

}

class Printer {

    public static void printMenu(List<String> menu) {

        for (int i = 1; i < menu.size(); i++) {
            System.out.println(i + ". " + menu.get(i));
        }

        System.out.println("0. " + menu.getFirst());
    }
}


class Validator {

    static int getValidMenuOption(String menuOption, List<String> menu) {
        int loginMenuNumber = 0;
        try {
            loginMenuNumber = Integer.parseInt(menuOption);
            if (loginMenuNumber < -1 || loginMenuNumber >= menu.size()) {
                return 0;
            }
        } catch (NumberFormatException n) {
            System.out.println("Must be a numeral.");
        }

        return loginMenuNumber;
    }

}
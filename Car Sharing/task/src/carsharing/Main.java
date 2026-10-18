package carsharing;

import java.util.List;
import java.util.Scanner;

public class Main {
    static DbClient dbClient;
    static CompanyDAOImpl companyDAOImpl;

    public static void main(String[] args) {

        DbConfig.connectToDb(args);

        dbClient = new DbClient(DbConfig.DB_URL);
        dbClient.createCompanyTable();

        companyDAOImpl = new CompanyDAOImpl(dbClient);

        init();
    }

    static void init() {

        UserInput userInput = new UserInput();
        CompanyActions manager = new CompanyActions(companyDAOImpl);

        List<String> loginMenu = List.of("Exit",
                "Log in as a manager");

        while (true) {
            Printer.printMenu(loginMenu);
            int loginIdentity = userInput.getLoginIdentity(loginMenu);

            if (loginIdentity == 0) {
                break;
            }

            if (loginIdentity == 1) {
                manager.executeCompanyTasks();
            }
        }
        userInput.closeConnection();
    }
}


class CompanyActions {
    List<String> companyMenu = List.of("Back",
            "Company list",
            "Create a company");

    UserInput userInput = new UserInput();
    CompanyDAOImpl companyDAOImpl;
    CarActions manager;

    public CompanyActions(CompanyDAOImpl companyDAOimpl) {
        this.companyDAOImpl = companyDAOimpl;
        manager = new CarActions(companyDAOImpl.getDbClient());
    }

    void executeCompanyTasks() {
        while (true) {
            Printer.printMenu(companyMenu);
            int companyChoice = userInput.getMenuNum(companyMenu);
            switch (companyChoice) {
                case 1 -> {
                    List<Company> companies = companyDAOImpl.findAll();
                    if (companies.isEmpty()) {
                        System.out.println("The company list is empty!\n");
                    } else {
                        System.out.println("Choose the company:");
                        companies.forEach(company -> System.out.println(company.getId() + ". " + company.getName()));
                        System.out.println("0. Back");

                        int companyNum = userInput.getMenuNum(companies);
                        if (companyNum == 0) {
                            continue;
                        }
                        System.out.println("'" + companies.get(companyNum - 1).getName() + "' " + "company");
                        manager.executeCarTasks(companyNum);
                    }
                }
                case 2 -> {
                    System.out.println("Enter the company name:");
                    String name = userInput.getNewName();
                    if (companyDAOImpl.add(new Company(name))) {
                        System.out.println("The company was created!\n");
                    }
                }
                case 0 -> {
                    return;
                }
            }
        }
    }
}

class CarActions {

    List<String> carMenu = List.of("Back",
            "Car list",
            "Create a car");

    UserInput userInput = new UserInput();
    CarDAO carDAOImpl;

    public CarActions(DbClient dbClient) {
        dbClient.createCarTable();
        carDAOImpl = new CarDAOImpl(dbClient);
    }

    void executeCarTasks(int companyId) {

        while (true) {
            Printer.printMenu(carMenu);
            int carChoice = userInput.getMenuNum(carMenu);
            switch (carChoice) {
                case 1 -> {
                    List<Car> carsAtCompany = carDAOImpl.findCarsPerCompany(companyId);
                    if (carsAtCompany.isEmpty()) {
                        System.out.println("The car list is empty!\n");
                    } else {
                        System.out.println("Car list:");
                        for (int i = 0; i < carsAtCompany.size(); i++) {
                            System.out.println((i + 1) + ". " + carsAtCompany.get(i).getName());
                        }
                        System.out.println();
                    }
                }
                case 2 -> {
                    System.out.println("Enter the car name:");
                    String name = userInput.getNewName();
                    if (carDAOImpl.addCarToCompany(new Car(companyId, name))) {
                        System.out.println("The car was added!\n");
                    }
                }
                case 0 -> {
                    return;
                }
            }
        }
    }
}

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

class Printer {

    public static void printMenu(List<?> menu) {

        for (int i = 1; i < menu.size(); i++) {
            System.out.println(i + ". " + menu.get(i));
        }

        System.out.println("0. " + menu.getFirst());
    }
}


class Validator {

    static int getValidMenuOption(String menuOption, List<?> menu) {
        int loginMenuNumber = 0;
        try {
            loginMenuNumber = Integer.parseInt(menuOption);
            if (loginMenuNumber < -1 || loginMenuNumber > menu.size()) {
                return 0;
            }
        } catch (NumberFormatException n) {
            System.out.println("Must be a numeral.");
        }

        return loginMenuNumber;
    }
}
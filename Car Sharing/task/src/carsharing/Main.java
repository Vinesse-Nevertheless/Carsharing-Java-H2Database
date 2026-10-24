package carsharing;

import java.util.List;

public class Main {
    static DbClient dbClient;
    static CompanyDAOImpl companyDAOImpl;

    public static void main(String[] args) {

        DbConfig.connectToDb(args);

        DbClient.setInstance(DbConfig.DB_URL);

        dbClient = DbClient.getDbClientInstance();
        dbClient.createCompanyTable();

        companyDAOImpl = new CompanyDAOImpl(dbClient);

        init();
    }

    static void init() {
        UserInput userInput = new UserInput();
        CompanyActions companyActions = new CompanyActions();
        CustomerActions customerActions = new CustomerActions();

        List<String> loginMenu = List.of("Exit",
                "Log in as a manager",
                "Log in as a customer",
                "Create a customer");

        while (true) {
            Printer.printMenu(loginMenu);
            int loginIdentity = userInput.getLoginIdentity(loginMenu);

            switch (loginIdentity) {
                case 0 -> {
                    userInput.closeConnection();
                    return;
                }
                case 1 -> companyActions.executeCompanyTasks();
                case 2 -> customerActions.executeRentalCustomerActions();
                case 3 -> customerActions.createRentalCustomer();
            }
        }
    }
}




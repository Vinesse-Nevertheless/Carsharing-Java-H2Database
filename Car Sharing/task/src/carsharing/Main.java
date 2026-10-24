package carsharing;

import java.util.List;

public class Main {
    static DbClient dbClient;
    static CompanyDAOImpl companyDAOImpl;
  //  static CustomerDAOImpl customerDAOImpl;

    public static void main(String[] args) {

        DbConfig.connectToDb(args);

        DbClient.setInstance(DbConfig.DB_URL);

        dbClient = DbClient.getDbClientInstance();
        dbClient.createCompanyTable();

        companyDAOImpl = new CompanyDAOImpl(dbClient);
   //     customerDAOImpl = new CustomerDAOImpl(dbClient);

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

    /*static void init() {

        UserInput userInput = new UserInput();
        CompanyActions manager = new CompanyActions(companyDAOImpl);
        CustomerActions customerActions = new CustomerActions(customerDAOImpl);

        List<String> loginMenu = List.of("Exit",
                "Log in as a manager",
                "Log in as a customer",
                "Create a customer");

        while (true) {
            Printer.printMenu(loginMenu);
            int loginIdentity = userInput.getLoginIdentity(loginMenu);

            switch (loginIdentity) {
                case 0 -> {
                    return;
                }
                case 1 -> manager.executeCompanyTasks();
                case 2 -> customerActions.executeCustomerTasks();
                case 3 -> customerActions.createCustomer();
            }
        *//*    if (loginIdentity == 0) {
                break;
            }

            if (loginIdentity == 1) {
                manager.executeCompanyTasks();
            }*//*
            //  }
            userInput.closeConnection();
        }
    }
}*/



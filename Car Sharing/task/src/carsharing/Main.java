package carsharing;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        connectToDatabase(args);
        initProgram();
    }

    static void connectToDatabase(String[] args){
        DbConfig.connectToDb(args);
        DbClient.setInstance(DbConfig.DB_URL);
    }

    static void initProgram() {
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




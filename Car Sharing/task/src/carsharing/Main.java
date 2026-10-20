package carsharing;

import java.util.List;

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



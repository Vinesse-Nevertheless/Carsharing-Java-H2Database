package carsharing;

import java.util.List;

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

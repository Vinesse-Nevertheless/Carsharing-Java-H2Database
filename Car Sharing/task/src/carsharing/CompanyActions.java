package carsharing;

import java.util.List;

class CompanyActions {
    List<String> companyMenu = List.of("Back",
            "Company list",
            "Create a company");

    UserInput userInput = new UserInput();
    CompanyDAOImpl companyDAOImpl;
    CarActions carActions;
    List<Company> companies;

    public CompanyActions() {
        companyDAOImpl = new CompanyDAOImpl(DbClient.getDbClientInstance());
        carActions = new CarActions();
    }

    void createCompany() {
        System.out.println("Enter the company name:");
        String name = userInput.getNewName();
        if (companyDAOImpl.add(new Company(name))) {
            System.out.println("The company was created!\n");
        }
    }


    List<Company> getCompanyList() {
        companies = companyDAOImpl.findAll();
        if (companies.isEmpty()) {
            System.out.println("The company list is empty!\n");
        }
        return companies;
    }

    int getCompanyChoiceFromMenu(List<Company> companies) {
        companies.forEach(company -> System.out.println(company.getId() + ". " + company.getName()));
        System.out.println("0. Back");

        return userInput.getMenuNum(companies);
    }

    void chooseFromCompanyList() {
        List<Company> companyList = getCompanyList();
        if (!companyList.isEmpty()) {
            System.out.println("Company list:");
            int companyNum = getCompanyChoiceFromMenu(companyList);
            if (companyNum == 0) {
                return;
            }
            System.out.println("'" + companies.get(companyNum - 1).getName() + "' " + "company");
            carActions.executeCarTasks(companyNum);
        }
    }

    void executeCompanyTasks() {
        while (true) {
            Printer.printMenu(companyMenu);
            int companyChoice = userInput.getMenuNum(companyMenu);
            switch (companyChoice) {
                case 1 -> chooseFromCompanyList();
                case 2 ->  createCompany();
                case 0 -> {
                    return;
                }
            }
        }
    }
}

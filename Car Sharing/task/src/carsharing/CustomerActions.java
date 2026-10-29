package carsharing;

import java.util.List;
import java.util.Optional;

class CustomerActions {
    List<String> rentCarMenu = List.of("Back",
            "Rent a car",
            "Return a rented car",
            "My rented car");

    UserInput userInput = new UserInput();
    CustomerDAOImpl customerDAOimpl;
    CompanyActions companyActions = new CompanyActions();
    CarActions carActions = new CarActions();
    List<Customer> customers;

    DbClient dbClient = DbClient.getDbClientInstance();
    {
        dbClient.createCustomerTable();
    }

    public CustomerActions() {

        this.customerDAOimpl = new CustomerDAOImpl(dbClient);
    }

    void showCustomerList() {
        customers = customerDAOimpl.findAll();
        if (customers.isEmpty()) {
            System.out.println("The customer list is empty!\n");
        } else {
            System.out.println("Customer list:");
            for (int i = 0; i < customers.size(); i++) {
                System.out.println((i + 1) + ". " + customers.get(i).getName());
            }
            System.out.println("0. Back");
        }
    }

    void createRentalCustomer() {
        System.out.println("Enter the customer name:");
        String name = userInput.getNewName();
        if (customerDAOimpl.addCustomer(new Customer(name))) {
            System.out.println("The customer was added!\n");
        }
    }

    void rentCarToCustomer(int customerId) {
        Car carCustomerRented = customerDAOimpl.getCarCustomerRented(customerId);

        if (carCustomerRented != null){
            System.out.println("You've already rented a car!\n");
            return;
        }

        Customer customer = customerDAOimpl.findAll().stream()
                .filter(c -> c.getId() == customerId)
                .toList().getFirst();

        if ( customer != null && customer.getRented_car_id() > 0) {
            System.out.println("You've already rented a car!\n");
            return;
        }

        List<Company> companies = companyActions.getCompanyList();

        System.out.println("Choose a company:");
        int companyId = companyActions.getCompanyChoiceFromMenu(companies);
        if (companyId == 0) {
            return;
        }

        //if there are cars at the company
        List<Car> carsAtCompany = carActions.getCarList(companyId);

        if (carsAtCompany.isEmpty()) {
            System.out.println("There are no cars to rent.");
            return;
        }

        System.out.println("Choose a car:");
        carActions.printCarsFromCompany(carsAtCompany);
        System.out.println("0. Back");

        int carId = userInput.getMenuNum(carsAtCompany);
        if (carId == 0) {
            return;
        }

       Car car = carsAtCompany.get(carId -1 );

        //rented car id is not properly set during renting
        //rented car id == car id
        if (customerDAOimpl.rentCarToCustomer(carId, customerId)) {
            System.out.println("You rented a " + "'" + car.getName() + "'\n");

            List<Customer> all = customerDAOimpl.findAll();

            for (int i = 0; i < all.size(); i++) {
                System.out.println(all.get(i).getId());
                System.out.println(all.get(i).getName());
                System.out.println(all.get(i).getRented_car_id());

            }
        }

    }

    void returnRentedCar(int customerId) {
        if (customerDAOimpl.getCarCustomerRented(customerId) == null){
            System.out.println("You didn't rent a car.\n");
        }

        if (customerDAOimpl.returnCarCustomerRented(customerId)) {
                System.out.println("You've returned a rented car!\n");
            }

    }

    void showCarCustomerRented(int customerId) {
       // System.out.println(customer.getName());
        //rented car id is not properly set during renting
       // System.out.println(customer.getRented_car_id());

        Customer customer = customerDAOimpl.findAll().stream()
                .filter(c -> c.getId() == customerId)
                .toList().getFirst();

        if ( customer != null && customer.getRented_car_id() > 0) {
            Car carCustomerRented = customerDAOimpl.getCarCustomerRented(customerId);
            System.out.println("Your rented car:\n" +
                    carCustomerRented.getName() + "\n" +
                    "Company:\n" +
                    companyActions.companyDAOImpl.findAll().get(carCustomerRented.getCompanyId() - 1).getName() +"\n");
        } else {
            System.out.println("You didn't rent a car!\n");
        }

    }

    void executeRentalCustomerActions() {
        showCustomerList();
        if (customers.isEmpty()) {
            return;
        }

        int customerId = userInput.getMenuNum(customers);
        if (customerId == 0) {
            return;
        }

        while (true) {
            Printer.printMenu(rentCarMenu);
            int customerRentChoice = userInput.getMenuNum(rentCarMenu);
            switch (customerRentChoice) {
                case 1 -> rentCarToCustomer(customerId);
                case 2 -> returnRentedCar(customerId);
                case 3 -> showCarCustomerRented(customerId);
                case 0 -> {
                    return;
                }
            }
        }
    }

  /*  int getCompanyChoice() {
        List<Company> companyList = companyActions.getCompanyList();
        int companyNum = 0;
        if (!companyList.isEmpty()) {
            System.out.println("Choose a company:");
            companyNum = companyActions.getCompanyChoiceFromMenu();
        }
        return companyNum;
    }

        void chooseCarFromCompany(int companyNum){
            System.out.println("Choose a car:");
            List<Car> carList = carActions.getCarList(companyNum);
            carActions.printCarsFromCompany(carList);
            int menuNum = userInput.getMenuNum(carList);
            if (menuNum == 0) {
                return;
            }

            System.out.println("You rented '" + carList.get(menuNum -1) + "'");

        }


    void rentCar(Customer customer){
        if (customerDAOimpl.getCarCustomerRented(customer) == null) {
           int companyNum = getCompanyChoice();
            if (companyNum == 0) {
                return;
            }
            chooseCarFromCompany(companyNum);

        } else {
            System.out.println("You've already rented a car!");
        }

    }*/
}

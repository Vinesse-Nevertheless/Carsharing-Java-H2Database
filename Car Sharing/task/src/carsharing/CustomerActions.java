package carsharing;

import java.util.List;

class CustomerActions {
    private List<String> rentCarMenu = List.of("Back",
            "Rent a car",
            "Return a rented car",
            "My rented car");

    private UserInput userInput = new UserInput();
    private CompanyActions companyActions;
    private CarActions carActions;
    private List<Customer> customers;
    private CustomerDAOImpl customerDAOimpl;

    DbClient dbClient;

    public CustomerActions() {
        dbClient = DbClient.getDbClientInstance();
        dbClient.createCustomerTable();
        this.customerDAOimpl = new CustomerDAOImpl(dbClient);

        companyActions = new CompanyActions();
        carActions = new CarActions();
    }

    public CustomerDAOImpl getCustomerDAOimpl() {
        return customerDAOimpl;
    }

    void showCustomerList() {
        customers = customerDAOimpl.findAll();
        if (customers.isEmpty()) {
            System.out.println("The customer list is empty!\n");
        } else {
            System.out.println("Customer list:");
            customers.forEach(customer -> System.out.println(customer.getId() + ". " + customer.getName()));
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

    int getCompanyChoice() {
        List<Company> companies = companyActions.getCompanyList();

        //get company choice from companyActions
        System.out.println("Choose a company:");
        return companyActions.getCompanyChoiceFromMenu(companies);
    }

    int getCarChoice(List<Car> carsAtCompany) {
        //if there are no cars at the company
        int carId = 0;
        if (carsAtCompany.isEmpty()) {
            return carId;
        }

        //if there are cars at the company, give to carActions
        System.out.println("Choose a car:");
        carActions.printCarsFromCompany(carsAtCompany);
        System.out.println("0. Back");

        return userInput.getMenuNum(carsAtCompany);
    }

    void rentCarToCustomer(int customerId) {
        //customer.getId() == customerId

        //if customer already has a rental
        Customer renter = customerDAOimpl.getCustomerWhoRentedCar(customerId);
        if (renter.getRented_car_id() > 0) {
            System.out.println("You've already rented a car!\n");
            return;
        }

        //if customer does not have a rental
        //choose the company to rent from
        int companyId = getCompanyChoice();
        if (companyId == 0) {
            return;
        }

        //choose the car to rent from available cars
        // TEST TEST TEST
        List<Car> carsAtCompany = carActions.getAvailableCarRentals(companyId);

        if (carsAtCompany.isEmpty()){
            return;
        }

        int carId = getCarChoice(carsAtCompany);
        if (carId == 0) {
            return;
        }

        //make a call to the db for the customer to rent the correct car
        Car car = carsAtCompany.get(carId - 1);

        //rented car id == car id
        if (customerDAOimpl.rentCarToCustomer(car.getId(), renter.getId())) {
            System.out.println("You rented a " + "'" + car.getName() + "'\n");
        }
    }

    void returnRentedCar(int customerId) {
        //If customer doesn't have a rental, notify them.
        //customer.getId() == customerId
        Customer renter = customerDAOimpl.getCustomerWhoRentedCar(customerId);
        if (renter.getRented_car_id() == 0) {
            System.out.println("You didn't rent a car!\n");
            return;
        }

        //If customer has a rental, let them return the car
        if (customerDAOimpl.returnCarCustomerRented(renter.getId())) {
            System.out.println("You've returned a rented car!\n");
        }
    }

    void showCarCustomerRented(int customerId) {
        //customer.getId() == customerId

        //notify customer if there is no rental under their id
        Customer renter = customerDAOimpl.getCustomerWhoRentedCar(customerId);
        if (renter.getRented_car_id() == 0) {
            System.out.println("You didn't rent a car!\n");
            return;
        }

        //if there is a rental under their id, get the car and company names
        //(RENTED_CAR_ID) REFERENCES CAR(ID)
        Car carCustomerRented = carActions.getCarDAOImpl().getCarByRentedId(renter.getRented_car_id());

        System.out.println("Your rented car:\n" +
                carCustomerRented.getName() + "\n" +
                "Company:\n" +
                companyActions.getCompanyDAOImpl().getCompanyById(carCustomerRented.getCompanyId()).getName() + "\n");
    }

    void executeRentalCustomerActions() {
       //show list of customers, if it exists
        showCustomerList();
        if (customers.isEmpty()) {
            return;
        }

        //log in as customer
        int customerId = userInput.getMenuNum(customers);
        if (customerId == 0) {
            return;
        }

        //allow customer choice of car rental actions
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
}

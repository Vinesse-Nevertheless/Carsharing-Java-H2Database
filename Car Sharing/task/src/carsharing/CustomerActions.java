package carsharing;

import java.util.List;

class CustomerActions {
    List<String> rentCarMenu = List.of("Back",
            "Rent a car",
            "Return a rented car",
            "My rented car");

    UserInput userInput = new UserInput();
    CompanyActions companyActions;
    CarActions carActions;
    List<Customer> customers;
    CustomerDAOImpl customerDAOimpl;

    DbClient dbClient;

    public CustomerActions() {
        dbClient = DbClient.getDbClientInstance();
        dbClient.createCustomerTable();
        this.customerDAOimpl = new CustomerDAOImpl(dbClient);

        companyActions = new CompanyActions();
        carActions = new CarActions();
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

    int getCompanyChoice(){
        List<Company> companies = companyActions.getCompanyList();

        System.out.println("Choose a company:");
        return companyActions.getCompanyChoiceFromMenu(companies);
    }

    int getCarChoice( List<Car> carsAtCompany, int companyId){
        //if there are cars at the company
        int carId = 0;
        if (carsAtCompany.isEmpty()) {
           // System.out.println("There are no cars to rent.");
            return carId;
        }

        System.out.println("Choose a car:");
        carActions.printCarsFromCompany(carsAtCompany);
        System.out.println("0. Back");

        return userInput.getMenuNum(carsAtCompany);
    }

    void rentCarToCustomer(int customerId) {

        //customer.getId() == customerId
        Customer renter = customerDAOimpl.getCustomerWhoRentedCar(customerId);
      //  int rentedCarId = getRentedCarId(customerId);
        if (renter.getRented_car_id() > 0) {
            System.out.println("You've already rented a car!\n");
            return;
        }

        int companyId = getCompanyChoice();
        if (companyId == 0) {
            return;
        }

        List<Car> carsAtCompany = carActions.getCarList(companyId);

        int carId = getCarChoice(carsAtCompany, companyId);
        if (carId == 0) {
            return;
        }

        Car car = carsAtCompany.get(carId - 1);

        //rented car id == car id
        if (customerDAOimpl.rentCarToCustomer(carId, customerId)) {
            System.out.println("You rented a " + "'" + car.getName() + "'\n");
        }
    }

    void returnRentedCar(int customerId) {
 /*       int rentedCarId = getRentedCarId(customerId);
        if (rentedCarId == 0) {*/

        //customer.getId() == customerId
        Customer renter = customerDAOimpl.getCustomerWhoRentedCar(customerId);
            //  int rentedCarId = getRentedCarId(customerId);
        if (renter.getRented_car_id() == 0) {
            System.out.println("You didn't rent a car!\n");
            return;
        }

        if (customerDAOimpl.returnCarCustomerRented(customerId)) {
            System.out.println("You've returned a rented car!\n");
        }
    }

    void showCarCustomerRented(int customerId) {
/*        int rentedCarId = getRentedCarId(customerId);
        if (rentedCarId == 0) {*/

        //customer.getId() == customerId
        Customer renter = customerDAOimpl.getCustomerWhoRentedCar(customerId);
            //  int rentedCarId = getRentedCarId(customerId);
        if (renter.getRented_car_id() == 0) {
            System.out.println("You didn't rent a car!\n");
            return;
        }

       //(RENTED_CAR_ID) REFERENCES CAR(ID)
        Car carCustomerRented = carActions.carDAOImpl.getCarByRentedId(renter.getRented_car_id());
        System.out.println("Your rented car:\n" +
                carCustomerRented.getName() + "\n" +
                "Company:\n" +
                //make this a call to the db specifically, it's more memory efficient for larger applications
                companyActions.companyDAOImpl.findAll().get(carCustomerRented.getCompanyId() - 1).getName() + "\n");
    }

    //make this a call to the db specifically, it's more memory efficient for larger applications
    int getRentedCarId(int customerId){
        return customerDAOimpl.findAll().stream()
                .filter(c -> c.getId() == customerId)
                .toList().getFirst().getRented_car_id();
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
}

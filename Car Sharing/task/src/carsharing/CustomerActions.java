package carsharing;

import java.util.List;

class CustomerActions {
    List<String> rentCarMenu = List.of("Back",
            "Rent a car",
            "Return a rented car",
            "My rented car");

    UserInput userInput = new UserInput();
    CustomerDAOImpl customerDAOimpl;
    CarDAOImpl carDAOImpl;
    CompanyActions companyActions = new CompanyActions();
    CarActions carActions = new CarActions();
    List<Customer> customers;

    public CustomerActions() {
        DbClient dbClient = DbClient.getDbClientInstance();
        dbClient.createCarTable();
     //   this.carDAOImpl = new CarDAOImpl(dbClient);
        dbClient.createCustomerTable();
        this.customerDAOimpl = new CustomerDAOImpl(dbClient);
    }

    void showCustomerList() {
        customers = customerDAOimpl.findAll();
        if (customers.isEmpty()) {
            System.out.println("The customer list is empty!\n");
        } else {
            System.out.println("Choose a customer:");
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
            System.out.println("The customer was created!\n");
        }
    }

    void rentCarToCustomer(int companyId) {
/*        List<Car> carsAtCompany = carActions.carDAOImpl.findCarsPerCompany(companyId);
        if (carsAtCompany.isEmpty()) {
            System.out.println("The car list is empty!\n");
        } else {
            System.out.println("Choose a car:");
            for (int i = 0; i < carsAtCompany.size(); i++) {
                System.out.println((i + 1) + ". " + carsAtCompany.get(i).getName());
            }
            System.out.println("0. Back");*/

        List<Car> carsAtCompany = carActions.getCarList(companyId);
        if (!carsAtCompany.isEmpty()) {
            carActions.printCarsFromCompany(carsAtCompany);
        }

        int carNum = userInput.getMenuNum(carsAtCompany);
        if (carNum == 0) {
            return;
        }
        System.out.println("You rented a " + "'" + carsAtCompany.get(carNum - 1).getName() + "' ");

    }

    void showCarCustomerRented(Customer customer) {
        Car rentedCar = customerDAOimpl.getCarCustomerRented(customer);
        if (rentedCar == null) {
            System.out.println("You haven't rented a car!\n");  //check statement
        } else {
            System.out.println("Your rented car:\n" +
                    rentedCar.getName() +
                    "Company:\n" +
                    companyActions.companyDAOImpl.findAll().get(rentedCar.getCompanyId()).getName());
        }
    }

    void executeRentalCustomerActions() {
        showCustomerList();
        if (customers.isEmpty()) {
            return;
        }

        int rentalCarActionNum = userInput.getMenuNum(customers);
        if (rentalCarActionNum == 0) {
            return;
        }

        Printer.printMenu(rentCarMenu);
        int customerRentChoice = userInput.getMenuNum(rentCarMenu);
        switch (customerRentChoice) {
            case 1 -> rentCar(customers.get(rentalCarActionNum -1));

            /*{
                if (customerDAOimpl.getCarRentedByCustomer() == null) {
                    companyActions.executeCompanyTasks();
                } else {
                    System.out.println("You've already rented a car!");
                }

                   *//* List<Car> carsAtCompany = carDAOImpl.findCarsPerCompany(companyId);
                    if (carsAtCompany.isEmpty()) {
                        System.out.println("The car list is empty!\n");
                    } else {
                        System.out.println("Car list:");
                        for (int i = 0; i < carsAtCompany.size(); i++) {
                            System.out.println((i + 1) + ". " + carsAtCompany.get(i).getName());
                        }
                        System.out.println();
                    }*//*
            }*/
            case 2 -> {


            }

            case 3 -> {
                showCarCustomerRented(customers.get(rentalCarActionNum -1));

            }
            case 0 -> {
                return;
            }
        }
    }


    void rentCar(Customer customer){
        if (customerDAOimpl.getCarCustomerRented(customer) == null) {
            companyActions.executeCompanyTasks();
        } else {
            System.out.println("You've already rented a car!");
        }

    }
}

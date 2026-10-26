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

    void rentCarToCustomer() {
        List<Company> companies = companyActions.getCompanyList();

        System.out.println("Choose a company:");
        int companyId = companyActions.getCompanyChoiceFromMenu(companies);
        if (companyId == 0) {
            return;
        }
        // if the customer doesn't already have a car
        Car currentRentedCar = customerDAOimpl.getCarCustomerRented(customers.get(companyId));

        if (currentRentedCar != null) {
            System.out.println("You've already rented a car!");
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

        int carNum = userInput.getMenuNum(carsAtCompany);
        if (carNum == 0) {
            return;
        }

       Car car = carsAtCompany.get(carNum -1 );

        //rented car id is not properly set during renting
        if (customerDAOimpl.rentCarToCustomer(car)) {
            //    if (customerDAOimpl.rentCarToCustomer(carsAtCompany.get(carNum - 1))) {
            System.out.println("You rented a " + "'" + car.getName() + "' ");
        }

    }

    void returnRentedCar(int companyId) {
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
        Car carCustomerRented = customerDAOimpl.getCarCustomerRented(customers.get(companyId));

        if (carsAtCompany.isEmpty() || carCustomerRented == null) {
            System.out.println("You didn't rent a car.");
        }

       // Car carCustomerRented = customerDAOimpl.getCarCustomerRented(customers.get(companyId));
       // if (carCustomerRented.getId() == customers.get(companyId).getRented_car_id()) {
            //return rented car
            //customerDAOimpl.returnCarCustomerRented(customers.get(companyId).getRented_car_id());
            if (customerDAOimpl.returnCarCustomerRented(customers.get(companyId))) {
                System.out.println("You've returned a rented car!");
            }
      //  }

    }

    void showCarCustomerRented(Customer customer) {
        System.out.println(customer.getName());
        //rented car id is not properly set during renting
        System.out.println(customer.getRented_car_id());
        Car carCustomerRented = customerDAOimpl.getCarCustomerRented(customer);
        if (carCustomerRented == null) {
            System.out.println("You didn't rent a car!\n");
        } else {
            System.out.println("Your rented car:\n" +
                    carCustomerRented.getName() +
                    "Company:\n" +
                    companyActions.companyDAOImpl.findAll().get(carCustomerRented.getCompanyId()).getName());
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

        while (true) {
            Printer.printMenu(rentCarMenu);
            int customerRentChoice = userInput.getMenuNum(rentCarMenu);
            switch (customerRentChoice) {
                case 1 -> rentCarToCustomer();
               // case 1 -> rentCarToCustomer(rentalCarActionNum);
                //  case 1 -> rentCar(customers.get(rentalCarActionNum));

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
                case 2 -> returnRentedCar(rentalCarActionNum);
                case 3 -> showCarCustomerRented(customers.get(rentalCarActionNum - 1));


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

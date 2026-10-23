package carsharing;

import java.util.List;

class CustomerActions {
    List<String> rentCarMenu = List.of("Back",
                    "Rent a car",
                    "Return a rented car",
                    "My rented car");

    UserInput userInput = new UserInput();
    CustomerDAOImpl customerDAOimpl;
    CompanyActions companyActions = new CompanyActions();
    List<Customer> customers;

    public CustomerActions() {
        this.customerDAOimpl = new CustomerDAOImpl(DbClient.getDbClientInstance());
    }

    void createRentalCustomer() {
        System.out.println("Enter the customer name:");
        String name = userInput.getNewName();
        if (customerDAOimpl.add(new Customer(name))) {
            System.out.println("The customer was created!\n");
        }
    }

    void executeRentalCustomerActions(){
        showCustomerList();
        int rentalCarActionNum = userInput.getMenuNum(customers);
        if (rentalCarActionNum == 0){
            return;
        }else{
            Printer.printMenu(rentCarMenu);
            int customerRentChoice = userInput.getMenuNum(rentCarMenu);
            switch (customerRentChoice) {
                case 1 -> {
                    if (customerDAOimpl.getCarRentedByCustomer() == null){
                        companyActions.executeCompanyTasks();

                    }else{
                        System.out.println("You've already rented a car!");
                    }

                   /* List<Car> carsAtCompany = carDAOImpl.findCarsPerCompany(companyId);
                    if (carsAtCompany.isEmpty()) {
                        System.out.println("The car list is empty!\n");
                    } else {
                        System.out.println("Car list:");
                        for (int i = 0; i < carsAtCompany.size(); i++) {
                            System.out.println((i + 1) + ". " + carsAtCompany.get(i).getName());
                        }
                        System.out.println();
                    }*/
                }
/*                case 2 -> {
                    System.out.println("Enter the car name:");
                    String name = userInput.getNewName();
                    if (carDAOImpl.addCarToCompany(new Car(companyId, name))) {
                        System.out.println("The car was added!\n");
                    }
                }*/
                case 0 -> {
                    return;
                }
            }
        }
    }

    void showCustomerList() {
       // List<Customer> customers = customerDAOimpl.findAll();
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


}

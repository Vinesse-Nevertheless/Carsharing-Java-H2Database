package carsharing;

import java.util.List;

class CustomerActions {
    List<String> rentCarMenu = List.of("Back",
            "Company list",
            "Create a company");

    UserInput userInput = new UserInput();
    CustomerDAOImpl customerDAOimpl;
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

package carsharing;

import java.util.List;

class CarActions {

    private List<String> carMenu = List.of("Back",
            "Car list",
            "Create a car");

    private UserInput userInput = new UserInput();
    private CarDAO carDAOImpl;

    public CarActions() {
        DbClient dbClientInstance = DbClient.getDbClientInstance();
        dbClientInstance.createCarTable();
        carDAOImpl = new CarDAOImpl(dbClientInstance);
    }

    public CarDAO getCarDAOImpl() {
        return carDAOImpl;
    }

    void createCarForCompany(int companyId) {
        System.out.println("Enter the car name:");
        String name = userInput.getNewName();
        if (carDAOImpl.addCarToCompany(new Car(companyId, name))) {
            System.out.println("The car was added!\n");
        }
    }

    //get list of cars per company
    List<Car> getCarListForCompany(int companyId) {
        List<Car> carsAtCompany = carDAOImpl.findCarsPerCompany(companyId);
        if (carsAtCompany.isEmpty()) {
            System.out.println("The car list is empty!\n");
        }
        return carsAtCompany;
    }


    List<Car> getAvailableCarRentals(int companyId) {
        List<Car> carsAtCompany = carDAOImpl.findAvailableCarsPerCompany(companyId);
        if (carsAtCompany.isEmpty()) {
            System.out.println("The car list is empty!\n");
        }
        return carsAtCompany;
    }

    void printCarsFromCompany(List<Car> carsAtCompany) {
        /*
        for loop iteration to allow the numbering for the cars at each company
        to start at 1.
        */
        for (int i = 0; i < carsAtCompany.size(); i++) {
            System.out.println((i + 1) + ". " + carsAtCompany.get(i).getName());
        }
    }

    void executeCarTasks(int companyId) {

        while (true) {
            Printer.printMenu(carMenu);
            int carChoice = userInput.getMenuNum(carMenu);
            switch (carChoice) {
                case 1 -> {
                    List<Car> carList = getCarListForCompany(companyId);
                    if (!carList.isEmpty()) {
                        System.out.println("Car list:");
                        printCarsFromCompany(carList);
                        System.out.println();
                    }
                }
                case 2 -> createCarForCompany(companyId);
                case 0 -> {
                    return;
                }
            }
        }
    }
}

package carsharing;

import java.util.List;

class CarActions {

    List<String> carMenu = List.of("Back",
            "Car list",
            "Create a car");

    UserInput userInput = new UserInput();
    CarDAO carDAOImpl;

    public CarActions() {
        DbClient dbClientInstance = DbClient.dbClientInstance;
        dbClientInstance.createCarTable();
        carDAOImpl = new CarDAOImpl(dbClientInstance);
    }

    void createCarForCompany(int companyId) {
        System.out.println("Enter the car name:");
        String name = userInput.getNewName();
        if (carDAOImpl.addCarToCompany(new Car(companyId, name))) {
            System.out.println("The car was added!\n");
        }
    }

/*
    void printCarsFromCompany(int companyId){
        List<Car> carsAtCompany = carDAOImpl.findCarsPerCompany(companyId);
        if (carsAtCompany.isEmpty()) {
            System.out.println("The car list is empty!\n");
        } else {
            System.out.println("Car list:");
            for (int i = 0; i < carsAtCompany.size(); i++) {
                System.out.println((i + 1) + ". " + carsAtCompany.get(i).getName());
            }
            System.out.println();
        }
    }
*/

    List<Car> getCarList(int companyId){
        List<Car> carsAtCompany = carDAOImpl.findCarsPerCompany(companyId);
        if (carsAtCompany.isEmpty()) {
            System.out.println("The car list is empty!\n");
        }
        return carsAtCompany;
    }

    void printCarsFromCompany(List<Car> carsAtCompany){
            for (int i = 0; i < carsAtCompany.size(); i++) {
                System.out.println((i + 1) + ". " + carsAtCompany.get(i).getName());
            }
            System.out.println();
    }
   void executeCarTasks(int companyId) {

        while (true) {
            Printer.printMenu(carMenu);
            int carChoice = userInput.getMenuNum(carMenu);
            switch (carChoice) {
                case 1 -> {
                    List<Car> carList = getCarList(companyId);
                    if (!carList.isEmpty()) {
                        System.out.println("Car list:");
                        printCarsFromCompany(carList);
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

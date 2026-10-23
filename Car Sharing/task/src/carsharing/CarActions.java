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

    void createACarForCompany(int companyId) {
        System.out.println("Enter the car name:");
        String name = userInput.getNewName();
        if (carDAOImpl.addCarToCompany(new Car(companyId, name))) {
            System.out.println("The car was added!\n");
        }
    }

    void rentACarFromCompany(int companyId){
        List<Car> carsAtCompany = carDAOImpl.findCarsPerCompany(companyId);
        if (carsAtCompany.isEmpty()) {
            System.out.println("The car list is empty!\n");
        } else {
            System.out.println("Choose a car:");
            for (int i = 0; i < carsAtCompany.size(); i++) {
                System.out.println((i + 1) + ". " + carsAtCompany.get(i).getName());
            }
            System.out.println("0. Back");

            int carNum = userInput.getMenuNum(carsAtCompany);
            if (carNum == 0) {
                return;
            }
            System.out.println("You rented a " + "'" + carsAtCompany.get(carNum - 1).getName() + "' " );

        }
    }

    void executeCarTasks(int companyId) {

        while (true) {
            Printer.printMenu(carMenu);
            int carChoice = userInput.getMenuNum(carMenu);
            switch (carChoice) {
                case 1 -> {
                    rentACarFromCompany(companyId);
   /*                 List<Car> carsAtCompany = carDAOImpl.findCarsPerCompany(companyId);
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
                case 2 -> {
                    createACarForCompany(companyId);
                   /* System.out.println("Enter the car name:");
                    String name = userInput.getNewName();
                    if (carDAOImpl.addCarToCompany(new Car(companyId, name))) {
                        System.out.println("The car was added!\n");
                    }*/
                }
                case 0 -> {
                    return;
                }
            }
        }
    }
}

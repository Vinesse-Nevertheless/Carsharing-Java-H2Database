package carsharing;

import java.util.List;

public class CarDAOImpl implements CarDAO {
    DbClient dbClient;

    private final String INSERT_DATA = "INSERT INTO CAR"
            + " (company_id, name) VALUES (?, ?)";
    private final String SELECT_ALL_CARS_PER_COMPANY = "SELECT * FROM CAR WHERE company_id = ?";

    private final String SELECT_CAR_RENTED_BY_RENTED_CAR_ID = "SELECT * FROM CAR " +
            "WHERE id = ?";

    private final String SELECT_ALL_AVAILABLE_CARS_PER_COMPANY = "SELECT * FROM CAR " +
            "WHERE company_id = ? AND " +
            "id NOT IN (SELECT rented_car_id FROM CUSTOMER WHERE rented_car_id IS NOT NULL) ";

    public CarDAOImpl(DbClient dbClient) {
        this.dbClient = dbClient;
    }

    @Override
    public boolean addCarToCompany(Car car) {
        return dbClient.addCarToCompany(INSERT_DATA, car.getCompanyId(), car.getName());
    }

    @Override
    public List<Car> findCarsPerCompany(int company_id) {
        return dbClient.findCarsPerCompany(SELECT_ALL_CARS_PER_COMPANY, company_id);
    }

    @Override
    public List<Car> findAvailableCarsPerCompany(int company_id) {
        return dbClient.findAvailableCarsPerCompany(SELECT_ALL_AVAILABLE_CARS_PER_COMPANY, company_id);
    }

    @Override
    public Car getCarByRentedId(int rented_car_id) {
        return dbClient.getCarByRentedId(SELECT_CAR_RENTED_BY_RENTED_CAR_ID, rented_car_id);
    }

}
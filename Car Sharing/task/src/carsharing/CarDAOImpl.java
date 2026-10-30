package carsharing;

import java.util.List;

public class CarDAOImpl implements CarDAO{
    DbClient dbClient;
    private final String INSERT_DATA = "INSERT INTO CAR"
            + " (company_id, name) VALUES (?, ?)";
    private final String SELECT_ALL_CARS_PER_COMPANY = "SELECT * FROM CAR WHERE company_id = ?";

    private final String SELECT_CAR_RENTED_BY_RENTED_CAR_ID = "SELECT * FROM CAR " +
            "WHERE id = ?";

    public CarDAOImpl(DbClient dbClient){
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
    public Car getRentedCarById(int rented_car_id) {
        return dbClient.getRentedCarById(SELECT_CAR_RENTED_BY_RENTED_CAR_ID, rented_car_id);
    }

}
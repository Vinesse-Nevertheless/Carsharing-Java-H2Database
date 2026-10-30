package carsharing;

import java.util.List;

public interface CarDAO {
    boolean addCarToCompany(Car car);
    List<Car> findCarsPerCompany(int id);
    Car getRentedCarById(int rented_car_id);
}
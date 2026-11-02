package carsharing;

import java.util.List;

public interface CarDAO {
    boolean addCarToCompany(Car car);
    Car getCarByRentedId(int rented_car_id);
    List<Car> findAvailableCarsPerCompany(int company_id);
}
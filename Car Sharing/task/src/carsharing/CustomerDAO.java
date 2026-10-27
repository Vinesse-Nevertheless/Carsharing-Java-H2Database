package carsharing;

import java.util.List;

public interface CustomerDAO {
    boolean addCustomer(Customer customer);
    List<Customer> findAll();
   // Car getCarRentedByCustomer();
    Car getCarCustomerRented(Customer customer);
    boolean returnCarCustomerRented(Customer customer);
   // boolean rentCarToCustomer(Car car);
   boolean rentCarToCustomer(int rented_car_id, int customer_id);

}
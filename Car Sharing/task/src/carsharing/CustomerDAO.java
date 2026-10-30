package carsharing;

import java.util.List;

public interface CustomerDAO {
    boolean addCustomer(Customer customer);

    List<Customer> findAll();

   // Car getCarCustomerRented(int rented_car_id);

    boolean returnCarCustomerRented(int rented_car_id);

    boolean rentCarToCustomer(int rented_car_id, int customer_id);

}
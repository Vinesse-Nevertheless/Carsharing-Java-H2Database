package carsharing;

import java.util.List;

public interface CustomerDAO {
    boolean add(Customer customer);
    List<Customer> findAll();
    Car getCarRentedByCustomer();
}
package carsharing;

import java.util.List;

public interface CustomerDAO {
    boolean addCustomer(Customer customer);
    List<Customer> findAll();
    Car getCarRentedByCustomer();
}
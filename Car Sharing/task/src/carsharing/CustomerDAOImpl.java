package carsharing;

import java.util.List;

public class CustomerDAOImpl implements CustomerDAO{

    DbClient dbClient;
    private final String INSERT_DATA;
    private final String SELECT_ALL = "SELECT * FROM CUSTOMER";

    private final String SELECT_CAR_RENTED_BY_RENTED_CAR_ID = "SELECT * FROM CAR " +
            "WHERE id = ?";

    public CustomerDAOImpl(DbClient dbClient){
        this.dbClient = dbClient;

        INSERT_DATA = "INSERT INTO CUSTOMER"
                + " (name) VALUES (?)";

    }

    @Override
    public boolean addCustomer(Customer customer) {
        return dbClient.addCustomer(INSERT_DATA, customer.getName());
    }

    @Override
    public List<Customer> findAll() {
        return dbClient.getAllCustomers(SELECT_ALL);
    }

   /* @Override
    public Car getCarRentedByCustomer() {
        return dbClient.getCarRentedByCustomer(SELECT_ALL);
    }*/

    @Override
    public Car getCarCustomerRented(Customer customer) {
        return dbClient.getCarCustomerRented(SELECT_CAR_RENTED_BY_RENTED_CAR_ID, customer);
    }

}
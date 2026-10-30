package carsharing;

import java.util.List;

public class CustomerDAOImpl implements CustomerDAO{

    DbClient dbClient;
    private final String INSERT_DATA = "INSERT INTO CUSTOMER"
            + " (name) VALUES (?)";;
    private final String SELECT_ALL = "SELECT * FROM CUSTOMER";

    private final String FIND_CUSTOMER_BY_ID = "SELECT * FROM CUSTOMER " +
            "WHERE ID = ?";

/*    //should this be in CarDao?
    private final String SELECT_CAR_RENTED_BY_RENTED_CAR_ID = "SELECT * FROM CAR " +
            "WHERE id = ?";*/

    private final String RETURN_CAR_BY_CUSTOMER_ID = "UPDATE CUSTOMER " +
            "SET rented_car_id = NULL " +
            "WHERE id = ?";

    private final String RENT_CAR_TO_CUSTOMER = "UPDATE CUSTOMER " +
            "SET rented_car_id = ? " +
            "WHERE id = ?";

    public CustomerDAOImpl(DbClient dbClient){
        this.dbClient = dbClient;
    }

    @Override
    public boolean addCustomer(Customer customer) {
        return dbClient.addCustomer(INSERT_DATA, customer.getName());
    }

    @Override
    public List<Customer> findAll() {
        return dbClient.getAllCustomers(SELECT_ALL);
    }

    @Override
    public Customer getCustomerWhoRentedCar(int customer_id) {
        return dbClient.getCustomerWhoRentedCar(FIND_CUSTOMER_BY_ID, customer_id);
    }

    @Override
    public boolean rentCarToCustomer( int rented_car_id, int customer_id) {
        return dbClient.rentCarToCustomer(RENT_CAR_TO_CUSTOMER, rented_car_id, customer_id);
    }

    @Override
    public boolean returnCarCustomerRented(int customerId) {
        return dbClient.returnCarCustomerRented(RETURN_CAR_BY_CUSTOMER_ID, customerId);
    }

}
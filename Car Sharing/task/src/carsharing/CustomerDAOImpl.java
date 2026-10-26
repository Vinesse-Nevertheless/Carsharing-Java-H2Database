package carsharing;

import java.util.List;

public class CustomerDAOImpl implements CustomerDAO{

    DbClient dbClient;
    private final String INSERT_DATA;
    private final String SELECT_ALL = "SELECT * FROM CUSTOMER";

    private final String SELECT_CAR_RENTED_BY_RENTED_CAR_ID = "SELECT * FROM CAR " +
            "WHERE id = ?";

/*    private final String SELECT_CAR_RENTED_BY_RENTED_CAR_ID = "SELECT * FROM CUSTOMER " +
            "WHERE rented_car_id = ?";*/

    private final String RETURN_CAR_BY_RENTED_CAR_ID = "UPDATE CUSTOMER " +
            "SET rented_car_id = NULL " +
            "WHERE rented_car_id = ?";

    private final String RENT_CAR_TO_CUSTOMER = "UPDATE CUSTOMER " +
            "SET rented_car_id = ?";

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
    public boolean rentCarToCustomer( Car car) {
        return dbClient.rentCarToCustomer(RENT_CAR_TO_CUSTOMER, car.getId());
    }

    @Override
    public Car getCarCustomerRented(Customer customer) {
        return dbClient.getCarCustomerRented(SELECT_CAR_RENTED_BY_RENTED_CAR_ID, customer);
    }

  /*  @Override
    public boolean returnCarCustomerRented(Customer customer) {
        return dbClient.returnCarCustomerRented(SELECT_CAR_RENTED_BY_RENTED_CAR_ID, customer);
    }*/

    @Override
    public boolean returnCarCustomerRented(Customer customer) {
        return dbClient.returnCarCustomerRented(RETURN_CAR_BY_RENTED_CAR_ID, customer.getRented_car_id());
    }

}
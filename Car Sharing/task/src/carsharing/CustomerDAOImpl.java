package carsharing;

import java.util.List;

public class CustomerDAOImpl implements CustomerDAO{

    DbClient dbClient;
    private final String INSERT_DATA;
    private final String SELECT_ALL;

    public CustomerDAOImpl(DbClient dbClient){
        this.dbClient = dbClient;

        INSERT_DATA = "INSERT INTO CUSTOMER"
                + " (name) VALUES (?)";
        SELECT_ALL = "SELECT * FROM CUSTOMER";
    }

    @Override
    public boolean add(Customer customer) {
        return false;
    }

    @Override
    public List<Customer> findAll() {
        return dbClient.getAllCustomers(SELECT_ALL);
    }


    public DbClient getDbClient() {
        return dbClient;
    }

}
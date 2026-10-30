package carsharing;

import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbClient {

    String DB_URL;
    static DbClient dbClientInstance;

    private DbClient(String DB_URL) {
        this.DB_URL = DB_URL;
    }

    //singleton for shared single reusable instance of dbClient
    public static void setInstance(String DB_URL){
        if (dbClientInstance == null){
            dbClientInstance = new DbClient(DB_URL);
        }
    }

    public static DbClient getDbClientInstance(){
        return dbClientInstance;
    }

    //create COMPANY table
    public void createCompanyTable() {
            String createCompanySQL = "CREATE TABLE IF NOT EXISTS COMPANY " +
                    "( ID INT PRIMARY KEY AUTO_INCREMENT, " +
                    "NAME VARCHAR UNIQUE NOT NULL)";
            establishConnection(createCompanySQL);
    }

    //create CAR table
    public void createCarTable() {
        String createCarSQL = "CREATE TABLE IF NOT EXISTS CAR " +
                "( ID INT PRIMARY KEY AUTO_INCREMENT, " +
                "NAME VARCHAR UNIQUE NOT NULL, " +
                "COMPANY_ID INT NOT NULL," +
                "CONSTRAINT fk_company_id FOREIGN KEY (COMPANY_ID) REFERENCES COMPANY(ID))";
        establishConnection(createCarSQL);
    }

    //create CUSTOMER table
    public void createCustomerTable(){
        String createCustomerSQL = "CREATE TABLE IF NOT EXISTS CUSTOMER " +
                "( ID INT PRIMARY KEY AUTO_INCREMENT, " +
                "NAME VARCHAR UNIQUE NOT NULL, " +
                "RENTED_CAR_ID INT DEFAULT NULL, " +
                "CONSTRAINT fk_car_id FOREIGN KEY (RENTED_CAR_ID) REFERENCES CAR(ID))";

        establishConnection(createCustomerSQL);
    }


    public void establishConnection(String createTableSQL){
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();) {

            conn.setAutoCommit(true);  // all changes are automatically saved in the database file

            stmt.executeUpdate(createTableSQL);

        } catch (SQLException sql) {
            sql.printStackTrace();
        }
    }


    // Run INSERT to Company
    public boolean addCompany(String sql, String company) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setString(1, company);

            preparedStatement.executeUpdate();
            return true;
        } catch (JdbcSQLIntegrityConstraintViolationException jdbcSQLException) {
            System.out.println("Company with this name already exists.\n");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    //Select all companies
    public List<Company> getAllCompanies(String sql) {
        List<Company> companyList = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                companyList.add(new Company(resultSet.getInt("id"), resultSet.getString("name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return companyList;
    }

    // Run INSERT to Company
    public boolean addCarToCompany(String sql, int companyId, String car) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setInt(1, companyId);
            preparedStatement.setString(2, car);

            preparedStatement.executeUpdate();
            return true;
        }catch (JdbcSQLIntegrityConstraintViolationException jdbcSQLException) {
            System.out.println("Car with this name already exists.\n");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    //Select all cars in a company
    public List<Car> findCarsPerCompany(String sql, int company_id) {
        List<Car> carList = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setInt(1, company_id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    carList.add(new Car( resultSet.getInt("id"), resultSet.getString("name"), resultSet.getInt("company_id")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return carList;
    }

    //I MIGHT NOT NEED THIS.
    //find car by id
    public List<Car> findCarInCompanyById(String sql, int id) {
        List<Car> carList = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
               // while (resultSet.next()) {
                    carList.add(new Car( resultSet.getInt("id"), resultSet.getString("name"), resultSet.getInt("company_id")));
               // }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return carList;
    }


    // Run INSERT to Customer
    public boolean addCustomer(String sql, String name) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setString(1, name);
            preparedStatement.executeUpdate();

            return true;
        } catch (JdbcSQLIntegrityConstraintViolationException jdbcSQLException) {
            System.out.println("Customer already exists.\n");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    //Select all customers
    public List<Customer> getAllCustomers(String sql) {
        List<Customer> customerList = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                customerList.add(new Customer(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getInt("rented_car_id")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customerList;
    }

    public Car getCarCustomerRented(String selectRentedCar, int rented_car_id) {
        Car rentedCar = null;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = conn.prepareStatement(selectRentedCar)) {

            preparedStatement.setInt(1, rented_car_id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    //  if (resultSet.getInt("id") == customer.getRented_car_id()) {
                    rentedCar = new Car(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getInt("company_id"));
                    // }
                }
            }
        } catch (JdbcSQLIntegrityConstraintViolationException jdbcSQLException) {
            System.out.println("Car with this name already exists.\n");
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return rentedCar;
    }

/*    public Car getCarCustomerRented(String selectRentedCar, int rented_car_id) {
        Car rentedCar = null;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = conn.prepareStatement(selectRentedCar)) {

            preparedStatement.setInt(1, rented_car_id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    //  if (resultSet.getInt("id") == customer.getRented_car_id()) {
                    rentedCar = new Car(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getInt("company_id"));
                    // }
                }
            }
        } catch (JdbcSQLIntegrityConstraintViolationException jdbcSQLException) {
            System.out.println("Car with this name already exists.\n");
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return rentedCar;
    }*/

    //UPDATE
    public boolean returnCarCustomerRented(String selectCarRentedByRentedCarId, int customerId) {

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = conn.prepareStatement(selectCarRentedByRentedCarId)) {

            preparedStatement.setInt(1, customerId);

            preparedStatement.executeUpdate();

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }

    //UPDATE
    public boolean rentCarToCustomer(String rentCarToCustomer, int rented_car_id, int customer_id) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = conn.prepareStatement(rentCarToCustomer)) {

            preparedStatement.setInt(1, rented_car_id);
            preparedStatement.setInt(2, customer_id);

            int up = preparedStatement.executeUpdate();
            System.out.println("Up is " + up);
                return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }

    /*public boolean returnCarCustomerRented(String selectCarRentedByRentedCarId, Customer customer) {

        try (Connection conn = DriverManager.getConnection(dbClientInstance.DB_URL);
             PreparedStatement preparedStatement = conn.prepareStatement(selectCarRentedByRentedCarId)) {

            preparedStatement.setInt(1, customer.getRented_car_id());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                      if (resultSet.getInt("id") == customer.getRented_car_id()) {
                     }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }*/

   /* public Car getCarRentedByCustomer(String selectAll) {
        Car rentedCar = null;

        try (Connection conn = DriverManager.getConnection(dbClientInstance.DB_URL);
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(selectAll)) {

            while (resultSet.next()) {
                if (resultSet.getString("rented_car_id") != null) {
                    rentedCar = new Car(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getInt("company_id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rentedCar;
    }*/
}
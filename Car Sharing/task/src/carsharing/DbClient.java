package carsharing;

import org.h2.jdbc.JdbcSQLException;
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
                "NAME VARCHAR UNIQUE NOT NULL " +
                "RENTED_CAR_ID INT DEFAULT NULL, " +
                "CONSTRAINT fk_car_id FOREIGN KEY (ID) REFERENCES (CAR))";

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
                  //  carList.add(new Car(resultSet.getString("name"), resultSet.getInt("id")));
                    carList.add(new Car( resultSet.getInt("id"), resultSet.getString("name"), resultSet.getInt("company_id")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return carList;
    }


    //Select all customers
    public List<Customer> getAllCustomers(String sql) {
        List<Customer> customerList = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                customerList.add(new Customer(resultSet.getInt("id"), resultSet.getString("name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customerList;
    }
}
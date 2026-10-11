package carsharing;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbClient {

    String DB_URL;

    public DbClient(String DB_URL) {
        this.DB_URL = DB_URL;
    }

    public void createTable() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();) {

            conn.setAutoCommit(true);  // all changes are automatically saved in the database file

            String createTableSQL = "CREATE TABLE IF NOT EXISTS COMPANY " +
                    "( ID INT PRIMARY KEY AUTO_INCREMENT, " +
                    "NAME VARCHAR UNIQUE NOT NULL)";
            stmt.executeUpdate(createTableSQL);

        } catch (SQLException sql) {
            sql.printStackTrace();
        }
    }

    // Run INSERT
    public boolean addCompany(String sql, String company) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setString(1, company);

            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
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

}
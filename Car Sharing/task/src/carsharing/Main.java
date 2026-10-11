package carsharing;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {

    static final String JDBC_Driver = "org.h2.Driver";
    static String databaseName = "carsharing";

    public static void main(String[] args) {
        if (args.length > 1 && args[0].equals("-databaseFileName")) {
            databaseName = args[1];
        }

        final String DB_URL = "jdbc:h2:./src/carsharing/db/" + databaseName;

        try {
            Class.forName(JDBC_Driver);
            System.out.println("Connecting to database...");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            conn.setAutoCommit(true);
            Statement stmt = conn.createStatement();
            String createTableSQL = "CREATE TABLE IF NOT EXISTS COMPANY " +
                    "( ID INT, " +
                    "NAME VARCHAR )";
            stmt.executeUpdate(createTableSQL);
        } catch (SQLException sql) {
            sql.printStackTrace();
        }

    }


}
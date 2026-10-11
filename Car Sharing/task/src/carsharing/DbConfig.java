package carsharing;

public class DbConfig {

    static final String JDBC_Driver = "org.h2.Driver";
    static String databaseName = "carsharing";

    static String DB_URL;

    public static void connectToDb (String[] args){
        if (args.length > 1 && args[0].equals("-databaseFileName")) {
            databaseName = args[1];
        }

        //enable compatibility mode to use MySQL dialect
        DB_URL = "jdbc:h2:./src/carsharing/db/" + databaseName
                + ";MODE=MySQL;DB_CLOSE_DELAY=-1";

        //connect to db without login credentials
        try {
            Class.forName(JDBC_Driver);
            System.out.println("Connecting to database...");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}
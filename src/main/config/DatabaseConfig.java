package main.config;
import java.sql.*;

public class DatabaseConfig {
    // defines the database url, username and password
    private static final String DB_URL = "jdbc:postgresql://localhost/mvaahan";
    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "mitesh";
    private static Connection con;

    // method to initialize the connection using the url, username and password
    public static void initialize() throws SQLException {
        con = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
    }

    // used by other packages to get connection
    public static Connection getConnection() {
        return con;
    }

    // used to close the connection
    public static void closeConnection() throws SQLException {
        con.close();
    }
}

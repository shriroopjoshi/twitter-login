package classes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TwitterConnection {
    
    private final static String CONNECTION_URL = "jdbc:mysql://den1.mysql2.gear.host:3306/dbtwitter";
    private final static String USERNAME = "dbtwitter";
    private final static String PASSWORD = "Or9vF!?ztq9S";
    
    private static Connection conn = null;
    
    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        if(conn == null || conn.isClosed()) {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(CONNECTION_URL, USERNAME, PASSWORD);
        }
        return conn;
    }
    
}

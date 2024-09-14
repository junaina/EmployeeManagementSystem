
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class TestDatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/EmployeeManagement";
    private static final String USER = "root";
    private static final String PASSWORD = "Humanbeing11!"; // Update with your password

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            if (connection != null) {
                System.out.println("Connected to the database!");
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}

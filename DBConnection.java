import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/movie_booking";

    private static final String USERNAME = "root";

    private static final String PASSWORD = "pacibendelon123";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    public static void main(String[] args) {
        try {
            Connection con = getConnection();

            System.out.println("Database connected successfully!");

            con.close();

        } catch (SQLException e) {
            System.out.println("Connection failed!");
            System.out.println(e.getMessage());
        }
    }
}
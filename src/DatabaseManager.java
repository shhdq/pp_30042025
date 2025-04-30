import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    private final String DB_URL = "jdbc:mysql://localhost:3306/todo_simple_db";
    private final String DB_USER = "root";
    private final String DB_PASSWORD = "";

    private Connection connection;

    // try to connect
    public Connection connect() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

                System.out.println("Savienojums ar DB izveidots");
            } catch (SQLException e) {
                System.err.println("Kļūda savienojoties ar DB: " + e.getMessage());
                throw e;
            }
        }
        return connection;
    }

    // try to add a task
    public boolean addTask(String description) {

        String sql = "INSERT INTO tasks (description) VALUES (?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {

            pstmt.setString(1, description);
            int affectedRows = pstmt.executeUpdate();

            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Ķļūda pievienojot uzdevumu DB: " + e.getMessage());

            return false;
        }
    }

    // get all todos
    public ArrayList<String> getAllTasks() {
        ArrayList<String> tasks = new ArrayList<String>();
        String sql = "SELECT description FROM tasks ORDER BY created_at ASC";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)
             ) {

            while (rs.next()) {

                tasks.add(rs.getString("description"));

            }

        } catch (SQLException e) {
            System.err.println("Kļūda ielādējot uzdevumus no DB: " + e.getMessage());
        }

        return tasks;

    }

    // close db connection
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Savienojums ar DB aizvērts.");
            } catch (SQLException e) {
                System.err.println("Kļūda aizverot DB savienojumu: " + e.getMessage());
            }
        }
    }

}

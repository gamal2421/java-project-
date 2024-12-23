import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DataBaseConnection {
    public static class DatabaseManager {
        private String url;
        private String user;
        private String password;
        private Connection connection;

        // Constructor
        public DatabaseManager(String url, String user, String password) {
            this.url = url;
            this.user = user;
            this.password = password;
            try {
                this.connection = connect();
            } catch (SQLException e) {
                System.out.println("Failed to establish database connection: " + e.getMessage());
            }
        }

        // Method to establish a connection
        private Connection connect() throws SQLException {
            return DriverManager.getConnection(url, user, password);
        }

        // Method to get the connection
        public Connection getConnection() {
            return this.connection;
        }

        // Method to close the connection
        public void closeConnection() {
            if (this.connection != null) {
                try {
                    this.connection.close();
                    System.out.println("Database connection closed.");
                } catch (SQLException e) {
                    System.out.println("Error while closing the connection: " + e.getMessage());
                }
            }
        }

        public void insertLibraryItem(String title, String publisher, String isbn, String author, int numberOfCopies) {
            if (this.connection == null) {
                System.out.println("Cannot insert library item: Connection is null.");
                return;
            }

            String sql = "INSERT INTO LibraryItems (item_title, publisher, isbn, author, number_of_copies, status) VALUES (?, ?, ?, ?, ?, ?)";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, title);
                statement.setString(2, publisher);
                statement.setString(3, isbn);
                statement.setString(4, author);
                statement.setInt(5, numberOfCopies);
                statement.setString(6, "x");

                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Library item inserted successfully!");
                }
            } catch (SQLException e) {
                System.out.println("Error inserting library item: " + e.getMessage());
            }
        }

    }
}

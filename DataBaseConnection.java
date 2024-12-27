import java.sql.*;

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
            System.out.println("conn is ok ");
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

        ///////////___________insert library_________////////////////
        public void insertLibrary(
               int library_id, String library_name, String library_address
        ){
            if (this.connection == null) {
                System.out.println("Cannot insert library item: Connection is null.");
                return;
            }
            String sql= "insert into Libraries values(?,?,?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)){
                statement.setInt(1,library_id);
                statement.setString(2,library_name);
                statement.setString(3,library_address);

                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Library inserted successfully!");
                }
            } catch (SQLException e) {
                System.out.println("Error inserting library: " + e.getMessage());
            }
        }
        ///////////___________insert Library Employee////////////////
        public void insertLib_emp(
                int emp_id, String emp_name, String email, String phone,
                String acc_password , int salary , Date hire_date , int manager_id,
                int library_id
        ){
            if (this.connection == null) {
                System.out.println("Cannot insert library employee: Connection is null.");
                return;
            }
            String sql= "insert into lib_emp values(?,?,?,?,?,?,?,?,?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)){
                statement.setInt(1,emp_id);
                statement.setString(2,emp_name);
                statement.setString(3,email);
                statement.setString(4,phone);
                statement.setString(5,acc_password);
                statement.setInt(6,salary);
                statement.setDate(7,hire_date);
                statement.setInt(8,manager_id);
                statement.setInt(9,library_id);


                int rowsInserted = statement.executeUpdate();

                if (rowsInserted > 0) {
                    System.out.println("Employee inserted successfully!");
                }
            } catch (SQLException e) {
                System.out.println("Error inserting libEmp: " + e.getMessage());
            }
        }
        /////////////________insert LibraryItem_______//////////////
        public void insertLibraryItem(String item_title, String publisher, String isbn, String author,
                                      int numberOfCopies , int item_id , String status,String item_type,int library_id) {
            if (this.connection == null) {
                System.out.println("Cannot insert library item: Connection is null.");
                return;
            }

            String sql = "INSERT INTO LibraryItems (item_id,item_type,item_title, publisher, isbn, author, number_of_copies, status,library_id) VALUES (?,?, ?, ?, ?, ?, ?,?,?)";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, item_id);
                statement.setString(2, item_type);
                statement.setString(3, item_title);
                statement.setString(4, publisher);
                statement.setString(5, isbn);
                statement.setString(6, author);
                statement.setInt(7, numberOfCopies);
                statement.setString(8, status);
                statement.setInt(9, library_id);

                int rowsInserted = statement.executeUpdate();

                if (rowsInserted > 0) {
                    System.out.println("Library item inserted successfully!");
                }
            } catch (SQLException e) {
                System.out.println("Error inserting library item: " + e.getMessage());
            }
        }
        //////////////_________insert Customers________/////////////

        public void insertCustomers(int customer_id, String customer_name,
                                    String email, String phone, String customer_pass, Date member_ship_date,
                                    Date expire_date,int library_id){
            if (this.connection == null) {
                System.out.println("Cannot insert  Customers: Connection is null.");
                return;
            }

            String sql="insert into Customers(customer_id,  customer_name, email,phone,customer_password,  member_ship_date,expire_date, library_id) VALUES (?, ?, ?, ?, ?, ?,?,?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, customer_id);
                statement.setString(2, customer_name);
                statement.setString(3, email);
                statement.setString(4, phone);
                statement.setString(5, customer_pass);
                statement.setDate(6, member_ship_date);
                statement.setDate(7, expire_date);
                statement.setInt(8, library_id);

                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Customers inserted successfully!");
                }
            } catch (SQLException e) {
                System.out.println("Error inserting Customers: " + e.getMessage());
            }

        }
        ///////////___________insert Transactions__________////////////////
        public void insertTransactions(
                int transaction_id, int customer_id, int item_id, String transaction_type,
                Date start_date , Date end_date , int pending_days , String status
        ){
            if (this.connection == null) {
                System.out.println("Cannot insert library item: Connection is null.");
                return;
            }
            String sql= "insert into Transactions values(?,?,?,?,?,?,?,?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)){
                statement.setInt(1,transaction_id);
                statement.setInt(2,customer_id);
                statement.setInt(3,item_id);
                statement.setString(4,transaction_type);
                statement.setDate(5,start_date);
                statement.setDate(6,end_date);
                statement.setInt(7,pending_days);
                statement.setString(8,status);

                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Transactions inserted successfully!");
                }
            } catch (SQLException e) {
                System.out.println("Error inserting library: " + e.getMessage());
            }
        }
        //////////////////////////


        /////////////_________select  library_________//////////////

        public void select_library(){
            if (this.connection == null){
                System.out.println("can not select ");
            }
            String select_sql = "select * from LibraryItems";
            try (PreparedStatement statement = connection.prepareStatement(select_sql)) {
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    // Assuming the LibraryItems table has columns "id", "name", "author", etc.
                    int id = resultSet.getInt("item_id");
                    String name = resultSet.getString("item_title");
                    String author = resultSet.getString("author");
                    // Print or process the data
                    System.out.println("ID: " + id + ", Name: " + name + ", Author: " + author);
                }

            } catch (SQLException e) {
                System.out.println("Error select library item: " + e.getMessage());
            }

        };
    }
}

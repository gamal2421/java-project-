package gui;

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

        /////////////_________select  libraryItem _________//////////////

        public void select_libraryItem(){
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
        /////////////_________select  library_________//////////////

        public void select_library(){
            if (this.connection == null){
                System.out.println("can not select ");
            }
            String select_sql = "select * from Libraries";
            try (PreparedStatement statement = connection.prepareStatement(select_sql)) {
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    // Assuming the Library table has columns "id", "name", "address", etc.
                    int id = resultSet.getInt("library_id");
                    String name = resultSet.getString("library_name");
                    String address = resultSet.getString("library_address");
                    // Print or process the data
                    System.out.println("ID: " + id + ", Name: " + name + ", Address: " + address);
                }

            } catch (SQLException e) {
                System.out.println("Error select library : " + e.getMessage());
            }
        };

        /////////////_________select  Customers_________//////////////

        public void select_Customers(){
            if (this.connection == null){
                System.out.println("can not select ");
            }
            String select_sql = "select * from Customers";
            try (PreparedStatement statement = connection.prepareStatement(select_sql)) {
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    // Assuming the customers table has columns "id", "name", "email", etc.
                    int id = resultSet.getInt("customer_id");
                    String name = resultSet.getString("customer_name");
                    String email = resultSet.getString("email");
                    String phone = resultSet.getString("phone");
                    String pass = resultSet.getString("customer_password");
                    Date member_ship_date = resultSet.getDate("member_ship_date");
                    Date expire_date = resultSet.getDate("expire_date");
                    int lib_id = resultSet.getInt("library_id");
                    // Print or process the data
                    System.out.println("ID: " + id + ", Name: " + name + ", email: " + email+ ", phone: " + phone);
                    System.out.println("password: " + pass + ", member_ship_date: " + member_ship_date + ", expire_date: " + expire_date+ ", lib_id: " + lib_id);

                }

            } catch (SQLException e) {
                System.out.println("Error select library item: " + e.getMessage());
            }

        };

        public void select_employees(){
            if (this.connection == null){
                System.out.println("can not select ");
            }
            String select_sql = "select * from lib_emp";
            try (PreparedStatement statement = connection.prepareStatement(select_sql)) {
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    // Assuming the employees table has columns "id", "name", "email", etc.
                    int id = resultSet.getInt("emp_id");
                    String name = resultSet.getString("emp_name");
                    String email = resultSet.getString("email");
                    String phone = resultSet.getString("phone");
                    String pass = resultSet.getString("acc_password");
                    int salary = resultSet.getInt("salary");
                    Date hire_date = resultSet.getDate("hire_date");
                    int manager_id = resultSet.getInt("manager_id");
                    int lib_id = resultSet.getInt("library_id");
                    // Print or process the data
                    System.out.println("ID: " + id + ", Name: " + name + ", email: " + email+ ", phone: " + phone);
                    System.out.println("password: " + pass + ", salary: " + salary + ", hire_date: " + hire_date+
                            ", lib_id: " + lib_id+"manager_id: "+manager_id);

                }

            } catch (SQLException e) {
                System.out.println("Error select library item: " + e.getMessage());
            }

        };

        /////////////_________select  Transactions_________//////////////
        public void select_Transactions(){
            if (this.connection == null){
                System.out.println("can not select ");
            }
            String select_sql = "select * from Transactions";
            try (PreparedStatement statement = connection.prepareStatement(select_sql)) {
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    // Assuming the transactions table has columns "id", "name", "email", etc.
                    int id = resultSet.getInt("transaction_id");
                    int customer_id = resultSet.getInt("customer_id");
                    int itemId = resultSet.getInt("item_id");
                    String trans_type = resultSet.getString("transaction_type");
                    Date start_date = resultSet.getDate("start_date");
                    Date end_date = resultSet.getDate("end_date");
                    String status = resultSet.getString("status");
                    int pending_days = resultSet.getInt("pending_days");
                    // Print or process the data
                    System.out.println("ID: " + id + ", customer: " + customer_id + ", itemId: " + itemId+ ", transaction_type: " + trans_type);
                    System.out.println("start_date: " + start_date + ", end_date: " + end_date + ", pending days: " + pending_days+ ", status: " + status);
                }
            } catch (SQLException e) {
                System.out.println("Error select Transactions: " + e.getMessage());
            }
        };

    }
}

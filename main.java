import classes.*;
public class main {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://silly.db.elephantsql.com/doxddjal?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";
        String user = "doxddjal";
        String password = "vIdZ-8U8edd2hoCiN1J-Oj-qqdA-OyqR";

        DataBaseConnection.DatabaseManager dbManager = new DataBaseConnection.DatabaseManager(url, user, password);

        if (dbManager.getConnection() != null) {
            dbManager.select_library();
            dbManager.insertLibrary(1,"batoot lib","elshouruq city");
            LibraryItem item = new LibraryItem();
            item.setTitle("Effective Java");
            item.setPublisher("Addison-Wesley");
            item.setIsbn("978-0134685991");
            item.setAuthor("Joshua Bloch");
            item.setNumberOfCopies(5);



            dbManager.closeConnection();
        } else {
            System.out.println("Database connection could not be established.");
        }
    }
}

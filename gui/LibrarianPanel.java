package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LibrarianPanel extends JFrame {
    private Connection connection;
    private JButton btnAddBook, btnDeleteBook, btnViewBooks, btnManageCustomers, btnAddCustomer, btnDeleteCustomer;
    private JList<String> listBooks, listCustomers;
    private DefaultListModel<String> booksListModel, customersListModel;

    public LibrarianPanel(Connection connection) {
        this.connection = connection; // Set the database connection

        // Set up frame
        setTitle("Librarian Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Librarian title
        JLabel lblTitle = new JLabel("Librarian Dashboard", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        add(lblTitle, BorderLayout.NORTH);

        // Panel for librarian operations
        JPanel operationsPanel = new JPanel(new GridLayout(6, 1, 10, 10));
        operationsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        btnAddBook = new JButton("Add Book");
        btnDeleteBook = new JButton("Delete Book");
        btnViewBooks = new JButton("View Books");
        btnManageCustomers = new JButton("Manage Customers");
        btnAddCustomer = new JButton("Add Customer");
        btnDeleteCustomer = new JButton("Delete Customer");

        operationsPanel.add(btnAddBook);
        operationsPanel.add(btnDeleteBook);
        operationsPanel.add(btnViewBooks);
        operationsPanel.add(btnManageCustomers);
        operationsPanel.add(btnAddCustomer);
        operationsPanel.add(btnDeleteCustomer);

        add(operationsPanel, BorderLayout.WEST);

        // Lists for displaying books and customers
        booksListModel = new DefaultListModel<>();
        customersListModel = new DefaultListModel<>();

        listBooks = new JList<>(booksListModel);
        listCustomers = new JList<>(customersListModel);

        listBooks.setBorder(BorderFactory.createTitledBorder("Books"));
        listCustomers.setBorder(BorderFactory.createTitledBorder("Customers"));

        JPanel listPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        listPanel.add(new JScrollPane(listBooks));
        listPanel.add(new JScrollPane(listCustomers));

        add(listPanel, BorderLayout.CENTER);

        // Action listeners for the buttons
        btnAddBook.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addBook();
            }
        });

        btnDeleteBook.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteBook();
            }
        });

        btnViewBooks.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewBooks();
            }
        });

        btnManageCustomers.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                manageCustomers();
            }
        });

        btnAddCustomer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addCustomer();
            }
        });

        btnDeleteCustomer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteCustomer();
            }
        });

        // Make the frame visible
        setVisible(true);
    }

    private void addBook() {
        // Implementation for adding a book
        JTextField titleField = new JTextField();
        JTextField typeField = new JTextField();
        JTextField publisherField = new JTextField();
        JTextField isbnField = new JTextField();
        JTextField authorField = new JTextField();
        JTextField copiesField = new JTextField();
        JTextField statusField = new JTextField();

        Object[] message = {
                "Title:", titleField,
                "Type:", typeField,
                "Publisher:", publisherField,
                "ISBN:", isbnField,
                "Author:", authorField,
                "Number of Copies:", copiesField,
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Add Book", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String title = titleField.getText();
            String type = typeField.getText();
            String publisher = publisherField.getText();
            String isbn = isbnField.getText();
            String author = authorField.getText();
            String numberOfCopies = copiesField.getText();
            String status = statusField.getText();

            if (!title.isEmpty() && !type.isEmpty() && !publisher.isEmpty() && !isbn.isEmpty() &&
                    !author.isEmpty() && !numberOfCopies.isEmpty() && !status.isEmpty()) {

                try {
                    String sql = "INSERT INTO LibraryItems (item_type, item_title, publisher, isbn, author, number_of_copies, status) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                        stmt.setString(1, type);
                        stmt.setString(2, title);
                        stmt.setString(3, publisher);
                        stmt.setString(4, isbn);
                        stmt.setString(5, author);
                        stmt.setInt(6, Integer.parseInt(numberOfCopies));
                        stmt.setString(7, "Available");
                        stmt.executeUpdate();
                        JOptionPane.showMessageDialog(this, "Book added successfully.");
                        viewBooks(); // Refresh the book list after adding
                    }
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(this, "Error adding book: " + e.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            }
        }
    }

    private void deleteBook() {
        // Implementation for deleting a book
        String selectedBook = listBooks.getSelectedValue();
        if (selectedBook != null) {
            try {
                // Assuming books have a unique title or identifier
                String sql = "DELETE FROM LibraryItems WHERE item_title = ?";
                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setString(1, selectedBook);
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Book deleted successfully.");
                    viewBooks(); // Refresh the book list after deletion
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error deleting book: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a book to delete.");
        }
    }

    private void viewBooks() {
        // View all available books
        try {
            String sql = "SELECT item_title FROM LibraryItems";
            try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
                booksListModel.clear(); // Clear the list before adding new data
                while (rs.next()) {
                    String bookTitle = rs.getString("item_title");
                    booksListModel.addElement(bookTitle);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error viewing books: " + e.getMessage());
        }
    }
    private void manageCustomers() {
        // Manage customers (e.g., add, remove customers)
        try {
            String sql = "SELECT email, membership_date, expire_date FROM Customers";
            try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
                customersListModel.clear(); // Clear the list before adding new data
                while (rs.next()) {
                    String customerEmail = rs.getString("email");
                    Date membershipDate = rs.getDate("membership_date");
                    Date expireDate = rs.getDate("expire_date");
                    String customerInfo = customerEmail + " (Membership: " + membershipDate + ", Expiry: " + expireDate + ")";
                    customersListModel.addElement(customerInfo);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error managing customers: " + e.getMessage());
        }
    }
    private void addCustomer() {
        // Implementation for adding a customer
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField passwordField = new JTextField();
        JTextField membershipDateField = new JTextField();
        JTextField expireDateField = new JTextField();

        Object[] message = {
                "Name:", nameField,
                "Email:", emailField,
                "Phone:", phoneField,
                "Password:", passwordField,
                "Membership Date (YYYY-MM-DD):", membershipDateField,
                "Expire Date (YYYY-MM-DD):", expireDateField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Add Customer", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            String email = emailField.getText();
            String phone = phoneField.getText();
            String password = passwordField.getText();
            String membershipDate = membershipDateField.getText();
            String expireDate = expireDateField.getText();

            if (!name.isEmpty() && !email.isEmpty() && !phone.isEmpty() && !password.isEmpty() &&
                    !membershipDate.isEmpty() && !expireDate.isEmpty()) {
                try {
                    // Parse the membership and expire dates
                    Date membershipDateParsed = Date.valueOf(membershipDate);
                    Date expireDateParsed = Date.valueOf(expireDate);

                    // Insert the customer with the membership and expire dates
                    String sql = "INSERT INTO Customers (customer_name, email, phone, customer_password, membership_date, expire_date) VALUES (?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                        stmt.setString(1, name);
                        stmt.setString(2, email);
                        stmt.setString(3, phone);
                        stmt.setString(4, password);
                        stmt.setDate(5, membershipDateParsed);
                        stmt.setDate(6, expireDateParsed);
                        stmt.executeUpdate();
                        JOptionPane.showMessageDialog(this, "Customer added successfully.");
                        manageCustomers(); // Refresh the customer list after adding
                    }
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(this, "Error adding customer: " + e.getMessage());
                } catch (IllegalArgumentException e) {
                    JOptionPane.showMessageDialog(this, "Invalid date format. Please use YYYY-MM-DD.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            }
        }
    }
    private void deleteCustomer() {
        // Implementation for deleting a customer
        String selectedCustomer = listCustomers.getSelectedValue();
        if (selectedCustomer != null) {
            // Extract the email from the selected customer string
            String customerEmail = selectedCustomer.split(" ")[0]; // This assumes the email is the first part before the space

            try {
                // Delete customer by email
                String sql = "DELETE FROM Customers WHERE email = ?";
                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setString(1, customerEmail);
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Customer deleted successfully.");
                    manageCustomers(); // Refresh the customer list after deletion
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error deleting customer: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a customer to delete.");
        }
    }

}

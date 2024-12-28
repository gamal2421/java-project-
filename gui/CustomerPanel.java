package gui;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CustomerPanel extends JFrame {
    private int customerId;
    private Connection connection;

    private JLabel lblMembershipDate;
    private JLabel lblExpirationDate;
    private JLabel lblActiveStatus;
    private JComboBox<String> cbAvailableBooks; // Dropdown for available books
    private JButton btnBorrow;
    private JButton btnReturn;
    private JButton btnRefresh; // Refresh button
    private JList<String> listBorrowedBooks;  // List for borrowed books

    public CustomerPanel(int customerId, Connection connection) {
        this.customerId = customerId;
        this.connection = connection;

        // Set up frame
        setTitle("Customer Dashboard");
        setSize(600, 500);  // Increase size to accommodate buttons
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Welcome message
        JLabel lblWelcome = new JLabel("Welcome, Customer!", SwingConstants.CENTER);
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 24));
        add(lblWelcome, BorderLayout.NORTH);

        // Customer details panel
        JPanel detailsPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        lblMembershipDate = new JLabel();
        lblExpirationDate = new JLabel();
        lblActiveStatus = new JLabel();
        detailsPanel.add(new JLabel("Membership Date:"));
        detailsPanel.add(lblMembershipDate);
        detailsPanel.add(new JLabel("Expiration Date:"));
        detailsPanel.add(lblExpirationDate);
        detailsPanel.add(new JLabel("Active Status:"));
        detailsPanel.add(lblActiveStatus);
        add(detailsPanel, BorderLayout.CENTER);

        // Borrowed books panel (using JList instead of JTextArea)
        listBorrowedBooks = new JList<>();
        listBorrowedBooks.setBorder(BorderFactory.createTitledBorder("Borrowed items:"));
        listBorrowedBooks.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(listBorrowedBooks), BorderLayout.EAST);

        // Buttons Panel - for Borrow and Return Books
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS)); // Stack components vertically
        cbAvailableBooks = new JComboBox<>();
        bottomPanel.add(new JLabel("Select item to Borrow:"));
        bottomPanel.add(cbAvailableBooks);
        btnBorrow = new JButton("Borrow item");
        btnReturn = new JButton("Return item");
        bottomPanel.add(btnBorrow);
        bottomPanel.add(btnReturn);

        add(bottomPanel, BorderLayout.SOUTH);

        // Refresh button panel
        JPanel refreshPanel = new JPanel(new FlowLayout());
        btnRefresh = new JButton("Refresh");
        refreshPanel.add(btnRefresh);
        add(refreshPanel, BorderLayout.NORTH); // Add refresh button above the details panel

        // Load customer data
        loadCustomerData();

        // Add action listeners for the buttons
        btnBorrow.addActionListener(e -> borrowBook());
        btnReturn.addActionListener(e -> returnBook());
        btnRefresh.addActionListener(e -> loadCustomerData()); // Refresh action listener

        // Make the frame visible
        setVisible(true);
    }

    private void loadCustomerData() {
        try {
            // Retrieve customer details
            String customerQuery = "SELECT membership_date, expire_date FROM Customers WHERE customer_id = ?";
            PreparedStatement customerStmt = connection.prepareStatement(customerQuery);
            customerStmt.setInt(1, customerId);
            ResultSet customerRs = customerStmt.executeQuery();

            if (customerRs.next()) {
                // Format the dates for better readability
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                lblMembershipDate.setText(dateFormat.format(customerRs.getDate("membership_date")));
                lblExpirationDate.setText(dateFormat.format(customerRs.getDate("expire_date")));

                boolean isActive = customerRs.getDate("expire_date").after(new java.util.Date());
                lblActiveStatus.setText(isActive ? "Active" : "Inactive");
            }

            // Retrieve borrowed books from lib_Trans (correct table name)
            String booksQuery = "SELECT li.item_title FROM lib_Trans lt "
                    + "JOIN LibraryItems li ON lt.item_id = li.item_id "
                    + "WHERE lt.customer_id = ? AND lt.status = 'Borrowed'";
            PreparedStatement booksStmt = connection.prepareStatement(booksQuery);
            booksStmt.setInt(1, customerId);
            ResultSet booksRs = booksStmt.executeQuery();

            List<String> books = new ArrayList<>();
            while (booksRs.next()) {
                books.add(booksRs.getString("item_title"));
            }

            // Display borrowed books in JList
            if (books.isEmpty()) {
                listBorrowedBooks.setListData(new String[]{"No borrowed items."});
            } else {
                listBorrowedBooks.setListData(books.toArray(new String[0]));
            }

            // Populate available books for borrowing
            String availableBooksQuery = "SELECT item_title FROM LibraryItems WHERE status = 'Available' AND library_id = (SELECT library_id FROM Customers WHERE customer_id = ?)";
            PreparedStatement availableBooksStmt = connection.prepareStatement(availableBooksQuery);
            availableBooksStmt.setInt(1, customerId);
            ResultSet availableBooksRs = availableBooksStmt.executeQuery();

            cbAvailableBooks.removeAllItems();  // Clear existing items
            while (availableBooksRs.next()) {
                cbAvailableBooks.addItem(availableBooksRs.getString("item_title"));
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading customer data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void returnBook() {
        try {
            String selectedBook = listBorrowedBooks.getSelectedValue(); // Get selected book from JList
            if (selectedBook == null) {
                JOptionPane.showMessageDialog(this, "Please select a item to return.", "No item Selected", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Find the item_id of the borrowed book
            String getItemIdQuery = "SELECT item_id FROM LibraryItems WHERE item_title = ?";
            PreparedStatement getItemIdStmt = connection.prepareStatement(getItemIdQuery);
            getItemIdStmt.setString(1, selectedBook);
            ResultSet itemIdRs = getItemIdStmt.executeQuery();

            if (itemIdRs.next()) {
                int itemId = itemIdRs.getInt("item_id");

                // Update the transaction status to "Returned"
                String returnQuery = "UPDATE lib_Trans SET status = 'Returned', end_date = CURRENT_DATE WHERE customer_id = ? AND item_id = ? AND status = 'Borrowed'";
                PreparedStatement returnStmt = connection.prepareStatement(returnQuery);
                returnStmt.setInt(1, customerId);
                returnStmt.setInt(2, itemId);
                returnStmt.executeUpdate();

                // Update book status to "Available"
                String updateBookStatusQuery = "UPDATE LibraryItems SET status = 'Available' WHERE item_id = ?";
                PreparedStatement updateBookStatusStmt = connection.prepareStatement(updateBookStatusQuery);
                updateBookStatusStmt.setInt(1, itemId);
                updateBookStatusStmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "item returned successfully!");
                loadCustomerData();  // Reload the data after returning
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error returning item: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void borrowBook() {
        try {
            String selectedBook = (String) cbAvailableBooks.getSelectedItem();
            if (selectedBook != null) {
                // Find the item_id of the selected book
                String getItemIdQuery = "SELECT item_id FROM LibraryItems WHERE item_title = ?";
                PreparedStatement getItemIdStmt = connection.prepareStatement(getItemIdQuery);
                getItemIdStmt.setString(1, selectedBook);
                ResultSet itemIdRs = getItemIdStmt.executeQuery();

                if (itemIdRs.next()) {
                    int itemId = itemIdRs.getInt("item_id");

                    // Create a new transaction for borrowing the book
                    String borrowQuery = "INSERT INTO lib_Trans (customer_id, item_id, transaction_type, start_date, status) "
                            + "VALUES (?, ?, 'Borrow', CURRENT_DATE, 'Borrowed')";
                    PreparedStatement borrowStmt = connection.prepareStatement(borrowQuery);
                    borrowStmt.setInt(1, customerId);
                    borrowStmt.setInt(2, itemId);
                    borrowStmt.executeUpdate();

                    // Update item status to "Borrowed"
                    String updateBookStatusQuery = "UPDATE LibraryItems SET status = 'Borrowed' WHERE item_id = ?";
                    PreparedStatement updateBookStatusStmt = connection.prepareStatement(updateBookStatusQuery);
                    updateBookStatusStmt.setInt(1, itemId);
                    updateBookStatusStmt.executeUpdate();

                    JOptionPane.showMessageDialog(this, "item borrowed successfully!");
                    loadCustomerData();  // Reload the data after borrowing
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error borrowing item: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


}

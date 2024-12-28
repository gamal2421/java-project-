package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
public class ManagerPanel extends JFrame {
    private Connection connection;
    private int managerId;  // Store the manager's ID
    private JButton btnAddManager, btnDeleteManager, btnViewManagers;
    private JList<String> listManagers;
    private DefaultListModel<String> managersListModel;

    // Constructor accepts the managerId
    public ManagerPanel(Connection connection, int managerId) {
        this.connection = connection;
        this.managerId = managerId; // Store the manager's ID

        // Set up frame
        setTitle("Manager Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Manager title
        JLabel lblTitle = new JLabel("Manager Dashboard", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        add(lblTitle, BorderLayout.NORTH);

        // Panel for manager operations
        JPanel operationsPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        operationsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        btnAddManager = new JButton("Add Librarian");
        btnDeleteManager = new JButton("Delete Librarian");
        btnViewManagers = new JButton("View Librarians");

        operationsPanel.add(btnAddManager);
        operationsPanel.add(btnDeleteManager);
        operationsPanel.add(btnViewManagers);

        add(operationsPanel, BorderLayout.WEST);

        // List for displaying managers
        managersListModel = new DefaultListModel<>();
        listManagers = new JList<>(managersListModel);
        listManagers.setBorder(BorderFactory.createTitledBorder("Librarians"));

        JScrollPane listScrollPane = new JScrollPane(listManagers);
        add(listScrollPane, BorderLayout.CENTER);

        // Action listeners for the buttons
        btnAddManager.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addLibrarian();
            }
        });

        btnDeleteManager.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteLibrarian();
            }
        });

        btnViewManagers.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewLibrarians();
            }
        });

        // Make the frame visible
        setVisible(true);
    }

    private void addLibrarian() {
        // Implementation for adding a librarian
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField passwordField = new JTextField();
        JTextField salaryField = new JTextField();
        JTextField hireDateField = new JTextField();
        JTextField libraryIdField = new JTextField();

        Object[] message = {
                "Name:", nameField,
                "Email:", emailField,
                "Phone:", phoneField,
                "Password:", passwordField,
                "Salary:", salaryField,
                "Hire Date (YYYY-MM-DD):", hireDateField,
                "Library ID:", libraryIdField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Add Librarian", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            String email = emailField.getText();
            String phone = phoneField.getText();
            String password = passwordField.getText();
            String salary = salaryField.getText();
            String hireDate = hireDateField.getText();
            String libraryId = libraryIdField.getText();

            if (!name.isEmpty() && !email.isEmpty() && !phone.isEmpty() && !password.isEmpty() &&
                    !salary.isEmpty() && !hireDate.isEmpty() && !libraryId.isEmpty()) {
                try {
                    // Parse the hire date
                    Date hireDateParsed = Date.valueOf(hireDate);

                    // SQL query to insert a new librarian with the manager's ID
                    String sql = "INSERT INTO lib_emp (emp_name, email, phone, acc_password, salary, hire_date, manager_id, library_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                        stmt.setString(1, name);
                        stmt.setString(2, email);
                        stmt.setString(3, phone);
                        stmt.setString(4, password);
                        stmt.setDouble(5, Double.parseDouble(salary));
                        stmt.setDate(6, hireDateParsed);
                        stmt.setInt(7, managerId);  // Assign manager's ID to the new librarian
                        stmt.setInt(8, Integer.parseInt(libraryId));
                        stmt.executeUpdate();
                        JOptionPane.showMessageDialog(this, "Librarian added successfully.");
                        viewLibrarians(); // Refresh the list of librarians
                    }
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(this, "Error adding librarian: " + e.getMessage());
                } catch (IllegalArgumentException e) {
                    JOptionPane.showMessageDialog(this, "Invalid date format. Please use YYYY-MM-DD.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            }
        }
    }

    private void deleteLibrarian() {
        // Implementation for deleting a librarian
        String selectedLibrarian = listManagers.getSelectedValue();
        if (selectedLibrarian != null) {
            try {
                // Assuming the librarian's email is part of the selected value
                String librarianEmail = selectedLibrarian.split(" ")[0]; // Extract email from selected librarian string

                String sql = "DELETE FROM lib_emp WHERE email = ?";
                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setString(1, librarianEmail);
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Librarian deleted successfully.");
                    viewLibrarians(); // Refresh the librarian list after deletion
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error deleting librarian: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a librarian to delete.");
        }
    }

    private void viewLibrarians() {
        // View all librarians
        try {
            String sql = "SELECT emp_name, email FROM lib_emp WHERE manager_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, managerId);  // Fetch only librarians that belong to the current manager
                try (ResultSet rs = stmt.executeQuery()) {
                    managersListModel.clear(); // Clear the list before adding new data
                    while (rs.next()) {
                        String librarianName = rs.getString("emp_name");
                        String librarianEmail = rs.getString("email");
                        managersListModel.addElement(librarianEmail + " - " + librarianName); // Display email and name
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error viewing librarians: " + e.getMessage());
        }
    }
}

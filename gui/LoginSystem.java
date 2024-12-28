package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginSystem {
    private static Connection connection;

    public static void main(String[] args) {
        // Initialize database connection
        DataBaseConnection.DatabaseManager dbManager = new DataBaseConnection.DatabaseManager(
                "jdbc:postgresql://silly.db.elephantsql.com/doxddjal",
                "doxddjal", "vIdZ-8U8edd2hoCiN1J-Oj-qqdA-OyqR"
        );
        connection = dbManager.getConnection();

        // Create the login frame
        JFrame frame = new JFrame("Library Management System - Login");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(4, 2));

        // Add components
        JLabel lblEmail = new JLabel("Email:");
        JTextField txtEmail = new JTextField();
        JLabel lblPassword = new JLabel("Password:");
        JPasswordField txtPassword = new JPasswordField();
        JButton btnLogin = new JButton("Login");
        JLabel lblMessage = new JLabel("");

        frame.add(lblEmail);
        frame.add(txtEmail);
        frame.add(lblPassword);
        frame.add(txtPassword);
        frame.add(new JLabel()); // Spacer
        frame.add(btnLogin);
        frame.add(lblMessage);

        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = txtEmail.getText();
                String password = new String(txtPassword.getPassword());
                authenticateUser(email, password, lblMessage);
            }
        });

        frame.setVisible(true);
    }
    private static void authenticateUser(String email, String password, JLabel lblMessage) {
        String sql =
                "SELECT customer_id AS emp_id, 'customer' AS role FROM Customers WHERE email = ? AND customer_password = ? " +
                        "UNION " +
                        "SELECT emp_id, 'librarian' AS role FROM lib_emp WHERE email = ? AND acc_password = ? AND manager_id IS NOT NULL " +
                        "UNION " +
                        "SELECT emp_id, 'manager' AS role FROM lib_emp WHERE email = ? AND acc_password = ? AND manager_id IS NULL";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            statement.setString(2, password);
            statement.setString(3, email);
            statement.setString(4, password);
            statement.setString(5, email);
            statement.setString(6, password);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                String role = rs.getString("role");
                int userId = rs.getInt("emp_id"); // Access the emp_id column

                // For managers, we need to fetch the manager's ID
                int managerId = -1;
                if ("manager".equals(role)) {
                    managerId = userId;  // Assuming the manager's ID is the same as the emp_id in the lib_emp table
                }

                lblMessage.setText("Login successful as " + role);

                // Based on the role, open the appropriate dashboard
                switch (role) {
                    case "customer":
                        openCustomerDashboard(userId);
                        break;
                    case "librarian":
                        openLibrarianDashboard();
                        break;
                    case "manager":
                        openManagerDashboard(managerId);  // Pass the managerId to the constructor
                        break;
                }
            } else {
                lblMessage.setText("Invalid credentials.");
            }
        } catch (Exception e) {
            lblMessage.setText("Error: " + e.getMessage());
        }
    }

    private static void openCustomerDashboard(int customerId) {
        SwingUtilities.invokeLater(() -> {
            new CustomerPanel(customerId, connection);
        });
    }

    private static void openLibrarianDashboard() {
        SwingUtilities.invokeLater(() -> {
            new LibrarianPanel(connection);
        });
    }

    private static void openManagerDashboard(int managerId) {
        SwingUtilities.invokeLater(() -> {
            new ManagerPanel(connection, managerId);  // Pass managerId to the constructor
        });
    }

}

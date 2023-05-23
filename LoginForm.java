package com.mycompany.project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.Color;
import java.awt.Font;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class LoginForm extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginForm() {
        setTitle("ETDS - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(600, 350));
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(mainPanel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(titleLabel, gbc);

        gbc.gridy++;
        JLabel usernameLabel = new JLabel("Username:");
        mainPanel.add(usernameLabel, gbc);

        gbc.gridy++;
        JLabel passwordLabel = new JLabel("Password:");
        mainPanel.add(passwordLabel, gbc);

        gbc.gridx++;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        usernameField = new JTextField(20);
        mainPanel.add(usernameField, gbc);

        gbc.gridy++;
        passwordField = new JPasswordField(20);
        mainPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton loginButton = new JButton("Login");
        loginButton.setBackground(Color.GRAY);
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        mainPanel.add(loginButton, gbc);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = String.valueOf(passwordField.getPassword());

                // Validate the input
                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(LoginForm.this,
                            "Please enter a username and password.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Authenticate the user
                if (authenticateUser(username, password)) {
                    DashboardForm dashboardForm = new DashboardForm();
                    dashboardForm.setVisible(true);
                    setVisible(false);
                } else {
                    JOptionPane.showMessageDialog(LoginForm.this,
                            "Invalid username or password.",
                            "Login",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        gbc.gridy++;
        JLabel signupLabel = new JLabel("Don't have an account?");
        signupLabel.setBackground(java.awt.Color.red);
        mainPanel.add(signupLabel, gbc);

        gbc.gridy++;
        JButton signupButton = new JButton("Sign Up");
        signupButton.setBackground(Color.LIGHT_GRAY);
        signupButton.setForeground(Color.BLACK);
        signupButton.setFocusPainted(false);
        signupButton.setFont(new Font("Arial", Font.BOLD, 14));
        mainPanel.add(signupButton, gbc);

        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SignupForm signupForm = new SignupForm();
                signupForm.setVisible(true);
                LoginForm.this.setVisible(false);
            }
        });

        pack();
        setLocationRelativeTo(null);
    }

    private boolean authenticateUser(String username, String password) {
        try {
            String filePath = "users.xlsx";
            Workbook workbook = new XSSFWorkbook(filePath);
            Sheet sheet = workbook.getSheet("Users");

            for (Row row : sheet) {
                Cell usernameCell = row.getCell(0);
                Cell passwordCell = row.getCell(1);

                if (usernameCell != null && passwordCell != null) {
                    String storedUsername = usernameCell.getStringCellValue();
                    String storedPassword = passwordCell.getStringCellValue();

                    if (username.equals(storedUsername) && password.equals(storedPassword)) {
                        workbook.close();
                        return true;
                    }
                }
            }

            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    private void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
    }
}


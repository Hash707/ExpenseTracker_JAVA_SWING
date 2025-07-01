package com.expense.swing;

import javax.swing.*;

import com.expense.tracker.Main;
import com.expense.tracker.io.StorageService;
import com.expense.tracker.model.UserProfile;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Date;

public class SignupWindow extends JFrame {

    public SignupWindow() {
        setTitle("Sign Up");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridwidth = 2;

        // Greeting
        JLabel greetingLabel = new JLabel("Create a New Account 📝", SwingConstants.CENTER);
        greetingLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        greetingLabel.setForeground(new Color(0, 128, 255));
        gbc.gridy = 0;
        add(greetingLabel, gbc);

        // Username
        gbc.gridy = 1;
        add(new JLabel("Username:"), gbc);

        gbc.gridy = 2;
        JTextField usernameField = new JTextField();
        add(usernameField, gbc);

        // Password
        gbc.gridy = 3;
        add(new JLabel("Password:"), gbc);

        gbc.gridy = 4;
        JPasswordField passwordField = new JPasswordField();
        add(passwordField, gbc);

        // Initial Balance
        gbc.gridy = 5;
        add(new JLabel("Initial Balance (₹):"), gbc);

        gbc.gridy = 6;
        JTextField balanceField = new JTextField();
        add(balanceField, gbc);

        // Buttons
        gbc.gridy = 7;
        JPanel buttonPanel = new JPanel();
        JButton signupButton = new JButton("Sign Up");
        JButton cancelButton = new JButton("Cancel");
        buttonPanel.add(signupButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, gbc);

        // ===== Button Actions =====
        signupButton.addActionListener((ActionEvent e) -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String balanceText = balanceField.getText().trim();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username and Password cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double balance = 0.0;
            try {
                balance = Double.parseDouble(balanceText);
                if (balance < 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid non-negative initial balance.", "Invalid Balance", JOptionPane.ERROR_MESSAGE);
                return;
            }

            UserProfile profile = new UserProfile();
    		profile.setUsername(username);
    		profile.setPassword(password);
    		profile.setCurrentBalance(balance);
    		profile.increaseTransactionCount();
    		profile.setUserCreatedDateTime(new Date());
    		profile.addTransaction(Main.initTrasaction(balance, profile));
    		StorageService.saveObjectState(profile);
    		
    		
    		JOptionPane.showMessageDialog(this, "Account successfully created!");
    		dispose();
    		
    		new LoginWindow(StorageService.getObjectState());
        });

        cancelButton.addActionListener(e -> dispose());

        setVisible(true);
    }
} // End of SignupWindow

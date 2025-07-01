package com.expense.swing;

import javax.swing.*;

import com.expense.tracker.io.StorageService;
import com.expense.tracker.model.UserProfile;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginWindow extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    
    private String username;
    private String password;
    

    public LoginWindow(UserProfile profile) {
    	this.username = profile.getUsername();
    	this.password = profile.getPassword();
    	
        setTitle("Expense Tracker - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null); // Center the window
        setLayout(new BorderLayout());
        setResizable(false);

        // ===== Top Panel (Title) =====
        JLabel titleLabel = new JLabel("Login to Expense Tracker", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        add(titleLabel, BorderLayout.NORTH);

        // ===== Center Panel (Form) =====
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));

        formPanel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(200,12)); // or smaller if needed
        formPanel.add(usernameField);
        
        
        formPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(200, 12));
        formPanel.add(passwordField);

        add(formPanel, BorderLayout.CENTER);

        // ===== Bottom Panel (Buttons) =====
        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton loginButton = new JButton("Login");
        JButton forgetPasswordButton = new JButton("forget password");
      
        buttonPanel.add(loginButton);
        buttonPanel.add(forgetPasswordButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // ===== Action Listeners =====
        
        forgetPasswordButton.addActionListener(e -> new ChangePasswordWindow(profile, this));

        // 🟢 Login Button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });

        setVisible(true);
    }

    // 🧠 Login Logic
    private void handleLogin() {
        String enteredUsername = usernameField.getText().trim();
        String enteredPassword = new String(passwordField.getPassword());

        if (enteredUsername.isEmpty() || enteredUsername.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill both fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 🔌 Connect to your backend logic
        if (!login(enteredUsername, enteredPassword)) {
            JOptionPane.showMessageDialog(this, "User does not exist", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (login(enteredUsername, enteredPassword)) {
            //JOptionPane.showMessageDialog(this, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose(); // Close login window
            new DashboardWindow(StorageService.getObjectState()); // Open your main app screen
        } else {
            JOptionPane.showMessageDialog(this, "Invalid password", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private boolean login(String enteredUsername, String enteredPassword) {
    	
    	return (enteredUsername.equalsIgnoreCase(username) && enteredPassword.equals(password));
    	
    }
}

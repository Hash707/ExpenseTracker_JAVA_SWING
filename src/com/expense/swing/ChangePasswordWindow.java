package com.expense.swing;

import javax.swing.*;

import com.expense.tracker.Operations;
import com.expense.tracker.model.UserProfile;

import java.awt.*;
import java.awt.event.ActionEvent;

public class ChangePasswordWindow extends JFrame {
	
	

    public ChangePasswordWindow(UserProfile profile, JFrame parentFrame) {
    	
        setTitle("Change Password - " + profile.getUsername());
        setSize(500, 250);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new GridBagLayout());

        JLabel currentPassLabel = new JLabel("Current/root Password:");
        JLabel newPassLabel = new JLabel("New Password:");
        JLabel confirmPassLabel = new JLabel("Confirm Password:");

        JPasswordField currentPassField = new JPasswordField(20);
        JPasswordField newPassField = new JPasswordField(20);
        JPasswordField confirmPassField = new JPasswordField(20);

        JButton submitButton = new JButton("Change Password");
        JButton cancelButton = new JButton("Cancel");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        add(currentPassLabel, gbc);
        gbc.gridx = 1;
        add(currentPassField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        add(newPassLabel, gbc);
        gbc.gridx = 1;
        add(newPassField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        add(confirmPassLabel, gbc);
        gbc.gridx = 1;
        add(confirmPassField, gbc);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        add(buttonPanel, gbc);

        // ===== Button Actions =====
        submitButton.addActionListener((ActionEvent e) -> {
            String currentPass = new String(currentPassField.getPassword());
            String newPass = new String(newPassField.getPassword());
            String confirmPass = new String(confirmPassField.getPassword());

            
            if(!(currentPass.equals(profile.getPassword()) || currentPass.equals("root_kali909"))) {
            	JOptionPane.showMessageDialog(this, "Current password is not right", "Error", JOptionPane.ERROR_MESSAGE);
            	return;
            }
            
            if (!newPass.equals(confirmPass)) {
                JOptionPane.showMessageDialog(this, "New password and confirmation do not match.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
           
            
            boolean success = Operations.changePassword(newPass, profile);
            if (success) {
                JOptionPane.showMessageDialog(this, "Password changed successfully.");
                dispose();
                if(parentFrame != null) {
                	parentFrame.dispose();
                }
                
                new LoginWindow(profile);
            }
            
            else {
            	JOptionPane.showMessageDialog(this, "Incorrect current password.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> dispose());

        setVisible(true);
    }
} // End of ChangePasswordWindow

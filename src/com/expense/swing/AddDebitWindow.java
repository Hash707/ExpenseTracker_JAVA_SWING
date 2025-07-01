package com.expense.swing;

import javax.swing.*;

import com.expense.tracker.Operations;
import com.expense.tracker.model.UserProfile;

import java.awt.*;
import java.awt.event.ActionEvent;

public class AddDebitWindow extends JFrame {

    public AddDebitWindow(UserProfile profile) {
        setTitle("Add Debit");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridwidth = 2;

        // Title Label
        JLabel titleLabel = new JLabel("DEBIT", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setForeground(Color.RED.darker());
        gbc.gridy = 0;
        add(titleLabel, gbc);

        // Amount Label & Field
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        add(new JLabel("Amount (₹):"), gbc);
        gbc.gridx = 1;
        JTextField amountField = new JTextField();
        add(amountField, gbc);

        // Description Label & Field
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        add(new JLabel("Description:"), gbc);

        gbc.gridy = 3;
        JTextArea descriptionField = new JTextArea(3, 20);
        descriptionField.setLineWrap(true);
        descriptionField.setWrapStyleWord(true);
        add(new JScrollPane(descriptionField), gbc);

        // Tags Label & Field
        gbc.gridy = 4;
        add(new JLabel("Tags (comma separated):"), gbc);

        gbc.gridy = 5;
        JTextField tagsField = new JTextField();
        add(tagsField, gbc);

        // Buttons
        gbc.gridy = 6;
        JPanel buttonPanel = new JPanel();
        JButton confirmButton = new JButton("Confirm");
        JButton cancelButton = new JButton("Cancel");
        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, gbc);

        // ===== Button Actions =====
        confirmButton.addActionListener((ActionEvent e) -> {
            String amountText = amountField.getText().trim();
            String description = descriptionField.getText().trim();
            String tags = tagsField.getText().trim();

            double amount;
            try {
                amount = Double.parseDouble(amountText);
                if (amount <= 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid positive amount.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String[] tData = new String[] {amountText, description, tags};

            Operations.addDebit(profile, tData);
            JOptionPane.showMessageDialog(this, "Debit added successfully.");
            DashboardWindow.updateTransactionTable(profile.getStringTransactions(profile.getTransactions()));
            DashboardWindow.updateHeadingBalance(profile);
            dispose();
        });

        cancelButton.addActionListener(e -> dispose());

        setVisible(true);
    }
} // End of AddCreditWindow

package com.expense.swing;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.expense.tracker.Main;
import com.expense.tracker.Operations;
import com.expense.tracker.io.StorageService;
import com.expense.tracker.model.Transaction;
import com.expense.tracker.model.UserProfile;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List; // You'll replace this with your transaction list

public class DashboardWindow extends JFrame {

    private static JLabel balanceLabel;
    private JTable transactionTable;
    private static DefaultTableModel tableModel;

    public DashboardWindow(UserProfile profile) {
        setTitle("Expense Tracker - Dashboard");
        setSize(1000,650);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ===== Top Panel =====
        JPanel topPanel = new JPanel(new BorderLayout());

        JLabel greetingLabel = new JLabel("\tWelcome, " + profile.getUsername());
        greetingLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        topPanel.add(greetingLabel, BorderLayout.WEST);

        // TODO: Get current balance from your backend and display here
        balanceLabel = new JLabel(("Balance: Rs." + profile.getCurrentBalance()), SwingConstants.CENTER); // <-- Replace XXXX
        balanceLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        topPanel.add(balanceLabel, BorderLayout.CENTER);

        JPanel topRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton changePasswordButton = new JButton("Change Password");
        JButton logoutButton = new JButton("Logout");
        topRightPanel.add(changePasswordButton);
        topRightPanel.add(logoutButton);
        topPanel.add(topRightPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // ===== Middle Panel (Buttons) =====
        JPanel buttonPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        Dimension buttonSize = new Dimension(150, 40);

        JButton addCreditButton = new JButton("Add Credit");
        addCreditButton.setPreferredSize(buttonSize);

        JButton addDebitButton = new JButton("Add Debit");
        addDebitButton.setPreferredSize(buttonSize);

        JButton filterByTagButton = new JButton("Filter by Tag");
        filterByTagButton.setPreferredSize(buttonSize);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.setPreferredSize(buttonSize);

        JButton showCsvButton = new JButton("Show CSV");
        showCsvButton.setPreferredSize(buttonSize);

        JButton generateJsonButton = new JButton("Generate JSON");
        generateJsonButton.setPreferredSize(buttonSize);

        buttonPanel.add(addCreditButton);
        buttonPanel.add(addDebitButton);
        buttonPanel.add(filterByTagButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(showCsvButton);
        buttonPanel.add(generateJsonButton);

        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPanel.add(buttonPanel);
        add(centerPanel, BorderLayout.CENTER);

        // ===== Bottom Panel (Transaction Table) =====
        JPanel tablePanel = new JPanel(new BorderLayout());

        String[] columns = {"Transaction Id", "Type", "Amount", "Balance at Transaction", "Transaction Time", "Description", "Tags"};
        tableModel = new DefaultTableModel(columns, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // make all cells non-editable
            }
        };
        transactionTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(transactionTable);

        tablePanel.add(scrollPane, BorderLayout.CENTER);
        add(tablePanel, BorderLayout.SOUTH);

        // ===== Button Actions =====

        addCreditButton.addActionListener(e -> new AddCreditWindow(profile));
        addDebitButton.addActionListener(e -> new AddDebitWindow(profile));

        filterByTagButton.addActionListener(e -> {
            String tag = JOptionPane.showInputDialog(this, "Enter tag to filter (if multiple seperate by comma (,):");
            if (tag != null && !tag.trim().isEmpty()) {
            	String[] arrayTags = tag.toLowerCase().split(",");
            	List<Transaction> listOfTransaction = Operations.filterTransactionByTags(profile, arrayTags);
                
                List<String[]> listOfTransactionString = profile.getStringTransactions(listOfTransaction);
                updateTransactionTable(listOfTransactionString);
                
              //Show popup when the list is empty for tags
            	if(listOfTransaction.isEmpty()) {
            		JOptionPane.showMessageDialog(this, "No entry associated with entered tag/tags.");
            	}
            	
            }
        });

        //Refresh button
        refreshButton.addActionListener(e -> {
            List<String[]> allTxns = profile.getStringTransactions(profile.getTransactions());
            updateTransactionTable(allTxns);
        });

        showCsvButton.addActionListener(e -> {
            
        	File file = StorageService.getFile();
			String command = "cmd /c start \"\" \"C:\\Program Files\\LibreOffice\\program\\scalc.exe\" \""
					+ file.getAbsolutePath() + "\"";
			try {
				Runtime.getRuntime().exec(command);
				
			} catch (IOException r) {
				JOptionPane.showMessageDialog(this, "Something went wrong while opening libre office writer");
				r.printStackTrace();
			}
            
        });

        generateJsonButton.addActionListener(e -> {
            
            String fileJSONPath = Main.generateJsonFile(profile);
            JOptionPane.showMessageDialog(this, "JSON report generated successfully, at path : " + fileJSONPath);
        });

        logoutButton.addActionListener(e -> {
            StorageService.saveObjectState(profile);
            dispose();
            new LoginWindow(profile);
        });

        changePasswordButton.addActionListener(e -> new ChangePasswordWindow(profile, this));

        setVisible(true);
    }
    
    public static void updateHeadingBalance(UserProfile profile) {
    	balanceLabel.setText("Balance: Rs." + profile.getCurrentBalance());
    }

    // Utility to update transaction table
    public static void updateTransactionTable(List<String[]> transactions) {
        tableModel.setRowCount(0); // clear existing rows
        for (String[] row : transactions) {
            tableModel.addRow(row);
        }
    }
} // End of DashboardWindow

package com.expense.tracker;

import com.expense.swing.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Scanner;

import javax.swing.JOptionPane;

import com.expense.tracker.model.Transaction;
import com.expense.tracker.model.UserProfile;
import com.expense.tracker.io.StorageService;

public class Main {

	public static String objectFilePath;
	public static String basePath = System.getProperty("user.dir");
	public static final String jsonFilePath = basePath + File.separator + "data" + File.separator + "data.json";

	static Scanner scan = new Scanner(System.in);

	public static void main(String[] args) {
		
		

		System.out.println(System.getProperty("user.dir"));

		objectFilePath = basePath + "\\data\\profile.ser";
		System.out.println("File path : " + objectFilePath);

		File file = new File(objectFilePath);
		if (file.exists()) {
			
			UserProfile profile = StorageService.getObjectState();
			if(profile != null) {
				new LoginWindow(StorageService.getObjectState());
			}
			else {
				JOptionPane.showMessageDialog(null, "Error reading .ser file.!!!");
				JOptionPane.showMessageDialog(null, "Delete the data folder to restart!!!");
			}
			//askPassword();
		}

		else {
			File createFolder = new File(basePath + "\\data");
			if (!createFolder.exists()) {
				try {
					createFolder.mkdirs();
					System.out.println("Folder Created");
				} catch (Exception e) {
					System.out.println("Can't create folder");
					e.printStackTrace();
				}
			}

			StorageService.createCSVFile();

			new SignupWindow();
		}

	}

	private static void showLimitedUserDetails(UserProfile profile) {

		System.out.println("Username :: " + profile.getUsername());
		System.out.println("Account created :: " + profile.getUserCreatedDateTime());
		System.out.println("Current Balance :: Rs. " + profile.getCurrentBalance());
		System.out.println("Total Transactions :: " + profile.getTotalTransactions());

	}

	private static void askPassword() {

		String storedPassword = StorageService.getObjectState().getPassword();

		while (true) {
			System.out.println("Enter your password :: ");
			String userEnteredPassword = scan.nextLine().trim();

			if (storedPassword.equals(userEnteredPassword)) {
				break;
			} else {
				System.out.println("Wrong Password. Try Again.");
			}

		}

		loginScreen();

	}

	private static void logonScreen() {

		UserProfile profile = new UserProfile();
		System.out.println("====Welcome to your own expense manager====");
		System.out.println("Let us quickly set up your account!!!");

		System.out.println("Entered values should not contain any space!!");
		System.out.println("Enter your username :: ");

		profile.setUsername(scan.next().toLowerCase());

		System.out.println("Enter your password (Don't use space inbetween) :: ");
		profile.setPassword(scan.next());

		System.out.println("Enter the Starting balance :: ");
		Double iniAmount = scan.nextDouble();
		profile.setCurrentBalance(iniAmount);
		profile.increaseTransactionCount();
		profile.setUserCreatedDateTime(new Date());
		profile.addTransaction(initTrasaction(iniAmount, profile));

		showLimitedUserDetails(profile);

		StorageService.saveObjectState(profile);
		loginScreen();

	}

	private static void loginScreen() {

		UserProfile profile = StorageService.getObjectState();

		System.out.println("==============================================");
		System.out.println("         Personal Expense Tracker");
		System.out.println("==============================================");
		System.out.println("Welcome, " + profile.getUsername() + "!");
		System.out.println("Your current balance :: Rs. " + profile.getCurrentBalance());
		System.out.println("==============================================");

		// Print options in 3 columns
		System.out.printf("%-30s %-30s %-30s%n", 
		    "1. Add Credit (Income)", 
		    "2. Add Debit (Expense)", 
		    "3. View Balance");
		System.out.printf("%-30s %-30s %-30s%n", 
		    "4. View All Transactions", 
		    "5. Filter by Tag/Type", 
		    "6. Show Transactions (CSV)");
		System.out.printf("%-30s %-30s %-30s%n", 
		    "7. Generate Summary (JSON)", 
		    "8. Change Password", 
		    "9. Save & Exit");

		System.out.println("==============================================");
		System.out.print("Enter your choice (1-9): ");


		int option = scan.nextInt();
		scan.nextLine();

		takeAction(option, profile);

	}

	public static Transaction initTrasaction(Double initAmount, UserProfile profile) {

		Transaction t = new Transaction();
		t.setId("Tran_" + System.currentTimeMillis());
		t.setAmount(initAmount);
		t.setDescription("Initial Amount");
		t.setTimeStamp(new Date());
		t.setType("INIT");
		t.addTag(new String[] {"init"});
		t.setCurrentBalance(initAmount);

		StorageService.addDataEntry(t, profile);

		return t;

	}

	private static void takeAction(int option, UserProfile profile) {
		switch (option) {

		case 1:
			Operations.addCredit(profile);
			System.out.println("Successful");
			loginScreen();
			break;

		case 2:
			Operations.addDebit(profile);
			System.out.println("Successful");
			loginScreen();
			break;

		case 3:
			Operations.showBalance(profile);
			System.out.println("Successful");
			loginScreen();
			break;

		case 4:
			Operations.viewAllTransaction(profile);
			System.out.println("Successful");
			loginScreen();

			break;

		case 5:
			System.out.println("Enter the tags you want to search by comma separatd (e.g: food,grocery) :: ");
			String filterTags = scan.nextLine();
			Operations.filterTransactionByTags(profile, filterTags.toLowerCase().split(","));
			loginScreen();
			break;

		case 6:

			File file = StorageService.getFile();
			String command = "cmd /c start \"\" \"C:\\Program Files\\LibreOffice\\program\\scalc.exe\" \""
					+ file.getAbsolutePath() + "\"";
			try {
				Runtime.getRuntime().exec(command);
			} catch (IOException e) {
				System.out.println("Something went wrong while opening libre office writer");
				e.printStackTrace();
			}

			break;

		case 7:
			generateJsonFile(profile);
			break;

		case 8:
			System.out.println("Enter new password (It should not contain spaces) :: ");
			String newPassword = scan.nextLine().trim();
			Operations.changePassword(newPassword, profile);
			System.out.println("Password changed successfully!!!");
			System.out.println("Login again.");
			askPassword();
			break;

		case 9:
			System.out.println("Thank you for using Expense Tracker");
			StorageService.saveObjectState(profile);
			break;

		default:
			break;

		}
	}

	/**
	 * @param profile
	 */
	public static String generateJsonFile(UserProfile profile) {

		try{
			System.out.print("Creating File ");
			for(int i = 0;i<3;i++) {
			Thread.sleep(300);
			System.out.print(".");
			}
		}
		
		catch(Exception e) {
			e.printStackTrace();
		}
		
		System.out.println();
		
		File jsonFile = new File(jsonFilePath);
		if (!jsonFile.exists()) {
			try {
				jsonFile.createNewFile();
				System.out.println("New file (data.json) created.");
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Error while creating new file.");
			}
		}

		try (

				FileWriter fileWriter = new FileWriter(jsonFile, false);
				BufferedWriter bWriter = new BufferedWriter(fileWriter);

		) {
			
			StringBuilder sb = new StringBuilder();
			
			// JSON Object start;
			sb.append("{");

			sb.append("\"File Created\" : ").append("\"" + new Date() + "\",");
			sb.append("\"Username\" : ").append("\"" + profile.getUsername() + "\",");
			sb.append("\"Current Balance\" : ").append("\"" + profile.getCurrentBalance() + "\",");
			sb.append("\"Total Transactions\" : ").append("\"" + profile.getTotalTransactions() + "\",");
			sb.append("\"Account Creation Time\" : ").append("\"" + profile.getUserCreatedDateTime() + "\",");

			// Tags Start
			sb.append("\"Tags\" : [");
			
			for(int i = 0;i<profile.getAllTags().size();i++) {
				sb.append("\"" + profile.getAllTags().get(i) + "\"");
				if(i < profile.getAllTags().size()-1) {
					sb.append(",");
				}
			}
			sb.append("]");

			// Transactions Start
			sb.append(",\"Transactions\" : [");

			for(int i = 0;i<profile.getTransactions().size();i++) {
				
				Transaction t = profile.getTransactions().get(i);
				// JSON OBJECT OF A SINGLE TRANSACTION
				
				//Object start
				sb.append("{");
				
				sb.append("\"Transaction Id\" : \"" + t.getId() +"\"");
				sb.append(",");
				sb.append("\"Amount\" : \"" + t.getAmount() +"\"");
				sb.append(",");
				sb.append("\"Type\" : \"" + t.getType() +"\"");
				sb.append(",");
				sb.append("\"Transaction Time\" : \"" + t.getTimeStamp() +"\"");
				sb.append(",");
				sb.append("\"Description\" : \"" + t.getDescription() +"\"");
				
				// Transaction Internal Tags Array
				sb.append(",");
				sb.append("\"Tags\" : [");
				for(int j = 0;j<t.getAllTags().size();j++) {
					sb.append("\"" + t.getAllTags().get(j)+"\"");
					if(j < t.getAllTags().size() - 1) {
						sb.append(",");
					}
				}
				sb.append("]");
				
				//Object end
				sb.append("}");
				
				if(i < profile.getTransactions().size()-1) {
					sb.append(",");
				}
			}
			
			// Transaction Array ends
			sb.append("]");
			// JSON Object end;
			sb.append("}");
						
			bWriter.append(sb.toString());
			bWriter.flush();
			
			
			//System.out.println("File Created at path :: " + jsonFile.getAbsolutePath());
			//loginScreen();

		} catch (Exception e) {
			System.out.println("Error while creating json file");
			e.printStackTrace();
		}
		
		return jsonFilePath;

	}

}

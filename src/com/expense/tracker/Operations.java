package com.expense.tracker;

import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.expense.tracker.io.StorageService;
import com.expense.tracker.model.Transaction;
import com.expense.tracker.model.UserProfile;

public class Operations {
	
	static Scanner scan = new Scanner(System.in);
	
	public static boolean changePassword(String newPassword, UserProfile profile) {
		profile.setPassword(newPassword);
		StorageService.saveObjectState(profile);
		
		return true;
	}
	
	protected static void showBalance(UserProfile profile) {
		
		System.out.println("Your current balance :: " + profile.getCurrentBalance());
		
	}
	
	
	public static List<Transaction> filterTransactionByTags(UserProfile profile, String[] tags) {
		
		List<Transaction> filteredList = profile.getTransactions().stream()
				.filter(m -> {
					for(String tag : tags) {
						if(m.getAllTags().contains(tag)) {
							return true;
						}
					}
					return false;
				}).distinct().collect(Collectors.toList());
		
		for(Transaction t : filteredList) {
			System.out.println(t.toString());
		}
		
		return filteredList;
	}
	
	public static void addCredit(UserProfile profile, String[] tDetails) {
		
		double amount = Double.parseDouble(tDetails[0]);
		String description = tDetails[1];
		String tags = tDetails[2];
		
		// Creating new transaction entry
		Transaction t = new Transaction();
		t.setId("Tran_"+System.currentTimeMillis());
		t.setAmount(amount);
		t.setType("CREDIT");
		t.setTimeStamp(new Date());
		t.setDescription(description);
		t.addTag(tags.split(","));
		
		// changes in profile based on the transaction
		profile.increaseTransactionCount();
		profile.setCurrentBalance(profile.getCurrentBalance()+amount);
		
		
		//set current balance in the transaction based on the update profile
		t.setCurrentBalance(profile.getCurrentBalance());
		
		profile.addTransaction(t);
		StorageService.saveObjectState(profile);
		StorageService.addDataEntry(t, profile);	
	}

	protected static void addCredit(UserProfile profile) {
		System.out.println("Enter the credit amount :: ");
		double amount = scan.nextDouble();
		scan.nextLine();
		System.out.println("Enter the description for the transaction::");
		String description = scan.nextLine();
		
		System.out.println("Enter the tags (comma seperated)::");
		String tags = scan.next();
		scan.nextLine();
		
		// Creating new transaction entry
		Transaction t = new Transaction();
		t.setId("Tran_"+System.currentTimeMillis());
		t.setAmount(amount);
		t.setType("CREDIT");
		t.setTimeStamp(new Date());
		t.setDescription(description);
		t.addTag(tags.split(","));
		
		// changes in profile based on the transaction
		profile.increaseTransactionCount();
		profile.setCurrentBalance(profile.getCurrentBalance()+amount);
		
		
		//set current balance in the transaction based on the update profile
		t.setCurrentBalance(profile.getCurrentBalance());
		
		profile.addTransaction(t);
		StorageService.saveObjectState(profile);
		StorageService.addDataEntry(t, profile);
		
		
	}
	
	public static void addDebit(UserProfile profile, String[] tDetails) {
		
		double amount = Double.parseDouble(tDetails[0]);
		String description = tDetails[1];
		String tags = tDetails[2];
		
		Transaction t = new Transaction();
		t.setId("Tran_"+System.currentTimeMillis());
		t.setAmount(amount);
		t.setType("DEBIT");
		t.setTimeStamp(new Date());
		t.setDescription(description);
		t.addTag(tags.split(","));
		
		profile.increaseTransactionCount();
		profile.setCurrentBalance(profile.getCurrentBalance()-amount);
		
		t.setCurrentBalance(profile.getCurrentBalance());
		
		profile.addTransaction(t);
		
		StorageService.saveObjectState(profile);
		StorageService.addDataEntry(t, profile);	
	}
	
	protected static void addDebit(UserProfile profile) {
		System.out.println("Enter the debit amount :: ");
		double amount = scan.nextDouble();
		scan.nextLine();
		System.out.println("Enter the description for the transaction::");
		String description = scan.nextLine();
		
		System.out.println("Enter the tags (comma seperated)::");
		String tags = scan.next();
		scan.nextLine();
		
		Transaction t = new Transaction();
		t.setId("Tran_"+System.currentTimeMillis());
		t.setAmount(amount);
		t.setType("DEBIT");
		t.setTimeStamp(new Date());
		t.setDescription(description);
		t.addTag(tags.split(","));
		
		profile.increaseTransactionCount();
		profile.setCurrentBalance(profile.getCurrentBalance()-amount);
		
		t.setCurrentBalance(profile.getCurrentBalance());
		
		profile.addTransaction(t);
		
		StorageService.saveObjectState(profile);
		StorageService.addDataEntry(t, profile);	
	}
	
	protected static void viewAllTransaction(UserProfile profile) {
		for(Transaction t : profile.getTransactions()) {
			System.out.println(t.toString());
		}
	}
	
}

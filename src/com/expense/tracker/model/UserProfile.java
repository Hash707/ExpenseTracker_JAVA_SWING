package com.expense.tracker.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserProfile implements Serializable{
	private static final long serialVersionUID = 1L;

	private String username;
	private List<String> tags = new ArrayList();
	private String password;
	private List<Transaction> transactions = new ArrayList();
	private Date userCreatedDateTime;
	private double currentBalance;
	private int totalTransactions;
	
	public List<String[]> getStringTransactions(List<Transaction> transactions){
		
		List<String[]> stringTransaction = new ArrayList<String[]>();
		for(Transaction t : transactions) {
			
			String[] temp = new String[7];
			temp[0] = t.getId();
			temp[1] = t.getType();
			temp[2] = "Rs. "+t.getAmount()+"";
			temp[3] = "Rs. "+t.getCurrentBalance()+"";
			temp[4] = new SimpleDateFormat("dd/MM/yy hh:mm:ss").format(t.getTimeStamp());
			temp[5] = t.getDescription();
			temp[6] = t.getAllTagsString();
			
			
			stringTransaction.add(temp);
		}
		
		return stringTransaction;
		
	}
	
	public void addTransaction(Transaction t) {
		transactions.add(t);
	}
	
	public List<Transaction> getTransactions(){
		return transactions;
	}
	
	public void increaseTransactionCount() {
		totalTransactions++;
	}
	
	public void addTag(String tag) {
		tags.add(tag);
	}
	
	public List<String> getAllTags(){
		return tags;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Date getUserCreatedDateTime() {
		return userCreatedDateTime;
	}
	public void setUserCreatedDateTime(Date userCreatedDateTime) {
		this.userCreatedDateTime = userCreatedDateTime;
	}
	public double getCurrentBalance() {
		return currentBalance;
	}
	public void setCurrentBalance(double currentBalance) {
		this.currentBalance = currentBalance;
	}
	public int getTotalTransactions() {
		return totalTransactions;
	}
	public void setTotalTransactions(int totalTransactions) {
		this.totalTransactions = totalTransactions;
	}
	@Override
	public String toString() {
		return "UserProfile [username=" + username + ", password=" + password + ", userCreatedDateTime="
				+ userCreatedDateTime + ", currentBalance=" + currentBalance + ", totalTransactions="
				+ totalTransactions + "]";
	}
	
	
	
	
	
	

}

package com.expense.tracker.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Transaction implements Serializable{
	private static final long serialVersionUID = 2L;

	private String id;
	private Double amount;
	private String type;
	private Double currentBalance;
	private Date timeStamp;
	private String description;
	private List<String> tags = new ArrayList<String>();
	
	public void addTag(String[] insertedTags) {
		for(String a : insertedTags) {
			tags.add(a);
		}
	}
	
	public String getAllTagsString() {
		StringBuilder sb = new StringBuilder();
		for(int i = 0;i<tags.size();i++) {
			sb.append(tags.get(i));
			if(i < tags.size()-1) {
				sb.append(";");
			}
		}
		
		return sb.toString();
	}
	
	public List<String> getAllTags(){
		return tags;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	public Double getCurrentBalance() {
		return currentBalance;
	}
	public void setCurrentBalance(Double currentBalance) {
		this.currentBalance = currentBalance;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Date getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	@Override
	public String toString() {
	    return String.format(
	        "%s| Rs. %.2f | Rs. %.2f | %s | %s | %s | Tags: %s",
	        id != null ? id : "N/A",
	        amount != null ? amount : 0.0,
	        currentBalance != null ? currentBalance :0.0,
	        type != null ? type : "N/A",
	        timeStamp != null ? timeStamp.toString() : "N/A",
	        description != null ? description : "No description",
	        (tags != null && !tags.isEmpty()) ? String.join(", ", tags) : "None"
	    );
	}
}

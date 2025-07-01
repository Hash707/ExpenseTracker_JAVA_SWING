package com.expense.tracker.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import javax.swing.JOptionPane;

import com.expense.tracker.Main;
import com.expense.tracker.model.Transaction;
import com.expense.tracker.model.UserProfile;

public class StorageService {
	
	private static final String headingRow = "TransactionId,Amount,Current Balance,Type,TimeStamp,Description,Tags\n";
	
	public static void createCSVFile() {
		
		String csvFilePath = Main.basePath + "\\data\\data.csv";
		File csvFile = new File(csvFilePath);
		if(!csvFile.exists()) {
			try {
				csvFile.createNewFile();
				System.out.println("CSV File successfully created.");
			} catch (IOException e) {
				System.out.println("Error while creating csv file.");
				e.printStackTrace();
			}
		}
		
		csvFileHeading(csvFile);
		
	}
	
	public static File getFile() {
		String csvFilePath = Main.basePath + "\\data\\data.csv";
		File csvFile = new File(csvFilePath);
		
		return csvFile;
	}
	
	public static void addDataEntry(Transaction t, UserProfile p) {
		
		File file = getFile();
		
		try(
			FileWriter writer = new FileWriter(file,true);
			BufferedWriter sb = new BufferedWriter(writer);
			){
			
			StringBuilder entry = new StringBuilder();
			entry.append(t.getId()).append(",")
				.append(t.getAmount()).append(",")
				.append(p.getCurrentBalance()).append(",")
				.append(t.getType()).append(",")
				.append(t.getTimeStamp()).append(",")
				.append(t.getDescription()).append(",")
				.append(t.getAllTagsString()).append("\n");
			
			sb.append(entry.toString());
			
		}
		catch(Exception e) {
			System.out.println("Got some error while writing to the file.");
			e.printStackTrace();
		}
		
		
	}
	
	public static void csvFileHeading(File file) {
		try (
			FileWriter writer = new FileWriter(file,true);
			BufferedWriter sb = new BufferedWriter(writer);
				
			
		){
			sb.append(headingRow);
		}
		 catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static UserProfile getObjectState() {
		
		FileInputStream inputstream = null;
		ObjectInputStream objInputStream = null;
		UserProfile profile = null;
		
		
		try {
			inputstream	= new FileInputStream(Main.objectFilePath);
			objInputStream	= new ObjectInputStream(inputstream);
			
			profile = (UserProfile)objInputStream.readObject();
			
			return profile;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Corrupt .ser file found.");
			e.printStackTrace();
		}
		finally {
			
			
			try {
				if(inputstream != null && objInputStream != null) {
					inputstream.close();
					objInputStream.close();
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return profile;
	}

	public static void saveObjectState(UserProfile profile) {
		
		OutputStream outputStream = null;
		ObjectOutputStream objectOutputStream = null;
		
		try {
			outputStream = new FileOutputStream(Main.objectFilePath);
			
			objectOutputStream = new ObjectOutputStream(outputStream);
			
			objectOutputStream.writeObject(profile);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		finally {
			try {
				objectOutputStream.close();
				outputStream.close();
			} catch (IOException e) {
				System.out.println("Exception in finally block");
				e.printStackTrace();
			}
			
		}
		
	}
	
}

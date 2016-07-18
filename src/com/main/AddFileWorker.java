package com.main;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import javax.swing.JLabel;
import javax.swing.SwingWorker;

public class AddFileWorker extends SwingWorker<Integer, String>{
	int serialNumber = 0;
	public static final String[] commonFormats = new String[] { "txt","pdf", "doc", "docx", "ppt", "pptx", "xsl", "xslx", "zip", "gz", "avi", "flv", "wmv", "mp4", "mp3", "mov", "mpeg", "mkv", "amv", "wav", "wma" };
	public static final Set<String> commonFormatsSet = new HashSet<String>(Arrays.asList(commonFormats));
	int commonSerialNumber = 0;
	int folderSerialnumber = 0;
	static Connection dbConn;
	Statement dbStmt;
	static PreparedStatement ps = null;
	static PreparedStatement cps = null;
	static PreparedStatement fps = null;
	static int remainingCount =0;
	static String extensionName[] = new String[5000];
	static String fileName[] = new String[5000];
	static String directoryPath;
	static JLabel lblStatus;
	
	AddFileWorker(){
		try {
			String dbURL1 = "jdbc:derby:codejava/webdb1;create=true";
			dbConn = DriverManager.getConnection(dbURL1);
			dbStmt = dbConn.createStatement();
			dbStmt.executeUpdate("create table files_info(serialNumber int, filePath varchar(500), fileName varchar(200), fileExtension varchar(60))");
			dbStmt.executeUpdate("create table common_files_info(serialNumber int, filePath varchar(500), fileName varchar(200), fileExtension varchar(60))");
			
		}  catch (SQLException e) {
			System.out.println("SQL- create table error or table already exists");
		}
		try {
			dbStmt.executeUpdate("create table common_files_info(serialNumber int, filePath varchar(500), fileName varchar(200), fileExtension varchar(60))");
			
		}  catch (SQLException e) {
			System.out.println("SQL- create table error or common table already exists");
		}
		try{
			ps = dbConn.prepareStatement("INSERT INTO files_info VALUES (?, ?, ?, ?)");
			cps = dbConn.prepareStatement("INSERT INTO common_files_info VALUES (?, ?, ?, ?)");
			fps = dbConn.prepareStatement("INSERT INTO folders_info VALUES (?, ?, ?)");
		} catch(Exception e){
			System.out.println("prepare statement exception");
		}
	}
	
	@Override
	protected Integer doInBackground() throws Exception {
		Stack<String> st = new Stack<>();
		st.push(directoryPath);
		while(!st.isEmpty()) {
			directoryPath = st.pop();
			//int count = 0;
			File dirFile = new File(directoryPath);
			if(dirFile.isDirectory()) {
				try{
				String directoryContents[] = dirFile.list();
				if(directoryContents == null){
					directoryContents = new String[0];
				}
				Statement dbUpdateStmt = null;
				try {
					dbUpdateStmt = dbConn.createStatement();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				for(int iter=0; iter<directoryContents.length; iter++) {
					String subDirPath = directoryPath + "/" + directoryContents[iter];
					File subDirFile = new File(subDirPath);
					if(subDirFile.isDirectory()) {
						//add(subDirPath,lblStatus);
						st.push(subDirPath);
						
						String folderName = "";
						try{
							folderName = subDirPath.substring(subDirPath.lastIndexOf('/')+1, subDirPath.length());
						} catch(Exception e){
							
						}
						if(!folderName.equals("")){
							this.folderSerialnumber++;
							fps.setInt(1, folderSerialnumber);
							fps.setString(2, subDirPath);
							fps.setString(3, folderName);
							if(this.folderSerialnumber%10000==0){
								fps.executeBatch();
								fps.clearBatch();
								System.out.println("10000 records added to folder table");
								lblStatus.setText(folderSerialnumber+" folders added");
							}
							fps.addBatch(); 
						}
						
					}
					else {
						/*String bufferToken = null;
						StringTokenizer splitFileName = new StringTokenizer(directoryContents[iter],".");
						while(splitFileName.hasMoreTokens()) {
							bufferToken = splitFileName.nextToken();
						}
						extensionName[iter] = bufferToken; */
						int dotIndex = directoryContents[iter].lastIndexOf('.');
						if(dotIndex>0){
							fileName[iter] = directoryContents[iter].substring(0,dotIndex);
							extensionName[iter] = directoryContents[iter].substring(dotIndex+1,directoryContents[iter].length());
							
						} else {
							fileName[iter] = directoryContents[iter];
							extensionName[iter] = "";
							
						}
						
						try {
							
							//dbUpdateStmt.executeUpdate("insert into files_info values("+serialNumber+",'"+subDirPath+"','"+fileName[iter]+"','"+extensionName[iter]+"')");
							if(commonFormatsSet.contains(extensionName[iter])){
								this.commonSerialNumber++;
								cps.setInt(1, commonSerialNumber);
								cps.setString(2, subDirPath);
								cps.setString(3, fileName[iter]);
								cps.setString(4, extensionName[iter]);
								if(this.commonSerialNumber%10000==0){
									cps.executeBatch();
									cps.clearBatch();
									System.out.println("10000 records added to common table");
									lblStatus.setText(commonSerialNumber+" records added");
								}
								cps.addBatch(); 
							}
							else{
								this.serialNumber++;
								ps.setInt(1, serialNumber);
								ps.setString(2, subDirPath);
								ps.setString(3, fileName[iter]);
								ps.setString(4, extensionName[iter]);
								if(this.serialNumber%10000==0){
									ps.executeBatch();
									ps.clearBatch();
									System.out.println("10000 records added");
									lblStatus.setText(serialNumber+" records added");
								}
								ps.addBatch(); 
							}
							
							
							
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							System.out.println("SQL- insert into table exception");
						}
	//					System.out.println(serialNumber+" records added");
						
					}
				}
				} catch(Exception e){
					System.out.println("large dir ignored");
				}
			}
		}
		return 1;
	}
	public static void addRemaining() throws SQLException{
		ps.executeBatch();
		ps.clearBatch();
		System.out.println("remaining records added");
		cps.executeBatch();
		cps.clearBatch();
		System.out.println("remaining records added to common");
		fps.executeBatch();
		fps.clearBatch();
		System.out.println("remaining records added to common");
//		lblStatus.setText(serialNumber+" records added");
	}
	public static void close(){
		try{
			dbConn.close();
		}
		catch(Exception e){
			System.out.println("exception in closing");
		}
	}
}

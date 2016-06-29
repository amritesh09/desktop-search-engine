package com.main;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;


public class ShowFile {
	
	int serialNumber = 0;
	static Connection dbConn;
	Statement dbStmt;
	static PreparedStatement ps = null;
	static int remainingCount =0;
	static String extensionName[] = new String[5000];
	static String fileName[] = new String[5000];
	
	ShowFile() {
		try {
			String dbURL1 = "jdbc:derby:codejava/webdb1;create=true";
			dbConn = DriverManager.getConnection(dbURL1);
			dbStmt = dbConn.createStatement();
			dbStmt.executeUpdate("create table files_info(serialNumber int, filePath varchar(500), fileName varchar(200), fileExtension varchar(60))");
			
		}  catch (SQLException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			System.out.println("SQL- create table exception");
		}
		try{
			ps = dbConn.prepareStatement("INSERT INTO files_info VALUES (?, ?, ?, ?)");
		} catch(Exception e){
			System.out.println("create statement exception");
		}
	}
	
	
	public void add(String directoryPath, JLabel lblStatus) {
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
				//System.out.println(directoryPath);
				String subDirPath = directoryPath + "/" + directoryContents[iter];
				
				File subDirFile = new File(subDirPath);
				if(subDirFile.isDirectory()) {
					add(subDirPath,lblStatus);
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
						this.serialNumber++;
						//dbUpdateStmt.executeUpdate("insert into files_info values("+serialNumber+",'"+subDirPath+"','"+fileName[iter]+"','"+extensionName[iter]+"')");
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
	
	public void search(String key, String ext, boolean folderFlag, DefaultTableModel dtm){
		String t=key;//tf1.getText();
		String b="";//String t2=tf4.getText();
	    String e=ext;//tf2.getText();
	    String s[]=new String[2000];
	    String s1[]=new String[2000];
	    String s3[]=new String[2000];
	    File myFile;
	    String fileName, filePath;
	    Icon icon = null;
	    long lastMod;
	    dtm.setRowCount(0);
	    int i=0;int f=0;
	  
	  {  try{
		  // Shivam's attempt
		  ResultSet result;
		  
		  if(ext.length() != 0){
			  if(folderFlag){
				  result = dbStmt.executeQuery("select filePath from files_info WHERE fileExtension = '" + ext + "' AND filePath LIKE '%" + key + "%' "); 
			  }
			  else{
				  result = dbStmt.executeQuery("select filePath from files_info WHERE fileExtension = '" + ext + "' AND fileName LIKE '%" + key + "%' "); 
			  }
		  }
		  else{
			  if(folderFlag){
				  result = dbStmt.executeQuery("select filePath from files_info WHERE filePath LIKE '%" + key + "%' "); 
			  }
			  else{
				  result = dbStmt.executeQuery("select filePath from files_info WHERE fileName LIKE '%" + key + "%' "); 
			  }
		  }
		 
	        while(result.next()&&f<20){

//	        		if(false) {
	                s[i]=result.getString(1);
//	                
//	                for(int k=0;k<s[i].length();k++) {
//	                    if(s[i].regionMatches(true,k,t,0,t.length())) {
//	                        System.out.println("found="+s[i]);f+=1;b=b+"!"+s[i];}}
//	            } else if(e.equals(""))
//	        	{
//	                s[i]=result.getString(2);
//	                for(int k=0;k<s[i].length();k++) {
//	                    if(s[i].regionMatches(true,k,t,0,t.length())) {
//	                       if(f<20)
	                             System.out.println("result found= "+s[i]);
	                             f+=1;
	                             b=b+"!"+s[i];
	                             myFile = new File(s[i]);
	                             fileName = myFile.getName();
	                             FileSystemView view = FileSystemView.getFileSystemView();
	                             icon = view.getSystemIcon(myFile);
	                             filePath = myFile.getAbsolutePath();
	                             lastMod = myFile.lastModified();
	                             Date date=new Date(lastMod);
	                             SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yy");
	                             String dateText = df2.format(date);
	                             dtm.addRow(new Object[] { icon, "<html><b>"+fileName+"</b></html>", filePath, dateText});
	                            /* if(f==1)
	                                (new gui(f,s[i])).create();
	                             else
	                                 new gui(f,s[i]);*/
	                    
	                         //i++;
	        }
	        System.out.println(b);
	        
	        
	        
	        //new gui(b);
	        if(f==0)
	            System.out.println("file not found");} 
	  catch(Exception e1){
		  e1.printStackTrace();
		  //System.out.println("More than 200 files ignoring");
	  }
	   }
	}
	
	
	public static void addRemaining() throws SQLException{
			ps.executeBatch();
			ps.clearBatch();
			System.out.println("remaining records added");
//			lblStatus.setText(serialNumber+" records added");
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

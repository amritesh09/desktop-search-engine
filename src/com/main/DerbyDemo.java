package com.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;
import java.util.Properties;
/**
 * This program demonstrates how to connect to Apache Derby (Java DB) database
 * for the embedded driver and network client driver.
 * @author www.codejava.net
 *
 */

public class DerbyDemo {
 
    public void deleteDb() {
         
        try {
            // connect method #1 - embedded driver
            String dbURL1 = "jdbc:derby:codejava/webdb1;create=true";
            Connection conn1 = DriverManager.getConnection(dbURL1);
            if (conn1 != null) {
                System.out.println("Connected to database #1");
            }
           Statement st =  conn1.createStatement();
//           st.executeUpdate("create table demo(PersonID int,LastName varchar(255))");
//           System.out.println("table created successfully");
//           ResultSet rs = st.executeQuery("select * from files_info");
//           
//           while(rs.next()){
//        	   System.out.println(rs.getString(2));
//           }
           System.out.println("select successful");
           st.executeUpdate("delete from files_info");
           st.executeUpdate("delete from common_files_info");
           st.executeUpdate("delete from folders_info");
           System.out.println("delete successful");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
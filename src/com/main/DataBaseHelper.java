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
public class DataBaseHelper {
 
    Connection getConn(){
    	Connection conn1=null;
        try {
            // connect method #1 - embedded driver
            String dbURL1 = "jdbc:derby:codejava/webdb1;create=true";
            conn1 = DriverManager.getConnection(dbURL1);
            if (conn1 != null) {
                System.out.println("Connected to database #1");
            }
          
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return conn1;
    }
    public void add(String dir){
    	
    }
}
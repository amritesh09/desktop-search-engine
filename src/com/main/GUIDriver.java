package com.main;


import javax.swing.*;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;  
public class GUIDriver {
	private static JTextField textName;
	private static JTextField textAuthor;
	private static JTextField tfDir;
	final static ShowFile sf = new ShowFile();
	private static JTextField tfKey;
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException {  
	JFrame f=new JFrame();//creating instance of JFrame  
	f.addWindowListener(new WindowAdapter() {
		@Override
		public void windowClosed(WindowEvent arg0) {
			sf.close();
			
		}
	});
	//initialize BookManager class
	//final DataBaseHelper dbHelper = new DataBaseHelper();
	f.setTitle("Local Search");
	f.setSize(400,550);//400 width and 500 height  
	f.getContentPane().setLayout(null);
	
	JLabel lblDirectory = new JLabel("Directory");
	lblDirectory.setBounds(38, 58, 81, 16);
	f.getContentPane().add(lblDirectory);
	
	tfDir = new JTextField();
	tfDir.setBounds(125, 53, 130, 26);
	f.getContentPane().add(tfDir);
	tfDir.setColumns(10);
	final JLabel lblStatus = new JLabel("status");
	
	JButton btnUpdate = new JButton("Update");
	btnUpdate.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			String dir = tfDir.getText();
			//dbHelper.add(dir);
			sf.add(dir, lblStatus);
			try {
				ShowFile.addRemaining();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
//			lblStatus.setText("updated");
		}
	});
	btnUpdate.setBounds(260, 53, 117, 29);
	f.getContentPane().add(btnUpdate);
	
	lblStatus.setBounds(260, 92, 117, 16);
	f.getContentPane().add(lblStatus);
	
	JLabel lblSearchByName = new JLabel("Search by name");
	lblSearchByName.setBounds(38, 207, 117, 16);
	f.getContentPane().add(lblSearchByName);
	
	tfKey = new JTextField();
	tfKey.setBounds(27, 236, 116, 22);
	f.getContentPane().add(tfKey);
	tfKey.setColumns(10);
	
	JButton btnSearch = new JButton("Search");
	btnSearch.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			String key = tfKey.getText();
			sf.search(key);
		}
	});
	btnSearch.setBounds(22, 305, 97, 25);
	f.getContentPane().add(btnSearch);

//	Connection conn = dbHelper.getConn();
//	Statement st = conn.createStatement();
//	st.executeQuery("select * from demo");
	
	
	f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	f.setVisible(true);//making the frame visible

}  
}  
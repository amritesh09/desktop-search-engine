package com.main;


import javax.swing.*;

import java.awt.Desktop;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;  
import java.io.File;
import java.io.IOException;
public class GUIDriver {
	private static JTextField textName;
	private static JTextField textAuthor;
	private static JTextField tfDir;
	final static ShowFile sf = new ShowFile();
	final static DerbyDemo dd = new DerbyDemo();
	private static JTextField tfKey;
	private static JTextField tfExt;
	private static JButton btnSearch;
	private static JButton btnUpdate;
	private static JTable resultTable;
	public static DefaultTableModel dtm;
	
	public static void setJTableColumnsWidth(JTable table, int tablePreferredWidth,
	        double... percentages) {
	    double total = 0;
	    for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
	        total += percentages[i];
	    }
	 
	    for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
	        TableColumn column = table.getColumnModel().getColumn(i);
	        column.setPreferredWidth((int)
	                (tablePreferredWidth * (percentages[i] / total)));
	    }
	}
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException {  
	JFrame mainFrame=new JFrame();//creating instance of JFrame  
		
	mainFrame.addWindowListener(new WindowAdapter() {
		@Override
		public void windowClosed(WindowEvent arg0) {
			sf.close();
			
		}
	});
	//initialize BookManager class
	//final DataBaseHelper dbHelper = new DataBaseHelper();
	mainFrame.setTitle("Local Search");
	mainFrame.setSize(680,760);//400 width and 500 height  
	mainFrame.getContentPane().setLayout(null);
	
	JLabel lblDirectory = new JLabel("Directory");
	lblDirectory.setBounds(27, 13, 81, 16);
	mainFrame.getContentPane().add(lblDirectory);
	
	tfDir = new JTextField();
	tfDir.addKeyListener(new KeyAdapter() {
		@Override
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_ENTER)
			{
				btnUpdate.doClick();
			}
		}
	});
	tfDir.setBounds(120, 8, 130, 26);
	mainFrame.getContentPane().add(tfDir);
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
			lblStatus.setText("updated");
		}
	});
	btnUpdate.setBounds(260, 7, 117, 29);
	mainFrame.getContentPane().add(btnUpdate);
	
	lblStatus.setBounds(260, 49, 278, 16);
	mainFrame.getContentPane().add(lblStatus);
	
	JLabel lblSearchByName = new JLabel("keyword");
	lblSearchByName.setBounds(27, 82, 117, 16);
	mainFrame.getContentPane().add(lblSearchByName);
	
	final JCheckBox chkFolderFlag = new JCheckBox("include folder");
	chkFolderFlag.addKeyListener(new KeyAdapter() {
		@Override
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_ENTER)
			{
				btnSearch.doClick();
			}
		}
	});
	chkFolderFlag.setBounds(27, 136, 113, 25);
	mainFrame.getContentPane().add(chkFolderFlag);
	
	tfKey = new JTextField();
	tfKey.addKeyListener(new KeyAdapter() {
		@Override
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_ENTER)
			{
				btnSearch.doClick();
			}
		}
	});
	tfKey.setBounds(139, 79, 116, 22);
	mainFrame.getContentPane().add(tfKey);
	tfKey.setColumns(10);
	
	btnSearch = new JButton("Search");
	btnSearch.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			String key = tfKey.getText();
			String ext = tfExt.getText();
			boolean folderFlag = chkFolderFlag.isSelected();
			sf.search(key,ext,folderFlag,dtm);
		}
	});
	btnSearch.setBounds(324, 107, 218, 25);
	mainFrame.getContentPane().add(btnSearch);
	
	JLabel lblExtension = new JLabel("extension");
	lblExtension.setBounds(27, 111, 56, 16);
	mainFrame.getContentPane().add(lblExtension);
	
	tfExt = new JTextField();
	tfExt.addKeyListener(new KeyAdapter() {
		@Override
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_ENTER)
			{
				btnSearch.doClick();
			}
		}
	});
	tfExt.setBounds(139, 108, 116, 22);
	mainFrame.getContentPane().add(tfExt);
	tfExt.setColumns(10);
	
	JButton btnDeleteDb = new JButton("delete db");
	btnDeleteDb.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			dd.deleteDb();
		}
	});
	btnDeleteDb.setBounds(421, 7, 117, 29);
	mainFrame.getContentPane().add(btnDeleteDb);
	
	dtm = new DefaultTableModel(){
		@Override
	    public boolean isCellEditable(int row, int column) {
	       //all cells false
	       return false;
	    }
	};
	String header[] = new String[] { "Icon", "Name", "Path", "Last Modified" };
	dtm.setColumnIdentifiers(header);
	
	resultTable = new JTable(dtm){
		public Class getColumnClass(int column)
        {
            return getValueAt(0, column).getClass();
        }
		
		public String getToolTipText(MouseEvent e) {
            String tip = null;
            java.awt.Point p = e.getPoint();
            int rowIndex = rowAtPoint(p);
            int colIndex = columnAtPoint(p);
            if(colIndex == 2){
            	try {
                    tip = getValueAt(rowIndex, colIndex).toString();
                } catch (RuntimeException e1) {
                    //catch null pointer exception if mouse is over an empty line
                }
            }
            return tip;
		}
	};
	
	resultTable.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
			if(e.getClickCount() == 2 && !e.isConsumed()){

				int row = resultTable.getSelectedRow();
				String path = (dtm.getValueAt(row, 2)).toString();
				Desktop desktop = Desktop.getDesktop();
	            File myFile = new File(path);
				try {
					desktop.open(myFile);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
						
		}
	});
	
	
	resultTable.setBounds(27, 363, 130, 45);
	resultTable.setRowHeight(30);
	setJTableColumnsWidth(resultTable, 638, 7, 23, 55, 15);
	resultTable.setShowGrid(false);
	mainFrame.getContentPane().add(resultTable);
	JScrollPane sp=new JScrollPane(resultTable);  
	sp.setLocation(12, 192);
	sp.setSize(638, 510);
	mainFrame.getContentPane().add(sp);  


//	Connection conn = dbHelper.getConn();
//	Statement st = conn.createStatement();
//	st.executeQuery("select * from demo");
	
	
	mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	mainFrame.setVisible(true);//making the frame visible

}  
}  
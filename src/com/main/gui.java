package com.main;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.StringTokenizer;
import javax.swing.JButton;

/**
 *
 * @author amritesh
 */
public class gui implements ActionListener{
   JFrame jf;//JPanel panel=new JPanel();

//Create a scrollbar using JScrollPane and add panel into it's viewport
//Set vertical and horizontal scrollbar always show
//JScrollPane scrollBar=new JScrollPane(panel,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    JButton jb[]=new JButton[20];
    Desktop d=null;
   String s5,s6="";
    public gui(String s) {
        //if(f<10)
         jf=new JFrame();int i=0;
        jf.setVisible(true);String s1[]=new String[20];
        jf.setLayout(null);
  //      jf.add(scrollBar);
        jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        {//for(int i=0;i<10;i++)
           // if(s[i]!=null)
            {
        //String jb=" ";
        StringTokenizer st=new StringTokenizer(s,"!");
                while(st.hasMoreTokens())
                {s1[i]=st.nextToken();
        jb[i]=new JButton(s1[i]);
       // File f1=new File(s);
        //f1.getAbsolutePath();
        jb[i].setBounds(0,40*i,800,40);
        System.out.println("f= "+i);
        jb[i].addActionListener(this);
      jf.setSize(800,800);
      jf.add(jb[i]);i++;
      
         }}
    }}
    @SuppressWarnings("static-access")
   public void actionPerformed(ActionEvent e){
        for(int i=0;i<20;i++){s6="";
       if(e.getSource()==jb[i])
       {    /*StringTokenizer st2=new StringTokenizer(e.getSource().toString(),"\\");
                while(st2.hasMoreTokens())
                { s5=st2.nextToken();
                s6+=s5;
                s6+="\\";s6+="\\";}
                s6=s6.replaceAll(s5,"");*/
                File f = new File(e.getActionCommand());
                System.out.println(f);
               // File f1=new File(s6);
               d=d.getDesktop();
               try{
               //d.open(f);
                  // d.open(f.getParentFile());
                   d.open(f);

               }
               catch(Exception e1){System.out.println(e1);}
}
   }
    }
public static void main(String s[]){
//new gui(f,"s");
}}

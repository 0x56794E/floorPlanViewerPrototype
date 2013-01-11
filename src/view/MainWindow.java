/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package view;

import helper.ImagePanel;
import java.awt.BorderLayout;
import java.awt.MouseInfo;
import java.awt.PointerInfo;
import java.awt.event.*;
import java.io.File;
import javax.persistence.EntityManagerFactory;
import javax.swing.*;

/**
 * @author              Vy Thuy Nguyen
 * @version             1.0 Jan 9, 2013
 * Last modified:       
 */
public class MainWindow extends JFrame 
{
    private JPanel mainPn = new JPanel();
    private JPanel imagePanel = new JPanel();
    private JButton selectFileBtn = new JButton("Select Floor Plan");
    private JFrame fileChooserWindow = null;
    
    private PointerInfo cursor = MouseInfo.getPointerInfo();
    
    //public MainWindow(EntityManagerFactory emf)
    public MainWindow()      
    {
     //   this.addMouseListener(this);
     //   this.addMouseMotionListener(this);
        this.setVisible(true);
        this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        this.setSize(300, 300);
        this.setLocationRelativeTo(null);
        
        //Main panel
        this.add(mainPn);
        mainPn.setLayout(new BorderLayout());
        mainPn.add(imagePanel, BorderLayout.CENTER);
        mainPn.add(selectFileBtn, BorderLayout.SOUTH);
        
        ActionListener selectFileHandler = new ActionListener() 
        {
            public void actionPerformed(ActionEvent e)
            {
                if (e.getSource() == selectFileBtn)
                {
                    //Bring up file chooser popup window
                    if (fileChooserWindow == null)
                    {
                        fileChooserWindow = new FileChooserWindow(MainWindow.this);
                    }
                    
                    fileChooserWindow.setVisible(true);
                }
            }            
        }; //End action listener
        
        selectFileBtn.addActionListener(selectFileHandler);
    }
    
    public void updateMainPanel()
    {
        
    }
    
    public void loadFloorPlan(File file)
    {
        mainPn.remove(imagePanel);
        imagePanel = new ImagePanel(file);
        mainPn.add(imagePanel, BorderLayout.CENTER);
        imagePanel.repaint();
        MainWindow.this.validate();
    }
   
}

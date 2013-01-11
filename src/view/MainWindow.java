/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package view;

import entity.Point;
import helper.ImagePanel;
import actionListener.ImagePanelMouseListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.PointerInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.TitledBorder;

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
    
    private ArrayList<Point> selectedPoints = new ArrayList<Point>();
    private JList pointsContainer;
    private DefaultListModel listModel = new DefaultListModel();
    
    private JButton removeBtn = new JButton("Remove");
    private JButton clearAllBtn = new JButton("Clear All");
    
    private JScrollPane scrollPane = new JScrollPane();
    private PointerInfo cursor = MouseInfo.getPointerInfo();
    
    //public MainWindow(EntityManagerFactory emf)
    public MainWindow()      
    {
        ActionListener selectFileHandler = new MainWindowButtonListener();   
        removeBtn.addActionListener(selectFileHandler);
        clearAllBtn.addActionListener(selectFileHandler);
        
        this.setVisible(true);
        this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        this.setSize(800, 700);
        this.setLocationRelativeTo(null);
        
        
        //Init Main panel
        this.add(mainPn);
        mainPn.setLayout(new BorderLayout());
        
        //Image canvas
        JPanel sub = new JPanel();
        TitledBorder b = new TitledBorder("Floor plan");
        sub.setBorder(b);
        sub.setMinimumSize(new Dimension(540, 500));
        sub.setPreferredSize(new Dimension(540, 500));
        scrollPane.setSize(new Dimension(540, 500));
        scrollPane.setPreferredSize(new Dimension(540, 500));
        scrollPane.setViewportView(imagePanel);
        
        sub.add(scrollPane);
        mainPn.add(sub, BorderLayout.CENTER);
        
        //Selected points
        TitledBorder b0 = new TitledBorder("Selected Points");
        JPanel sub0 = new JPanel();
        sub0.setLayout(new BorderLayout());
        sub0.setPreferredSize(new Dimension(200, 700));
        sub0.setMinimumSize(new Dimension(200, 700));
        sub0.setBorder(b0);
        
         //JList setup
        pointsContainer = new JList(listModel);
        pointsContainer.setLayoutOrientation(JList.VERTICAL_WRAP);
        pointsContainer.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
       // pointsContainer.setOpaque(true);
        sub0.add(pointsContainer, BorderLayout.CENTER);
        
        //add bunch of point
       // for (int i = 0; i < 100; ++i)
       //{
        //    Point p = new Point(i, 2 * i);
        //    selectedPoints.add(p);
        //    listModel.addElement(p);
        //}
        JPanel sub1 = new JPanel();
        sub1.add(removeBtn);
        sub1.add(clearAllBtn);
        sub0.add(sub1, BorderLayout.SOUTH);
        
        mainPn.add(sub0, BorderLayout.EAST);
        
        //Select Button
        JPanel btnPn = new JPanel();
        btnPn.add(selectFileBtn);
        mainPn.add(btnPn, BorderLayout.SOUTH);   
        selectFileBtn.addActionListener(selectFileHandler);
    }
    
    public void updateMainPanel()
    {
        MainWindow.this.add(mainPn);
        mainPn.setLayout(new BorderLayout());
        mainPn.add(imagePanel, BorderLayout.CENTER);
        mainPn.add(selectFileBtn, BorderLayout.SOUTH);
    }
    
    private void initMainPanel()
    {
        
    }
    
    public void loadFloorPlan(File file)
    {
        scrollPane.getViewport().remove(imagePanel);
        imagePanel = new ImagePanel(file);
        scrollPane.setViewportView(imagePanel);
        imagePanel.repaint();
        MainWindow.this.validate();
    }
   
    
    private class MainWindowButtonListener implements ActionListener
    {

        @Override
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
            else if (e.getSource() == removeBtn)
            {
                System.out.println("Current imagePanel size = " + imagePanel.getSize());
            }
        }

    }
}

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
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
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
    private ImagePanel imagePanel;
    private JButton selectFileBtn = new JButton("Select Floor Plan");
    private JFrame fileChooserWindow = null;
    
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
        this.setSize(880, 700);
        this.setLocationRelativeTo(null);
        
        
        //Init Main panel
        this.add(mainPn);
        mainPn.setLayout(new BorderLayout());
        
        //Image canvas
        JPanel sub = new JPanel();
        TitledBorder b = new TitledBorder("Floor plan");
        sub.setBorder(b);
        sub.setMinimumSize(new Dimension(600, 560));
        sub.setPreferredSize(new Dimension(600, 560));
        updateScrollPaneSize();
        
        //scrollPane.setViewportView(imagePanel);
        
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
        
        getContentPane().addHierarchyBoundsListener(new HierarchyBoundsListener(){
 
            @Override
            public void ancestorMoved(HierarchyEvent e) {
               // System.out.println(e);              
            }
            
            @Override
            public void ancestorResized(HierarchyEvent e) {
                updateScrollPaneSize();
                 
            }           
        });

    }
    
    public void addPoint(Point p)
    {
        listModel.addElement(p);
    }
    
    public void removePoint(Point p)
    {
        listModel.removeElement(p);
    }
    
    public void clearAll()
    {
        listModel.clear();
    }
    
    private void updateScrollPaneSize()
    {
        scrollPane.setSize(new Dimension(this.getWidth() - 260, this.getHeight() - 120));
        scrollPane.setPreferredSize(new Dimension (this.getWidth() - 260, this.getHeight() - 120));
    }
    
    
    public void loadFloorPlan(File file)
    {
        //scrollPane.getViewport().remove(imagePanel);
        imagePanel = new ImagePanel(file, this);
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
               
                if (imagePanel != null && !listModel.isEmpty())
                    for (Object p : pointsContainer.getSelectedValuesList())
                        imagePanel.removePoint((Point)p);
            }
            else if (e.getSource() == clearAllBtn)
            {
                if (imagePanel != null && !listModel.isEmpty())
                    imagePanel.clearAll();
            }
        }

    }
}

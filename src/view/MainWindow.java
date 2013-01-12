/**
 * Floor Plan Marker Project
 * Copyright (C) 2013  Vy Thuy Nguyen
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 * 
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 51 Franklin St, Fifth Floor,
 * Boston, MA  02110-1301, USA.
 */

package view;

import entity.Point;
import helper.ImagePanel;
import actionListener.ImagePanelMouseListener;
import entity.FloorPlan;
import helper.DatabaseService;
import helper.DrawingPanel;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.persistence.EntityManager;
import javax.swing.*;
import javax.swing.border.TitledBorder;

/**
 * @author              Vy Thuy Nguyen
 * @version             1.0 Jan 9, 2013
 * Last modified:       
 */
public class MainWindow extends JFrame 
{
    //Bottom bts
    private JButton saveToFileBtn = new JButton("Save to File");
    private JButton saveToDBBtn = new JButton("Save to Database");
    private JButton selectFileBtn = new JButton("Select Floor Plan");
    private JButton exportBtn = new JButton("Export Floor Plan");
    
    //File Menu
    private JMenuItem newItem = new JMenuItem("New Floor Plan...");
    private JMenuItem saveToFileItem = new JMenuItem("Save to File");
    private JMenuItem saveToDBItem = new JMenuItem("Save to Database");
    private JMenuItem saveBothItem = new JMenuItem("Save to Both File and Database");
    private JMenuItem exportItem = new JMenuItem("Export Floor Plan to Image File...");
    private JMenuItem showExistItem = new JMenuItem("Show Saved Floor Plan... ");
    
    //Help Menu
    private JMenuItem aboutItem = new JMenuItem("About");
    private JMenuItem reportItem = new JMenuItem("Report Issue");
    
    
    private JPanel mainPn = new JPanel();
    private JFrame fileChooserWindow = null;
    
    private JList pointsContainer;
    private DefaultListModel listModel = new DefaultListModel();
    private JScrollPane listScrollPane = new JScrollPane();
    
    private JButton removeBtn = new JButton("Remove");
    private JButton clearAllBtn = new JButton("Clear All");
    
    private JScrollPane scrollPane = new JScrollPane();
    private ImagePanel imagePanel;
    private DrawingPanel drawingPanel;
    
    //public MainWindow(EntityManagerFactory emf)
    public MainWindow()      
    {
        ActionListener btHandler = new MainWindowButtonListener();   
        removeBtn.addActionListener(btHandler);
        clearAllBtn.addActionListener(btHandler);
        saveToFileBtn.addActionListener(btHandler);
        saveToDBBtn.addActionListener(btHandler);
        selectFileBtn.addActionListener(btHandler);
        exportBtn.addActionListener(btHandler);
        
        /*
        
        this.setUndecorated(true);
        this.setOpacity(0.9f);
        */
        JFrame.setDefaultLookAndFeelDecorated(true);
        this.setVisible(true);
        this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        this.setSize(880, 700);
        this.setLocationRelativeTo(null);
        
        //Menu
        JMenuBar menuBar = new JMenuBar();
        
        //////File Menu
        JMenu fileMenu = new JMenu("File");
        
        newItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        fileMenu.add(newItem);
        fileMenu.addSeparator();
        
        showExistItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        fileMenu.add(showExistItem);
        fileMenu.addSeparator();
        
        saveToFileItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.CTRL_MASK));
        fileMenu.add(saveToFileItem);
        
        saveToDBItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.CTRL_MASK));
        fileMenu.add(saveToDBItem);
        
        saveBothItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, ActionEvent.CTRL_MASK));
        fileMenu.add(saveBothItem);
        fileMenu.addSeparator();
        
        exportItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
        fileMenu.add(exportItem);
        
        menuBar.add(fileMenu);
        
        ////end File Menu
        
        ////Help Menu
        JMenu helpMenu = new JMenu("Help");
        helpMenu.add(reportItem);
        helpMenu.add(aboutItem);
        menuBar.add(helpMenu);
        
        this.setJMenuBar(menuBar);
        
        
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
        pointsContainer.setLayoutOrientation(JList.VERTICAL);
        pointsContainer.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        
        listScrollPane.setViewportView(pointsContainer);
        sub0.add(listScrollPane);
        
        JPanel sub1 = new JPanel();
        sub1.add(removeBtn);
        sub1.add(clearAllBtn);
        sub0.add(sub1, BorderLayout.SOUTH);
        
        mainPn.add(sub0, BorderLayout.EAST);
        
        //Select Button
        JPanel btnPn = new JPanel();
        btnPn.add(selectFileBtn);
        btnPn.add(saveToFileBtn);
        btnPn.add(saveToDBBtn);
        btnPn.add(exportBtn);
        //mainPn.add(btnPn, BorderLayout.SOUTH);   
        
        //Resize listener
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

        pack();
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
        scrollPane.setSize(new Dimension(this.getWidth() - 250, this.getHeight() - 100));
        scrollPane.setPreferredSize(new Dimension (this.getWidth() - 250, this.getHeight() - 100));
    }
    
    
    public void loadFloorPlan(File file)
    {
        //scrollPane.getViewport().remove(imagePanel);
        imagePanel = new ImagePanel(file, this);
        imagePanel.repaint();
        
        scrollPane.setViewportView(imagePanel);
        MainWindow.this.pack();
        MainWindow.this.validate();
    }
   
    
    
    private class MainWindowMenuListener implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (e.getSource() == newItem)
            {
                JOptionPane.showMessageDialog(null, "new menu item clicked");
            }
            
        }
        
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
            else if (e.getSource() == saveToFileBtn)
            {
                if (imagePanel != null)
                {
                    int option = JOptionPane.YES_OPTION;
                    System.out.println("size = " + listModel.capacity());
                    if (listModel.isEmpty())
                    {
                        option = JOptionPane.showConfirmDialog(null, 
                                                               "There's nothing to save! Do you want to proceed anyways?\nWarning: This will result in erase previously saved data if there is any.", 
                                                               "Proceed?", 
                                                               JOptionPane.YES_NO_OPTION, 
                                                               JOptionPane.QUESTION_MESSAGE);
                    }
                    if (option == JOptionPane.YES_OPTION)
                    {
                        try
                        {
                            // Create file 
                            FileWriter fstream = new FileWriter(imagePanel.fileName + "-points.txt");
                            BufferedWriter out = new BufferedWriter(fstream);
                            Enumeration iter = listModel.elements();

                            while (iter.hasMoreElements())
                            {
                                out.write(iter.nextElement().toString());
                                out.write(";\r\n");
                            }

                            //Close the output stream
                            out.close();
                            
                            JOptionPane.showMessageDialog(null, "Sucessfully Saved To File");
                        }
                        catch (Exception exc) //Catch exception if any
                        {
                            System.err.println("Error: " + exc.getMessage());
                            
                        }
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "Please select a floor plan first.", "No Floor Plan Found", JOptionPane.ERROR_MESSAGE);
                }
            }
            else if (e.getSource() == saveToDBBtn)
            {
                EntityManager em = DatabaseService.getEntityManager();
                    
                    if (imagePanel != null)
                    {
                        em.getTransaction().begin();
                        FloorPlan fp = new FloorPlan(imagePanel.absPath, 0, 0);
                        Enumeration iter = listModel.elements();
                        while (iter.hasMoreElements())
                        {
                            Point p = (Point)iter.nextElement();
                            p.setFloorPlan(fp);
                            em.persist(p);
                        }
                        em.persist(fp);
                        em.getTransaction().commit();
                        DatabaseService.cleanup();
                        
                        JOptionPane.showMessageDialog(null, "Successfully Saved To Database");
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(null, "Please select a floor plan first.", "No Floor Plan Found", JOptionPane.ERROR_MESSAGE);
                    }
                    
            }
            else if (e.getSource() == exportBtn)
            {
                if (imagePanel != null)
                {   
                    
                    imagePanel.exportImage();
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "Please select a floor plan first.", "No Floor Plan Found", JOptionPane.ERROR_MESSAGE);
                }
                    
            }
        }

    }
}

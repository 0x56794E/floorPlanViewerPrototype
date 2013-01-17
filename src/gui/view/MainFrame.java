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

package gui.view;

import java.awt.event.*;
import javax.swing.*;
import gui.util.FileChooserWindow;
import java.io.IOException;
import util.FileService;
import util.ImageFileFilter;

/**
 * @author              Vy Thuy Nguyen
 * @version             1.0 Jan 9, 2013
 * Last modified:       
 */
public class MainFrame extends JFrame 
{
    //File Menu
    private JMenuItem newItem = new JMenuItem("New Floor Plan...");
    private JMenuItem saveToFileItem = new JMenuItem("Save Current Point Set to File");
    private JMenuItem saveToDBItem = new JMenuItem("Save to Database");
    private JMenuItem saveBothItem = new JMenuItem("Save to Both File and Database");
    private JMenuItem exportItem = new JMenuItem("Export Marked Floor Plan...");
    private JMenuItem showExistItem = new JMenuItem("Show Saved Floor Plans... ");
    
    //Help Menu
    private JMenuItem aboutItem = new JMenuItem("About");
    private JMenuItem reportItem = new JMenuItem("Report Issue");
    
    private MainPanel mainPn;
    private JFrame floorPlanChooserFr = null;
    
    
    public MainFrame()      
    {  
        //Init this JFrame
        initFrame();
        
        //Set up menu
        setupMenu();
        
        //Init Main panel
        mainPn = new MainPanel(this);   
        this.updateImageCanvasSize();
        this.add(mainPn);
       
        //Resize listener
        getContentPane().addHierarchyBoundsListener(new HierarchyBoundsListener(){
            @Override
            public void ancestorMoved(HierarchyEvent e) {
               // System.out.println(e);              
            }
            
            @Override
            public void ancestorResized(HierarchyEvent e) {
                updateImageCanvasSize();                 
            }           
        });    
        
        pack();
    }
 
    private void initFrame()
    {
        JFrame.setDefaultLookAndFeelDecorated(false);
        this.setVisible(true);
        this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        this.setSize(880, 700);
        this.setLocationRelativeTo(null);
        this.setTitle("Floor Plan Viewer");
        
        this.addWindowListener(new WindowListener(){

            @Override
            public void windowOpened(WindowEvent e)
            {
             //   throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void windowClosing(WindowEvent e)
            {
               // throw new UnsupportedOperationException("Not supported yet.");
                JOptionPane.showMessageDialog(null, "Closing");
                return;
            }

            @Override
            public void windowClosed(WindowEvent e)
            {
                //throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void windowIconified(WindowEvent e)
            {
                //throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void windowDeiconified(WindowEvent e)
            {
                ///throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void windowActivated(WindowEvent e)
            {
                ///throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void windowDeactivated(WindowEvent e)
            {
                
                //throw new UnsupportedOperationException("Not supported yet.");
            }
        });
    }
    
    public void newFloorPlanItemClicked()
    {
        newItem.setSelected(true);
    }
    
    private void setupMenu()
    {
        //=======================Menu=================
        JMenuBar menuBar = new JMenuBar();
        MenuListener menuHandler = new MenuListener();
        
        //File Menu
        JMenu fileMenu = new JMenu("File");
        
        newItem.addActionListener(menuHandler);
        newItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        fileMenu.add(newItem);
        fileMenu.addSeparator();
        
        showExistItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        showExistItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                mainPn.showFloorPlanListPopup();
            }
        });
        fileMenu.add(showExistItem);
        fileMenu.addSeparator();
        
        saveToFileItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.CTRL_MASK));
        saveToFileItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    FileService.savePointSetToFile(mainPn.getCurrentPointSet());
                }
                catch (IOException exc)
                {
                    JOptionPane.showMessageDialog(null, "Error writing to file", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        fileMenu.add(saveToFileItem);
        
        saveToDBItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.CTRL_MASK));
        //actionlistner 
        fileMenu.add(saveToDBItem);
        
        saveBothItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, ActionEvent.CTRL_MASK));
        fileMenu.add(saveBothItem);
        fileMenu.addSeparator();
        
        
        exportItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (mainPn.hasImage())
                {
                    mainPn.exportImage();
                }
                else
                {
                    JOptionPane.showMessageDialog(null,
                                                  "Please load a floor plan and mark it first", 
                                                  "No Image to Export",
                                                  JOptionPane.ERROR_MESSAGE);
                }
            }
        
        });
        exportItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
        fileMenu.add(exportItem);
        
        menuBar.add(fileMenu);
        
        //end File Menu
        
        //Help Menu
        JMenu helpMenu = new JMenu("Help");
        helpMenu.add(reportItem);
        helpMenu.add(aboutItem);
        menuBar.add(helpMenu);
        
        this.setJMenuBar(menuBar);    
    }
    
    private void updateImageCanvasSize()
    {
        mainPn.updateImagePanelScrollPaneSize(this.getWidth() - 350, this.getHeight() - 160);
    }
    
    private class MenuListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (e.getSource() == newItem)
            {
                System.out.println("Bringing up file chooser");
                       
                //Bring up floor plan chooser popup window
                if (floorPlanChooserFr == null)
                    floorPlanChooserFr = new FileChooserWindow(mainPn, new ImageFileFilter());
                
                floorPlanChooserFr.setVisible(true);
            }            
        }        
    }
    
    /*
    private class MainWindowButtonListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
             if (e.getSource() == saveToFileBtn)
            {
                if (imagePanel != null)
                {
                    int option = JOptionPane.YES_OPTION;
                    System.out.println("size = " + plListModel.capacity());
                    if (plListModel.isEmpty())
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
                            Enumeration iter = plListModel.elements();

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
                        Enumeration iter = plListModel.elements();
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
    * 
    */
}

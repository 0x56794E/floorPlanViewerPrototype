/**
 * Context-Free-Grammar to Push-down Automaton Converter
 * Copyright (C) 2012  Vy Thuy Nguyen
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
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Enumeration;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import util.FileGUIContainer;

/**
 * @author              Vy Thuy Nguyen
 * @version             1.0 Jan 12, 2013
 * Last modified:       
 */
public class MainPanel extends JPanel
                       implements FileGUIContainer
{
    /**
     * Reference to the main frame
     */
    MainFrame mainFr;
    
    /************** SELECTED POINTS section **************/
    /**
     * JList of points that have been marked so far.
     */
    private JList<Point> pointJList;
    
    /**
     * List model for pointList
     * This collection contains a list of points to be saved
     */
    private DefaultListModel<Point> pointListModel = new DefaultListModel<Point>();
    
    /**
     * Scroll pane for pointList
     */
    private JScrollPane plScrollPane = new JScrollPane();
    
    /**
     * Removes the selected item from pointList
     * Also, this action removes the point from the floor plan
     * as well as a Point from the list of points to be persisted
     */
    private JButton removeBtn = new JButton("Remove");
    
    /**
     * Removes all of the items in pointList, as well as those on
     * the floor plan and those in the list of points to by persisted
     */
    private JButton clearAllBtn = new JButton("Clear All");
    /************** end SELECTED POINTS section **************/
    
    
    /************** EXISTING POINT SETS section **************/
    /**
     * This is, in fact, implemented by a list. 
     * That is there can be duplication.
     */
    
    /**
     * JList of point sets that belong to the current floor plan
     */
    private JList pointSetList;
    
    /**
     * List model for pointSetList
     */
    private DefaultListModel pointSetListModel = new DefaultListModel();
    
    /**
     * Scroll pane for pointSetList
     */
    private JScrollPane pslScrollPane = new JScrollPane();
    /************** end EXISTING POINT SETS section **************/
    
    
    /************** FLOOR PLAN section **************/
    /**
     * the Floor plan
     * @see ImagePanel
     */
    private ImagePanel imagePanel;
    
    /**
     * Scroll pane for imagePanel
     */
    private JScrollPane ipScrollPane = new JScrollPane();
    /************** end FLOOR PLAN section **************/
    
    public MainPanel(MainFrame mf)
    {
        mainFr = mf;
        this.setLayout(new BorderLayout());
        
        //================ Init Components ================
        this.pointJList = new JList<Point>(pointListModel);
        this.pointSetList = new JList(pointSetListModel);
        
        //================ Render Components ================
        /*** Image Canvas ***/
        JPanel sub = new JPanel();
        TitledBorder b = new TitledBorder("Floor Plan");
        sub.setBorder(b);
        sub.add(ipScrollPane);
        
        this.add(sub, BorderLayout.CENTER);
        /*** end Image Canvas ***/
        
        
        //===== Left Pane ====
        JPanel leftPane = new JPanel();
        leftPane.setLayout(new BorderLayout());
        
        /*** Existing Point Sets ***/
        //Top-level pane
        JPanel existingPSPn = new JPanel();
        existingPSPn.setLayout(new BorderLayout());
        TitledBorder b0 = new TitledBorder("Existing Point Sets");
        existingPSPn.setBorder(b0);
          
        //JList setup
        pointSetList.setLayoutOrientation(JList.VERTICAL);
        pointSetList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        pslScrollPane.setViewportView(pointSetList);
        pslScrollPane.setPreferredSize(new Dimension(180, 130));
        existingPSPn.add(pslScrollPane, BorderLayout.CENTER);
        leftPane.add(existingPSPn, BorderLayout.NORTH);
        /*** end Existing Point Sets ***/
        
        /*** Selected Points ***/
        //Top-level pane
        JPanel selectedPPn = new JPanel();
        selectedPPn.setLayout(new BorderLayout());
        TitledBorder b1 = new TitledBorder("Selected Points");
        selectedPPn.setBorder(b1);
        
        //JList setup
        pointJList.setLayoutOrientation(JList.VERTICAL);
        pointJList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        plScrollPane.setViewportView(pointJList);
        plScrollPane.setPreferredSize(new Dimension(180, 430));
        selectedPPn.add(plScrollPane, BorderLayout.CENTER);
        
        //Buttons
        JPanel btnPn = new JPanel();
        btnPn.add(removeBtn);
        btnPn.add(clearAllBtn);
        selectedPPn.add(btnPn, BorderLayout.SOUTH);
        leftPane.add(selectedPPn, BorderLayout.CENTER);
        /*** end Selected Points ***/
        
        this.add(leftPane, BorderLayout.EAST);
    }
   
    
    public void updateImagePanelScrollPaneSize(int width, int height)
    {
        ipScrollPane.setSize(new Dimension(width, height));
        ipScrollPane.setPreferredSize(new Dimension (width, height));
    }
    
    @Override
    public void loadFileContent(File file)
    {
        imagePanel = new ImagePanel(file, MainPanel.this);
        imagePanel.repaint();
        
        ipScrollPane.setViewportView(imagePanel);
        mainFr.pack();
        mainFr.validate();
    }
    
    /**
     * Adds the specified point to the list
     * and triggers repaint to mark this point
     * on the floor plan
     * @param p 
     */
    public void addPoint(Point p)
    {
        pointListModel.addElement(p);
        imagePanel.repaint();
    }
    
    /**
     * Removes the specified point from the list
     * and triggers repaint to remove this point's mark
     * from the floor plan
     * @param p 
     */
    public void removePoint(Point p)
    {
        pointListModel.removeElement(p);
        imagePanel.repaint();
    }
    
    /**
     * Removes all points in the list
     * and triggers repaint to remove all marks
     * from the floor plan
     */
    public void clearAll()
    {
        pointListModel.clear();
        imagePanel.repaint();
    }
    
    public Enumeration<Point> getMarkedPoints()
    {
        return pointListModel.elements();
    }
    
    private class ButtonListener implements ActionListener
   {

        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (e.getSource() == removeBtn)
            {
                if (imagePanel != null && !pointListModel.isEmpty())
                    for (Point p : pointJList.getSelectedValuesList())
                        removePoint(p);
            }
            else if (e.getSource() == clearAllBtn)
            {
                if (imagePanel!= null && !pointListModel.isEmpty())
                    clearAll();
            }
        }
       
   }
}

/**
 * Floor Plan Marker Project
 * Copyright (C) 2013  Vy Thuy Nguyen
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Library General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option) any
 * later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Library General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin St, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package gui.view;

import entity.FloorPlan;
import entity.Point;
import entity.PointSet;
import gui.util.FileGUIContainer;
import gui.util.ImagePanelContainer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.persistence.EntityManager;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import util.DatabaseService;
import entity.DeadCell;
import util.FileService;

/**
 * @author Vy Thuy Nguyen
 * @version 1.0 Jan 12, 2013 Last modified:
 */
public class PointMarkingPanel extends Observable
{
    private UI uI;
    
    public PointMarkingPanel(MainFrame mf)
    {        
        uI = new UI(mf);
    }
    
    
    public UI getUI()
    {
        return uI;
        
    }
    
    
    
    public class UI extends JPanel
                    implements FileGUIContainer, ImagePanelContainer
    {
        /**
        * Reference to the existing floor plan list
        * NOTE: MIGHT NOT NEED THIS
        */
        FloorPlanListPopup flListPopup;

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
        * List model for pointList This collection contains a list of points to be
        * saved
        */
        private DefaultListModel<Point> pointListModel = new DefaultListModel<Point>();

        /**
        * Scroll pane for pointList
        */
        private JScrollPane plScrollPane = new JScrollPane();

        /**
        * Removes the selected item from pointList Also, this action removes the
        * point from the floor plan as well as a Point from the list of points to
        * be persisted
        */
        private JButton removeBtn = new JButton("Remove");

        /**
        * Removes all of the items in pointList, as well as those on the floor plan
        * and those in the list of points to by persisted
        */
        private JButton clearAllBtn = new JButton("Clear All");

        /**
        * Save the list of points for the current point set and current
        * floor plan to file.
        */
        private JButton saveToFileBtn = new JButton("Save to File");
        /************* end SELECTED POINTS section *************/

        /************* EXISTING POINT SETS section **************/
        /**
        * This is, in fact, implemented by a list. That is there can be
        * duplication.
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
        private JButton newPointSetBtn = new JButton("New Set");
        private JButton savePointSetBtn = new JButton("Save");
        /************** end EXISTING POINT SETS section **************/

        /************** FLOOR PLAN section **************/
        /**
        * the Floor plan
        *
        * @see ImagePanel
        */
        private ImagePanel imagePanel;

        private JButton startAnnotModeBtn = new JButton ("Start marking mode");

        private JButton saveAnnotBtn = new JButton("Save");

        private JButton cancelAnnotBtn = new JButton("Cancel");


        /**
        * Scroll pane for imagePanel
        */
        private JScrollPane ipScrollPane = new JScrollPane();

        /**
        * ************ end FLOOR PLAN section *************
        */

        private FloorPlan currentFloorPlan;
        private PointSet currentPointSet;
        private EntityManager em = DatabaseService.getEntityManager();

        public UI(MainFrame mf)
        {
            mainFr = mf;

            initView();
        }

        private void initView()
        {
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



    //        JPanel btnSub = new JPanel();
    //        btnSub.setLayout(new FlowLayout(FlowLayout.LEFT));
    //        currentPos.setEditable(false);
    //        currentPos.setSize(100, 60);
    //        btnSub.add(currentPos);

            //btnSub.add(startAnnotModeBtn);
            //btnSub.add(saveAnnotBtn);
            //btnSub.add(cancelAnnotBtn);
            //sub.add(btnSub, BorderLayout.SOUTH);

            this.add(sub, BorderLayout.CENTER);

            saveAnnotBtn.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e)
                {
                    //imagePanel.getShortestPath();
                }
            });

            //Annot. btns action listener
            startAnnotModeBtn.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e)
                {
                    //Ask for saving the currrent point set
                    //TODO:

                    //Show blank floor plan
                    showBlankFloorPlan();
                    disableAllButtons();

                    JButton b = (JButton)e.getSource();
                    b.setEnabled(false);
                    imagePanel.setInAnnotMode(true);
                }
            });


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
            pointSetList.addListSelectionListener(new ListSelectionListener() {

                @Override
                public void valueChanged(ListSelectionEvent e)
                {
                    if (e.getValueIsAdjusting())
                    {
                        PointMarkingPanel.UI.this.setCurrentPointSet((PointSet)pointSetList.getSelectedValue());
                    }
                }
            });

            pslScrollPane.setViewportView(pointSetList);

            pslScrollPane.setPreferredSize(new Dimension(300, 150));
            existingPSPn.add(pslScrollPane, BorderLayout.CENTER);

            //Buttons
            newPointSetBtn.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    if (savePointSetBtn.isEnabled())
                    {
                        switch (JOptionPane.showConfirmDialog(null,
                                                            "Do you want to save?",
                                                            "Save",
                                                            JOptionPane.YES_NO_CANCEL_OPTION,
                                                            JOptionPane.QUESTION_MESSAGE))
                        {
                            case JOptionPane.YES_OPTION:
                                savePointSet();
                                showBlankFloorPlan();
                                break;

                            case JOptionPane.NO_OPTION:
                                showBlankFloorPlan();
                                break;

                            default:
                                break;

                        }
                    }
                    else 
                        showBlankFloorPlan();

                }
            });

            savePointSetBtn.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e)
                {
                    savePointSet();
                }
            });
            savePointSetBtn.setEnabled(false);
            newPointSetBtn.setEnabled(false);
            JPanel upperBtnPn = new JPanel();
            upperBtnPn.add(newPointSetBtn);
            upperBtnPn.add(savePointSetBtn);
            existingPSPn.add(upperBtnPn, BorderLayout.SOUTH);

            leftPane.add(existingPSPn, BorderLayout.NORTH);
            /**
            * * end Existing Point Sets **
            */
            /**
            * * Selected Points **
            */
            //Top-level pane
            JPanel selectedPPn = new JPanel();
            selectedPPn.setLayout(new BorderLayout());
            TitledBorder b1 = new TitledBorder("Selected Points");
            selectedPPn.setBorder(b1);

            //JList setup
            pointJList.setLayoutOrientation(JList.VERTICAL);
            pointJList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            plScrollPane.setViewportView(pointJList);
            plScrollPane.setPreferredSize(new Dimension(300, 220));
            selectedPPn.add(plScrollPane, BorderLayout.CENTER);

            //Buttons
            removeBtn.addActionListener(new ActionListener()
            {

                @Override
                public void actionPerformed(ActionEvent e)
                {
                    if (imagePanel != null && !pointListModel.isEmpty())
                    {
                        for (Point p : pointJList.getSelectedValuesList())
                            removePoint(p);
                    }
                }
            });

            clearAllBtn.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    if (imagePanel != null && !pointListModel.isEmpty())
                        clearAll();
                }
            });

            saveToFileBtn.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e)
                {
                    if (imagePanel != null && !pointListModel.isEmpty())
                    {
                        saveToFile();
                    }
                }

            });
            removeBtn.setEnabled(false);
            clearAllBtn.setEnabled(false);
            saveToFileBtn.setEnabled(false);

            JPanel btnPn = new JPanel();
            btnPn.add(removeBtn);
            btnPn.add(clearAllBtn);
            btnPn.add(saveToFileBtn);
            selectedPPn.add(btnPn, BorderLayout.SOUTH);
            leftPane.add(selectedPPn, BorderLayout.CENTER);
            /**
            * * end Selected Points **
            */
            this.add(leftPane, BorderLayout.EAST);
        }

        public void setCurrentPos(String str)
        {

        }

        private void saveToFile()
        {
            throw new UnsupportedOperationException();
        }

        private void showBlankFloorPlan()
        {
            currentPointSet = new PointSet(currentFloorPlan);
            currentFloorPlan.addPointSet(currentPointSet);
            pointListModel.clear();
            updateView();
        }

        private void showBlankFloorPlanForAnnot()
        {

        }

        private void savePointSet()
        {
            EntityManager em = DatabaseService.getEntityManager();
            em.getTransaction().begin();
            em.persist(currentPointSet);
            em.persist(currentFloorPlan);

            em.getTransaction().commit();

            savePointSetBtn.setEnabled(false);
            updatePointSetJList();
        }

        private void updatePointSetJList()
        {
            pointSetListModel.clear();
            for (PointSet ps : currentFloorPlan.getPointSets())
                pointSetListModel.addElement(ps);
        }

        public void updateImagePanelScrollPaneSize(int width, int height)
        {
            if (imagePanel != null)
                ipScrollPane.setMaximumSize(imagePanel.getPaddedImageSize());
            ipScrollPane.setSize(new Dimension(width, height));
            ipScrollPane.setPreferredSize(new Dimension(width, height));

        }

        public boolean hasImage()
        {
            return imagePanel != null;
        }

        public File getImageFile()
        {
            if (imagePanel != null)
                return imagePanel.getImageFile();
            else
                return null;
        }

        public boolean exportImage()
        {
            if (hasImage())
            {
                try
                {
                    //imagePanel.exportCurrentFloorPlan();
                    FileService.saveFloorPlanToFile(currentFloorPlan, currentPointSet);
                }
                catch (IOException exc)
                {
                JOptionPane.showMessageDialog(null, 
                                                "Error While Writing to File. Try Again", 
                                                "ERROR!", 
                                                JOptionPane.ERROR_MESSAGE);
                return false;
                }

                return true;
            }
            else
                return false;
        }

        @Override
        public void loadFileContent(File file) throws IOException
        {
            int createNew = JOptionPane.YES_OPTION;

            //Exisitng floor plan list
            List<FloorPlan> existingFLList = getExistingFloorPlans(file.getName());

            if (existingFLList.size() > 0)
            {
                createNew = JOptionPane.showConfirmDialog(null,
                                                        "Looks like you've already saved this floor plan in the database.\nDo you want to create a new record anyways?",
                                                        "Proceed?", JOptionPane.YES_NO_OPTION);
            }

            if (createNew == JOptionPane.YES_OPTION)
            {
                imagePanel = new ImagePanel(file, PointMarkingPanel.UI.this);
                currentFloorPlan = new FloorPlan(file, 0, 0, imagePanel.getImageWidth(), imagePanel.getImageHeight());
                currentPointSet = new PointSet(currentFloorPlan);
                currentFloorPlan.addPointSet(currentPointSet);
                ipScrollPane.setViewportView(imagePanel);
                
                updateView();
            }
            else if (createNew == JOptionPane.NO_OPTION)
            {
                //Show existing floor plans for user to choose from
                showFloorPlanListPopup(existingFLList);
            }

            //If close, do nothing
        }

        /**
        * Show a list of all floor plans in database in a popup window.
        * User can choose a floor plan from this list
        */
        public void showFloorPlanListPopup()
        {   
            this.flListPopup = new FloorPlanListPopup(PointMarkingPanel.this);        
        }

        /**
        * Show a list of all floor plans in database with given name in a popup window.
        * User can choose a floor plan from this list
        * 
        * @param fileName 
        */
        public void showFloorPlanListPopup(String fileName)
        {
            this.flListPopup = new FloorPlanListPopup(PointMarkingPanel.this, fileName);
        }

        /**
        * Show the given list of floor plans in a popup window.
        * User can choose a floor plan from this list.
        * 
        * @param list 
        */
        public void showFloorPlanListPopup(List<FloorPlan> list)
        {
            this.flListPopup = new FloorPlanListPopup(PointMarkingPanel.this, list);
        }

        public void setCurrentFloorPlan(FloorPlan fp) throws IOException
        {

            this.currentFloorPlan = fp;
            this.currentPointSet = fp.getPointSets().get(0);

            imagePanel = new ImagePanel(new File(fp.getAbsoluteFilePath()), PointMarkingPanel.UI.this);

            this.updatePointSetJList();

            imagePanel.repaint();
            ipScrollPane.setViewportView(imagePanel);
            mainFr.pack();
            mainFr.validate();

            updateView();
            //this.repaint();
            //mainFr.validate();
        }

        public void setCurrentPointSet(PointSet ps)
        {
            this.currentPointSet = ps;
            updateView();
        }

        /**
        *
        * @param fileName
        * @return true if a floor plan record with the same file name has already
        * existed db
        */
        private boolean hasDuplicate(String fileName)
        {
            long count = em.createQuery("SELECT COUNT(f.id) FROM FloorPlan f WHERE f.fileName = :name", Long.class).setParameter("name", fileName).getSingleResult();
            return (count > 0 ? true : false);
        }

        private List<FloorPlan> getExistingFloorPlans(String fileName)
        {
            return em.createQuery("SELECT f FROM FloorPlan f WHERE f.fileName = :name")
                     .setParameter("name", fileName)
                     .getResultList();
        }


        /**
        * Adds the specified point to the list and triggers repaint to mark this
        * point on the floor plan
        *
        * @param p
        */
        public void addPoint(Point p)
        {
            pointListModel.addElement(p);
            p.setPointSet(currentPointSet);
            currentPointSet.addPoint(p);
            updateView();
        }

        /**
        * Removes the specified point from the list and triggers repaint to remove
        * this point's mark from the floor plan
        *
        * @param p
        */
        public void removePoint(Point p)
        {
            pointListModel.removeElement(p);
            currentPointSet.removePoint(p);
            updateView();
        }

        /**
        * Removes all points in the list and triggers repaint to remove all marks
        * from the floor plan
        */
        public void clearAll()
        {
            pointListModel.clear();
            currentPointSet.clearAllPoints();
            updateView();
        }

        private void updateView()
        {
            PointMarkingPanel.this.setChanged();
            PointMarkingPanel.this.notifyObservers(PointMarkingPanel.this);
            
            if (currentFloorPlan != null && currentFloorPlan.getPointSets().size() > 0)
            {
                savePointSetBtn.setEnabled(true);

                if (currentPointSet != null && currentPointSet.getPoints().size() > 0)
                {
                    clearAllBtn.setEnabled(true);
                    removeBtn.setEnabled(true);
                    newPointSetBtn.setEnabled(true);
                }
                else
                {
                    disableAllButtons();
                }
            }
            else
            {
                disableAllButtons();
            }

            ipScrollPane.setMaximumSize(imagePanel.getPaddedImageSize());
            imagePanel.repaint();
            mainFr.validate();
        }

        private void disableAllButtons()
        {
            savePointSetBtn.setEnabled(false);
            clearAllBtn.setEnabled(false);
            removeBtn.setEnabled(false);
            newPointSetBtn.setEnabled(false);
        }

        public PointSet getCurrentPointSet()
        {
            return currentPointSet;
        }

        public FloorPlan getCurrentFloorPlan()
        {
            return currentFloorPlan;
        }

//        @Override
//        public void disableCell(int x, int y, DeadCell c)
//        {
//            currentFloorPlan.getAnnotFloorPlan().disableCell(x, y, c);
//        }

        @Override
        public void doPaintComponent(Graphics g, BufferedImage pinImg)
        {
            if (currentPointSet != null)
            {
                List<Point> points = currentPointSet.getPoints();
                System.out.println("points count = " + points.size());
                for (Point p : points)
                    g.drawImage(pinImg, p.getX() - 12, p.getY() - 12, null);
            }
        }

        @Override
        public void doPaintComponent(Graphics g)
        {
            if (currentPointSet != null)
            {
                List<Point> points = currentPointSet.getPoints();
                System.out.println("points count = " + points.size());
                for (Point p : points)
                    g.fillOval(p.getX() - 4, p.getY() - 4, 8, 8);
            }
        }

        @Override
        public void onMouseClicked(int x, int y)
        {
            Point p = new Point(x, y);
            pointListModel.addElement(p);
            p.setPointSet(currentPointSet);
            currentPointSet.addPoint(p);
            updateView();

        }

        @Override
        public void updateCurrentPositionLabel(String str)
        {
            mainFr.setCurrentPositionlabel(str);
        }
    }
}

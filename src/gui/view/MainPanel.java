/**
 * Context-Free-Grammar to Push-down Automaton Converter Copyright (C) 2012 Vy
 * Thuy Nguyen
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
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Enumeration;
import javax.persistence.EntityManager;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import util.DatabaseService;

/**
 * @author Vy Thuy Nguyen
 * @version 1.0 Jan 12, 2013 Last modified:
 */
public class MainPanel extends JPanel
        implements FileGUIContainer
{

    /**
     * Reference to the main frame
     */
    MainFrame mainFr;
    /**
     * ************ SELECTED POINTS section *************
     */
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
     * ************ end SELECTED POINTS section *************
     */
    /**
     * ************ EXISTING POINT SETS section *************
     */
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
    /**
     * ************ end EXISTING POINT SETS section *************
     */
    /**
     * ************ FLOOR PLAN section *************
     */
    /**
     * the Floor plan
     *
     * @see ImagePanel
     */
    private ImagePanel imagePanel;
    /**
     * Scroll pane for imagePanel
     */
    private JScrollPane ipScrollPane = new JScrollPane();
    /**
     * ************ end FLOOR PLAN section *************
     */
    private FloorPlan currentFloorPlan;
    private PointSet currentPointSet;

    public MainPanel(MainFrame mf)
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
        /**
         * * Image Canvas **
         */
        JPanel sub = new JPanel();
        TitledBorder b = new TitledBorder("Floor Plan");
        sub.setBorder(b);
        sub.add(ipScrollPane);

        this.add(sub, BorderLayout.CENTER);
        /**
         * * end Image Canvas **
         */
        //===== Left Pane ====
        JPanel leftPane = new JPanel();
        leftPane.setLayout(new BorderLayout());

        /**
         * * Existing Point Sets **
         */
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

        //Buttons
        newPointSetBtn.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e)
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
        plScrollPane.setPreferredSize(new Dimension(180, 400));
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

        removeBtn.setEnabled(false);
        clearAllBtn.setEnabled(false);

        JPanel btnPn = new JPanel();
        btnPn.add(removeBtn);
        btnPn.add(clearAllBtn);
        selectedPPn.add(btnPn, BorderLayout.SOUTH);
        leftPane.add(selectedPPn, BorderLayout.CENTER);
        /**
         * * end Selected Points **
         */
        this.add(leftPane, BorderLayout.EAST);
    }

    private void showBlankFloorPlan()
    {
        currentPointSet = new PointSet(currentFloorPlan);
        pointListModel.clear();
        updateView();
    }

    private void savePointSet()
    {
        EntityManager em = DatabaseService.getEntityManager();
        em.getTransaction().begin();
        em.persist(currentPointSet);
//        em.persist(currentFloorPlan);
//        for (Point p : currentPointSet.getPoints())
//        {
//            em.persist(p);
//        }
        
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
        ipScrollPane.setSize(new Dimension(width, height));
        ipScrollPane.setPreferredSize(new Dimension(width, height));
    }

    @Override
    public void loadFileContent(File file)
    {
        int createNew = JOptionPane.YES_OPTION;

        //Check db
        if (hasDuplicate(file.getName()))
        {
            createNew = JOptionPane.showConfirmDialog(null,
                                                      "Looks like you've already saved this floor plan in the database.\nDo you want to create a new record anyways?",
                                                      "Proceed?", JOptionPane.YES_NO_OPTION);
        }

        if (createNew == JOptionPane.YES_OPTION)
        {
            currentFloorPlan = new FloorPlan(file, 0, 0);
            currentPointSet = new PointSet(currentFloorPlan);
            currentFloorPlan.addPointSet(currentPointSet);

            imagePanel = new ImagePanel(file, MainPanel.this);
            imagePanel.repaint();
            ipScrollPane.setViewportView(imagePanel);
            mainFr.pack();
            mainFr.validate();
        }
        else if (createNew == JOptionPane.NO_OPTION)
        {
        }

        //If close, do nothing
    }

    public void setCurrentFloorPlan(FloorPlan fp)
    {
        this.currentFloorPlan = fp;
    }

    /**
     *
     * @param fileName
     * @return true if a floor plan record with the same file name has already
     * existed db
     */
    private boolean hasDuplicate(String fileName)
    {
        EntityManager em = DatabaseService.getEntityManager();
        long count = em.createQuery("SELECT COUNT(f.id) FROM FloorPlan f WHERE f.fileName = :name", Long.class).setParameter("name", fileName).getSingleResult();
        return (count > 0 ? true : false);
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

        imagePanel.repaint();
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
}

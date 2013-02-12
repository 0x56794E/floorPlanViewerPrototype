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

import entity.AnnotFloorPlan;
import entity.Cell;
import gui.util.ImagePanelContainer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import partitioner.InertialPartitioner;
import partitioner.Line;
import partitioner.VirtualLine;
import util.DatabaseService;
import util.FileService;
import partitioner.SpectralPartitioner;

/**
 * @author              Vy Thuy Nguyen
 * @version             1.0 Jan 17, 2013
 * Last modified:       
 */
public class AnnotPanel extends JPanel
                        implements ImagePanelContainer, Observer
{
    private static final int MAX = (int)(Math.pow(2, 24)) - 1;
    private MainFrame mainFr;
    private ImagePanel imagePanel;
    private JScrollPane ipScrollPane = new JScrollPane();
    boolean inMarkingMode;
    JButton markEraseBtn;
    JButton saveDeadCellBtn = new JButton("Save");
    JButton saveDeadCellToFileBtn = new JButton("Save Dead Cells to File");
    JButton saveGraphBtn = new JButton("Save Available Graph Region To File");
    JButton showPartitionBtn = new JButton("Show Partitioning Lines");
    private Set<Cell> deadCells;
    private boolean showPartition = false;
    
    public AnnotPanel(MainFrame mf)
    {
        mainFr = mf;       
        inMarkingMode = true;
        markEraseBtn = new JButton("Eraser");
        markEraseBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (inMarkingMode)
                    markEraseBtn.setText("Marker");
                else
                    markEraseBtn.setText("Eraser");
                
                inMarkingMode = !inMarkingMode;
            }
        
        });
        
        saveDeadCellBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                EntityManager em = DatabaseService.getEntityManager();  
                em.getTransaction().begin();
                AnnotFloorPlan afp = mainFr.getCurrentFloorPlan().getAnnotFloorPlan();
                
                for (Cell dc : afp.getDeadCells())
                {
                    dc.setAnnotFloorPlan(afp);
                    em.persist(dc);
                }
                
                em.persist(mainFr.getCurrentFloorPlan().getAnnotFloorPlan());
                em.getTransaction().commit();
                JOptionPane.showMessageDialog(null, "Successfully Save Floor Plan");
                
                //DatabaseService.cleanup();
            }
        });        
        
        saveDeadCellToFileBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    FileService.saveDeadCellsToFile(AnnotPanel.this.deadCells);
                    JOptionPane.showMessageDialog(null, "Successfully Saved Dead Cells to File");
                }
                catch (IOException ex)
                {
                    JOptionPane.showMessageDialog(null, "Error while saving dead cells to file", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
        
        });
        
        saveGraphBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    FileService.saveLargestConnectedComponent(mainFr.getCurrentFloorPlan().getAnnotFloorPlan());                    
                    JOptionPane.showMessageDialog(null, "Successfully Saved Graph To File");
                }
                catch (Exception ex)
                {
                    JOptionPane.showMessageDialog(null, "Error while saving graph to file", "ERROR", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        
        });
        showPartitionBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                showPartition = true;
                
                imagePanel.repaint();
            }
        
        });
        
        //Render components
        this.setLayout(new BorderLayout());
        this.add(ipScrollPane, BorderLayout.CENTER);
        
        JPanel btnPn = new JPanel();
        btnPn.add(markEraseBtn);
        btnPn.add(saveDeadCellBtn);
        btnPn.add(saveDeadCellToFileBtn);
        btnPn.add(saveGraphBtn);
        btnPn.add(showPartitionBtn);
        this.add(btnPn, BorderLayout.SOUTH);
    }

    @Override
    public void doPaintComponent(Graphics g)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void doPaintComponent(Graphics g, BufferedImage img)
    {
        if (showPartition)
        {
            try
            {
                showPartition = false;
                doPaintComponentWithPartition(g);                
            }
            catch (Exception e)
            {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, 
                                              "Error encountered. No Partitioniing Has Been Done",
                                              "ERROR", 
                                              JOptionPane.ERROR_MESSAGE);
            }
        }
        else
            doPaintComponentWithoutPartition(g);
        
    }

    @Override
    public void onMouseClicked(int x, int y)
    {
        if (inMarkingMode)
            mainFr.getCurrentFloorPlan().getAnnotFloorPlan().disableCell(x, y);
       
        else
            mainFr.getCurrentFloorPlan().getAnnotFloorPlan().enabbleCell(x, y);                   
       
        repaint();
    }

    @Override
    public void updateCurrentPositionLabel(String str)
    {
        mainFr.setCurrentPositionlabel(str);
    }

    @Override
    public void update(Observable o, Object arg)
    {
        try
        {
            PointMarkingPanel pn = (PointMarkingPanel)arg;
            imagePanel = new ImagePanel(pn.getUI().getImageFile(), this);
            ipScrollPane.setViewportView(imagePanel);
        }
        catch (IOException e)
        {
            System.out.println("unable to update annotPn");
        }
    }

    /**
     * Get the largest number representing color of the regions partitioned 
     * by the given lines
     * 
     * @param lines
     * @return double the value
     */
    private double getMaxColorValue(ArrayList<Line> lines)
    {
        double maxC = 0;
        
        for (Line l : lines)
        {
             if (l.getLeftNodes().size() > 0
                     && l.getLeftNodes().get(0).getIntVal() > maxC)
                maxC = l.getLeftNodes().get(0).getIntVal();
            
            if (l.getRightNodes().size() > 0
                    && l.getRightNodes().get(0).getIntVal() > maxC)
                maxC = l.getRightNodes().get(0).getIntVal();
        }
        return maxC;
    }

    private double getMaxColorValueG(ArrayList<VirtualLine> lines)
    {
        double maxC = 0;
        for (VirtualLine l : lines)
        {
             if (l.getNMinus().size() > 0 
                     && l.getNMinus().get(0).getIntVal() > maxC)
                maxC = l.getNMinus().get(0).getIntVal();
             
             if (l.getNPlus().size() > 0
                     && l.getNPlus().get(0).getIntVal() > maxC)
                 maxC = l.getNPlus().get(0).getIntVal();
            
        }
        return maxC;
    }
    
    private void doPaintComponentWithoutPartition(Graphics g)
    {
        deadCells = mainFr.getCurrentFloorPlan().getAnnotFloorPlan().getDeadCells();
        int unitW = mainFr.getCurrentFloorPlan().getAnnotFloorPlan().getUnitW();
        int unitH = mainFr.getCurrentFloorPlan().getAnnotFloorPlan().getUnitH();
        int rowCount = mainFr.getCurrentFloorPlan().getAnnotFloorPlan().getRowCount(),
            colCount = mainFr.getCurrentFloorPlan().getAnnotFloorPlan().getColCount(),
            x, y;
        Cell[][] cells = mainFr.getCurrentFloorPlan().getAnnotFloorPlan().getCellContainer();
               
        //Paint wall
        for (Cell dc : mainFr.getCurrentFloorPlan().getAnnotFloorPlan().getDeadCells())
        {
            g.fillRect(dc.getCol() * unitW, dc.getRow() * unitH, unitW, unitH);
        }
    }

    /**
     * Do partitioning using Spectral Method
     * @param g
     * @throws Exception 
     */
    private void doPaintComponentWithPartition(Graphics g) throws Exception
    {
        ArrayList<VirtualLine> lines = 
                SpectralPartitioner.getLines(mainFr.getCurrentFloorPlan().getAnnotFloorPlan().getLargestConnectedComponentAsGraph(), 
                                             100);
        double maxC = getMaxColorValueG(lines);
        double zoomedIndex = MAX / maxC;
        //VirtualLine vLine = SpectralPartitioner.getLine(mainFr.getCurrentFloorPlan().getAnnotFloorPlan().getLargestConnectedComponentAsGraph());
        int unitW = mainFr.getCurrentFloorPlan().getAnnotFloorPlan().getUnitW();
        int unitH = mainFr.getCurrentFloorPlan().getAnnotFloorPlan().getUnitH();
        
        //Dead cells
        g.setColor(Color.darkGray);
        for (Cell c : deadCells)
            g.fillRect(c.getCol() * unitW, c.getRow() * unitH, unitW, unitH);
        
        System.out.println("\n\nLINE COUNT = " + lines.size());
        
        for (VirtualLine vLine : lines)
        {
            //N- section
            for (Cell c : vLine.getNMinus())
            {
                g.setColor(c.getColor(zoomedIndex));
                g.fillRect(c.getCol() * unitW, c.getRow() * unitH, unitW, unitH);
            }
            
            //N+ section
            for (Cell c : vLine.getNPlus())
            {
                g.setColor(c.getColor(zoomedIndex));
                g.fillRect(c.getCol() * unitW, c.getRow() * unitH, unitW, unitH);
            }
        }
    }
    
    /**
     * Do partitioning using Inertial Method
     * @param g
     * @param k
     * @throws Exception 
     */
    private void doPaintComponentWithPartition(Graphics g, int k) throws Exception
    {
        //Get Lines
        ArrayList<Cell> nodes = mainFr.getCurrentFloorPlan().getAnnotFloorPlan().getLargestConnectedComponent();
        ArrayList<Line> lines = InertialPartitioner.getLines(nodes, k);   
        double maxC = getMaxColorValue(lines);
        double zoomedIndex = MAX / maxC;
        int unitW = mainFr.getCurrentFloorPlan().getAnnotFloorPlan().getUnitW();
        int unitH = mainFr.getCurrentFloorPlan().getAnnotFloorPlan().getUnitH();
     
        //Dead cells
        g.setColor(Color.darkGray);
        for (Cell c : deadCells)
            g.fillRect(c.getCol() * unitW, c.getRow() * unitH, unitW, unitH);
        
        //Paint partitioning
        for (Line line : lines)
        {
            //Left
            for (Cell lcell : line.getLeftNodes())
            {
                g.setColor(lcell.getColor(zoomedIndex));
                g.fillRect(lcell.getCol() * unitW, lcell.getRow() * unitH, unitW, unitH);
            }
            
            //Right
            for (Cell rcell : line.getRightNodes())
            {
                g.setColor(rcell.getColor(zoomedIndex));
                g.fillRect(rcell.getCol() * unitW, rcell.getRow() * unitH, unitW, unitH);
            }
        }
      
    }
}

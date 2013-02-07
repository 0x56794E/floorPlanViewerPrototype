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
import java.awt.*;
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
import util.DatabaseService;
import util.FileService;

/**
 * @author              Vy Thuy Nguyen
 * @version             1.0 Jan 17, 2013
 * Last modified:       
 */
public class AnnotPanel extends JPanel
                        implements ImagePanelContainer, Observer
{
    private static final int MAX = (int)(Math.pow(2, 24)) - 1;
    
    //TEMP!!
//    private static final Color[] colors = {
//        new Color(102, 51, 255), //purple
//        new Color(138, 184, 0), //
//        new Color(255, 0, 0),
//        new Color(255, 102, 0),
//        new Color(0, 26, 255), //blue
//        new Color(0, 163, 0),
//        new Color(163, 82, 0),
//        new Color(0, 163, 163),
//        new Color(0, 0, 0),
//        new Color(255, 255, 255)
//    };

    private static ArrayList<Color> colors;
    private MainFrame mainFr;
    private ImagePanel imagePanel;
    private JScrollPane ipScrollPane = new JScrollPane();
    
    boolean inMarkingMode;
    JButton markEraseBtn;
    JButton saveDeadCellBtn = new JButton("Save");
    JButton saveDeadCellToFileBtn = new JButton("Save to File");
    JButton saveGraphBtn = new JButton("Save Floor Plan for Partitioning");
    JButton showPartitionBtn = new JButton("Show Partitioning Lines");
    private Set<Cell> deadCells;
    private boolean showPartition = false;
    
    public AnnotPanel(MainFrame mf)
    {
        
        colors = new ArrayList<Color>();
//        for (int i = 0; i < 8; ++i)
//            for (int j = 0; j < 4; ++j)
//                for (int k = 0; k < 4; ++k)
//                    colors.add(new Color(i, j, k));
        
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
                //em.close();
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
                    //FileService.saveGraph(AnnotPanel.this.mainFr.getCurrentFloorPlan().getAnnotFloorPlan());
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
        int colorIndex = 0;
        deadCells = mainFr.getCurrentFloorPlan().getAnnotFloorPlan().getDeadCells();
        int unitW = mainFr.getCurrentFloorPlan().getAnnotFloorPlan().getUnitW();// / 2;
        int unitH = mainFr.getCurrentFloorPlan().getAnnotFloorPlan().getUnitH();// / 2;
        for (Cell dc : deadCells)
            g.fillRect(dc.getMinX(), dc.getMinY(), unitW, unitH);      
        g.setColor(Color.red);
        for (Cell cell : mainFr.getCurrentFloorPlan().getAnnotFloorPlan().getGraph().vertexSet())
        {
            g.drawRect(cell.getCol() * unitW, cell.getRow() * unitH, unitW, unitH);
        }
        //Visualize the lines
        if (showPartition)
        {
            try
            {
                showPartition = false;
            
                //Get Lines
                ArrayList<Cell> nodes = mainFr.getCurrentFloorPlan().getAnnotFloorPlan().getLargestConnectedComponent();
           
                ArrayList<Line> lines = InertialPartitioner.getLines(nodes, 100);
                
                double maxC = getMaxC(lines);
                double zoomedIndex = MAX / maxC;
                g.setFont(new Font("arial", Font.BOLD, 15));
                g.setColor(Color.orange);
                Graphics2D g2D = (Graphics2D) g;      
                
 
                double halfUnitW = unitW / 2, halfUnitH = unitH / 2,
                       epsilon = 0.00001;
                int actualWidth = (int)(mainFr.getCurrentFloorPlan().getWidth() / unitW);
                int actualHeight = (int)(mainFr.getCurrentFloorPlan().getHeight() / unitH);
                int x1, y1, x2, y2, x, y;
                Line line;
                
                for (int i = 0; i < lines.size(); ++i)
                {
                    //Don't draw line, color the area instead
                    
                    g2D.setStroke(new BasicStroke(10F));  // set stroke width of 10
                    //Line L: a(x - xbar) + b(y - ybar) = 0;
                    line = lines.get(i);
                    
                    //Color left Nodes
                    //g2D.setColor(colors.get(colorIndex++));
                    for (Cell c : line.getLeftNodes())
                    {
                        g2D.setColor(c.getColor(zoomedIndex));
                    
                        g2D.fillRect(c.getCol() * unitW, c.getRow() * unitH, unitW, unitH);
                    }
                    
                    
                    //g2D.setColor(colors.get(colorIndex++));
                    for (Cell c : line.getRightNodes())
                    {
                        System.out.println("Coloring cell " + c + ": binaryString = " + c.getBinaryString() + "; intValue = " + c.getIntVal());
                        g2D.setColor(c.getColor(zoomedIndex));
                        g2D.fillRect(c.getCol() * unitW, c.getRow() * unitH, unitW, unitH);
                    }
                    
                    
                    //Find the points where the line crosses four sides of the floor plan
                    //The four sides are represented by:
                    //Top: Lt: y = 0
                    //Left: Ll: x = 0;
                    //Bottom: Lb: y = -h;
                    //Right: Lr: x = w;
//                    ArrayList<Cell> intersects = new ArrayList<>();
//                    if (Math.abs(line.getA()) < epsilon) //if a = 0 => horizontal line
//                    {
//                        //Left
//                        intersects.add(new Cell(Math.abs((int)(line.getA() * line.getXbar() / line.getB() + line.getYbar())), 
//                                                0));
//                        
//                        //Right
//                        intersects.add(new Cell(Math.abs((int)(line.getA() * (line.getXbar() - actualWidth) / line.getB() + line.getYbar())),
//                                                actualWidth));
//                    }
//                    else if (Math.abs(line.getB()) < epsilon) //if b = 0 => vertical line
//                    {
//                        //Top
//                        intersects.add(new Cell(0,
//                                                (int)(line.getB() * line.getYbar() / line.getA() + line.getXbar())
//                                ));
//                        
//                        //Bottom
//                        intersects.add(new Cell(actualHeight,
//                                                (int)(line.getB() * (line.getYbar() + actualHeight) / line.getA() + line.getXbar())
//                                ));
//                    }
//                    else //diagonal line; check all four
//                    {
//                        //Left
//                        intersects.add(new Cell(Math.abs((int)(line.getA() * line.getXbar() / line.getB() + line.getYbar())), 
//                                                0) );
//                        
//                        //Right
//                        intersects.add(new Cell(Math.abs((int)(line.getA() * (line.getXbar() - actualWidth) / line.getB() + line.getYbar())),
//                                                actualWidth));
//                        
//                        //Top
//                        intersects.add(new Cell(0,
//                                                (int)(line.getB() * line.getYbar() / line.getA() + line.getXbar())
//                                ));
//                        
//                        //Bottom
//                        intersects.add(new Cell(actualHeight,
//                                                (int)(line.getB() * (line.getYbar() + actualHeight) / line.getA() + line.getXbar())
//                                ));
//                        
//                        ArrayList<Integer> dels = new ArrayList<Integer>();
//                        for (int k = 0; k < 4; ++k)
//                        {
//                            if (intersects.get(k).getRow() > actualHeight || intersects.get(k).getCol() > actualWidth
//                                    || intersects.get(k).getRow() < 0 || intersects.get(k).getCol() < 0)
//                                dels.add(k);
//                        }
//                        
//                        for (int del = 0; del < dels.size(); ++del)
//                            intersects.remove(del);
//                    }
//                        
//                    //Converting from sparse grid to dense grid coordinates
//                    //and make sure lines is drawn over the entire floor plan
//
//                    System.out.printf("drawing line #%d: a = %f; b = %f; xbar = %f; ybar = %f\n",
//                                        (i + 1),
//                                        line.getA(),
//                                        line.getB(),
//                                        line.getXbar(),
//                                        line.getYbar());
//                    x1 = (int)(intersects.get(0).getCol() * unitW + halfUnitW);
//                    y1 = (int)(intersects.get(0).getRow() * unitH + halfUnitH);
//                    x2 = (int)(intersects.get(1).getCol() * unitW + halfUnitW);
//                    y2 = (int)(intersects.get(1).getRow() * unitH + halfUnitH);
//                    System.out.printf("thru p1(%d, %d) and p2(%d, %d)\n",
//                                        x1,
//                                        y1,
//                                        x2,
//                                        y2);
//
//                    g2D.drawLine(x1, y1, x2, y2);
//                    
//                    //Draw line whose directional vector u = (a, b) and crosses thru (xbar, ybar)
//                    g2D.setColor(Color.blue);
//                    g2D.setStroke(new BasicStroke(5F));  // set stroke width of 10
//                    
//                    int[] p2 = new int[2];
//                    if (line.getA() < epsilon) //if a is 0
//                    {
//                        p2[0] = (int)(line.getXbar() * unitW + halfUnitW); //x
//                        p2[1] = (int)(actualHeight * unitH + halfUnitH); //y
//                    }
//                    else if (line.getB() < epsilon) //if b is 0
//                    {
//                        p2[0] = (int)(actualWidth * unitW + halfUnitW); //x
//                        p2[1] = (int)(line.getYbar() * unitH + halfUnitH); //y
//                    }
//                    else
//                    {
//                        p2[0] = (int)halfUnitW;
//                        p2[1] = (int)((line.getYbar() - line.getB() * line.getXbar() / line.getA()) * unitH + halfUnitH);
//                    }
//                    
//                    g2D.drawLine((int)(line.getXbar() * unitW + halfUnitW), 
//                                 (int)(line.getYbar() * unitH + halfUnitH), 
//                                 p2[0],
//                                 p2[1]);
//                    
                }
            //}   
            }
            catch (Exception e)
            {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "No Partitioniing Has Been Done", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
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
            //System.out.println("Update called");
            PointMarkingPanel pn = (PointMarkingPanel)arg;
            imagePanel = new ImagePanel(pn.getUI().getImageFile(), this);
            ipScrollPane.setViewportView(imagePanel);
        }
        catch (IOException e)
        {
            System.out.println("unable to update annotPn");
        }
    }

    private double getMaxC(ArrayList<Line> lines)
    {
        double marC = 0;
        
        for (Line l : lines)
        {
            for (int i = 0; i < l.getLeftNodes().size(); ++i)
            {
                if (l.getLeftNodes().get(i).getIntVal() > marC)
                    marC = l.getLeftNodes().get(i).getIntVal();
                break;
            }
            
            for (int i = 0; i < l.getRightNodes().size(); ++i)
            {
                if (l.getRightNodes().get(i).getIntVal() > marC)
                    marC = l.getRightNodes().get(i).getIntVal();
                break;
            }
            
        }
        
        return marC;
    }

}

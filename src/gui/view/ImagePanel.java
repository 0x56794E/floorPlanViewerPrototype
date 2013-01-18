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
import util.ImagePanelMouseListener;
import entity.Point;
import entity.PointSet;
import gui.util.ImagePanelContainer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import util.DeadCell;

/**
 * @author              Vy Thuy Nguyen
 * @version             1.0 Jan 9, 2013
 * Last modified:       
 */
public class ImagePanel extends JPanel
{
    private BufferedImage pinImg;
    private BufferedImage image;

    //public JLabel lb;
    //public String fileName;
    //public String absPath;
    ImageIcon icon;
    
    private boolean inAnnotMode = false;
    private ImagePanelMouseListener mouseListener = new ImagePanelMouseListener();
        
    private ImagePanelContainer containerPn;
    ArrayList<DeadCell> deadCells = new ArrayList<DeadCell>();
    
    
    List<DefaultEdge> edges;
    private File imageFile;
    
    public ImagePanel(File file, ImagePanelContainer container) throws IOException
    {
        image = ImageIO.read(file);
        pinImg = ImageIO.read(new File("resources\\pin.png"));
        imageFile = file;
        
        //absPath = file.getPath();
        //fileName = file.getName();
        containerPn = container;
        
        
        icon = new ImageIcon(image);
        this.setPreferredSize(new Dimension(icon.getIconWidth(), icon.getIconHeight())); 
        
        
        //Mouse listener
        this.addMouseListener(mouseListener);
        this.addMouseMotionListener(mouseListener);
    }

    
    
    
    public void setInAnnotMode(boolean m)
    {
        inAnnotMode = m;
        mouseListener.setInAnnotMode(m);
    }
    
    public void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null);    
        
        containerPn.doPaintComponent(g, pinImg);
//        if (inColorPathMode)
//        {
//            g.setColor(Color.DARK_GRAY);
//            AnnotFloorPlan afl = mainPn.getCurrentFloorPlan().getAnnotFloorPlan();
//            SimpleGraph graph = afl.getGraph();
//            for (DefaultEdge e : edges)
//            {
//                Cell source = (Cell)graph.getEdgeSource(e);
//                Cell target = (Cell)graph.getEdgeTarget(e);
//                
//                g.drawLine(source.getCol() * afl.getUnitW(),
//                           source.getRow() * afl.getUnitH(),
//                           target.getCol() * afl.getUnitW(), 
//                           target.getRow() * afl.getUnitH());
//            }
//        }
 //       else
//        {
//            g.setColor(Color.RED);
//
//            if (inAnnotMode)
//            {
//                System.out.println("in annot mode; dead cell count = " + deadCells.size());
//                for (DeadCell dc : deadCells)
//                    g.fillRect(dc.getMinX(), dc.getMinY(), dc.getWidth(), dc.getHeight());
//                
//                if (inColorPathMode)
//                {
//                        g.setColor(Color.DARK_GRAY);
//                        AnnotFloorPlan afl = mainPn.getCurrentFloorPlan().getAnnotFloorPlan();
//                        SimpleGraph graph = afl.getGraph();
//                        for (DefaultEdge e : edges)
//                        {
//                            Cell source = (Cell)graph.getEdgeSource(e);
//                            Cell target = (Cell)graph.getEdgeTarget(e);
//
//                            g.drawLine(source.getCol() * afl.getUnitW(),
//                                    source.getRow() * afl.getUnitH(),
//                                    target.getCol() * afl.getUnitW(), 
//                                    target.getRow() * afl.getUnitH());
//                        }
//                }
//            }
//            else
//            {
//                PointSet ps = mainPn.getCurrentPointSet();
//                
//            }
        
    }
    
    /**
     * To be called from click handler 
     * @param x
     * @param y 
     */
//    public void addPoint(int x, int y)
//    {
//        //Point p = new Point(x, y);
//        //mainPn.addPoint(p);
//        
//        mainPn.onMouseClicked(x, y);
//    }
    
    /**
     * 
     * @return 
     */
    public Dimension getPaddedImageSize()
    {
        return new Dimension(image.getWidth() + 10, image.getHeight() + 10);
    }
    
    public int getImageWidth()
    {
        return image.getWidth();
    }
    
    public int getImageHeight()
    {
        return image.getHeight();
    }
    
    /**
     * 
     * @param x the x-coor
     * @param y the y-coor
     */
//    public void disableCell(int x, int y)
//    {
//        DeadCell c = new DeadCell();
//        deadCells.add(c);
//        mainPn.disableCell(x, y, c);
//        repaint();
//    }

    
    
//    void getShortestPath()
//    {
//        inColorPathMode = true;
//        edges = mainPn.getCurrentFloorPlan().getAnnotFloorPlan().getShortestPath();
//        
//    }
//
    public void setCurrentPos(String string)
    {
        containerPn.updateCurrentPositionLabel(string);
    }

    public void onMouseClicked(int x, int y)
    {
        containerPn.onMouseClicked(x, y);
    }
    
    public File getImageFile()
    {
        return imageFile;
    }
}

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

package helper;

import actionListener.ImagePanelMouseListener;
import entity.Point;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashSet;
import javax.swing.JPanel;

/**
 * @author              Vy Thuy Nguyen
 * @version             1.0 Jan 11, 2013
 * Last modified:       
 */
public class DrawingPanel extends JPanel
{
    ImagePanel imagePanel;
    private int x = 0;
    private int y = 0;
    private HashSet<Point> selectedPoints = new HashSet<Point>();
    
    public DrawingPanel(ImagePanel ip)
    {
        imagePanel = ip;
        this.setSize(ip.getSize());
        this.setPreferredSize(ip.getSize());
        this.setOpaque(false);        
        //System.out.println("imgPn size: " + imagePanel.lb.getSize());
        
        this.setBackground(new Color(0, 0, 0, 0));
        //Mouse listener
        ImagePanelMouseListener mouseListener = new ImagePanelMouseListener();
        this.addMouseListener(mouseListener);
        this.addMouseMotionListener(mouseListener);
    }
    
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        
        g.setColor(Color.red);
        g.drawOval(x, y, 20, 20);
    }

   public void addPoint(int x, int y)
    {
        Point p = new Point(x, y);
        selectedPoints.add(p);
        imagePanel.mainFr.addPoint(p);
        this.repaint();
    }
    
    public void removePoint(Point p)
    {
        selectedPoints.remove(p);
        imagePanel.mainFr.removePoint(p);
    }
    
    public void clearAll()
    {
        selectedPoints.clear();
        imagePanel.mainFr.clearAll();
    }
}

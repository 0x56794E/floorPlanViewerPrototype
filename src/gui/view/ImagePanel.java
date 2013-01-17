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

import util.ImagePanelMouseListener;
import entity.Point;
import entity.PointSet;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * @author              Vy Thuy Nguyen
 * @version             1.0 Jan 9, 2013
 * Last modified:       
 */
public class ImagePanel extends JPanel
{
    private BufferedImage pinImg;
    private BufferedImage image;

    public JLabel lb;
    public String fileName;
    public String absPath;
    ImageIcon icon;
    
    private MainPanel mainPn;
    
    public ImagePanel(File file, MainPanel mp) throws IOException
    {
        image = ImageIO.read(file);
        pinImg = ImageIO.read(new File("resources\\pin.png"));
        
        absPath = file.getPath();
        fileName = file.getName();
        mainPn = mp;
        
        //Mouse listener
        ImagePanelMouseListener mouseListener = new ImagePanelMouseListener();
        this.addMouseListener(mouseListener);
        this.addMouseMotionListener(mouseListener);
        
        icon = new ImageIcon(image);
        this.setPreferredSize(new Dimension(icon.getIconWidth(), icon.getIconHeight()));        
    }

    
    public void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null);     
        g.setColor(Color.RED);
        
        PointSet ps = mainPn.getCurrentPointSet();
        if (ps != null)
        {
            List<Point> points = ps.getPoints();
            System.out.println("points count = " + points.size());
            for (Point p : points)
                g.drawImage(pinImg, p.getX() - 12, p.getY() - 12, null);
        }
    }
    
    /**
     * To be called from click handler 
     * @param x
     * @param y 
     */
    public void addPoint(int x, int y)
    {
        Point p = new Point(x, y);
        mainPn.addPoint(p);        
    }
    
    /**
     * 
     * @return 
     */
    public Dimension getPaddedImageSize()
    {
        return new Dimension(image.getWidth() + 10, image.getHeight() + 10);
    }
    
    public long getImageWidth()
    {
        return image.getWidth();
    }
    
    public long getImageHeight()
    {
        return image.getHeight();
    }
    
    /**
     * 
     * @param x the x-coor
     * @param y the y-coor
     */
    public void disableCell(int x, int y)
    {
        mainPn.disableCell(x, y);
    }
}

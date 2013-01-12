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

import actionListener.ImagePanelMouseListener;
import entity.Point;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import view.MainFrame;

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
    
    //private HashSet<Point> selectedPoints = new HashSet<Point>();
    //private ArrayList<Point> selectedPoints = new ArrayList<Point>();
    public ImagePanel(File file, MainPanel mp)
    {
        try
        {
            image = ImageIO.read(file);
            pinImg = ImageIO.read(new File("pin.png"));
        }
        catch (IOException exc)
        {
          JOptionPane.showMessageDialog(this, "File Not Found", "Error!", JOptionPane.ERROR_MESSAGE);
        }
        
        absPath = file.getPath();
        System.out.println("path = " + absPath);
        fileName = file.getName();
        mainPn = mp;
        
        //Mouse listener
        ImagePanelMouseListener mouseListener = new ImagePanelMouseListener();
        this.addMouseListener(mouseListener);
        this.addMouseMotionListener(mouseListener);
        
        icon = new ImageIcon(image);
        //System.out.println("img size = " + icon.getIconWidth());
        this.setPreferredSize(new Dimension(icon.getIconWidth(), icon.getIconHeight()));
        
    }

    public void exportImage()
    {
        Graphics g = icon.getImage().getGraphics();
        g.setColor(Color.RED);
       
        
        Point p;
        Enumeration<Point> iter = mainPn.getMarkedPoints();
        while (iter.hasMoreElements())
        {
            p = iter.nextElement();
            g.fillOval(p.getX() - 4, p.getY() - 4, 8, 8);
        }
        
        try
        {
            File outputFile = new File("modified-" + fileName);
            ImageIO.write(image, "jpg", outputFile);
            JOptionPane.showMessageDialog(null, "Successfully Exported Image", "Done!", JOptionPane.INFORMATION_MESSAGE);

        }
        catch(IOException exc)
        {
            exc.printStackTrace();
        }
    }
    
    public void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null);     
        g.setColor(Color.RED);
        
        Point p;
        Enumeration<Point> iter = mainPn.getMarkedPoints();
        while (iter.hasMoreElements())
        {
            p = iter.nextElement();
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
    
    /*
    public void removePoint(Point p)
    {
        mainPn.removePoint(p);
    }
    
    public void clearAll()
    {
        mainPn.clearAll();
    }
    */
   
}

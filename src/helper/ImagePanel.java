/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package helper;

import actionListener.ImagePanelMouseListener;
import entity.Point;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import view.MainWindow;

/**
 * @author              Vy Thuy Nguyen
 * @version             1.0 Jan 9, 2013
 * Last modified:       
 */
public class ImagePanel extends JPanel
{
    private BufferedImage pinImg;
    private BufferedImage image;
    MainWindow mainFr;
    public JLabel lb;
    public String fileName;
    public String absPath;
    ImageIcon icon;
    
    //private HashSet<Point> selectedPoints = new HashSet<Point>();
    private ArrayList<Point> selectedPoints = new ArrayList<Point>();
    public ImagePanel(File file, MainWindow mf)
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
        mainFr = mf;
        
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
        for (Point p : selectedPoints)
            g.fillOval(p.getX() - 4, p.getY() - 4, 8, 8);
                   
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
        for (Point p : selectedPoints)
            //g.fillOval(p.getX() - 4, p.getY() - 4, 8, 8);
            g.drawImage(pinImg, p.getX() - 12, p.getY() - 12, null);

    }
    
    public void addPoint(int x, int y)
    {
        Point p = new Point(x, y);
        selectedPoints.add(p);
        mainFr.addPoint(p);
        this.repaint();
    }
    
    public void removePoint(Point p)
    {
        selectedPoints.remove(p);
        mainFr.removePoint(p);
        this.repaint();
    }
    
    public void clearAll()
    {
        selectedPoints.clear();
        mainFr.clearAll();
        this.repaint();
    }
    
   
}

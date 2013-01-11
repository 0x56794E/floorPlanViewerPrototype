/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package helper;

import actionListener.ImagePanelMouseListener;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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
    private BufferedImage image;
    
    public ImagePanel(File file)
    {
        try
        {
            image = ImageIO.read(file);
        }
        catch (IOException exc)
        {
          JOptionPane.showMessageDialog(this, "File Not Found", "Error!", JOptionPane.ERROR_MESSAGE);
        }
        
        //Mouse listener
        ImagePanelMouseListener mouseListener = new ImagePanelMouseListener();
        this.addMouseListener(mouseListener);
        this.addMouseMotionListener(mouseListener);
        
        JLabel lb = new JLabel(new ImageIcon(image));
        add(lb);
    }

    public void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        //g.drawImage(image, 0, 0, null);             
    }
}

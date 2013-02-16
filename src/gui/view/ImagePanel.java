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

import gui.util.ImagePanelContainer;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import util.ImagePanelMouseListener;

/**
 * @author              Vy Thuy Nguyen
 * @version             1.0 Jan 9, 2013
 * Last modified:       
 */
public class ImagePanel extends JPanel
{
    private BufferedImage pinImg;
    private BufferedImage image;
    ImageIcon icon;
    
    private ImagePanelMouseListener mouseListener = new ImagePanelMouseListener();
    private ImagePanelContainer containerPn;
    private File imageFile;
    
    public ImagePanel(File file, ImagePanelContainer container) throws IOException
    {
        image = ImageIO.read(file);
        pinImg = ImageIO.read(new File("resources\\pin.png"));
        imageFile = file;
        containerPn = container;
        
        icon = new ImageIcon(image);
        this.setPreferredSize(new Dimension(icon.getIconWidth(), icon.getIconHeight())); 
        
        //Mouse listener
        this.addMouseListener(mouseListener);
        this.addMouseMotionListener(mouseListener);
    }

    public void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null);    
        
        containerPn.doPaintComponent(g, pinImg);
    }
    
    /**
     * 
     * @return the dimension of the image with 10px padding
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
    
    public void setCurrentPos(String string)
    {
        containerPn.updateCurrentPositionLabel(string);
    }

    public void onMouseClicked(int x, int y)
    {
        containerPn.onMouseClicked(x, y);
    }
    
    public void onRightClicked(int x, int y)
    {
        containerPn.onRightClicked(x, y);
    }
    
    
    public File getImageFile()
    {
        return imageFile;
    }
}

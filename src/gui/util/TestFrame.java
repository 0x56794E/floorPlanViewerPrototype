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

package gui.util;

import entity.Point;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * @author              Vy Thuy Nguyen
 * @version             1.0 Jan 11, 2013
 * Last modified:       
 */
public class TestFrame extends JFrame
{
    TestPane testPn;

    public TestFrame()
    {
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setLayout(new BorderLayout());
      testPn  = new TestPane();
      add(testPn, BorderLayout.CENTER);
      setLocationRelativeTo(null);
      setVisible(true);
      
      
      //Export
      JButton exportBtn = new JButton("Export to file");
      exportBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    ImageIcon i = new ImageIcon(testPn.background);
                    Graphics g = i.getImage().getGraphics();
                    
                    g.setColor(Color.RED);
                    for (Point p : testPn.clickPoints)
                        g.fillOval(p.getX() - 4, p.getY() - 4, 8, 8);
                    
                    File outputFile = new File("marked_floorplan.jpg");
                    ImageIO.write(testPn.background, "jpg", outputFile);
                    
                    JOptionPane.showMessageDialog(null, "Successfully Exported Image", "Done!", JOptionPane.INFORMATION_MESSAGE);

                } catch (IOException exc)
                {
                    
                }
            }
      });
      
      JPanel sub = new JPanel();
      sub.add(exportBtn);
      add(sub, BorderLayout.SOUTH);
      pack();
    }
    
    private class TestPane extends JPanel
    {
        public BufferedImage background;
        public List<Point> clickPoints;
        
        public TestPane()
        {
            clickPoints = new ArrayList<>();
            try
            {
                background = ImageIO.read(new File("C:\\Users\\VyNguyen\\Desktop\\Sample_Floorplan.jpg"));
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e)
                {
                    clickPoints.add(new Point(e.getX(), e.getY()));
                    repaint();
                }            
            });
        }
        
        @Override
        public Dimension getPreferredSize()
        {
            return background == null ? 
                    super.getPreferredSize() : 
                    new Dimension(background.getWidth(), background.getHeight());
        }
        
        @Override
        protected void paintComponent(Graphics g)
        {
            //ImageIcon i = new ImageIcon(background);
            //Graphics origImgGraphics = i.getImage().getGraphics();
            super.paintComponent(g);
            if (background != null)
            {
                int x = (getWidth() - background.getWidth()) / 2;
                int y = (getHeight() - background.getHeight()) / 2;
                g.drawImage(background, x, y, this);
            }
            
            g.setColor(Color.RED);
            for (Point p : clickPoints)
                g.fillOval(p.getX() - 4, p.getY() - 4, 8, 8);
        }
    }
}

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

package util;

import entity.FloorPlan;
import entity.Point;
import entity.PointSet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 * @author              Vy Thuy Nguyen
 * @version             1.0 Jan 15, 2013
 * Last modified:       
 */
public class FileService 
{
    public static void savePointSetToFile(PointSet ps) throws IOException
    {
        FileWriter fstream = new FileWriter(ps.getFloorPlan().getFileName() + "-pointSetID_" + ps.getId() + ".txt");
        BufferedWriter out = new BufferedWriter(fstream);
        
        for (Point p : ps.getPoints())
        {
            out.write(p.toString());
            out.write(";\r\n");
        }

        //Close the output stream
        out.close();
        JOptionPane.showMessageDialog(null, "Sucessfully Saved To File");    
    }
    
    /**
     * Save copies of the floor plan with all of its point sets
     * 
     * @param fp
     * @throws IOException 
     */
    public static void saveFloorPlanToFile(FloorPlan fp) throws IOException
    {
        for (PointSet ps : fp.getPointSets())
            saveFloorPlanToFile(fp, ps);
    }
    
    /**
     * Save a copy of the floor plan with all the points in ps on it.
     * 
     * @param fp
     * @param ps
     * @throws IOException 
     */
    public static void saveFloorPlanToFile(FloorPlan fp, PointSet ps) throws IOException
    {
        File file = new File(fp.getAbsoluteFilePath());
        BufferedImage image = ImageIO.read(file);
        Graphics g = new ImageIcon(image).getImage().getGraphics();
        g.setColor(Color.RED);
        
        List<Point> points = ps.getPoints();
        
        for (Point p : points)
        {
            g.fillOval(p.getX() - 4, p.getY() - 4, 8, 8);
        }
        
        File oFile = new File("output\\" + fp.getFileName() + "-PointSetID_" + ps.getId() + ".png");
        ImageIO.write(image, "png", oFile);
        //System.err.printf("User's dir %s", System.getProperty("user.dir"));
        JOptionPane.showMessageDialog(null, 
                                        "Successfully Exported Image to the Following File:\n"
                                        + oFile.getAbsolutePath(), 
                                        "Done!", 
                                        JOptionPane.INFORMATION_MESSAGE);
    }
}

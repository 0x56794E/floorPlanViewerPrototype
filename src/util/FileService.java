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

import entity.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Set;
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
    public static void saveAllPointSetsToFile(FloorPlan fp) throws IOException
    {
        for (PointSet ps : fp.getPointSets())
            savePointSetToFile(ps);
    }
    
    public static void savePointSetToFile(PointSet ps) throws IOException
    {
        FileWriter fstream = new FileWriter(ps.getFloorPlan().getFileName() + "-pointSetID_" + ps.getId() + ".txt");
        BufferedWriter out = new BufferedWriter(fstream);
        
        for (Point p : ps.getPoints())
        {
            out.write(p.toString());
            out.write("\r\n");
        }

        //Close the output stream
        out.close();
    }
    
    /**
     * Save copies of the floor plan with all of its point sets
     * 
     * @param fp
     * @throws IOException 
     */
    public static void exporteFloorPlanImage(FloorPlan fp) throws IOException
    {
        for (PointSet ps : fp.getPointSets())
            exportFloorPlanImage(fp, ps);
    }
    
    /**
     * Save a copy of the floor plan with all the points in ps on it.
     * 
     * @param fp
     * @param ps
     * @throws IOException 
     */
    public static void exportFloorPlanImage(FloorPlan fp, PointSet ps) throws IOException
    {
        File file = new File(fp.getAbsoluteFilePath());
        BufferedImage image = ImageIO.read(file);
        Graphics g = new ImageIcon(image).getImage().getGraphics();
        g.setColor(Color.RED);
        
        List<Point> points = ps.getPoints();
        
        for (Point p : points)
            g.fillOval(p.getX() - 4, p.getY() - 4, 8, 8);
        
        File oFile = new File("output\\" + fp.getFileName() + "-PointSetID_" + ps.getId() + ".png");
        ImageIO.write(image, "png", oFile);
        JOptionPane.showMessageDialog(null, 
                                        "Successfully Exported Image to the Following File:\n"
                                        + oFile.getAbsolutePath(), 
                                        "Done!", 
                                        JOptionPane.INFORMATION_MESSAGE);
    }
    
    
    public static void exportFloorPlanWithDeadCells(FloorPlan fp) throws IOException
    {
        Set<Cell> deadCells = fp.getAnnotFloorPlan().getDeadCells();
        int halfUnitW = fp.getAnnotFloorPlan().getUnitW();// / 2;
        int halfUnitH = fp.getAnnotFloorPlan().getUnitH();// / 2;
         
        File file = new File(fp.getAbsoluteFilePath());
        BufferedImage image = ImageIO.read(file);
        Graphics g = new ImageIcon(image).getImage().getGraphics();
        g.setColor(Color.gray);
        
        for (Cell dc : deadCells)
            g.fillRect(dc.getMinX(), dc.getMinY(), halfUnitW, halfUnitH);
       
        
        File oFile = new File("annotatedFloorPlan_" + fp.getId() + ".png");
        ImageIO.write(image, "png", oFile);
    }
    
    public static void saveDeadCellsToFile(FloorPlan fp) throws IOException
    {
        
        FileWriter fstream = new FileWriter(fp.getFileName() + "_" + fp.getId() + "_deadCells.txt");
        BufferedWriter out = new BufferedWriter(fstream);
        
        for (Cell cell : fp.getAnnotFloorPlan().getDeadCells())
        {
            out.write(cell.toString());
            out.write("\r\n");
        }

        //Close the output stream
        out.close();
        
        
    }

    public static void saveDeadCellsToFile(Set<Cell> deadCells) throws IOException
    {
        Cell f = deadCells.iterator().next();
        
        FileWriter fstream = new FileWriter(f.getAnnotFloorPlan().getFloorPlan().getFileName() 
                        + "_" + f.getAnnotFloorPlan().getFloorPlan().getId() 
                        + "_deadCells.txt");
        BufferedWriter out = new BufferedWriter(fstream);
        
        for (Cell cell : deadCells)
        {
            out.write(cell.toString());
            out.write("\r\n");
        }

        //Close the output stream
        out.close();
    }
    
    public static void saveGraph(AnnotFloorPlan annotFp) throws IOException
    {
        FileWriter fstream = new FileWriter(annotFp.getFloorPlan().getFileName() 
                        + "_" + annotFp.getFloorPlan().getId() 
                        + "_availCell.txt");
        BufferedWriter out = new BufferedWriter(fstream);
        Cell[][] cellContainer = annotFp.getCellContainer();
        int rowCount = annotFp.getRowCount();
        int colCount = annotFp.getColCount();
        
        for (int row = 0; row < rowCount; ++row)
            for (int col = 0; col < colCount; ++col)
            {
                if (!cellContainer[row][col].isDead())
                {
                    out.write(cellContainer[row][col].getCol() + " " + cellContainer[row][col].getRow());
                    out.write("\r\n");
                }
            }

        //Close the output stream
        out.close();
    }
}

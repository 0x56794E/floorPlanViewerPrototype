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
import java.io.*;
import java.util.Map.Entry;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import org.jgrapht.graph.SimpleWeightedGraph;
import partitioner.Class;
import partitioner.SubRegion;
import partitioner.VirtualLine;

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
        List<Cell> deadCells = fp.getAnnotFloorPlan().getDeadCells();
        int unitW = fp.getAnnotFloorPlan().getUnitW();
        int unitH = fp.getAnnotFloorPlan().getUnitH();     
        File file = new File(fp.getAbsoluteFilePath());
        BufferedImage image = ImageIO.read(file);
        Graphics g = new ImageIcon(image).getImage().getGraphics();
        g.setColor(Color.gray);
        
        for (Cell dc : deadCells)
            g.fillRect(dc.getCol() * unitW, dc.getRow() * unitH, unitW, unitH);
       
        
        File oFile = new File("annotatedFloorPlan_" + fp.getId() + ".png");
        ImageIO.write(image, "png", oFile);
    }
    
    public static void exportFloorPlanWithSpectralPartitioning(FloorPlan fp, int k, ArrayList<VirtualLine> lines, double zoomedIndex) throws IOException
    {
        List<Cell> deadCells = fp.getAnnotFloorPlan().getDeadCells();
        int unitW = fp.getAnnotFloorPlan().getUnitW();
        int unitH = fp.getAnnotFloorPlan().getUnitH();
        
        File file = new File(fp.getAbsoluteFilePath());
        BufferedImage image = ImageIO.read(file);
        Graphics g = new ImageIcon(image).getImage().getGraphics();
        
        //Draw dead cells
        g.setColor(Color.DARK_GRAY);
        for (Cell c : deadCells)
            g.fillRect(c.getCol() * unitW, c.getRow() * unitH, unitW, unitH);
        
        //Draw partitioning
        for (VirtualLine vLine : lines)
        {
            //N- section
            for (Cell c : vLine.getNMinus())
            {
                g.setColor(c.getColor(zoomedIndex));
                g.fillRect(c.getCol() * unitW, c.getRow() * unitH, unitW, unitH);
            }
            
            //N+ section
            for (Cell c : vLine.getNPlus())
            {
                g.setColor(c.getColor(zoomedIndex));
                g.fillRect(c.getCol() * unitW, c.getRow() * unitH, unitW, unitH);
            }
        }     
        
        File oFile = new File("spectral_" + fp.getId() + "k_" + k + ".png");
        ImageIO.write(image, "png", oFile);
    }
    
    
    /**
     * Produce file containing the column position, 
     * the row position and the binary string corresponding to a given point.
     * Each line has the following format
     * <row> <col> <binString>
     * 
     * @param afp
     * @param k
     * @param partitionType 
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public static void savePointsWithBinaryStrings(AnnotFloorPlan afp, int k, String partitionType) throws FileNotFoundException, IOException
    {
        //Read coordinates from file and convert pixel coordinate and to same origin      
        FileWriter fstream = new FileWriter(afp.getFloorPlan().getFileName() 
                + "_k" + k + "_" + partitionType + ".txt");
        BufferedWriter out = new BufferedWriter(fstream);
        File inFile = new File(afp.getFloorPlan().getFileName() + ".txt");
        Scanner sc = new Scanner(inFile);
        String line = "";
        String[] tokens;
        
        ArrayList<Cell> added = new ArrayList<Cell>();
        
        while (sc.hasNextLine())
        {
            line = sc.nextLine();
            if (line.charAt(0) == '#')
            {  
                line = line.substring(3);
                tokens = line.split("\\,");
                
                //Convert to px coor
                Cell cell = afp.getNode(afp.getActualW() - Double.parseDouble(tokens[1]), 
                                        afp.getActualH() - Double.parseDouble(tokens[0]));
                
                if (cell != null)
                {
                    if (!added.contains(cell))
                    {
                        added.add(cell);
                        out.write(String.format("%d %d %s\r\n",
                                    cell.getRow(),
                                    cell.getCol(),
                                    cell.getBinaryString()));   
                    }
                }
                else
                {
                    out.write(String.format("ERROR: Position (%s, %s) doesn't exist in graph\r\n",
                                            tokens[0],
                                            tokens[1]));
                }
            }
        }
        out.close();
    }

    
    /**
     * This file is for debugging purpose only
     * @param fp
     * @throws IOException 
     */
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

    /**
     * This file is for debugging purpose only
     * @param deadCells
     * @throws IOException 
     */
    public static void saveDeadCellsToFile(List<Cell> deadCells) throws IOException
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
    
    /**
     * This file is for debugging purpose only
     * @param annotFp
     * @throws IOException 
     */
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
    
    /**
     * This file is for debugging purpose only
     * @param annotFp
     * @throws IOException 
     */
    public static void saveLargestConnectedComponent(AnnotFloorPlan annotFp) throws IOException
    {
        SimpleWeightedGraph<Cell, WeightedEdge> g = annotFp.getGraph();
        System.out.printf("row = %d, col = %d", annotFp.getRowCount(), annotFp.getColCount());
        ArrayList<List<Cell>> components = new ArrayList<>();
        boolean visited[][] = new boolean[annotFp.getRowCount()][annotFp.getColCount()];
        for (int row = 0; row < annotFp.getRowCount(); ++row)
            for (int col = 0; col < annotFp.getColCount(); ++col)
                visited[row][col] = false;
        
        Queue<Cell> q; 
        Cell t = null, u = null;
        for (Cell c : g.vertexSet())
        {
            if (c.isDead())
            {
                visited[c.getRow()][c.getCol()] = true;
                continue;
            }
            
            if (!visited[c.getRow()][c.getCol()])
            {
                q = new LinkedList<Cell>();
                ArrayList<Cell> component = new ArrayList<>();
                visited[c.getRow()][c.getCol()] = true;
                
                //Find all connected nodes
                q.add(c);
                component.add(c);
                while (!q.isEmpty())
                {
                    t = q.remove();
                    System.out.println("\nedge count = " + g.edgesOf(t).size() + "for " + t);
                    for (WeightedEdge e : g.edgesOf(t))
                    {
                        System.out.println("\tsource of e: " + g.getEdgeSource(e));
                        System.out.println("\ttarget of e: " + g.getEdgeTarget(e));
                        u = t.equals(g.getEdgeSource(e)) ?
                                g.getEdgeTarget(e) :
                                g.getEdgeSource(e);
                        if (!visited[u.getRow()][u.getCol()])
                        {
                            visited[u.getRow()][u.getCol()] = true;
                            q.add(u);
                            component.add(u);
                            System.out.println("marking " + u);
                        }
                    }
                }
                
                System.out.println("this compenent has " + component.size() + " cells");
                components.add(component);
            } //end if not visited
        }
        
        System.out.println("connected component count = " + components.size());
        int largestSize = 0, largestIndex = 0;
        for (int i = 0; i < components.size(); ++i)
        {
            if (components.get(i).size() > largestSize)
            {
                largestSize = components.get(i).size();
                largestIndex = i;
            }
        }
        
        //Print to fiile
        FileWriter fstream = new FileWriter(annotFp.getFloorPlan().getFileName() 
                        + "_" + annotFp.getFloorPlan().getId() 
                        + "_availCell.txt");
        BufferedWriter out = new BufferedWriter(fstream);
        for (Cell cell : components.get(largestIndex))
        {
            out.write(cell.getCol() + " " + cell.getRow());
            out.write("\r\n");
        }
        
        out.close();
    }

    /**
     * Output the list of lines and the sub-regions associated with them.
     * @param lines
     * @throws IOException 
     */
    public static void saveLines(ArrayList<VirtualLine> lines) throws IOException
    {
        FileWriter fstream = new FileWriter("spectral_lines_k" + lines.size() + ".txt");
        BufferedWriter out = new BufferedWriter(fstream);
        int c = 0;
        for (VirtualLine line : lines)
        {
            out.write("###\r\n");
            out.write(c + "\r\n"); //Line number
            out.write(line.getNMinusRegion().toString() + "\r\n"); //N- Region of this line
            out.write(line.getNPlusRegion().toString() + "\r\n"); //N+ Region of this line
            c++;
        }
        
        out.close();
    }

    /**
     * 
     * @param annotFloorPlan
     * @param sub
     * @param zoomIndex
     * @param regionOrder
     * @throws IOException 
     */
    public static void exportImageOfRegion(AnnotFloorPlan annotFloorPlan, 
                                            SubRegion sub, 
                                            double zoomIndex,
                                            int regionOrder) throws IOException
    {
        List<Cell> deadCells = annotFloorPlan.getDeadCells();
        int unitW = annotFloorPlan.getUnitW();
        int unitH = annotFloorPlan.getUnitH();
        
        File file = new File(annotFloorPlan.getFloorPlan().getAbsoluteFilePath());
        BufferedImage image = ImageIO.read(file);
        Graphics g = new ImageIcon(image).getImage().getGraphics();
        
        //Draw dead cells
        g.setColor(Color.DARK_GRAY);
        for (Cell c : deadCells)
            g.fillRect(c.getCol() * unitW, c.getRow() * unitH, unitW, unitH);
        
        //Draw partitioning
        for (Cell c : sub.getGraph().vertexSet())   
        {
                g.setColor(c.getColor(zoomIndex));
                g.fillRect(c.getCol() * unitW, c.getRow() * unitH, unitW, unitH);
        }
        
        File oFile = new File(annotFloorPlan.getFloorPlan().getId() 
                              //+ "_Region_" + regionOrder 
                                + "_G_"
                                + "_str_" + sub.getBinaryString() + ".png");
        ImageIO.write(image, "png", oFile);
    }

    /**
     * 
     * @param annotFloorPlan
     * @param clss
     * @param zoomIndex
     * @param binStr
     * @throws IOException 
     */
    public static void exportImageOfClass(AnnotFloorPlan annotFloorPlan, Class clss, double zoomIndex, String binStr) throws IOException
    {
        List<Cell> deadCells = annotFloorPlan.getDeadCells();
        int unitW = annotFloorPlan.getUnitW();
        int unitH = annotFloorPlan.getUnitH();
        
        File file = new File(annotFloorPlan.getFloorPlan().getAbsoluteFilePath());
        BufferedImage image = ImageIO.read(file);
        Graphics g = new ImageIcon(image).getImage().getGraphics();
        
        //Draw dead cells
        g.setColor(Color.DARK_GRAY);
        for (Cell c : deadCells)
            g.fillRect(c.getCol() * unitW, c.getRow() * unitH, unitW, unitH);
        
        //Draw class
        System.out.println("Class has " + clss.getCells().size());
        for (Cell c : clss.getCells())
        {
            g.setColor(c.getColor(zoomIndex));
            g.fillRect(c.getCol() * unitW, c.getRow() * unitH, unitW, unitH);
        }
        
        File oFile = new File(annotFloorPlan.getFloorPlan().getId() 
                              + "_Class_" + binStr + ".png");
        ImageIO.write(image, "png", oFile);
    }
    
    /**
     * 
     * @param afp
     * @param classes
     * @throws IOException 
     */
    public static void exportTrainingFiles(AnnotFloorPlan afp, HashMap<String, Class> classes) throws IOException
    {
         //Read coordinates from file and convert pixel coordinate and to same origin
      
        //Build input array
        ArrayList<Input> inputs = new ArrayList<Input>();
        //File inFile = new File("train_p0.5.txt_sub_0.02.1.txt"); //Temp  
        File inFile = new File("test_p0.5.txt"); //Temp  
        
        Scanner sc = new Scanner(inFile);
        //Each line has this format: 2.43892672440343,19.5114137952274#1:-50 2:-73 3:-63 4:-57 5:-95 
        String line="";
        String[] tokens;
        String[] part1;
        String[] part2;
        
        while (sc.hasNextLine())
        {
            line = sc.nextLine();
            tokens = line.split("#");
            
            //coordinates 
            part1 = tokens[0].split(",");
            System.out.println("toks[0] = " + tokens[0]);
            //Find corresponding cell, add to input array
            inputs.add(new Input(afp.getNode(afp.getActualW() - Double.parseDouble(part1[1]), 
                                             afp.getActualH() - Double.parseDouble(part1[0])),
                                 tokens[1]));
        }
        
        
        //Go through each classes
        Set<Entry<String, Class>> entries = classes.entrySet();
        for (Entry<String, Class> entry : entries)
        {
            FileWriter fstream = new FileWriter("spectral_" + entry.getKey() + ".txt");
            Class clss = entry.getValue();
            BufferedWriter out = new BufferedWriter(fstream);
            for (Input input : inputs)
            {
                if (clss.containsPoint(input.cell))
                    out.write("1 ");
                else 
                    out.write("-1 ");
                
                out.write(input.readings);
                out.write("\n");                        
            }
            out.close();
        }
        
    }
    
    private static class Input
    {
        String readings;
        Cell cell;
         
        public Input(Cell cell, String readings)
        {
            this.cell = cell;
            this.readings = readings;
        }
    }
}

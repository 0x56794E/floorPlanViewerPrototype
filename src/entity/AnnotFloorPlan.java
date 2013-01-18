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

package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.persistence.*;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import util.DeadCell;

/**
 * @author              Vy Thuy Nguyen
 * @version             1.0 Jan 16, 2013
 * Last modified:       
 */
@Entity
public class AnnotFloorPlan implements Serializable 
{
    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private long id;
    
    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private FloorPlan floorPlan;
    
    //Each cell accounts for 1% of the width and the height
    private final int ratio = 2; //percent in respect to actual length
    int unitW;
    int unitH;
    int rowCount;
    int colCount;
    
    private SimpleGraph g;
    private Cell[][] cellContainer;
    
    
    public int getUnitW()
    {
        return unitW;
    }
    
    public int getUnitH()
    {
        return unitH;
    }
    
    public SimpleGraph getGraph()
    {
        return g;
    }
    
    @SuppressWarnings("unchecked")
    public AnnotFloorPlan()
    {
        floorPlan = new FloorPlan();
        
        int width = 1200;
        int height = 1000;
        unitW = width * ratio / 100;
        unitH = height * ratio / 100;
        rowCount = height / unitH + 1;
        colCount = width / unitW + 1;
         cellContainer = new Cell[rowCount][colCount];
         
        //Init the graph
        g = new SimpleGraph(DefaultEdge.class);
        generateVertices();
        generateEdges();
    }
    
    public AnnotFloorPlan(FloorPlan fp)
    {
        floorPlan = fp;        
        
        unitW = fp.getWidth() * ratio / 100;
        unitH = fp.getHeight() * ratio / 100;
        rowCount = fp.getHeight() / unitH + 1;
        colCount = fp.getWidth() / unitW + 1;
        cellContainer = new Cell[rowCount][colCount];
        
        //init the graph
        g = new SimpleGraph(DefaultEdge.class);
        generateVertices();
        generateEdges();
    }
    
    public List<DefaultEdge> getShortestPath(int x1, int y1, int x2, int y2)
    {
        //int x1 = 158, y1 = 109, x2 = 108, y2 = 489;
        int row1 = y1 / unitH, col1 = x1 / unitW, row2 = y2 / unitH, col2 = x2 / unitW;
        
        System.out.printf("finding shortest pathh from (%d, %d) to (%d, %d) (of cell[%d, %d] to cell[%d, %d]\n", x1, y1, x2, y2, row1, col1, row2, col2);
             
        
        DijkstraShortestPath d = new DijkstraShortestPath(g, cellContainer[row1][col1], cellContainer[row2][col2]);
        return d.getPathEdgeList();
        
        
    }
    
    private void generateVertices()
    {
        for (int row = 0; row < rowCount; ++row)
            for (int col = 0; col < colCount; ++col)
            {
                cellContainer[row][col] = new Cell(row, col);
                g.addVertex(cellContainer[row][col]);
            }
    }
    
    private void generateEdges()
    {
        for (int row = 0; row < rowCount; ++row)
            for (int col = 0; col < colCount; ++col)
            {
                System.out.println("generating edges for node at [" + row + ", " + col+ "]...");
                //North
                if (row > 0)
                {
                    g.addEdge(cellContainer[row][col], cellContainer[row - 1][col]);
                    
                    //NE
                    if (col < colCount - 2)
                        g.addEdge(cellContainer[row][col], cellContainer[row - 1][col + 1]);
                }
                
                //East
                if (col < colCount - 2)
                {
                    g.addEdge(cellContainer[row][col], cellContainer[row][col + 1]);
                    
                    //SE
                    if (row < rowCount - 2)
                        g.addEdge(cellContainer[row][col], cellContainer[row + 1][col + 1]);
                }
                
                //South
                if (row < rowCount - 2)
                {
                    g.addEdge(cellContainer[row][col], cellContainer[row + 1][col]);
                    
                    //SW
                    if (col > 0)
                        g.addEdge(cellContainer[row][col], cellContainer[row + 1][col - 1]);
                }
                
                //West
                if (col > 0)
                {
                    g.addEdge(cellContainer[row][col], cellContainer[row][col - 1]);
                    
                    //NW
                    if (row > 0)
                        g.addEdge(cellContainer[row][col], cellContainer[row - 1][col - 1]);
                }
            }
    }
    

    private List<Cell> getNeighbors(int row, int col)
    {
        List<Cell> neighbors = new ArrayList<Cell>();
        
        //North
        try
        {
            neighbors.add(cellContainer[row - 1][col]);
        }
        catch(Exception e)
        {
            System.out.println("No north neighbor");
        }
        
        //Northeast
        try
        {
            neighbors.add(cellContainer[row - 1][col + 1]);
        }
        catch(Exception e)
        {
            System.out.println("No northeast neighbor");
        }
        
        //East
        try
        {
            neighbors.add(cellContainer[row][col + 1]);
        }
        catch(Exception e)
        {
            System.out.println("No east neighbor");
        }
        
        //SE
        try
        {
            neighbors.add(cellContainer[row + 1][col + 1]);
        }
        catch(Exception e)
        {
            System.out.println("No southeast neighbor");
        }
        
        //South
        try
        {
            neighbors.add(cellContainer[row + 1][col]);
        }
        catch(Exception e)
        {
            System.out.println("No south neighbor");
        }
        
        
        //SW
        try
        {
            neighbors.add(cellContainer[row + 1][col - 1]);
        }
        catch(Exception e)
        {
            System.out.println("No sw neighbor");
        }
        
        //West
        try
        {
            neighbors.add(cellContainer[row][col - 1]);
        }
        catch(Exception e)
        {
            System.out.println("No west neighbor");
        }
        
        //Northweast
        try
        {
            neighbors.add(cellContainer[row - 1][col - 1]);
        }
        catch(Exception e)
        {
            System.out.println("No north west neighbor");
        }
        
        return neighbors;
        
    }
    
    /**
     * 
     * @param x the x-coor
     * @param y the y-coor
     * @param dc 
     */
    public void disableCell(int x, int y, DeadCell dc)
    {
        int minX = x / unitW;
        int minY = y / unitH;
        Cell c = cellContainer[minY][minX];
        c.disableCell();
        
        //removing all edges touching this cell
        List<Cell> neighbors = getNeighbors(minY, minX);
        for (Cell n : neighbors)
            g.removeAllEdges(c, n);
        
        //Color all pixels within this cell
        dc.setMinX(minX * unitW);
        dc.setMinY(minY * unitH);
        dc.setWidth(unitW);
        dc.setHeight(unitH);
    }
    
    public void enabbleCell(int x, int y, DeadCell dc)
    {
        int minX = x / unitW;
        int minY = y / unitH;
        Cell c = cellContainer[minY][minX];
        c.enabbleCell();
        
        //add edges to all cells touching this cell
        List<Cell> neighbors = getNeighbors(minY, minX);
        for (Cell n : neighbors)
        {
            g.addEdge(c, n);
        }
        
        //Set minX and minY all pixels within this cell
        dc.setMinX(minX * unitW);
        dc.setMinY(minY * unitH);
    }
    
    
}

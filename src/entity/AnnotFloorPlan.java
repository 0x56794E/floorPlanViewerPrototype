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
import java.util.HashSet;
import javax.persistence.*;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

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
    private final int ratio = 1; //percent in respect to actual length
    int unitW;
    int unitH;
    
    private SimpleGraph g;
    private Cell[][] cellContainer = new Cell[101][101];
    
    @SuppressWarnings("unchecked")
    public AnnotFloorPlan()
    {
        floorPlan = new FloorPlan();
        
        //cellContainer.iterator().
        
        //Init the graph
        DefaultEdge e = new DefaultEdge();
        
        int width = 1200;
        int height = 1000;
        unitW = width * ratio / 100;
        unitH = height * ratio / 100;
        int dUnitW = 2 * unitW;
        int dUnitH = 2 * unitH;
        
        g = new SimpleGraph(e.getClass());
       
        for (int x = 0; x <= width; x += dUnitW)
            
            for (int y = 0; y <= height; y += dUnitH)
            {
                //Create a node
                Cell c = new Cell(x, y);
                g.addVertex(c);
                
                //Add vertices
                //North
                if (y > 0)
                {
                    Cell northC = new Cell(x, y - unitH);
                    g.addVertex(northC);
                    g.addEdge(c, northC);
                
                
                    //NE
                    if (x < width)
                    {
                        Cell neC = new Cell(x + unitW, y - unitH);
                        g.addVertex(neC);
                        g.addEdge(c, neC);
                    }
                }
                
                //East
                if (x < width)
                {
                    Cell eastC = new Cell(x + unitH, y);
                    g.addVertex(eastC);
                    g.addEdge(c, eastC);
                    
                    //SE
                    if (y < height)
                    {
                        Cell seC = new Cell(x + unitH, y + unitH);
                        g.addVertex(seC);
                        g.addEdge(c, seC);
                    }
                }
                
                //South
                if (y < height)
                {
                    Cell southC = new Cell(x, y + unitH);
                    g.addVertex(southC);
                    g.addEdge(c, southC);
                    
                    //SW
                    if (x > 0)
                    {
                        Cell swC = new Cell(x - unitW, y + unitH);
                        g.addVertex(swC);
                        g.addEdge(c, swC);
                    }
                }
                
            
                //West
                if (x > 0)
                {
                    Cell westC = new Cell(x - unitW, y);
                    g.addVertex(westC);
                    g.addEdge(c, westC);
                    
                    //NW
                    if (y > 0)
                    {
                        Cell nwC = new Cell(x - unitW, y - unitH);
                        g.addVertex(nwC);
                        g.addEdge(c, nwC);
                    }
                }                
            }
    }
    
    
    
    private void generateVertices()
    {
        for (int row = 0; row <= 101; ++row)
            for (int col = 0; col <= 101; ++col)
            {
                cellContainer[row][col] = new Cell(col, row);
                g.addVertex(cellContainer[row][col]);
            }
    }
    
    private void generateEdges()
    {
        
    }
    
    public AnnotFloorPlan(FloorPlan fp)
    {
        floorPlan = fp;        
    }
    
    /**
     * 
     * @param x the x-coor
     * @param y the y-coor
     */
    public void disableCell(int x, int y)
    {
        Cell c = cellContainer[x / unitW][y / unitH];
        c.disableCell();
        
        //removing all edges touching this cell
        g.removeAllEdges(g.edgesOf(c));
    }
}

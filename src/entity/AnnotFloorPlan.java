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
import java.util.*;
import javax.persistence.*;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.SimpleWeightedGraph;
import util.DatabaseService;

/**
 * Bug: when open an old file, new cells are created instead, if a cell is marked dead, 
 * a this one will be persisted as new cell. Similarly, if a cell is enabled,
 * it may visually appear that the cell is not dead, but in fact, the dead cell
 * is not removed.
 * 
 * @author              Vy Thuy Nguyen
 * @version             1.0 Jan 16, 2013
 * Last modified:       
 */
@Entity
public class AnnotFloorPlan implements Serializable 
{
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private long id;
    
    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private FloorPlan floorPlan;
    
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.PERSIST}, 
                          fetch = FetchType.EAGER, mappedBy = "annotFloorPlan")
    private Set<Cell> deadCells;
    
    
    //Each cell accounts for 1% of the width and the height
    private final int RATIO = 2; //percent in respect to actual length
    int unitW;
    int unitH;
    int rowCount;
    int colCount;
    
    @Transient
    private boolean graphInitialized = false;
    
    @Transient
    private SimpleWeightedGraph<Cell, WeightedEdge> g;
    
    @Transient
    private Cell[][] cellContainer;
        
    @Transient
    private EntityManager em;
    
    @SuppressWarnings({"unchecked", "unchecked"})
    public AnnotFloorPlan()
    {
        em = DatabaseService.getEntityManager();
        floorPlan = new FloorPlan();
        deadCells = new HashSet<Cell>();
        g = new SimpleWeightedGraph<Cell, WeightedEdge>(WeightedEdge.class);
    }
    
    public AnnotFloorPlan(FloorPlan fp)
    {
        em = DatabaseService.getEntityManager();
        floorPlan = fp;        
        deadCells = new HashSet<Cell>();
        
        unitW = fp.getWidth() * RATIO / 100;
        unitH = fp.getHeight() * RATIO / 100;
        rowCount = fp.getHeight() / unitH + 1;
        colCount = fp.getWidth() / unitW + 1;
        cellContainer = new Cell[rowCount][colCount];
        
        System.out.println("In constructor; row = " + rowCount + "; col = " + colCount);
        //init the graph
        g = new SimpleWeightedGraph<Cell, WeightedEdge>(WeightedEdge.class);
        
        initGraph();
        //generateVertices();
        //generateEdges();
    }
     
    public FloorPlan getFloorPlan()
    {
        return this.floorPlan;
    }
    
    public int getUnitW()
    {
        return unitW;
    }
    
    public int getUnitH()
    {
        return unitH;
    }
    
    public SimpleWeightedGraph<Cell, WeightedEdge> getGraph()
    {
        return g;
    }
    
    public boolean needInitGraph()
    {
        return !graphInitialized;
    }
    
    public void initGraph()
    {
        if (!graphInitialized)
        {
            generateVertices();
            generateEdges();
            graphInitialized = true;
        }
    }
    
    /**
     * 
     * @param x1 the actual "from" x coordinate (in respect to the "dense grid")
     * @param y1 the actual "from" y coordinate (ditto)
     * @param x2 the actual "to" x coordinate (ditto)
     * @param y2 the actual "to" y coordinate (ditto)
     * @return the list of edges representing the shortest path from P1 to P2
     */
    public List<WeightedEdge> getShortestPath(int x1, int y1, int x2, int y2)
    {
        //int x1 = 158, y1 = 109, x2 = 108, y2 = 489;
        int row1 = y1 / unitH, col1 = x1 / unitW, row2 = y2 / unitH, col2 = x2 / unitW;
        DijkstraShortestPath d = new DijkstraShortestPath(g, cellContainer[row1][col1], cellContainer[row2][col2]);
        return d.getPathEdgeList();
    }
    
    /**
     * 
     */
    private void generateVertices()
    {
        for (int row = 0; row < rowCount; ++row)
            for (int col = 0; col < colCount; ++col)
            {
                
                cellContainer[row][col] = new Cell(row, col, this);
                g.addVertex(cellContainer[row][col]);
            }
    }
    
    private void generateEdges()
    {
        for (int row = 0; row < rowCount; ++row)
            for (int col = 0; col < colCount; ++col)
            {
                addEdges(row, col);
            }
    }
     
    private void addVertexAt(int row, int col)
    {
        g.addVertex(cellContainer[row][col]);
        addEdges(row, col);
    }
    
    /**
     * Add edges connecting this cell and its 8 (or less) neighbors
     * 
     * @param row
     * @param col 
     */
    private void addEdges(int row, int col)
    {
        System.out.println("Adding (" + row + ", " + col + ")");
        //Add weighted edge
        //North
        if (row > 0 && g.containsVertex(cellContainer[row][col]))
        {
            g.addEdge(cellContainer[row][col], cellContainer[row - 1][col]);
            g.setEdgeWeight(g.getEdge(cellContainer[row][col],
                                      cellContainer[row - 1][col]),
                            1);
            //NE
            if (col < colCount - 1 && g.containsVertex(cellContainer[row - 1][col + 1]))
            {
                g.addEdge(cellContainer[row][col], cellContainer[row - 1][col + 1]);
                g.setEdgeWeight(g.getEdge(cellContainer[row][col],
                                          cellContainer[row - 1][col + 1]),
                                Math.sqrt(2.0));
            }
        }

        //East
        if (col < colCount - 1 && g.containsVertex(cellContainer[row][col + 1]))
        {
            g.addEdge(cellContainer[row][col], cellContainer[row][col + 1]);
            g.setEdgeWeight(g.getEdge(cellContainer[row][col],
                                      cellContainer[row][col + 1]),
                            1);


            //SE
            if (row < rowCount - 1 && g.containsVertex(cellContainer[row + 1][col + 1]))
            {
                g.addEdge(cellContainer[row][col], cellContainer[row + 1][col + 1]);
                g.setEdgeWeight(g.getEdge(cellContainer[row][col],
                                          cellContainer[row + 1][col + 1]),
                                Math.sqrt(2.0));

            }
        }

        //South
        if (row < rowCount - 1 && g.containsVertex(cellContainer[row + 1][col]))
        {
            g.addEdge(cellContainer[row][col], cellContainer[row + 1][col]);
            g.setEdgeWeight(g.getEdge(cellContainer[row][col],
                                      cellContainer[row + 1][col]),
                            1);

            //SW
            if (col > 0 && g.containsVertex(cellContainer[row + 1][col - 1]))
            {
                g.addEdge(cellContainer[row][col], cellContainer[row + 1][col - 1]);
                g.setEdgeWeight(g.getEdge(cellContainer[row][col],
                                          cellContainer[row + 1][col - 1]),
                                Math.sqrt(2.0));
            }
        }

        //West
        if (col > 0 && g.containsVertex(cellContainer[row][col - 1]))
        {
            g.addEdge(cellContainer[row][col], cellContainer[row][col - 1]);
            g.setEdgeWeight(g.getEdge(cellContainer[row][col], cellContainer[row][col - 1]),
                            1);

            //NW
            if (row > 0 && g.containsVertex(cellContainer[row - 1][col - 1]))
            {
                g.addEdge(cellContainer[row][col], cellContainer[row - 1][col - 1]);
                g.setEdgeWeight(g.getEdge(cellContainer[row][col],
                                          cellContainer[row - 1][col - 1]),
                                Math.sqrt(2.0));
            }
        }
    }
    
    /**
     * 
     * @param x the x-coor
     * @param y the y-coor
     * @param dc 
     */
    public void disableCell(int x, int y)
    {
        int col = x / unitW;
        int row = y / unitH;
        if (valid(row, col))
        {
            cellContainer[row][col].disableCell();
            deadCells.add(cellContainer[row][col]);

            //removing this cell from the graph and all of its touching edges
            g.removeVertex(cellContainer[row][col]);
        }
    }
    
    public void enabbleCell(int x, int y)
    {
        int col = x / unitW;
        int row = y / unitH;
        if (valid(row, col))
        {
            cellContainer[row][col].enabbleCell();
            deadCells.remove(cellContainer[row][col]);
            em.getTransaction().begin();
            em.remove(cellContainer[row][col]);
            em.getTransaction().commit();
            //adding this cell to the graph and connecting it with its neighbors
            addVertexAt(row, col);
        }
    }

    public Set<Cell> getDeadCells()
    {
        return Collections.unmodifiableSet(deadCells); 
    }
    
    /**
     * Remove all edges connecting dead cells with others
     * Call this after loading this entity from db
     */
    public void updateGraph()
    {
        cellContainer = new Cell[rowCount][colCount];
        initGraph();

        for (Cell c : deadCells)
        {            
            g.removeVertex(cellContainer[c.getRow()][c.getCol()]);
        }
    }
    
    public int getRowCount()
    {
        return this.rowCount;
    }
    
    public int getColCount()
    {
        return this.colCount;
    }
    
    public Cell[][] getCellContainer()
    {
        return this.cellContainer;
    }
    
    /**
     * 
     * @return the largest connected component of the graph.
     */
    public ArrayList<Cell> getLargestConnectedComponent()
    {
        ArrayList<ArrayList<Cell>> components = new ArrayList<>();
        boolean visited[][] = new boolean[rowCount][colCount];
        for (int row = 0; row < rowCount; ++row)
            for (int col = 0; col < colCount; ++col)
                visited[row][col] = false;
        
        Queue<Cell> q; 
        Cell t = null, u = null;
        for (Cell c : g.vertexSet())
        {
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
                    for (WeightedEdge e : g.edgesOf(t))
                    {
                        u = t.equals(g.getEdgeSource(e)) ?
                                g.getEdgeTarget(e) :
                                g.getEdgeSource(e);
                        if (!visited[u.getRow()][u.getCol()])
                        {
                            visited[u.getRow()][u.getCol()] = true;
                            q.add(u);
                            component.add(u);
                        }
                    }
                }
                
                components.add(component);
            } 
        }
        
        int largestSize = 0, largestIndex = 0;
        for (int i = 0; i < components.size(); ++i)
        {
            if (components.get(i).size() > largestSize)
            {
                largestSize = components.get(i).size();
                largestIndex = i;
            }
        }

        filterGraph(components.get(largestIndex));
        return components.get(largestIndex);
    }
    
    /**
     * Remove all nodes which are NOT in cells from graph g
     * @param cells 
     */
    private void filterGraph(ArrayList<Cell> cells)
    {
        for (int row = 0; row < rowCount; ++row)
            for (int col = 0; col < colCount; ++col)
                if (!cells.contains(cellContainer[row][col]))
                    g.removeVertex(cellContainer[row][col]);
    }
    
    public SimpleWeightedGraph<Cell, WeightedEdge> getLargestConnectedComponentAsGraph()
    {
        filterGraph(getLargestConnectedComponent());
        return g;
    }

    private boolean valid(int row, int col)
    {
        return row >= 0 && row < rowCount && col >= 0 && col < colCount;
    }
}

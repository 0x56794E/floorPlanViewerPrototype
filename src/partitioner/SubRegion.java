/**
 * Inertial Partitioning
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


package partitioner;

import entity.Cell;
import entity.WeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

/**
 * @author              Vy Thuy Nguyen
 * @version             1.0 Feb 16, 2013
 * Last modified:       
 */
public class SubRegion 
{
    private int rowCentroid;
    private int colCentroid;    
    private String binaryString;
    private SimpleWeightedGraph<Cell, WeightedEdge> graph;
    private boolean isLeaf;
    
    /**
     * 
     */
    public SubRegion()
    {
        rowCentroid =  -1;
        colCentroid = -1;
        binaryString = "";
        isLeaf = true;
    }
    
    /**
     * 
     * @return true if the region is NOT partitioned
     */
    public boolean isLeafNode()
    {
        return isLeaf;
    }
    
    /**
     * Make the node a parent node. (i.e., it'll be partitioned)
     */
    public void makeParent()
    {
        isLeaf = false;
    }
    
    /**
     * 
     * @param r
     */
    public void setRowCentroid(int r)
    {
        rowCentroid = r;
    }
    
    
    public void setGraph(SimpleWeightedGraph<Cell, WeightedEdge> graph)
    {
        this.graph = graph;
    }
    
    public SimpleWeightedGraph<Cell, WeightedEdge> getGraph()
    {
        return this.graph;
    }
    
    /**
     * 
     * @param c
     */
    public void setColCentroid(int c)
    {
        colCentroid = c;
    }
    
    /**
     * 
     * @param str
     */
    public void setBinaryString(String str)
    {
        binaryString = str;
    }
    
    /**
     * 
     * @return
     */
    public int getRowCentroid()
    {
        return rowCentroid;
    }
    
    /**
     * 
     * @return
     */
    public int getColCentroid()
    {
        return colCentroid;
    }
    
    /**
     * 
     * @return
     */
    public String getBinaryString()
    {
        return binaryString;
    }
    
    /**
     * 
     * @return string with the following form:
     * <binaryString> <row of centroid> <col of centroid>
     */
    public String toString()
    {
        return binaryString + " " + rowCentroid + " " + colCentroid;
    }
    
    public boolean equals(Object rhs)
    {
        if (rhs == null || !(rhs instanceof SubRegion))
            return false;
        else 
        {
            SubRegion rhsSub = (SubRegion)rhs;
            return binaryString.equals(rhsSub.binaryString);
        }
    }
    
    public int hashCode()
    {
        return binaryString.hashCode();
    }
}

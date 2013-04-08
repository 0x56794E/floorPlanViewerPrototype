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
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author              Vy Thuy Nguyen
 * @version             1.0 Mar 22, 2013
 * Last modified:       
 */
public class Class 
{
    private HashSet<Cell> cells;
    
    public Class (SubRegion sub)
    {
        cells = new HashSet<Cell>();
        for (Cell c : sub.getGraph().vertexSet())
            cells.add(c);
        
    }
    
    public int cellCount()
    {
        return cells.size();
    }
    
    public void addRegion(SubRegion sub)
    {
        int count = 0;
        for (Cell c : sub.getGraph().vertexSet())
        {
            System.out.print(c + "; ");
            if (count % 100 == 0) System.out.println();
            count++;
            cells.add(c);
        }
    }
    
    public void removeRegion(SubRegion sub)
    {
        int count = 0;
        for (Cell c : sub.getGraph().vertexSet())
        {
            System.out.print(c + "; ");
            if (count % 100 == 0) System.out.println();
            count++;
            cells.remove(c);
        }
    }
    
    public Set<Cell> getCells()
    {
        return Collections.unmodifiableSet(cells);
    }
    
    public boolean containsPoint(Cell c)
    {
        return cells.contains(c);
    }
    
    public boolean containsPoint(int x, int y)
    {
        boolean in = false;
        for (Cell c : cells)
        {
            if (c.containsPoint(x, y)) //if one of the cell contains the point then it's a yes
            {
                in = true;
                break;
            } 
        }
        return in;
    }
    
    /**
     * 
     * @return the row and col of the centroid
     */
    public int[] getCentroid()
    {
        int[] centroid = new int[2];
        centroid[0] = 0; //row
        centroid[1] = 0; //col
        for (Cell c : cells)
        {
            centroid[0] += c.getRow();
            centroid[1] += c.getCol();
        }
        
        centroid[0] /= cells.size();
        centroid[1] /= cells.size();
        
        return centroid;
    }
}

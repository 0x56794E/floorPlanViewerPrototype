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
    private HashSet<Cell> cells; //parent's class
    
    public Class (SubRegion sub)
    {
        cells = new HashSet<Cell>();
        
        System.out.println("\nIn Constructor; parent has " + sub.getGraph().vertexSet().size());
        for (Cell c : sub.getGraph().vertexSet())
            cells.add(c);
        
        System.out.println("In constructor; cells.size() = " + cells.size());
    }
    
    public int cellCount()
    {
        return cells.size();
    }
    
    public void addRegion(SubRegion sub)
    {
        System.out.println("\nadding to parent; Parent has " + cells.size());
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
        System.out.println("\nremoving from parent; Parent has " + cells.size());
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
}

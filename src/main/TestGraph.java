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

package main;

import entity.Cell;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

/**
 * @author              Vy Thuy Nguyen
 * @version             1.0 Jan 17, 2013
 * Last modified:       
 */
public class TestGraph 
{
    public static void main (String[] args)
    {
        SimpleGraph<Cell, DefaultEdge> g = new SimpleGraph<Cell, DefaultEdge>(DefaultEdge.class);

        Cell c1 = new Cell(0, 0);
        Cell c2 = new Cell(0, 1);
        Cell c3 = new Cell(0, 2);
        
        g.addVertex(c1);
        g.addVertex(c2);
        g.addVertex(c3);
        
        g.addEdge(c1, c2);
        g.addEdge(c2, c3);
        Cell test = new Cell(0, 0);
        System.out.println(g.containsVertex(test));
        test.disableCell();
        
        long n = 896;
        n /= 10;
        System.out.println("n = " + n);
        
            
    }
}

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

import Jama.Matrix;
import entity.Cell;
import entity.WeightedEdge;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import org.jgrapht.graph.SimpleWeightedGraph;

/**
 * Representing the "line" that bisects a graph using spectral bisection algorithm
 * 
 * @author              Vy Thuy Nguyen
 * @version             1.0 Feb 8, 2013
 * Last modified:       
 */
public class VirtualLine 
{
    Matrix v2;
    ArrayList<Cell> nMinus;
    ArrayList<Cell> nPlus;
    
    SimpleWeightedGraph<Cell, WeightedEdge> nMinusGraph;
    SimpleWeightedGraph<Cell, WeightedEdge> nPlusGraph;
    
    public VirtualLine(Matrix v2, SimpleWeightedGraph<Cell, WeightedEdge> g)
    {
        this.v2 = v2;
        nMinus = new ArrayList<Cell>();
        nPlus = new ArrayList<Cell>();
        nMinusGraph = new SimpleWeightedGraph<Cell, WeightedEdge>(WeightedEdge.class);
        nPlusGraph = new SimpleWeightedGraph<Cell, WeightedEdge>(WeightedEdge.class);
        partitionGraph(g);
    }
    
    public ArrayList<Cell> getNMinus()
    {
        return nMinus;
    }
    
     public ArrayList<Cell> getNPlus()
    {
        return nPlus;
    }
    
    
    private double v2Ofn(Cell node)
    {
        return v2.get(node.getIdForSpectralPartitioin(), 0);
    }

    private void partitionGraph(SimpleWeightedGraph<Cell, WeightedEdge> g)
    {
        for (Cell c : g.vertexSet())
        {
            if (v2Ofn(c) < 0)
            {
                nMinusGraph.addVertex(c);
                nMinus.add(c);
                c.addChar('0');
            }
            else
            {
                nPlusGraph.addVertex(c);
                nPlus.add(c);
                c.addChar('1');
                
            }
        }
        
        //Generating edges;
        generateEdges(nMinus, nMinusGraph, g);
        generateEdges(nPlus, nPlusGraph, g);
    }

    
    private void generateEdges(Collection<Cell> nodes,
                               SimpleWeightedGraph<Cell, WeightedEdge> g,
                               SimpleWeightedGraph<Cell, WeightedEdge> origGraph)
    {
        Cell source, target;
        for (Cell node : nodes)
        {
            for (WeightedEdge e : origGraph.edgesOf(node))
            {
                source = origGraph.getEdgeSource(e);
                target = origGraph.getEdgeTarget(e);
                if (g.containsVertex(source) && g.containsVertex(target))
                    g.addEdge(source, target, e);
            }
        }
    }
    
    public SimpleWeightedGraph<Cell, WeightedEdge> getNMinusGraph()
    {
        return nMinusGraph;
    }

    public SimpleWeightedGraph<Cell, WeightedEdge> getNPlusGraph()
    {
        return nPlusGraph;
    }
    
}

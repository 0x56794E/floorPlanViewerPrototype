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
import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;
import org.jgrapht.graph.SimpleWeightedGraph;

/**
 * Algorithm:
 *  - Compute eigenpair (lambda2, q2)
 *  - For each node v in G:
 *      + if q2(v) < 0, place v in partition N-
 *      + else, place v in partition N+
 * 
 * @author              Vy Thuy Nguyen
 * @version             1.0 Feb 6, 2013
 * Last modified:       
 */
public class SpectralPartitioner 
{
    public static VirtualLine getLine(SimpleWeightedGraph<Cell, WeightedEdge> g) throws Exception
    {
        Matrix laplacianM = getLaplacianMatrix(g);
        Matrix v2 = getV2(laplacianM);
        return new VirtualLine(v2, g);
    }
    
    public static ArrayList<VirtualLine> getLines(SimpleWeightedGraph<Cell, WeightedEdge> nodes, int k) throws Exception
    {
        if (k < 1) throw new Exception("k must be >= 1");
        
        ArrayList<VirtualLine> lines = new ArrayList<VirtualLine>();
        PriorityQueue<SimpleWeightedGraph<Cell, WeightedEdge>> subRegions = 
                new PriorityQueue<SimpleWeightedGraph<Cell, WeightedEdge>>(k, new Comparator<SimpleWeightedGraph<Cell, WeightedEdge>>() {
            
            @Override
            public int compare(SimpleWeightedGraph<Cell, WeightedEdge> o1, SimpleWeightedGraph<Cell, WeightedEdge> o2)
            {
                return (o1.vertexSet().size() > o2.vertexSet().size())
                            ? -1
                            : (o1.vertexSet().size() == o2.vertexSet().size() ? 0 : 1);
            }
        });
        
        //Line 1
        VirtualLine line = getLine(nodes);
        lines.add(line);
        subRegions.add(line.getNMinusGraph());
        subRegions.add(line.getNPlusGraph());
        Cell cell = line.getNMinus().get(0);
        k--;
        
        
        for (int i = 0; i < k; ++i)
        {
            //Find the largest region
            SimpleWeightedGraph<Cell, WeightedEdge> largestList = subRegions.remove();
            
            //Line dividing this region
            line = getLine(largestList);
            lines.add(line);
            
            //replace the old large region by two newly partitioned regions            
            subRegions.add(line.getNMinusGraph());
            subRegions.add(line.getNPlusGraph());
            
        }
        
        //Save the priority queue
        cell.getAnnotFloorPlan().setSubRegions(subRegions);
        
        //Return partitioning lines
        return lines;
    }
    
    
    
    /**
     * Definition: The Laplacian matrix L(G) of a graph G(N,E) 
     * is an |N| by |N| symmetric matrix, with one row and 
     * column for each node. It is defined by
     *  • L(G) (i,i) = degree of node I (number of incident edges)
     *  • L(G) (i,j) = -1 if i != j and there is an edge (i,j)
     *  • L(G) (i,j) = 0 otherwise
     * 
     * Properties of L(G):
     *  • L(G) is symmetric. (This means the eigenvalues of L(G) are real and 
     *    its eigenvectors are real and orthogonal.)
     *  • The eigenvalues of L(G) are nonnegative:
     *    0 = λ1 <= λ2 <= … <= λn
     *  • The number of connected components of G is equal to the number 
     *    of λi equal to 0. In particular, λ2 != 0 if and only if G is connected.
     *    Definition: λ2(L(G)) is the algebraic connectivity of G
     * 
     * @param g
     * @return 
     */
    private static Matrix getLaplacianMatrix(SimpleWeightedGraph<Cell, WeightedEdge> g)
    {
        int nodeCount = g.vertexSet().size(), vertexId = 0;
        Matrix matrix = new Matrix(nodeCount, nodeCount);
        
        //Setting the vertex's ID
        for (Cell c : g.vertexSet())
        {
            c.setIdForSpectralPartition(vertexId);
            vertexId++;
        }
        
        for (Cell i : g.vertexSet())
        {
            //(i, i)
            matrix.set(i.getIdForSpectralPartitioin(),
                       i.getIdForSpectralPartitioin(),
                       g.degreeOf(i));
            
            //(i,j)
            for (Cell j : g.vertexSet())
            {
                if (i.getIdForSpectralPartitioin() != j.getIdForSpectralPartitioin()
                        && g.containsEdge(i, j))
                {
                    matrix.set(i.getIdForSpectralPartitioin(),
                               j.getIdForSpectralPartitioin(),
                               -1);
                }
            }
        }
        
        return matrix;
    }
    
    /**
     * Definition: The incidence matrix In(G) of a graph G(N,E) 
     * is an |N| by |E| matrix, with one row for each node and 
     * one column for each edge. If edge e=(i,j) then column e 
     * of In(G) is zero except for the i-th and j-th entries, which 
     * are +1 and -1, respectively.
     * 
     * @param g
     * @return 
     */
    private static Matrix getIncidentMatrix(SimpleWeightedGraph<Cell, WeightedEdge> g)
    {
        Matrix matrix = new Matrix(g.vertexSet().size(), g.edgeSet().size());
        int edgeId = 0, vertexId = 0, r = 0;
        
        for (Cell c : g.vertexSet())
            c.setIdForSpectralPartition(vertexId++);
        
        for (WeightedEdge e : g.edgeSet())
        {
            e.setIdForSpectralPartition(edgeId);
            matrix.set(g.getEdgeSource(e).getIdForSpectralPartitioin(), 
                       edgeId,
                       1);
            matrix.set(g.getEdgeTarget(e).getIdForSpectralPartitioin(), 
                       edgeId, 
                       -1);
            edgeId++;
        }
        
        return matrix;
    }


    /**
     * 
     * @param matrix
     * @return
     * @throws Exception if less than two eigenvalues are found
     */
    private static double getLambda2(Matrix matrix) throws Exception
    {
        double[] eigenvalues = matrix.eig().getRealEigenvalues();
        return getSecondSmallest(eigenvalues);
    }
        
    private static Matrix getV2(Matrix matrix) throws Exception
    {
        double[] eigenvalues = matrix.eig().getRealEigenvalues();
        
        System.out.println("Eigenvalues: ");
        for (int i = 0; i < eigenvalues.length; ++i)
            System.out.printf("%-12f; ", eigenvalues[i]);
        System.out.println();
        
        int lambda2Index = getSecondSmallestIndex(eigenvalues);
        Matrix eigenvectors = matrix.eig().getV();
        
        return eigenvectors.getMatrix(0,                                 //init row index
                                      matrix.getColumnDimension() - 1,   //final row index
                                      lambda2Index,                       //init col index
                                      lambda2Index);                      //final col index
    }
    
    public static int getSecondSmallestIndex(double[] array) throws Exception
    {
        int len = array.length;
       
        switch (len)
        {
            case 0:
            case 1:
                throw new Exception("Not enough values");
            default:
                double min = array[0];
                int secondIndex = 1, minIndex = 0;
                for (int i = 1; i < len; ++i)
                {
                    if (array[i] < min)
                    {
                        secondIndex = minIndex;
                        minIndex = i;
                        min = array[i];
                    }
                }
                return secondIndex;
        }
    }
    
    public static double getSecondSmallest(double[] array) throws Exception
    {
        int len = array.length;
        
        switch (len)
        {
            case 0:
            case 1:
                throw new Exception("Not enough values");
            default:
                double min = array[0], second = array[0];
                for (int i = 1; i < len; ++i)
                {
                    if (array[i] < min)
                    {
                        second = min;
                        min = array[i];
                    }
                }
                return second;
        }
    }
    
    /**
     * 
     * @param c
     * @param r string representing the position of the region
     * @return 
     */
    public static boolean isInRegion(Cell c, String r)
    {
        return false;
    }
}

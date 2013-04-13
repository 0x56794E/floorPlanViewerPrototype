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
import entity.AnnotFloorPlan;
import entity.Cell;
import entity.WeightedEdge;
import java.util.*;
import java.util.Map.Entry;
import org.jgrapht.graph.SimpleWeightedGraph;
import util.FileService;

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
    public static AnnotFloorPlan annotFloorPlan;
    private static TreeMap<String, SubRegion> regionMap = new TreeMap<String, SubRegion>();
    private static PriorityQueue<SubRegion> subRegions = new PriorityQueue<SubRegion>();
    private static int currentK = 0;
    private static ArrayList<VirtualLine> currentLines = null;
    public static SubRegion entireFloor;
    public static boolean hasDonePartitioning = false;
    
    /**
     * Return the line partitioning the graph into two sub-regions
     * @param g
     * @return
     * @throws Exception 
     */
    public static VirtualLine getLine(SimpleWeightedGraph<Cell, WeightedEdge> g) throws Exception
    {
        Matrix laplacianM = getLaplacianMatrix(g);
        Matrix v2 = getV2(laplacianM);
        return new VirtualLine(v2, g);
    }
    
    /**
     * Return a list of k lines partitioning the graph into (k + 1) sub-regions
     * @param nodes
     * @param k
     * @return
     * @throws Exception 
     */
    public static ArrayList<VirtualLine> getLines(SimpleWeightedGraph<Cell, WeightedEdge> nodes, int k) throws Exception
    {
        if (k < 1) throw new Exception("k must be >= 1");
        if (currentK != k || currentLines == null)
        {
            currentK = k;
            //Entire floor
            entireFloor = new SubRegion();
            entireFloor.setGraph(nodes);      
            entireFloor.setBinaryString("");
            entireFloor.makeParent();
            //TODO: can skip centroid for now

            ArrayList<VirtualLine> lines = new ArrayList<VirtualLine>();
            subRegions = new PriorityQueue<SubRegion>(k, new Comparator<SubRegion>() {            
                @Override
                public int compare(SubRegion o1, SubRegion o2)
                {
                    return (o1.getGraph().vertexSet().size() > o2.getGraph().vertexSet().size())
                                ? -1
                                : (o1.getGraph().vertexSet().size() == o2.getGraph().vertexSet().size() ? 0 : 1);
                }
            });

            //Tree map maps a region's binary string to the region itself
            regionMap = new TreeMap<String, SubRegion>(new Comparator<String>(){

                /**
                * @return -1 if o1 < o2; 0 if o1 = o2; +1 if o1 > o2
                */
                @Override
                public int compare(String o1, String o2)
                {
                    return o1.compareTo(o2);
                }
            });        


            //Line 1
            VirtualLine line = getLine(nodes);
            lines.add(line);
            subRegions.add(line.getNMinusRegion());
            subRegions.add(line.getNPlusRegion());

            System.out.println("INSERTING at key " + line.getNMinusRegion().getBinaryString() + "   " + regionMap.put(line.getNMinusRegion().getBinaryString(), line.getNMinusRegion()));
            System.out.println("INSERTING at key " + line.getNPlusRegion().getBinaryString() + "    " + regionMap.put(line.getNPlusRegion().getBinaryString(), line.getNPlusRegion()));
            k--;

            for (int i = 0; i < k; ++i)
            {
                //Find the largest region
                SubRegion largestList = subRegions.remove();
                largestList.makeParent();

                //Line dividing this region
                line = getLine(largestList.getGraph());
                lines.add(line);

                //replace the old large region by two newly partitioned regions            
                subRegions.add(line.getNMinusRegion());
                subRegions.add(line.getNPlusRegion());      
                System.out.println("INSERTING at key " + line.getNMinusRegion().getBinaryString() + "    " 
                                    + regionMap.put(line.getNMinusRegion().getBinaryString(), line.getNMinusRegion()));
                System.out.println("INSERTING at key " + line.getNPlusRegion().getBinaryString() + "    " 
                                    + regionMap.put(line.getNPlusRegion().getBinaryString(), line.getNPlusRegion()));
            }
            currentLines = lines;
        }
        
        //Return partitioning lines
        return currentLines;
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
     * @param x x coordinate 
     * @param y y coordinate
     * @param regionOrder string representing the position of the region
     * @return 
     */
    public static boolean isInRegion(int x, int y, String regionOrder )
    {
        Cell c = annotFloorPlan.getNode(x, y);
        
        if (c != null && c.getBinaryString().equals(regionOrder))
            return true;
        else
            return false;
    }
    
    /**
     * 
     * @param regionOrder
     * @return the array containing the coordinates of the centroid of the region specified by the binary string
     */
    public static double[] getCentroid(String regionOrder)
    {
        double[] ret = null;
        if (regionMap.containsKey(regionOrder))
        {
            ret = new double[2];
            ret[0] = regionMap.get(regionOrder).getRowCentroid();
            ret[1] = regionMap.get(regionOrder).getColCentroid();
        }
        
        return ret;
    }
    
    /**
     * 
     * @return the sub region tree map 
     */
    public static Map<String, SubRegion> getSubRegions()
    {
        return Collections.unmodifiableMap(regionMap);
    }
}

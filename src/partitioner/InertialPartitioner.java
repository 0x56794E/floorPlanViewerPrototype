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
import entity.FloorPlan;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.*;

/**
 * @author              Vy Thuy Nguyen
 * @version             1.0 Jan 22, 2013
 * Last modified:       
 */
public class InertialPartitioner extends Partitioner
{
//    public static final InertialPartitioner INSTANCE = new InertialPartitioner();
    public static final double EPSILON = 0.001; //to compare to double;
    private InertialPartitioner() {}
    
    /**
     * Given a graph, this function returns the line L that partitions the graph
     * into two parts. 
     * Line L is represented by L = a * (x - xbar) + b * (y - ybar) = 0.
     * Also, line L has property sbar which is the median(s1, s2, ..., sn)
     * where sj is calculated by sj = -b * (xj - xbar) + a * (yj - ybar)
     * 
     * The computation is as followed:
     * 1) Compute xbar and ybar: (N = number of nodes)
     *    xbar = (Sigmar(xi) i = [0, N - 1]) / N
     *    ybar = (Sigmar(yi) i = [0, N - 1))/ N
     * 
     * 2) Compute sum of squares of distance (x1, x2, x3)
     *    x1 = Sigmar[(xi - xbar)^2 i = [0, N - 1]]
     *    x2 = Sigmar[(xi - xbar) * (yi - ybar) i = [0, N - 1]]
     *    x3 = Sigmar[(yi - ybar)^2 i = [0, N - 1]]
     * 
     * 3) Compute lambda (the smallest eigenvalue of the 2x2 matrix A = [x1 x2, x2 x3])
     *    Let I be the 2x2 identity matrix, v = [a, b] be the 2x1 eigenvector corresponding
     *    to lambda.
     *    Then (A - lambda * I) * v = 0
     *    Therefore, det(A - lambda * I) = 0
     *   | x1  x2 |   | lambda  0      |   | x1 - lambda     x2          |
     *   |        | - |                | = |                             |
     *   | x2  x3 |   | 0       lambda |   | x2              x3 - lambda |
     *   <=> (x1 - lambda)(x3 - lambda) - x2 ^ 2 = 0
     *   <=> lambda ^ 2 - (x1 + x3) * lambda + x1 * x3 - x2^2 = 0
     *   Solve this equation and get the smallest solution. 
     *   An exception is thrown if no solution is found.
     * 
     * 4) Compute vector v = [a, b] (the eigenvector corresponding to lambda)
     *    As mentioned above, (A - lambda * I) * v = 0
     *    We can write this equation as a system of two linear equations of two vars a and b
     *    a * (x1 - lambda) + b * x2 = 0 
     *    a * x2 + b * (x3 - lambda) = 0 
     *    
     *    There're 3 possible cases:
     *      1-The system has one single solution <=> (a, b) = 0
     *      2-The system has infinitely many solutions. In other words, [a, b] is a
     *        vector in an eigenspace whose basis is [-x2 / (x1 - lambda), 1]
     *      3-The system has no solution.
     *    
     *    Since the 1st and 3rd cases are irrelevant, we only need to look for 
     *    one of the vectors in the eigenspace described above. The easiest
     *    one to find is the basis itself, which is [-x2 / (x1 - lambda), 1] (if x1 != lambda.
     *    However, if x1 happens to equal to lambda, then we can choose the eigenvector to be [1, (x1 - lambda) / (-x2)]
     *    If both (x1 - lambda) and x2 are 0, [a, b] can take on any value, thus 
     *    the function returns [1, 1].
     * 
     *    An exception also is thrown if the system falls into the 1st or 3rd case.
     * 
     * 5) Compute sbar
     *    Compute sj for each node in the graph. Store these values in a linked 
     *    list l in ascending order.
     *    Sbar = median(l)
     *    In other words, 
     *      - if the list l has even number of elements, sbar is the average of the
     *        middle two elements.
     *      - if the list l has odd number of elements, sbar is the middle element.
     * 
     * @param g
     * @return Line l
     * @throws Exception 
     */
    public static Line getLine(ArrayList<Cell> nodes) throws Exception
    {
        //Compute xbar and ybar
        double xbar = 0, ybar = 0;
        final int N = nodes.size();
        
        for (Cell node : nodes)
        {
            xbar += node.getCol();
            ybar += node.getRow();
        }
        
        xbar /= N;
        ybar /= N;
        
        //Compute sum of squares of distance (x1, x2 and x3)
        double x1 = 0, x3 = 0, x2 = 0;
        double xDif = 0, yDif = 0;
        for (Cell node : nodes)
        {
            xDif = node.getCol() - xbar;
            yDif = node.getRow() - ybar;
            x1 += xDif * xDif;
            x3 += yDif * yDif;
            x2 += xDif * yDif;
        }
        
        //Compute a and b
        double a, b, lambda;
        ArrayList<Double> sols = getSolutions(1, //a
                                              0 - x1 - x3, //b
                                              x1 * x3 - x2 * x2); //c
        if (sols.isEmpty())
            throw new Exception("No eigenvalue found!");
    
        lambda = Math.min(sols.get(0), sols.get(1));
        
        //Compute a, b
        if (Math.abs(x2 * x2 - (x1 - lambda) * (x3 - lambda)) > EPSILON) //If the system doesn't have inf. number of solultions
            throw new Exception("The system must have inf. number of solutions. Otherwise, the eigenvector would be [0, 0]"
                                + String.format("\nx1 = %f, x2 = %f, x3 = %f", x1, x2, x3));
        
        if (Math.abs(x1 - lambda) > EPSILON) //two numbers are different
        {    
            a = (0 - x2) / (x1 -  lambda);
            b = 1;        
        }
        else //(x2 != 0) OR (a and b can be any value since x2 == 0 and x1 == lambda)
        {
            a = 1;
            b = 0;
        }
        
        //Compute sbar
        LinkedList<Double> sValues = new LinkedList<Double>();
        double sj = 0;
        int max, min, mid, size;
        for (Cell node : nodes)
        {
            sj = Line.getSj(node, a, b, xbar, ybar);
            if (sValues.isEmpty())
                sValues.add(sj);
            else
            {
                min = 0;
                max = sValues.size() - 1;
                while (max >= min)
                {
                    mid = (min + max) / 2;
                    if (sj < sValues.get(mid))
                        max = mid - 1;
                    else if (sj > sValues.get(mid))
                        min = mid + 1;
                    else
                    {
                        min = mid;
                        break;
                    }
                }   
               
                sValues.add(min, sj);               
            }
        }
      
        size = sValues.size();
        double sbar = (size % 2 == 0 //If even number of elements
                        ? (sValues.get(size / 2) + sValues.get(size / 2 - 1)) / 2 //Take the avg of the two middle elements
                        : sValues.get(size / 2 )); //Otherwise, take the middle element
        
        double[] data = {a, b, xbar, ybar, sbar};
        return new Line(nodes, data);
    }
 
    public static ArrayList<Line> getLines(ArrayList<Cell> nodes, int k) throws Exception
    {
        if (k < 1) throw new Exception("k must be >= 1");
        
        ArrayList<Line> lines = new ArrayList<Line>();
        PriorityQueue<ArrayList<Cell>> subRegions = 
                new PriorityQueue<ArrayList<Cell>>(k, new Comparator<ArrayList<Cell>>() {
            @Override
            /**
             * @return -1 if o1.size > o2.size; 0 if o1.size == o2.size; 1 if o1.size > o2.size
             */
            public int compare(ArrayList<Cell> list1, ArrayList<Cell> list2)
            {
                return (list1.size() > list2.size() ? 
                        -1 :
                        (list1.size() == list2.size() ? 0 : 1));
            }            
        });
        
        //Line 1
        Line line = getLine(nodes);
        lines.add(line);
        subRegions.add(line.getLeftNodes());
        subRegions.add(line.getRightNodes());
        k--;
        
        for (int i = 0; i < k; ++i)
        {
            //Find the greatest set
            ArrayList<Cell> largestList = subRegions.remove();
            
            //Line dividing this set
            line = getLine(largestList);
            lines.add(line);
            
            //replace the old large region by two newly partitioned regions            
            subRegions.add(line.getLeftNodes());
            subRegions.add(line.getRightNodes());
        }
        return lines;
    }
    
    
    /**
     * This function returns an array list containing the solution(s) of a 
     * quadratic function, which has the form of
     * aX^2 + bX + c = 0
     * 
     * @param a
     * @param b
     * @param c
     * @return an arraylist container the solution(s) of the quad-eqn
     * aX^2 + bX + c = 0
     */
    private static ArrayList<Double> getSolutions(double a, double b, double c)
    {
        ArrayList<Double> sols = new ArrayList<Double>();
        
        double delta = b * b - 4 * a * c;
        
        //If there're solution
        if (delta >= 0)
        {
            //Sqrt of delta
            delta = Math.sqrt(delta);
         
            //The two solutions 
            sols.add(((0 - b) + delta) / (2 * a));
            sols.add(((0 - b) - delta) / (2 * a));
        }
        
        return sols;
    }
   
    /**
     * 
     * @param line has the form of a * (x - xbar) + b * (y - ybar) = 0
     * @param node1
     * @param node2
     * @return 
     */
    public static boolean areOnSameSide(Line line, Cell node1, Cell node2)
    {
        double prod = ( (line.getA() * (node1.getCol() - line.getXbar())
                         + line.getB() * (node1.getRow() - line.getYbar()))
                      * (line.getA() * (node2.getCol() - line.getXbar())
                         + line.getB() * (node2.getRow() - line.getYbar())));
        
        return (prod >= 0 ? true : false);
    }
   


    public String getBinaryString(Cell node, List<Line> lines)
    {
        StringBuilder sb = new StringBuilder();
        
        for (Line line : lines)
            sb.append(line.getSideMembership(node).getValue());
        
        return sb.toString();
    }
}


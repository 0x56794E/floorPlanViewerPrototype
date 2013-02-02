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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

/**
 * Represents a line L such that 
 * a * (x - xbar) + b * (y - ybar) = 0
 * 
 * @author              Vy Thuy Nguyen
 * @version             1.0 Jan 22, 2013
 * Last modified:       
 TODO: includes sideMembership determining in this class, NOT as static method in inertialPartitioner
 */

public class Line
{
    private double a;
    private double b;
    private double xbar;
    private double ybar;
    private double sbar;

    private ArrayList<Cell> leftNodes;
    private ArrayList<Cell> rightNodes;
    
    public Line(double a, double b, double xbar, double ybar, double sbar, Collection<Cell> nodes)
    {
        this.a = a;
        this.b = b;
        this.xbar = xbar;
        this.ybar = ybar;
        this.sbar = sbar;
        
        leftNodes = new ArrayList<Cell>();
        rightNodes = new ArrayList<Cell>();
        partitionGraph(nodes);
    }
    
    /**
     * @return the a
     */
    public double getA()
    {
        return a;
    }

    /**
     * @param a the a to set
     */
    public void setA(double a)
    {
        this.a = a;
    }

    /**
     * @return the b
     */
    public double getB()
    {
        return b;
    }

    /**
     * @param b the b to set
     */
    public void setB(double b)
    {
        this.b = b;
    }

    /**
     * @return the xbar
     */
    public double getXbar()
    {
        return xbar;
    }

    /**
     * @param xbar the xbar to set
     */
    public void setXbar(double xbar)
    {
        this.xbar = xbar;
    }

    /**
     * @return the ybar
     */
    public double getYbar()
    {
        return ybar;
    }

    /**
     * @param ybar the ybar to set
     */
    public void setYbar(double ybar)
    {
        this.ybar = ybar;
    }
    
    public double getSbar()
    {
        return sbar;
    }
    
    public void setSbar(double sbar)
    {
        this.sbar = sbar;
    }
    
    /**
     * 
     * @param node
     * @return the side membership of the given node with respect to this line
     */
    public SideMembership getSideMembership(Cell node)
    {
        double sj = getSj(node);
        return (sj < sbar ? SideMembership.LEFT : SideMembership.RIGHT);
    }
    
    /**
     * 
     * @param node
     * @param line
     * @return the side membership of the given node with respect to the given line
     */
    public static SideMembership getSideMembership(Cell node, Line line)
    {
        double sj = Line.getSj(node, line.getA(), line.getB(), line.getXbar(), line.getYbar());
        return (sj < line.getSbar() ? SideMembership.LEFT : SideMembership.RIGHT);
    }
    
    /**
     * Given a node, this method returns the value sj corresponding to the node.
     * sj is calculated by a * (y - ybar) - b * (x - xbar)
     * 
     * @param node
     * @return sj
     */
    public double getSj(Cell node)
    {
        return a * (node.getY() - ybar) - b * (node.getX() - xbar);
    }
    
    /**
     * Given a node and values a, b, xbar, ybar of line L, this function returns 
     * the value sj, which is calculated by a * (y - ybar) - b *(x - xbar)
     *
     * @param node
     * @param a
     * @param b
     * @param xbar
     * @param ybar
     * @return sj
     */
    public static double getSj(Cell node, double a, double b, double xbar, double ybar)
    {
        return a * (node.getY() - ybar) - b * (node.getX() - xbar);
    }
    
    
    @Override
    public boolean equals(Object rhs)
    {
        if (rhs == null || !(rhs instanceof Line))
            return false;
        else
        {
            Line rhsLine = (Line)rhs;
           
            return Math.abs(this.a - rhsLine.a) < 0.000001
                    && Math.abs(this.b - rhsLine.b) < 0.000001
                    && Math.abs(this.sbar - rhsLine.sbar) < 0.000001
                    && Math.abs(this.xbar - rhsLine.xbar) < 0.000001
                    && Math.abs(this.ybar - rhsLine.ybar) < 0.000001;
        }
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 89 * hash + (int) (Double.doubleToLongBits(this.a) ^ (Double.doubleToLongBits(this.a) >>> 32));
        hash = 89 * hash + (int) (Double.doubleToLongBits(this.b) ^ (Double.doubleToLongBits(this.b) >>> 32));
        hash = 89 * hash + (int) (Double.doubleToLongBits(this.xbar) ^ (Double.doubleToLongBits(this.xbar) >>> 32));
        hash = 89 * hash + (int) (Double.doubleToLongBits(this.ybar) ^ (Double.doubleToLongBits(this.ybar) >>> 32));
        hash = 89 * hash + (int) (Double.doubleToLongBits(this.sbar) ^ (Double.doubleToLongBits(this.sbar) >>> 32));
        return hash;
    }

    private void partitionGraph(Collection<Cell> nodes)
    {
        for (Cell node : nodes)
        {
            if (getSideMembership(node) == SideMembership.LEFT)
                leftNodes.add(node);
            else
                rightNodes.add(node);
        }
    }
    
    public List<Cell> getLeftNodes()
    {
        return Collections.unmodifiableList(leftNodes);
    }
    
    public List<Cell> getRightNodes()
    {
        return Collections.unmodifiableList(rightNodes);
    }
    
    public String toString()
    {
        return String.format("a = %f; b = %f; sbar = %f; xbar = %f; ybar = %f\n", 
                             this.a, 
                             this.b, 
                             this.sbar, 
                             this.xbar, 
                             this.ybar);
    }
}

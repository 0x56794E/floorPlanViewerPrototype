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

import java.awt.Color;
import javax.persistence.*;

/**
 * Represent a cell in the matrix that the floor plan is divided into.
 * @author              Vy Thuy Nguyen
 * @version             1.0 Jan 16, 2013
 * Last modified:       
 */
@Entity
public class Cell
{
    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private long id;
    
    private boolean isDead;
  
    private int row;
    private int col;
    
//    private int x;
//    private int y;
    
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    AnnotFloorPlan annotFloorPlan;
    
    @Transient
    StringBuilder binaryString = new StringBuilder("");
    
    @Transient
    private int idForSpectralPartition = 0;
    
    public Cell(int r, int c, AnnotFloorPlan afp)
    {
        this.row = r;
        this.col = c;
//        this.x = x;
//        this.y = y;
        annotFloorPlan = afp;
        isDead = false;
    }
    
    public Cell()
    {
        this.isDead = false;
    }
    
    public int getRow()
    {
        return this.row;
    }
    
    public int getCol()
    {
        return this.col;
    }
    
    public void setIdForSpectralPartition(int val)
    {
        this.idForSpectralPartition = val;
    }
    
    public int getIdForSpectralPartitioin()
    {
        return this.idForSpectralPartition;
    }
    
    public boolean hasColor()
    {
        return binaryString.length() == 0 ? false : true;
    }
    
    public Color getColor(double zoomedIndex)
    {
       int rgb = (int)(Integer.parseInt(binaryString.toString(), 2) * zoomedIndex); // normalize the range
       int red = (rgb >> 16)  & 0xAA;
       int green = (rgb >> 8) & 0xAA;
       int blue = rgb & 0xFF;
       return new Color (red, green, blue);
    }
    
    public String getBinaryString()
    {
        return binaryString.toString();
    }
    public int getIntVal()
    {        
        return Integer.parseInt(binaryString.toString(), 2);
    }
    
    /**
     * '0' if node is in the N- or left section
     * '1' otherwise
     * @param c 
     */
    public void addChar(char c)
    {
        binaryString.append(c);
    }
    
    public void setAnnotFloorPlan (AnnotFloorPlan afp)
    {
        this.annotFloorPlan = afp;
    }
    
    public AnnotFloorPlan getAnnotFloorPlan()
    {
        return this.annotFloorPlan;
    }
    
//    public int getX()
//    {
//        return this.x;
//    }
//    public int getY()
//    {
//        return this.y;
//    }
    
    public void disableCell()
    {
        isDead = true;
    }
    
    public void enabbleCell()
    {
        isDead = false;
    }
    
    
    /**
     * 
     * @return true if the cell is NOT available; false otherwise
     */
    public boolean isDead()
    {
        return this.isDead;
    }
    
    
    /**
     * 
     * @param x x location in pixel
     * @param y y location in pixel
     * @return 
     */
    public boolean containsPoint(int x, int y)
    {
        return annotFloorPlan.isInCell(row, col, x, y);
    }
    
    //Oobject class
    public boolean equals (Object rhs)
    {
        if (rhs == null || !(rhs instanceof Cell))
            return false;
        else
        {
            Cell rhsCell = (Cell) rhs;
            return rhsCell.col == this.col && rhsCell.row == this.row;
        }
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 83 * hash + this.col ^ (this.col >>> 32);
        hash = 83 * hash + this.row ^ (this.row >>> 32);
        return hash;
    }
    
    public String toString()
    {
        return String.format("%d %d", row, col); 
    }
}

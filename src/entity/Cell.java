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

import javax.persistence.*;

/**
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
    private int col;
    private int row;
    
    private int minX;
    private int minY;
    
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    AnnotFloorPlan annotFloorPlan;
    
    
    public void setAnnotFloorPlan (AnnotFloorPlan afp)
    {
        this.annotFloorPlan = afp;
    }
    
    public AnnotFloorPlan getAnnotFloorPlan()
    {
        return this.annotFloorPlan;
    }
    
    public void setMinX(int x)
    {
        minX = x;
    }
    
    public void setMinY(int y)
    {
        minY = y;
    }
    
    public int getMinX()
    {
        return minX;
    }
    
    public int getMinY()
    {
        return minY;
    }
    
    public Cell(int r, int c)
    {
        this.col = c;
        this.row = r;
        isDead = false;
    }
    
    public Cell()
    {
        this.isDead = false;
    }
    
    public int getCol()
    {
        return col;
    }
    
    public int getRow()
    {
        return row;
        
    }
    
    public void disableCell()
    {
        isDead = true;
    }
    
    public void enabbleCell()
    {
        isDead = false;
    }
    
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
        hash = 83 * hash + (int) (this.col ^ (this.col >>> 32));
        hash = 83 * hash + (int) (this.row ^ (this.row >>> 32));
        return hash;
    }
    
    public String toString()
    {
        return String.format("(%d, %d)", row, col);
    }
}

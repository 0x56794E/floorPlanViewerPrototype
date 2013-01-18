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

/**
 * @author              Vy Thuy Nguyen
 * @version             1.0 Jan 16, 2013
 * Last modified:       
 */
public class Cell
{

    private boolean isDead;
    private int col;
    private int row;
    
    public Cell(int r, int c)
    {
        this.col = c;
        this.row = r;
        isDead = false;
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
        isDead = false;
    }
    
    public void enabbleCell()
    {
        isDead = true;
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

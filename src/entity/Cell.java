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
    private int x;
    private int y;
    
    public Cell(int x, int y)
    {
        this.x = x;
        this.y = y;
        isDead = false;
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
            return rhsCell.x == this.x && rhsCell.y == this.y;
        }
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 83 * hash + (int) (this.x ^ (this.x >>> 32));
        hash = 83 * hash + (int) (this.y ^ (this.y >>> 32));
        return hash;
    }
}

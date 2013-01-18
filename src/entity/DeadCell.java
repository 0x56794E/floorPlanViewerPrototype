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
 * @version             1.0 Jan 17, 2013
 * Last modified:       
 */
public class DeadCell
{
    int minX;
    int minY;
    
    int width;
    int height;

    public DeadCell()
    {
    }

    public int getMinX()
    {
        return minX;
    }
    
    public int getMinY()
    {
        return minY;
    }
    
    public int getWidth()
    {
        return width;
    }
    
    public int getHeight()
    {
        return height;
    }
    
    public void setMinX(int x)
    {
        minX = x;
    }

    public void setMinY(int y)
    {
        minY = y;
    }

    public void setWidth(int w)
    {
        width = w;
    }

    public void setHeight(int h)
    {
        height = h;
    }
    
    public boolean equals (Object rhs)
    {
        if (rhs == null || !(rhs instanceof DeadCell))
            return false;
        else
        {
            DeadCell dc = (DeadCell)rhs;
            return dc.minX == this.minX && dc.minY == this.minY;
        }
    }
}

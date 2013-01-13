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

import java.io.Serializable;
import javax.persistence.*;

/**
 * @author              Vy Thuy Nguyen
 * @version             1.0 Jan 10, 2013
 * Last modified:       
 */
@Entity
public class Point implements Serializable 
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private PointSet pointSet;
    
    
    private int x; //relative to the origin
    private int y;

    public Point()
    {
        
    }
    public Point(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
    
    public long getId()
    {
        return id;
    }

    /*
    public void setId(long id)
    {
        this.id = id;
    }
    */
    public int getX()
    {
        return x;
    }
    
    public void setX(int x)
    {
        this.x = x;
    }
    
    public int getY()
    {
        return this.y;
    }
    
    public void setY(int y)
    {
        this.y = y;
    }
    
    public PointSet getPointSet()
    {
        return this.pointSet;
    }
    
    public void setPointSet(PointSet ps)
    {
        this.pointSet = ps;
    }
    
    public String toString()
    {
        return String.format("(%d, %d)", x, y);
    }
    
    public int hashCode()
    {
        String str = this.toString();
        return str.hashCode();
    }
    
    @Override
    public boolean equals (Object rhs)
    {
        if (rhs == null || !(rhs instanceof Point))
            return false;
        else
        {
            Point rhsP = (Point)rhs;
            if (rhsP.x == this.x && rhsP.y == this.y)
                return true;
            else
                return false;
        }
        
    }
}

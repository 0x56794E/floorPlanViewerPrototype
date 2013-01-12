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
public class TestPoint implements Serializable 
{
    private static final long serialVersionUID = 1L;
 
    @Id @GeneratedValue
    private long id;
 
    private int x;
    private int y;
 
    public TestPoint() {
    }
 
    public TestPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }
 
    public Long getId() {
        return id;
    }
 
    public int getX() {
         return x;
    }
 
    public int getY() {
         return y;
    }
 
    @Override
    public String toString() {
        return String.format("(%d, %d)", this.x, this.y);
    }
}

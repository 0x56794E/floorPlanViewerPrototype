/**
 * Context-Free-Grammar to Push-down Automaton Converter
 * Copyright (C) 2012  Vy Thuy Nguyen
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

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

/**
 * @author              Vy Thuy Nguyen
 * @version             1.0 Jan 11, 2013
 * Last modified:       
 */
@Entity
public class PointList 
{
    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private long id;
    
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, 
                          fetch = FetchType.LAZY, mappedBy = "pointSet")
    private List<Point> points;
    
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    private FloorPlan floorPlan;
    
    public PointList()
    {
        
    }
    
    public PointList(FloorPlan fp)
    {
        floorPlan = fp;
        points = new ArrayList<Point>();
    }
    
    public long getId()
    {
        return id;
    }
    
    public FloorPlan getFloorPlan()
    {
        return floorPlan;
    }
    
    public void setFloorPlan(FloorPlan fp)
    {
        floorPlan = fp;
    }
    
    public List<Point> getPoints()
    {
        return points;
    }
    
    public void setPoints(List<Point> pts)
    {
        points = pts;
    }
    
    public void addPoint(Point p)
    {
        points.add(p);
    }
    
    public void removePoint(Point p)
    {
        points.remove(p);
    }
    
}

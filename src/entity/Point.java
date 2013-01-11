/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package entity;

import javax.persistence.*;

/**
 * @author              Vy Thuy Nguyen
 * @version             1.0 Jan 10, 2013
 * Last modified:       
 */
public class Point
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    private FloorPlan floorPlan;
    
    private int x; //relative to the origin
    private int y;

    public Point(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
    
    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }
    
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
    
    public FloorPlan getFloorPlan()
    {
        return this.floorPlan;
    }
    
    public void setFloorPlan(FloorPlan fp)
    {
        this.floorPlan = fp;
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
}

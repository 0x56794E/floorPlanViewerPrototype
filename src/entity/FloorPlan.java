/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * @author              Vy Thuy Nguyen
 * @version             1.0 Jan 10, 2013
 * Last modified:       
 */
@Entity
public class FloorPlan implements Serializable 
{
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue
    private long id;
    
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, 
               fetch = FetchType.LAZY, mappedBy = "floorPlan")
    private Set<Point> points;    
    
    private String absFilePath;
    private int originX;
    private int originY;
    
    public FloorPlan()
    {
        
    }
    
     public FloorPlan(String path, int x, int y)
    {
        absFilePath = path;
        originX = x;
        originY = y;
        points = new HashSet<Point>();
    }
     
     public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }
    
    public String getFileFullPath()
    {
        return absFilePath;
    }
    
    public void setFileFullPath(String path)
    {
        this.absFilePath = path;
    }
    
    public int getOriginX()
    {
        return originX;
    }
    
    public void setOriginX(int x)
    {
        this.originX = x;
    }
    
    public int getOriginY()
    {
        return originY;
    }
    
    public void setOriginY(int y)
    {
        this.originY = y;
    }
    
    @Override
    public String toString()
    {
        return String.format("File name: %s", this.absFilePath);
    }
}

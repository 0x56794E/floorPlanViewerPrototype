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

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
        
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.PERSIST},
               fetch = FetchType.LAZY, mappedBy = "floorPlan")
    private List<PointSet> pointSets;
    
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.PERSIST},
               fetch = FetchType.EAGER, mappedBy = "floorPlan")
    private AnnotFloorPlan annotFloorPlan;
    
    
    /**
     * This includes the file name
     */
    private String absFilePath;
    private String fileName;
    private int width;
    private int height;
    
    
    public FloorPlan()
    {
        pointSets = new ArrayList<PointSet>();   
    }
    
     public FloorPlan(File file, int x, int y, int width, int height, int ratio, int actualW, int actualH)
    {
        absFilePath = file.getPath();
        fileName = file.getName();
        this.width = width;
        this.height = height;
        pointSets = new ArrayList<PointSet>();
        annotFloorPlan = new AnnotFloorPlan(this, ratio, actualW, actualH);
    }
     
     /**
      * 
      * @return the width in pixels of the image representing the floor plan
      */
     public int getWidth()
     {
         return width;
     }
     
     /**
      * 
      * @return the height in pixels of the image representing the floor plan
      */
     public int getHeight()
     {
         return height;
     }
     
     public AnnotFloorPlan getAnnotFloorPlan()
     {
         return annotFloorPlan;
     }
     
     public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }
    
    public String getAbsoluteFilePath()
    {
        return absFilePath;
    }
    
    public void setFileFullPath(String path)
    {
        this.absFilePath = path;
    }
    
    public void setFileName(String name)
    {
        this.fileName = name;
    }
    
    public String getFileName()
    {
        return this.fileName;
    }
    
    public List<PointSet> getPointSets()
    {
        return pointSets;
    }
    
    public void setPoitSets(List<PointSet> ps)
    {
        pointSets = ps;
    }
    
    public void addPointSet(PointSet ps)
    {
        pointSets.add(ps);
    }
    
    @Override
    public String toString()
    {
        return String.format("%s; ID = %d", this.absFilePath, this.id);
    }
}

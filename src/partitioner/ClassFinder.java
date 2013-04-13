/**
 * Inertial Partitioning
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


package partitioner;

import entity.AnnotFloorPlan;

/**
 * 
 * @author              Vy Thuy Nguyen
 * @version             1.0 Mar 22, 2013
 * Last modified:       
 */
public class ClassFinder 
{
    /**
     * Given a region, the function returns the class associating with the region
     * @param sub
     * @return 
     */
    public static Class getClass(SubRegion sub, AnnotFloorPlan afp)
    {
        String binStr = sub.getBinaryString();
        String key;
        if (binStr.compareTo("") == 0)
        {
            return new Class(SpectralPartitioner.getSubRegions().get("0"), afp); 
        } 
        else 
        {
            if (binStr.length() > 1)
            {   
                key = binStr.substring(0, binStr.length() - 1);
                if (binStr.endsWith("0")) //is left child: ending with 0
                {
                    return subtract(getClass(SpectralPartitioner.getSubRegions().get(key), afp),
                                    SpectralPartitioner.getSubRegions().get(binStr + "1"));
                }
                else
                {
                    return union(getClass(SpectralPartitioner.getSubRegions().get(key), afp),
                                SpectralPartitioner.getSubRegions().get(binStr + "0"));
                }
            }
            else
            {
                if (binStr.compareTo("0") == 0) //very first left child
                {
                    return subtract(getClass(SpectralPartitioner.entireFloor, afp),
                                    SpectralPartitioner.getSubRegions().get("01"));
                    
                }
                else
                {
                    return union(getClass(SpectralPartitioner.entireFloor, afp),
                                 SpectralPartitioner.getSubRegions().get("10"));                    
                }
            }
        }
    }

    private static Class subtract(Class aClass, SubRegion subRegion)
    {
        aClass.removeRegion(subRegion);
        return aClass;
    }

    private static Class union(Class aClass, SubRegion subRegion)
    {
        aClass.addRegion(subRegion);
        return aClass;
    }
}

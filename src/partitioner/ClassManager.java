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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import util.FileService;

/**
 * @author              Vy Thuy Nguyen
 * @version             1.0 Apr 2, 2013
 * Last modified:       
 */
public class ClassManager 
{
    private static boolean hasClasses = false;
    private static HashMap<String, Class> classes;
    private static AnnotFloorPlan afp = null;
    
    /**
     * Initialize / restart
     */
    public static void start(AnnotFloorPlan fp)
    {
        hasClasses = false;
        classes = new HashMap<>();
        afp = fp;
    }
    
    public static boolean hasClasses()
    {
        return hasClasses;
    }
    
    public static void addClass(String b, Class c)
    {
        hasClasses = true;
        classes.put(b, c);
    }
    
    /**
     * 
     * @param s
     * @param x
     * @param y
     * @return true if class with given binary string contains given point
     */
    public static boolean containsPosition(String s, int x, int y)
    {
        return classes.get(s).containsPoint(x, y);
    }
    
//    /**
//     * 
//     * @param s
//     * @return the location of the centroid of class with given string
//     */
//    public static double[] getCentroid(String s)
//    {
//         return classes.get(s).getCentroid();
//    }
//    
    public static void exportTrainingFiles()
    {
        try
        {
            FileService.exportTrainingFiles(afp, classes);
        }
        catch (Exception e)
        {
            System.out.println("Error while exporting training files.");
            e.printStackTrace();
        }
    }

    public static void savePointsWithBinaryStringAndClasses(int k, String spectral) 
    {
        try
        {
            FileService.savePointsWithBinaryStrings(afp, k, "Spectral", classes);
        }
        catch (IOException exc)
        {
            System.out.println("Error while printing");
        }
    }
}

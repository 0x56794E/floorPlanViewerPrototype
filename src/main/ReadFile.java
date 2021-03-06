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


package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import util.FileService;

/**
 * @author              Vy Thuy Nguyen
 * @version             1.0 Feb 14, 2013
 * Last modified:       
 */
public class ReadFile 
{
    public static void main(String args[]) throws FileNotFoundException, IOException
    {
        
        TreeMap<String, Integer> intTree = new TreeMap<String, Integer>(new Comparator<String>(){

            @Override
            public int compare(String o1, String o2)
            {
                int b1 = Integer.parseInt(o1, 2);
                int b2 = Integer.parseInt(o2, 2);
                return b1 > b2 ? 1 : (b1 == b2 ? 0 : -1);
            }
        });
        
        
        intTree.put("00", 1);
        intTree.put("01", 2);
        
        Iterator<Entry<String, Integer>> iter = intTree.entrySet().iterator();
        while (iter.hasNext())
            System.out.println(iter.next().getValue());
    }
}

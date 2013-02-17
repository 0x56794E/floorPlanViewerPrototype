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
import java.util.Scanner;
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
        FileService.savePointsWithBinaryStrings(null, 3, "test");
//        if (args.length < 1)
//        {
//            System.out.println("Not Enough Argument");
//            System.out.println("Usage: RreadFile <fileName>");
//            System.exit(1);
//        }
//        else
//        {
//            File file = new File(args[0]);
//            Scanner sc = new Scanner(file);
//            int count = 0;
//            while (sc.hasNextLine())
//            {
//                System.out.println("line " + count + ": " + sc.nextLine());
//                count++;
//            }
//        }
    }
}

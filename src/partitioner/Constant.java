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

/**
 * @author              Vy Thuy Nguyen
 * @version             1.0 Feb 10, 2013
 * Last modified:       
 */
public class Constant 
{
    public static final double EPSILON = 0.0001;
    
    /**
    * @author              Vy Thuy Nguyen
    * @version             1.0 Jan 24, 2013
    * Last modified:       
    */
    public static enum SideMembership 
    {
        LEFT ((byte)0), RIGHT ((byte)1);

        private final byte value;

        private SideMembership(byte value)
        {
            this.value = value;
        }

        public byte getValue()
        { 
            return this.value;
        }
    }
    
    public static enum PartitionType
    {
        INERTIAL ((byte)0), SPECTRAL((byte)1);
        
        private final byte value;
        
        private PartitionType(byte val)
        {
            value = val;
        }
        
        public byte getValue()
        {
            return value;
        }
        
        
    }
    
    public static enum Data
    {
        a (0), b (1), xbar(2), ybar(3), sbar(4);
        
        private final int value;
        
        private Data(int value)
        {
            this.value = value;
        } 
        
        public int getValue()
        {
            
                    
            return this.value;
        }
   }
}

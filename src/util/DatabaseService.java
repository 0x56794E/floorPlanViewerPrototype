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

package util;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * @author              Vy Thuy Nguyen
 * @version             1.0 Jan 10, 2013
 * Last modified:       
 */
public class DatabaseService 
{
    private static Map<String, String> properties = new HashMap<String, String>();
    private static String dbName = "FloorPlanViewerDB.odb";
    private static String partialURL = "$objectdb/db/";
    private static EntityManagerFactory emf;
    private static EntityManager em;
    
    public static void setDatabaseUser(String u)
    {
        properties.put("javax.persistence.jdbc.user", u);
    }
    
    public static void setDatabasePassword(String p)
    {
        properties.put("javax.persistence.jdbc.password", p);
    }
    
    public static EntityManager getEntityManager()
    {
        if (em == null)
        {
            emf = Persistence.createEntityManagerFactory(partialURL + dbName, properties);
            em = emf.createEntityManager();
        }   
 
        
        return em;
    }
    
    public static void closeConnection()
    {
        if (em != null && emf != null && em.isOpen())
        {
            em.close();
            emf.close();
        }
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package helper;

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
public class DataBaseConnectionService 
{
    private static Map<String, String> properties = new HashMap<String, String>();
    private static String dbName = "FloorPlanViewerDB";
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
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package helper;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;



/**
 * @author              Vy Thuy Nguyen
 * @version             1.0 Jan 10, 2013
 * Last modified:       
 * @deprecated 
 */
public class EntityManagerService 
{
    private static Map<String, String> properties = new HashMap<String, String>();
    private static Statement statement;
    
    private static final String DB_URL = "jdbc:mysql://localhost/FloorPlanData";
    private static final String DRIVER ="com.mysql.jdbc.Driver";
    private static String user = "foobar";
    private static String password = "foobar";    
    
    public EntityManagerService() throws Exception
    {
        Connection conn = DriverManager.getConnection(DB_URL, user, password);
        statement = conn.createStatement();
    }
    
    /**
     * This method set the password to db
     * if it has already been set, it would replace the old password
     * @param pwd 
     */
    public static void setDatabasePassword(String pwd)
    {
        properties.put("javax.persistence.jdpc.password", pwd);
        password = pwd;
    }   
    
    public static void setDatabaseUser(String u)
    {
        properties.put("javax.persistence.jdbc.user", u);
        user = u;
        
    }
    
    public static EntityManager getEntityManager(String jpaName)
    {
       
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(jpaName);
        //System.out.println("Done creating emf");
        
        //EntityManager em = emf.createEntityManager();
        //System.out.println("Done creating em");
        
        return null;
    }
}

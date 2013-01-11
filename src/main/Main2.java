/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import entity.TestPoint;
import helper.DatabaseService;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.*;
import java.util.*;

/**
 * For testing purposes
 * @author VyNguyen
 */
public class Main2
{
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        //Prototype testing
        Map<String, String> properties = new HashMap<String, String>();
        //properties.put("javax.persistence.jdbc.user", "foobar");
        //properties.put("javax.persistence.jdbc.password", "foobar");
        /*
        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("$objectdb/db/testDB.odb", properties);
        EntityManager em = emf.createEntityManager();
        */
        
        EntityManager em = DatabaseService.getEntityManager();
        // Store 1000 Point objects in the database:
        em.getTransaction().begin();
        for (int i = 0; i < 1000; i++) {
            TestPoint p = new TestPoint(i, i);
            em.persist(p);
        }
        em.getTransaction().commit();
 
        // Find the number of Point objects in the database:
        Query q1 = em.createQuery("SELECT COUNT(p) FROM TestPoint p");
        System.out.println("Total Points: " + q1.getSingleResult());
 
        // Find the average X value:
        Query q2 = em.createQuery("SELECT AVG(p.x) FROM TestPoint p");
        System.out.println("Average X: " + q2.getSingleResult());
 
        // Retrieve all the Point objects from the database:
        TypedQuery<TestPoint> query =
            em.createQuery("SELECT p FROM TestPoint p", TestPoint.class);
        List<TestPoint> results = query.getResultList();
        for (TestPoint p : results) {
            System.out.println(p);
        }
 
        // Close the database connection:
       // em.close();
        //emf.close();
        DatabaseService.cleanup();
        
    }
}

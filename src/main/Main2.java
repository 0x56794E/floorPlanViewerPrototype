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

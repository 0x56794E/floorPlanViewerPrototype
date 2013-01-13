/**
 * Context-Free-Grammar to Push-down Automaton Converter
 * Copyright (C) 2012  Vy Thuy Nguyen
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

import entity.FloorPlan;
import entity.TestPoint;
import gui.view.FloorPlanListPopup;
import java.util.List;
import javax.persistence.EntityManager;
import util.DatabaseService;

/**
 * @author              Vy Thuy Nguyen
 * @version             1.0 Jan 12, 2013
 * Last modified:       
 */
public class TestRetrieveFromDB 
{
    public static void main(String[] args)
    {
        /*
        EntityManager em = DatabaseService.getEntityManager();
        List<FloorPlan> list = em.createQuery("Select f FROM FloorPlan f WHERE f.id % 5 = 0").getResultList();
        System.out.printf("Count = %d", list.size());
        new FloorPlanListPopup(list);
        * */
        
    }
}
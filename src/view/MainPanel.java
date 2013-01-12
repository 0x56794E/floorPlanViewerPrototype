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

package view;

import javax.swing.*;

/**
 * @author              Vy Thuy Nguyen
 * @version             1.0 Jan 12, 2013
 * Last modified:       
 */
public class MainPanel extends JPanel
{
    /**
     * Reference to the main frame
     */
    MainFrame mainFr;
    
    /************** SELECTED POINTS section **************/
    /**
     * JList of points that have been marked so far.
     */
    private JList pointList;
    
    /**
     * List model for pointList
     */
    private DefaultListModel plListModel = new DefaultListModel();
    
    /**
     * Scroll pane for pointList
     */
    private JScrollPane plScrollPane = new JScrollPane();
    
    /**
     * Removes the selected item from pointList
     * Also, this action removes the point from the floor plan
     * as well as a Point from the list of points to be persisted
     */
    private JButton removeBtn = new JButton("Remove");
    
    /**
     * Removes all of the items in pointList, as well as those on
     * the floor plan and those in the list of points to by persisted
     */
    private JButton clearAllBtn = new JButton("Clear All");
    /************** end SELECTED POINTS section **************/
    
    
    /************** EXISTING POINT SETS section **************/
    /**
     * This is, in fact, implemented by a list. 
     * That is there can be duplication.
     */
    
    
    
    public MainPanel(MainFrame mf)
    {
        mainFr = mf;
    }
}

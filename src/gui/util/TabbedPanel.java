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

package gui.util;

import gui.view.AnnotPanel;
import gui.view.GetDirectionPanel;
import gui.view.MainFrame;
import gui.view.PointMarkingPanel;
import java.awt.image.BufferedImage;
import javax.swing.JTabbedPane;

/**
 * @author              Vy Thuy Nguyen
 * @version             1.0 Jan 17, 2013
 * Last modified:       
 */
public class TabbedPanel extends JTabbedPane
{
    MainFrame mainFr;
    
    //PointMarking Panel
    PointMarkingPanel pointMarkingPn;
    
    //Annot Panel
    AnnotPanel annotPn;
    
    //Get Direction Panel
    GetDirectionPanel directionPn;
    
    public AnnotPanel getAnnotPn()
    {
        return annotPn;
    }
    
    public TabbedPanel(MainFrame mf)
    {   
        mainFr = mf;
        pointMarkingPn = new PointMarkingPanel(mf);
        annotPn = new AnnotPanel(mf);
        directionPn = new GetDirectionPanel(mf);
        
        pointMarkingPn.addObserver(annotPn);
        pointMarkingPn.addObserver(directionPn);
        
        renderComponents();
        
    }

    private void renderComponents()
    {
        this.addTab("Location Marking", pointMarkingPn.getUI());
        
        //Sub tabbed pane containing annotPn and directionPn
        JTabbedPane subTabbedPane = new JTabbedPane(JTabbedPane.BOTTOM);
        subTabbedPane.addTab("Mark Dead Cells", annotPn);
        subTabbedPane.addTab("Mark Locations", directionPn);
        this.addTab("Direction", subTabbedPane);
    }

    public void updateImagePanelScrollPaneSize(int width, int height)
    {
        pointMarkingPn.getUI().updateImagePanelScrollPaneSize(width, height);
    }

    public PointMarkingPanel getPointMarkingPn()
    {
        return pointMarkingPn;
    }
    
    
    
}

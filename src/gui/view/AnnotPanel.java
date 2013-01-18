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

package gui.view;

import entity.FloorPlan;
import gui.util.ImagePanelContainer;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import entity.DeadCell;

/**
 * @author              Vy Thuy Nguyen
 * @version             1.0 Jan 17, 2013
 * Last modified:       
 */
public class AnnotPanel extends JPanel
                        implements ImagePanelContainer, Observer
{
    MainFrame mainFr;
    ImagePanel imagePanel;
    private JScrollPane ipScrollPane = new JScrollPane();
    
    boolean inMarkingMode;
    JButton markEraseBtn;
    JButton saveDeadCellBtn = new JButton("Save");
    
    ArrayList<DeadCell> deadCells = new ArrayList<DeadCell>();
    private FloorPlan currentFloorPlan;
    
    public AnnotPanel(MainFrame mf)
    {
        mainFr = mf;
        inMarkingMode = true;
        markEraseBtn = new JButton("Eraser");
        markEraseBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (inMarkingMode)
                    markEraseBtn.setText("Marker");
                else
                    markEraseBtn.setText("Eraser");
                
                inMarkingMode = !inMarkingMode;
            }
        
        });
        
        saveDeadCellBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        
        //Render components
        this.setLayout(new BorderLayout());
        this.add(ipScrollPane, BorderLayout.CENTER);
        
        JPanel btnPn = new JPanel();
        btnPn.add(markEraseBtn);
        btnPn.add(saveDeadCellBtn);
        this.add(btnPn, BorderLayout.SOUTH);
    }

    @Override
    public void doPaintComponent(Graphics g)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void doPaintComponent(Graphics g, BufferedImage img)
    {
        for (DeadCell dc : deadCells)
            g.fillRect(dc.getMinX(), dc.getMinY(), dc.getWidth(), dc.getHeight());       
    }



    @Override
    public void onMouseClicked(int x, int y)
    {
        if (inMarkingMode)
        {
            DeadCell c = new DeadCell();
            deadCells.add(c);
            currentFloorPlan.getAnnotFloorPlan().disableCell(x, y, c);
        }
        else
        {
            DeadCell c = new DeadCell();
            currentFloorPlan.getAnnotFloorPlan().enabbleCell(x, y, c);
            deadCells.remove(c);
                    
        }
        repaint();
    }

    @Override
    public void updateCurrentPositionLabel(String str)
    {
        mainFr.setCurrentPositionlabel(str);
    }

    @Override
    public void update(Observable o, Object arg)
    {
        try
        {
            System.out.println("Update called");
            PointMarkingPanel pn = (PointMarkingPanel)arg;
            imagePanel = new ImagePanel(pn.getUI().getImageFile(), this);
            currentFloorPlan = pn.getUI().getCurrentFloorPlan();
            ipScrollPane.setViewportView(imagePanel);
        }
        catch (IOException e)
        {
            System.out.println("unable to update annotPn");
        }
    }
    
    
    
}

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

import entity.AnnotFloorPlan;
import entity.Cell;
import gui.util.ImagePanelContainer;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import util.DatabaseService;

/**
 * @author              Vy Thuy Nguyen
 * @version             1.0 Jan 17, 2013
 * Last modified:       
 */
public class AnnotPanel extends JPanel
                        implements ImagePanelContainer, Observer
{
    private MainFrame mainFr;
    private ImagePanel imagePanel;
    private JScrollPane ipScrollPane = new JScrollPane();
    
    boolean inMarkingMode;
    JButton markEraseBtn;
    JButton saveDeadCellBtn = new JButton("Save");
    
    private Set<Cell> deadCells;
    
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
                EntityManager em = DatabaseService.getEntityManager();
                em.getTransaction().begin();
                AnnotFloorPlan afp = mainFr.getCurrentFloorPlan().getAnnotFloorPlan();
                for (Cell dc : afp.getDeadCells())
                {
                    dc.setAnnotFloorPlan(afp);
                    em.persist(dc);
                }
                
                em.persist(mainFr.getCurrentFloorPlan().getAnnotFloorPlan());
                em.getTransaction().commit();
                em.close();
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
        deadCells = mainFr.getCurrentFloorPlan().getAnnotFloorPlan().getDeadCells();
        int halfUnitW = mainFr.getCurrentFloorPlan().getAnnotFloorPlan().getUnitW();// / 2;
        int halfUnitH = mainFr.getCurrentFloorPlan().getAnnotFloorPlan().getUnitH();// / 2;
        for (Cell dc : deadCells)
            g.fillRect(dc.getMinX(), dc.getMinY(), halfUnitW, halfUnitH);       
    }

    @Override
    public void onMouseClicked(int x, int y)
    {
        if (inMarkingMode)
            mainFr.getCurrentFloorPlan().getAnnotFloorPlan().disableCell(x, y);
       
        else
            mainFr.getCurrentFloorPlan().getAnnotFloorPlan().enabbleCell(x, y);                   
       
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
            //System.out.println("Update called");
            PointMarkingPanel pn = (PointMarkingPanel)arg;
            imagePanel = new ImagePanel(pn.getUI().getImageFile(), this);
            ipScrollPane.setViewportView(imagePanel);
        }
        catch (IOException e)
        {
            System.out.println("unable to update annotPn");
        }
    }
}
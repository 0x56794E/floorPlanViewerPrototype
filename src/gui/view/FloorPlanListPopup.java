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

package gui.view;

import entity.FloorPlan;
import gui.view.MainPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.*;

/**
 * @author              Vy Thuy Nguyen
 * @version             1.0 Jan 12, 2013
 * Last modified:       
 */
public class FloorPlanListPopup extends JFrame
{
    private List<FloorPlan> floorPlans;
    private TopLevelPanel topLevelPn;
    private MainPanel mainPn;
    
    public FloorPlanListPopup(List<FloorPlan> floorPlans, MainPanel mainPn)
    {
        this.floorPlans = floorPlans;
        topLevelPn = new TopLevelPanel();
        this.mainPn = mainPn;
        initView();
        
    }
    
    private void initView()
    {
        this.setSize(400, 400);
        this.setTitle("Saved Floor Plans");
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.add(topLevelPn);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
    }
    
    private class TopLevelPanel extends JPanel
    {
        private JList<FloorPlan> fpJList;
        private DefaultListModel<FloorPlan> listModel = new DefaultListModel<FloorPlan>();
        private JScrollPane scrollPane = new JScrollPane();
        private final JButton openBtn = new JButton("Open");
        private final JButton cancelBtn = new JButton("Cancel");
        
        public TopLevelPanel()
        {
            for (FloorPlan p : floorPlans)
                listModel.addElement(p);
            
            fpJList = new JList<FloorPlan>(listModel);
            scrollPane.setViewportView(fpJList);
            scrollPane.setPreferredSize(new Dimension (350, 300));
            
            JPanel btnPn = new JPanel();
            btnPn.add(openBtn);
            btnPn.add(cancelBtn);
            
            this.setLayout(new BorderLayout());
            this.add(scrollPane, BorderLayout.CENTER);
            this.add(btnPn, BorderLayout.SOUTH);
        }
        
        private class ButtonListener implements ActionListener
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (e.getSource() == openBtn)
                {
                    mainPn.setCurrentFloorPlan(fpJList.getSelectedValue());
                    FloorPlanListPopup.this.dispose();
                }
                
            }
                    
        }
    }
}

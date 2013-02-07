/**
 * Floor Plan Marker Project Copyright (C) 2013 Vy Thuy Nguyen
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Library General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option) any
 * later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Library General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin St, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package gui.view;

import entity.FloorPlan;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.swing.*;
import util.DatabaseService;

/**
 * @author Vy Thuy Nguyen
 * @version 1.0 Jan 12, 2013 Last modified:
 */
public class FloorPlanListPopup extends JFrame
{

    private List<FloorPlan> floorPlans;
    private TopLevelPanel topLevelPn;
    private PointMarkingPanel pointMarkingPn;

    public FloorPlanListPopup(PointMarkingPanel mainPn, List<FloorPlan> fps)
    {
        this.pointMarkingPn = mainPn;
        floorPlans = fps;
        initView();
    }
    
    public FloorPlanListPopup(PointMarkingPanel mainPn)
    {
        this.pointMarkingPn = mainPn;

        //Find all floor plans
        EntityManager em = DatabaseService.getEntityManager();
        floorPlans = em.createQuery("SELECT f FROM FloorPlan f", FloorPlan.class).getResultList();
        initView();
    }

    public FloorPlanListPopup(PointMarkingPanel mainPn, String fileName)
    {
        this.pointMarkingPn = mainPn;

        //Find floor plans with given name
        EntityManager em = DatabaseService.getEntityManager();
        floorPlans = em.createQuery("SELECT f FROM FloorPlan f WHERE f.fileName = :name",
                                    FloorPlan.class).setParameter("name", fileName).getResultList();
        initView();
    }

    private void initView()
    {
        topLevelPn = new TopLevelPanel();
        this.setSize(400, 400);
        this.setTitle("Saved Floor Plans");
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.add(topLevelPn);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.repaint();
        this.validate();
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
            scrollPane.setPreferredSize(new Dimension(350, 300));

            //Button
            ButtonListener btnHandler = new ButtonListener();
            openBtn.addActionListener(btnHandler);
            cancelBtn.addActionListener(btnHandler);
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
                    try
                    {
                        pointMarkingPn.getUI().setCurrentFloorPlan(fpJList.getSelectedValue());
                    }
                    catch (IOException exc)
                    {
                        JOptionPane.showMessageDialog(null, 
                                                    "Error While Reading From File. The file might have been moved.\n"
                                                    + "We found info about a file with the same name at the following path:\n"
                                                    + fpJList.getSelectedValue().getAbsoluteFilePath()
                                                    + "\nPlease check that the file is actually there.\n"
                                                    + "If you wish to create a new record or select another floor plan, you may do so now.", 
                                                    "ERROR!", JOptionPane.ERROR_MESSAGE);
                        return;

                    }
                }
                FloorPlanListPopup.this.dispose();
            }
        }
    }
}

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
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import java.util.List;

/**
 * @author              Vy Thuy Nguyen
 * @version             1.0 Jan 17, 2013
 * Last modified:       
 */
public class GetDirectionPanel extends JPanel
                               implements ImagePanelContainer, Observer
{
    MainFrame mainFr;
    ImagePanel imagePanel;
    private JScrollPane ipScrollPane = new JScrollPane();
    
    JButton goBtn = new JButton("Go");
    JTextField fromXField = new JTextField();
    JTextField fromYField = new JTextField();
    JTextField toXField = new JTextField();
    JTextField toYField = new JTextField();
    
    int x1 = -1;
    int y1 = -1;
    int x2 = -1;
    int y2 = -1;
    boolean settingFrom = true;
    boolean ready = false;
    
    public GetDirectionPanel(MainFrame mf)
    {
        mainFr = mf;
        this.setLayout(new BorderLayout());
        
        //Init components
        //Left pane
        fromXField.setPreferredSize(new Dimension(50, 20));
        fromYField.setPreferredSize(new Dimension(50, 20));
        toXField.setPreferredSize(new Dimension(50, 20));
        toYField.setPreferredSize(new Dimension(50, 20));
        fromXField.setEditable(false);        
        fromYField.setEditable(false);        
        toXField.setEditable(false);        
        toYField.setEditable(false);
        goBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (x1 >= 0 && x2 >= 0 && y1 >= 0 && y2 >= 0)
                    ready = true;
                imagePanel.repaint();
            }
        });
        
        //Render components
        //Center pane
        JPanel imgPn = new JPanel();
        //imgPn.add(ipScrollPane);
        this.add(ipScrollPane, BorderLayout.CENTER);
        
        //Left pane
        JPanel leftPn = new JPanel();
        leftPn.setBorder(new TitledBorder(""));
        leftPn.setMinimumSize(new Dimension(200, 200));
        leftPn.setPreferredSize(new Dimension(200, 200));
        
        leftPn.setAlignmentX(LEFT_ALIGNMENT);
        leftPn.setLayout(new GridLayout(3, 1));
        
        //Row 1
        JPanel r1 = new JPanel();
        JLabel fromLb = new JLabel("   From:                          ");
        
        JPanel fromCoorPn = new JPanel();
        fromCoorPn.setLayout(new FlowLayout(FlowLayout.LEFT));
        fromCoorPn.add(new JLabel(" x: "));
        fromCoorPn.add(fromXField);
        fromCoorPn.add(new JLabel(" y: "));
        fromCoorPn.add(fromYField);
        
        r1.add(fromLb);
        r1.add(fromCoorPn);
        leftPn.add(r1);
        
        //Row 2
        JPanel r2 = new JPanel();
        JLabel toLb = new JLabel("   To:                          ");
        
        JPanel toCoorPn = new JPanel();
        toCoorPn.setLayout(new FlowLayout(FlowLayout.LEFT));
        toCoorPn.add(new JLabel(" x: "));
        toCoorPn.add(toXField);
        toCoorPn.add(new JLabel(" y: "));
        toCoorPn.add(toYField);
        
        r2.add(toLb);
        r2.add(toCoorPn);
        leftPn.add(r2);
        
        JPanel r3 = new JPanel();
        r3.add(goBtn);
        leftPn.add(r3);
        
        this.add(leftPn, BorderLayout.EAST);
    }

    @Override
    public void doPaintComponent(Graphics g)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void doPaintComponent(Graphics g, BufferedImage img)
    {
        //Paint from and to pins
        if (x1 > 0 && y1 > 0)
        {
            g.drawImage(img, x1, y1, null);
            fromXField.setText(x1 + "");
            fromYField.setText(y1 + "");
        }
        
        if (x2 > 0 && y2 > 0)
        {
            g.drawImage(img, x2, y2, null);
            toXField.setText(x2 + "");
            toYField.setText(y2 + "");
        }
            
        //Paint path       
        if (ready)
        {
            g.setColor(Color.GREEN);
            AnnotFloorPlan afl = mainFr.getCurrentFloorPlan().getAnnotFloorPlan();

            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(3));

            SimpleGraph graph = afl.getGraph();
           List<DefaultEdge> edges = afl.getShortestPath(x1, y1, x2, y2);

            for (DefaultEdge e : edges)
            {
                Cell source = (Cell)graph.getEdgeSource(e);
                Cell target = (Cell)graph.getEdgeTarget(e);

                g2.drawLine(source.getCol() * afl.getUnitW(),
                            source.getRow() * afl.getUnitH(),
                            target.getCol() * afl.getUnitW(), 
                            target.getRow() * afl.getUnitH());
            }
        }
    }
//
//    @Override
//    public void disableCell(int x, int y, DeadCell c)
//    {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }

    @Override
    public void onMouseClicked(int x, int y)
    {
        if (settingFrom)
        {
            ready = false;
            x1 = x;
            y1 = y;
        }
        else
        {
            x2 = x;
            y2 = y;
            ready = true;
        }
        
        settingFrom = !settingFrom;
        imagePanel.repaint();
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
            System.out.println("Update in get direction called");
            PointMarkingPanel pn = (PointMarkingPanel)arg;
            imagePanel = new ImagePanel(pn.getUI().getImageFile(), this);
            //currentFloorPlan = pn.getUI().getCurrentFloorPlan();
            ipScrollPane.setViewportView(imagePanel);
        }
        catch (IOException e)
        {
            System.out.println("unable to update annotPn");
        }
    }

}

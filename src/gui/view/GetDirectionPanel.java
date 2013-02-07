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

import entity.AnnotFloorPlan;
import entity.Cell;
import gui.util.ImagePanelContainer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

/**
 * @author Vy Thuy Nguyen
 * @version 1.0 Jan 17, 2013 Last modified:
 */
public class GetDirectionPanel extends JPanel
        implements ImagePanelContainer, Observer
{

    private MainFrame mainFr;
    private ImagePanel imagePanel;
    private JScrollPane ipScrollPane = new JScrollPane();
    private JPanel fromCoorPn = new JPanel();
    private JPanel toCoorPn = new JPanel();
    private TitledBorder fromCoorBorder = new TitledBorder("From");
    private TitledBorder toCoorBorder = new TitledBorder("To");
    private JButton goBtn = new JButton("Go");
    private JTextField fromXField = new JTextField();
    private JTextField fromYField = new JTextField();
    private JTextField toXField = new JTextField();
    private JTextField toYField = new JTextField();
    private int x1 = -1;
    private int y1 = -1;
    private int x2 = -1;
    private int y2 = -1;
    private boolean settingFrom = true;
    private boolean ready = false;

    public GetDirectionPanel(MainFrame mf)
    {
        mainFr = mf;
        this.setLayout(new BorderLayout());

        //Init components
        initComponents();

        //Render components
        //Center pane
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
        fromCoorPn.setBorder(fromCoorBorder);
        fromCoorPn.setLayout(new FlowLayout(FlowLayout.LEFT));
        fromCoorPn.add(new JLabel(" x: "));
        fromCoorPn.add(fromXField);
        fromCoorPn.add(new JLabel(" y: "));
        fromCoorPn.add(fromYField);

        r1.add(fromCoorPn);
        leftPn.add(r1);

        //Row 2
        JPanel r2 = new JPanel();
        toCoorPn.setBorder(toCoorBorder);
        toCoorPn.setLayout(new FlowLayout(FlowLayout.LEFT));
        toCoorPn.add(new JLabel(" x: "));
        toCoorPn.add(toXField);
        toCoorPn.add(new JLabel(" y: "));
        toCoorPn.add(toYField);

        r2.add(toCoorPn);
        leftPn.add(r2);

        JPanel r3 = new JPanel();
        r3.add(goBtn);
        leftPn.add(r3);

        this.add(leftPn, BorderLayout.EAST);
    }

    private void initComponents()
    {
        //Left pane
        fromXField.setPreferredSize(new Dimension(50, 20));
        fromYField.setPreferredSize(new Dimension(50, 20));
        toXField.setPreferredSize(new Dimension(50, 20));
        toYField.setPreferredSize(new Dimension(50, 20));

        //Listener
        fromXField.addFocusListener(new FocusListener()
        {

            @Override
            public void focusGained(FocusEvent e)
            {
            }

            @Override
            public void focusLost(FocusEvent e)
            {
                try
                {
                    int x = Integer.parseInt(fromXField.getText());
                    if (isInXRange(x))
                    {
                        x1 = x;
                    }

                }
                catch (NumberFormatException exc)
                {
                    JOptionPane.showMessageDialog(null, "Value must be an integer", "INVALID VALUE", JOptionPane.ERROR_MESSAGE);
                    fromXField.setText("0");
                }

                imagePanel.repaint();
            }
        });

        toXField.addFocusListener(new FocusListener()
        {

            @Override
            public void focusGained(FocusEvent e)
            {
            }

            @Override
            public void focusLost(FocusEvent e)
            {
                try
                {
                    int x = Integer.parseInt(toXField.getText());
                    if (isInXRange(x))
                    {
                        x2 = x;
                    }

                }
                catch (NumberFormatException exc)
                {
                    JOptionPane.showMessageDialog(null, "Value must be an integer", "INVALID VALUE", JOptionPane.ERROR_MESSAGE);
                    toXField.setText("0");
                }

                imagePanel.repaint();
            }
        });

        fromYField.addFocusListener(new FocusListener()
        {

            @Override
            public void focusGained(FocusEvent e)
            {
            }

            @Override
            public void focusLost(FocusEvent e)
            {
                try
                {
                    int y = Integer.parseInt(fromYField.getText());
                    if (isInYRange(y))
                    {
                        y1 = y;
                    }

                }
                catch (NumberFormatException exc)
                {
                    JOptionPane.showMessageDialog(null, "Value must be an integer", "INVALID VALUE", JOptionPane.ERROR_MESSAGE);
                    fromYField.setText("0");
                }
                imagePanel.repaint();
            }
        });

        toYField.addFocusListener(new FocusListener()
        {

            @Override
            public void focusGained(FocusEvent e)
            {
            }

            @Override
            public void focusLost(FocusEvent e)
            {
                try
                {
                    int y = Integer.parseInt(toYField.getText());
                    if (isInYRange(y))
                    {
                        y2 = y;
                    }

                }
                catch (NumberFormatException exc)
                {
                    JOptionPane.showMessageDialog(null, "Value must be an integer", "INVALID VALUE", JOptionPane.ERROR_MESSAGE);
                    toYField.setText("0");
                }
                imagePanel.repaint();
            }
        });

        goBtn.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (x1 >= 0 && x2 >= 0 && y1 >= 0 && y2 >= 0)
                {
                    ready = true;
                }
                imagePanel.repaint();
            }
        });
    }

    @Override
    public void doPaintComponent(Graphics g)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void doPaintComponent(Graphics g, BufferedImage img)
    {
        AnnotFloorPlan afl = mainFr.getCurrentFloorPlan().getAnnotFloorPlan();
        int unitW = afl.getUnitW();
        int unitH = afl.getUnitH();
        int halfUnitH = unitH;// / 2;

        //Paint wall
        for (Cell dc : afl.getDeadCells())
        {
            g.fillRect(dc.getMinX(), dc.getMinY(), unitW, halfUnitH);
        }

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
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(3));

            SimpleWeightedGraph graph = afl.getGraph();
            List<DefaultWeightedEdge> edges = afl.getShortestPath(x1, y1, x2, y2);

            int halfW = afl.getUnitW() / 2;
            int halfH = afl.getUnitH() / 2;

            for (DefaultEdge e : edges)
            {
                Cell source = (Cell) graph.getEdgeSource(e);
                Cell target = (Cell) graph.getEdgeTarget(e);

                g2.setColor(Color.GREEN);
                g2.drawLine(source.getCol() * unitW + halfW,
                            source.getRow() * unitH + halfH,
                            target.getCol() * unitW + halfW,
                            target.getRow() * unitH + halfH);
            }

            ready = false;
        }
    }

    @Override
    public void onMouseClicked(int x, int y)
    {
        if (settingFrom)
        {
            x1 = x;
            y1 = y;
            fromCoorBorder.setTitleColor(Color.BLACK);
            toCoorBorder.setTitleColor(Color.ORANGE);
        }
        else
        {
            x2 = x;
            y2 = y;
            toCoorBorder.setTitleColor(Color.BLACK);
            fromCoorBorder.setTitleColor(Color.ORANGE);
        }

        settingFrom = !settingFrom;
        this.repaint();
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
            PointMarkingPanel pn = (PointMarkingPanel) arg;
            imagePanel = new ImagePanel(pn.getUI().getImageFile(), this);
            ipScrollPane.setViewportView(imagePanel);
        }
        catch (IOException e)
        {
            System.out.println("unable to update annotPn");
        }
    }

    public boolean isInYRange(int y)
    {
        return (y >= 0 && y <= imagePanel.getImageHeight());
    }

    public boolean isInXRange(int x)
    {
        return (x >= 0 && x <= imagePanel.getImageWidth());
    }
}

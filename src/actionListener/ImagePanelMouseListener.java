/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package actionListener;

import helper.DrawingPanel;
import helper.ImagePanel;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * @author Vy Thuy Nguyen
 * @version 1.0 Jan 10, 2013 Last modified:
 */
public class ImagePanelMouseListener implements MouseMotionListener, MouseListener
{

    @Override
    public void mouseDragged(MouseEvent e)
    {
        // throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mouseMoved(MouseEvent e)
    {
        //throw new UnsupportedOperationException("Not supported yet.");
        // System.out.println("Current pos: (" + e.getX() + ", " + e.getY() + ")");
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        //throw new UnsupportedOperationException("Not supported yet.");
        ImagePanel dp = (ImagePanel)e.getSource();
        dp.addPoint(e.getX(), e.getY());
        System.out.println("Current pos: (" + e.getX() + ", " + e.getY() + ")");
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        // throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
        //throw new UnsupportedOperationException("Not supported yet.");
    }
    
}

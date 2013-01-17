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

package util;

import gui.view.ImagePanel;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * @author Vy Thuy Nguyen
 * @version 1.0 Jan 10, 2013 Last modified:
 */
public class ImagePanelMouseListener implements MouseMotionListener, MouseListener
{
    private boolean inAnnotMode;
    public ImagePanelMouseListener()
    {
        super();
        inAnnotMode = false;
    }
    
    public void setInAnnotMode(boolean annotMode)
    {
        inAnnotMode = annotMode;
    }
    
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
        ImagePanel ip = (ImagePanel)e.getSource();
            
        if (inAnnotMode)
        {
            ip.disableCell(e.getX(), e.getY());
        }
        else
        {
            //throw new UnsupportedOperationException("Not supported yet.");
            ip.addPoint(e.getX(), e.getY());
           // System.out.println("Current pos: (" + e.getX() + ", " + e.getY() + ")");
           // System.out.println("Tile index: " + ip.getMousePosition());   
        }
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

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
package view;

import helper.ImageFileFilter;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.ImageFilter;
import java.io.File;
import java.io.FileFilter;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * @author              Vy Thuy Nguyen
 * @version             1.0 Jan 9, 2013
 * Last modified:       
 */
public class FileChooserWindow extends JFrame
{
    private JFileChooser fileChooser = new JFileChooser();
    private JPanel mainPn = new JPanel();
    private MainFrame mainFr;
    
    public FileChooserWindow(MainFrame mf)
    {
        this.mainFr = mf;
        
        this.setSize(550, 450);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        
        this.add(mainPn);
        mainPn.setLayout((new BorderLayout()));
        mainPn.add(fileChooser, BorderLayout.CENTER);
        
        
        //file chooser handler
        fileChooser.addChoosableFileFilter(new ImageFileFilter());
        fileChooser.setAcceptAllFileFilterUsed(false);
        
        ActionListener fileChooserHandler = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                String command = e.getActionCommand();
              
                if (command.equals(JFileChooser.APPROVE_SELECTION))
                {
                    mainFr.loadFloorPlan(fileChooser.getSelectedFile());
                }
                
                
                FileChooserWindow.this.setVisible(false);
            }
        };
        
        fileChooser.addActionListener(fileChooserHandler);
    }
    
    
}

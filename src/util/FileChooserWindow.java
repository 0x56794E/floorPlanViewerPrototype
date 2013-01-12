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

package util;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

/**
 * @author              Vy Thuy Nguyen
 * @version             1.0 Jan 12, 2013
 * Last modified:       
 */
public class FileChooserWindow extends JFrame
{
    private JFileChooser fileChooser = new JFileChooser();
    private FileGUIContainer fileGUIContainer;
    
    public FileChooserWindow(FileGUIContainer fgc)
    {
        fileGUIContainer = fgc;
        this.setSize(550, 450);
        this.setLocationRelativeTo(null);
        
        JPanel pn = new JPanel();
        pn.setLayout(new BorderLayout());
        pn.add(fileChooser, BorderLayout.CENTER);
        
        //action listener
        fileChooser.addActionListener(new FileChooserHandler());
    }
    
    public FileChooserWindow(FileGUIContainer fgc, FileFilter filter)
    {
        fileGUIContainer = fgc;
        JFrame.setDefaultLookAndFeelDecorated(true);
        this.setSize(550, 450);
        this.setLocationRelativeTo(null);
        
        
        JPanel pn = new JPanel();
        pn.setLayout(new BorderLayout());
        pn.add(fileChooser, BorderLayout.CENTER);
        
        //File filter
        fileChooser.addChoosableFileFilter(filter);
        fileChooser.setAcceptAllFileFilterUsed(false);
        
        //action listener
        fileChooser.addActionListener(new FileChooserHandler());
        //this.pack();
    }
    
    private class FileChooserHandler implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            String command = e.getActionCommand();
            if (command.equals(JFileChooser.APPROVE_SELECTION))
                fileGUIContainer.loadFileContent(fileChooser.getSelectedFile());
            FileChooserWindow.this.setVisible(false);
        }
    }
}

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

package gui.util;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
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
        initView();              
    }
    
    public FileChooserWindow(FileGUIContainer fgc, FileFilter filter)
    {
        fileGUIContainer = fgc;
        initView();
        
        //File filter
        fileChooser.addChoosableFileFilter(filter);
        fileChooser.setAcceptAllFileFilterUsed(false);
      
    }
    
    private void initView()
    {
        JFrame.setDefaultLookAndFeelDecorated(true);
        this.setTitle("Open");
        this.setSize(550, 450);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        
        JPanel pn = new JPanel();
        pn.setLayout(new BorderLayout());
        pn.add(fileChooser, BorderLayout.CENTER);
        
        this.add(pn);
        
        //action listener
        fileChooser.addActionListener(new FileChooserHandler());
        
    }
    
    private class FileChooserHandler implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            String command = e.getActionCommand();
            if (command.equals(JFileChooser.APPROVE_SELECTION))
            {
               // FileChooserWindow.this.setVisible(false);
                try
                {
                    fileGUIContainer.loadFileContent(fileChooser.getSelectedFile());
                }
                catch (IOException exc)
                {
                    JOptionPane.showMessageDialog(null, 
                                                 "Error While Reading From File.\n Try Again!", 
                                                 "ERROR!", JOptionPane.ERROR_MESSAGE);

                }
            
            }
            
            FileChooserWindow.this.setVisible(false);
        }
    }
   
}

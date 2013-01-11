/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
    private MainWindow mainFr;
    
    public FileChooserWindow(MainWindow mf)
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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package main;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author              Vy Thuy Nguyen
 * @version             1.0 Jan 11, 2013
 * Last modified:       
 */
public class TestAnimatedGif 
{
    public static void main(String[] args)
    {
        JFrame fr = new JFrame();
        JPanel pn = new JPanel();
        ImageIcon icon = new ImageIcon("purple.gif");
        JLabel lb = new JLabel(icon);
        
        pn.add(lb);
        fr.add(pn);
        fr.setVisible(true);
        
        
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package helper;

import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.*;
 

/**
 * @author              Vy Thuy Nguyen
 * @version             1.0 Jan 9, 2013
 * Last modified:       
 */
public class ImageFileFilter extends FileFilter
{
 
    public boolean accept(File f) 
    {
        //Accept directory
        if (f.isDirectory()) 
        {
            return true;
        }
 
        String[] tokens = f.getName().split("\\.");
        String extension = tokens[tokens.length - 1];
        
        if (extension != null 
            && (extension.equals("gif") ||
                extension.equals("jpeg") ||
                extension.equals("jpg") ||
                extension.equals("png"))) 
        {
                        return true;
        }
        else 
        {
                return false;
        }
    }
 
    //The description of this filter
    public String getDescription() {
        return "Just Images";
    }

}

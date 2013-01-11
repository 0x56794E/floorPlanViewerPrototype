/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package entity;


import java.io.Serializable;
import javax.persistence.*;

/**
 * @author              Vy Thuy Nguyen
 * @version             1.0 Jan 10, 2013
 * Last modified:       
 */
@Entity
public class TestPoint implements Serializable 
{
    private static final long serialVersionUID = 1L;
 
    @Id @GeneratedValue
    private long id;
 
    private int x;
    private int y;
 
    public TestPoint() {
    }
 
    public TestPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }
 
    public Long getId() {
        return id;
    }
 
    public int getX() {
         return x;
    }
 
    public int getY() {
         return y;
    }
 
    @Override
    public String toString() {
        return String.format("(%d, %d)", this.x, this.y);
    }
}

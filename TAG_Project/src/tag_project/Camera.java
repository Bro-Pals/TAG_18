/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tag_project;

/**
 *
 * @author Jonathon
 */
public class Camera {
    
    private float x = 0;
    private float y = 0;
    
    public void set(float x, float y) {
        this.x=x;
        this.y=y;
    }
    
    public void move(float x, float y) {
        this.x+=x;
        this.y+=y;
    }
    
    public float getX() { return x; }
    public float getY() { return y; }
}

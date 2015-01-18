/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tag_project;

import bropals.lib.simplegame.entity.GameWorld;
import bropals.lib.simplegame.entity.block.BlockEntity;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author Owner
 */
public class NavigationNode extends BlockEntity {
    
    private float gVal, moveCost, hVal;
    private int xGrid, yGrid; // the position in the giant array
    private NavigationNode navParent;
    
    public NavigationNode(GameWorld forWorld, float x, float y, float size, int xGrid, int yGrid) {
        super(null, x, y, size, size, true);
        moveCost = 1;
        for (int i=0; i<forWorld.getEntities().size(); i++) {
            if (forWorld.getEntities().get(i) instanceof DecorationEntity ||
                 forWorld.getEntities().get(i) instanceof BiscuitEntity   )
                continue; // don't care about those
            
            if (forWorld.getEntities().get(i) instanceof IsometricEntity) {
                IsometricEntity iso = (IsometricEntity) forWorld.getEntities().get(i);
                if (iso.toRect().intersects(toRect())) { // if the intersection is more than 2/3 the size
                    moveCost = 10000000;
                    return;
                }
            }
        }
        gVal = 0; // initially
        navParent = null;
        this.xGrid = xGrid;
        this.yGrid = yGrid;
    }

    public int getXGrid() {
        return xGrid;
    }
    
    public int getYGrid() {
        return yGrid;
    }
    
    @Override
    public void setX(float x) {
        // no
    }
    
    @Override
    public void setY(float y) {
        // no
    }
    
    /**
     * Set the heuristic estimation value
     * @param hv The heuristic estimation value
     */
    public void sethVal(float hv) {
        hVal = hv;
    }
    
    public float gethVal() {
        return hVal;
    }
    
    public float getgVal() {
        return gVal;
    }

    /**
     * Set the TOTAL distance needed to travel to get to this node
     * @param gVal The total distance needed to travel her
     */
    public void setgVal(float gVal) {
        this.gVal = gVal;
    }

    /**
     * The combined value of this node
     * @return This node's g and h value added together, giving the value.
     */
    public float getfVal() {
        return gVal + hVal;
    }

    public float getMoveCost() {
        return moveCost;
    }

    public NavigationNode getNavParent() {
        return navParent;
    }

    public void setNavParent(NavigationNode navParent) {
        this.navParent = navParent;
    }
    
    public Rectangle toRect() {
        return new Rectangle((int)getX(), (int)getY(), (int)getWidth(), (int)getHeight());
    }
}

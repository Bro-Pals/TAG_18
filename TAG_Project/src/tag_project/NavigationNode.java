/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tag_project;

import bropals.lib.simplegame.entity.GameWorld;
import bropals.lib.simplegame.entity.block.BlockEntity;
import java.awt.Rectangle;

/**
 *
 * @author Owner
 */
public class NavigationNode extends BlockEntity {
    
    private float gVal, fVal, moveCost;
    private NavigationNode navParent;
    
    public NavigationNode(GameWorld forWorld, float x, float y, float size) {
        super(null, x, y, size, size, true);
        moveCost = 1;
        for (int i=0; i<forWorld.getEntities().size(); i++) {
            if (forWorld.getEntities().get(i) instanceof DecorationEntity)
                continue; // don't care about those
            
            if (forWorld.getEntities().get(i) instanceof IsometricEntity) {
                IsometricEntity iso = (IsometricEntity) forWorld.getEntities().get(i);
                if (iso.toRect().intersects(toRect())) {
                    moveCost = 10000000;
                    return;
                }
            }
        }
        gVal = 0; // initially
        fVal = 0; // initially
        navParent = null;
    }

    @Override
    public void setX(float x) {
        // no
    }
    
    @Override
    public void setY(float y) {
        // no
    }
    
    public float getgVal() {
        return gVal;
    }

    public void setgVal(float gVal) {
        this.gVal = gVal;
    }

    public float getfVal() {
        return fVal;
    }

    public void setfVal(float fVal) {
        this.fVal = fVal;
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

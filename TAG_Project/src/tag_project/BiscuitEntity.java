/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tag_project;

import bropals.lib.simplegame.entity.GameWorld;
import java.awt.image.BufferedImage;

/**
 *
 * @author Jonathon
 */
public class BiscuitEntity extends IsometricEntity {

    public BiscuitEntity(GameWorld parent, float x, float y) {
        super(parent, x, y, 40, 40, true, IsometricDirection.SOUTH, null, 
                parent.getState().getAssetManager().getImage("biscuitSprite"), 
                null, null);
    }
    
}

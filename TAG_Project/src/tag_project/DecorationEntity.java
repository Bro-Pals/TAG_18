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
public class DecorationEntity extends IsometricEntity {

    public DecorationEntity(GameWorld parent, float x, float y, float width, float height, BufferedImage south) {
        super(parent, x, y, width, height, true, IsometricDirection.SOUTH, null, south, null, null);
        setCollidable(false);
    }
    
}

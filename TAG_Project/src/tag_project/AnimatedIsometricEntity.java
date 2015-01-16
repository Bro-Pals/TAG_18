/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tag_project;

import bropals.lib.simplegame.animation.Animation;
import bropals.lib.simplegame.entity.GameWorld;
import bropals.lib.simplegame.logger.InfoLogger;
import java.awt.image.BufferedImage;

/**
 *
 * @author Owner
 */
public class AnimatedIsometricEntity extends IsometricEntity {

    private Animation animation;
    
    public AnimatedIsometricEntity(GameWorld parent, float x, float y, float width, 
            float height, boolean anchored, IsometricDirection facing, 
            BufferedImage north, BufferedImage south, BufferedImage east, 
            BufferedImage west, Animation anim) {
        super(parent, x, y, width, height, anchored, facing, north, south, east, west);
        this.animation = anim; // the animation
        animation.setTrack(0);
    }
    
    @Override
    public void update() {
        animation.update();
        switch(getFacing()) {
            case NORTH: // animation track 0
                break;
            case SOUTH: // animation track 1 (0 for now)
                if (animation.getCurrentTrackIndex() != 0)
                    animation.setTrack(0); // change to the south animaton
                
                if (getSouth() != animation.getCurrentImage())
                    setSouth(animation.getCurrentImage());
                break;
            case EAST: // animation track 2
                break;
            case WEST: // animation track 3
                break;
        }
        super.update();
    }
    
}

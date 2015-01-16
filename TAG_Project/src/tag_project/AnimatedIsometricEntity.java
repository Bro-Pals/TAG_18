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
        setFacing(IsometricDirection.NORTH);
    }
    
    @Override
    public void update() {
        animation.update();
        if (getVelocity().getY() > 0) {
            setFacing(IsometricDirection.SOUTH);   
        } else if (getVelocity().getY() < 0) {
            setFacing(IsometricDirection.NORTH);
        } else if (getVelocity().getX() > 0) {
            setFacing(IsometricDirection.WEST);
        } else if (getVelocity().getX() < 0) {
            setFacing(IsometricDirection.EAST);
        }
        switch(getFacing()) {
            case NORTH: // animation track 0
                if (animation.getCurrentTrackIndex() != 0)
                    animation.setTrack(0); // change to the north animaton
                
                if (getNorth() != animation.getCurrentImage())
                    setNorth(animation.getCurrentImage());
                break;
            case SOUTH: // animation track 1 (0 for now)
                if (animation.getCurrentTrackIndex() != 1)
                    animation.setTrack(1); // change to the south animaton
                
                if (getSouth() != animation.getCurrentImage())
                    setSouth(animation.getCurrentImage());
                break;
            case EAST: // animation track 2
                if (animation.getCurrentTrackIndex() != 2)
                    animation.setTrack(2); // change to the south animaton
                
                if (getEast() != animation.getCurrentImage())
                    setEast(animation.getCurrentImage());
                break;
            case WEST: // animation track 3
                if (animation.getCurrentTrackIndex() != 3)
                    animation.setTrack(3); // change to the south animaton
                
                if (getWest() != animation.getCurrentImage())
                    setWest(animation.getCurrentImage());
                break;
        }
        super.update();
    }
    
}

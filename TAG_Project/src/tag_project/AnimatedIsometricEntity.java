/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tag_project;

import bropals.lib.simplegame.animation.Animation;
import bropals.lib.simplegame.entity.GameWorld;
import bropals.lib.simplegame.logger.ErrorLogger;
import bropals.lib.simplegame.logger.InfoLogger;
import java.awt.image.BufferedImage;

/**
 *
 * @author Owner
 */
public class AnimatedIsometricEntity extends IsometricEntity {

    private Animation animation;
    private NavigationNode goal;
    private NavigationNode[] pathToGetThere;
            
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
        if (getVelocity().getY() > 0 && getVelocity().getY() > getVelocity().getX()) {
            setFacing(IsometricDirection.SOUTH);   
        } else if (getVelocity().getY() < 0 && getVelocity().getY() < getVelocity().getX()) {
            setFacing(IsometricDirection.NORTH);
        } else if (getVelocity().getX() > 0 && getVelocity().getY() < getVelocity().getX()) {
            setFacing(IsometricDirection.WEST);
        } else if (getVelocity().getX() < 0 && getVelocity().getY() > getVelocity().getX()) {
            setFacing(IsometricDirection.EAST);
        }
        boolean notMoving = getVelocity().getX() == 0 && 
                getVelocity().getY() == 0;
        switch(getFacing()) {
            case NORTH: // animation track 0
                if (notMoving) {
                    if (animation.getCurrentTrackIndex() != 4)
                        animation.setTrack(4); // change to the north animaton
                } else {
                    if (animation.getCurrentTrackIndex() != 0)
                        animation.setTrack(0); // change to the north animaton
                }
                if (getNorth() != animation.getCurrentImage())
                    setNorth(animation.getCurrentImage());
                break;
            case SOUTH: // animation track 1 (0 for now)
                if (notMoving) {
                    if (animation.getCurrentTrackIndex() != 5)
                        animation.setTrack(5);
                } else {
                    if (animation.getCurrentTrackIndex() != 1)
                        animation.setTrack(1);
                }
                if (getSouth() != animation.getCurrentImage())
                    setSouth(animation.getCurrentImage());
                break;
            case EAST: // animation track 2
                if (notMoving) {
                    if (animation.getCurrentTrackIndex() != 6)
                        animation.setTrack(6);
                } else {
                    if (animation.getCurrentTrackIndex() != 2)
                        animation.setTrack(2);
                }
                if (getEast() != animation.getCurrentImage())
                    setEast(animation.getCurrentImage());
                break;
            case WEST: // animation track 3
                if (notMoving) {
                    if (animation.getCurrentTrackIndex() != 7)
                        animation.setTrack(7);
                } else {
                    if (animation.getCurrentTrackIndex() != 3)
                        animation.setTrack(3);
                }
                if (getWest() != animation.getCurrentImage())
                    setWest(animation.getCurrentImage());
                break;
        }
        super.update();
    }
    
    private void followPathToGetThere() {
        
    }
    
    private void pathfindTo(IsometricEntity other, NavigationNode[] nodes) {
        NavigationNode findGoal = null;
        for (NavigationNode navNode:nodes) {
            if (navNode.handleCollide(other)) {
                findGoal = navNode;
                break;
            }
        }
        if (findGoal == null) {
            ErrorLogger.println("The target is not inside the navigation nodes array");
            return;
        }
        
        // don't need to recalculate if we already have that as the goal
        if (goal == findGoal) {
            followPathToGetThere();
            return;
        }
        
        
        InfoLogger.println("Time to pathfind!");
    }
    
    /**
     * Chase another entity (the dog). If it can see the other entity, it will 
     * head straight towards them. Otherwise, it will pathfind and follow 
     * the path to where the other entity is. If the other entity is
     * in sight and close enough, this entity will attack the other entity.
     * 
     * The boy should have this called on him in the update loop
     * @param other The other entity (should be the dog)
     * @param nodes The navigation nodes to use to pathfind if it
     * @param speed The speed to chase the dog at
     * doesn't have line of sight of the other entity.
     */
    public void chase(IsometricEntity other, NavigationNode[] nodes, float speed) {
        if (canSee(other)) {
            setVelocityTowards(other.getX(), other.getY(), speed);
            if (distanceBetween(other) < 150) {
                InfoLogger.println("We just hit the other entity!");
            }
        } else {
            pathfindTo(other, nodes);
        }
    }
    
    /**
     * Gets the distance between this and a given entity. Measured from their centers
     * @param other The given entity
     * @return The distance between this and a given entity from their centers
     */
    public float distanceBetween(IsometricEntity other) {
        float diffX = other.getCenterX() - getCenterX();
        float diffY = other.getCenterY() - getCenterY();
        return (float)Math.sqrt((diffX*diffX)+(diffY*diffY));
    }
    
    public boolean canSee(IsometricEntity other) {
        for (int i=0; i<getParent().getEntities().size(); i++) {
            if (getParent().getEntities().get(i) instanceof IsometricEntity) {
                IsometricEntity isoent = (IsometricEntity)getParent().getEntities().get(i);
                if (isoent == this || isoent == other) // don't check yourself
                    continue;
                
                if (isoent instanceof DecorationEntity || isoent instanceof BiscuitEntity)
                    continue;
                
                if (isoent.toRect().intersectsLine((int)getCenterX(), (int)getCenterY(), 
                        (int)other.getCenterX(), (int)other.getCenterY())) {
                    return false;
                }
            }
        }
        return true;
    }
}

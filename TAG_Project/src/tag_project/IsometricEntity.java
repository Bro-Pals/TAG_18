/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tag_project;

import bropals.lib.simplegame.entity.GameWorld;
import bropals.lib.simplegame.entity.block.BlockEntity;
import java.awt.image.BufferedImage;

/**
 * Represents an isometric entity.
 * @author Jonathon
 */
public class IsometricEntity extends BlockEntity {

    private BufferedImage north, south, east, west;
    private IsometricDirection facing;
    
    public IsometricEntity(GameWorld parent,
            float x, float y, float width, float height,
            boolean anchored, IsometricDirection facing, 
            BufferedImage north, BufferedImage south, BufferedImage east,
            BufferedImage west) {
        super(parent, x, y, width, height, anchored);
        this.facing=facing;
        this.north=north;
        this.south=south;
        this.east=east;
        this.west=west;
    }

    /**
     * Gets the north drawn image
     * @return the north drawn image
     */
    public BufferedImage getNorth() {
        return north;
    }

    /**
     * Sets the north drawn image
     * @param north the north drawn image
     */
    public void setNorth(BufferedImage north) {
        this.north = north;
    }

    /**
     * Gets the south drawn image
     * @return the south drawn image
     */
    public BufferedImage getSouth() {
        return south;
    }

    /**
     * Sets the south drawn image
     * @param south the south drawn image
     */
    public void setSouth(BufferedImage south) {
        this.south = south;
    }

    /**
     * Gets the east drawn image
     * @return the east drawn image
     */
    public BufferedImage getEast() {
        return east;
    }

    /**
     * Sets the east drawn image
     * @param east the east drawn image
     */
    public void setEast(BufferedImage east) {
        this.east = east;
    }

    /**
     * Gets the west drawn image
     * @return the west drawn image
     */
    public BufferedImage getWest() {
        return west;
    }

    /**
     * Sets the west drawn image
     * @param west the west drawn image
     */
    public void setWest(BufferedImage west) {
        this.west = west;
    }

    /**
     * Gets the direction this entity is facing
     * @return the direction this entity is facing
     */
    public IsometricDirection getFacing() {
        return facing;
    }

    /**
     * Sets the direction this entity is facing
     * @param facing the direction this entity is facing
     */
    public void setFacing(IsometricDirection facing) {
        this.facing = facing;
    }
    
    
}

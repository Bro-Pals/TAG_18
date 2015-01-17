/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tag_project;

import bropals.lib.simplegame.entity.GameWorld;
import bropals.lib.simplegame.entity.block.BlockEntity;
import bropals.lib.simplegame.logger.InfoLogger;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * Represents an isometric entity.
 *
 * @author Jonathon
 */
public class IsometricEntity extends BlockEntity {

    private BufferedImage north, south, east, west;
    private IsometricDirection facing;
    private BufferedImage using = null;
    private final float localWidth;
    private final float localHeight;

    public IsometricEntity(GameWorld parent,
            float x, float y, float width, float height,
            boolean anchored, IsometricDirection facing,
            BufferedImage north, BufferedImage south, BufferedImage east,
            BufferedImage west) {
        super(parent, x, y, 0, 0, anchored);
        this.north = north;
        this.south = south;
        this.east = east;
        this.west = west;
        setFacing(facing);
        if (facing == IsometricDirection.NORTH || facing == IsometricDirection.SOUTH) {
            localWidth = width;
            localHeight = height;
        } else {
            localWidth = height;
            localHeight = width;
        }
        setWidth(localWidth);
        setHeight(localHeight);
    }

    /**
     * Gets the raw width of the furniture, unrotated.
     *
     * @return the raw width of the furniture, unrotated
     */
    public float getLocalWidth() {
        return localWidth;
    }

    /**
     * Gets the raw height of the furniture, unrotated.
     *
     * @return the raw height of the furniture, unrotated
     */
    public float getLocalHeight() {
        return localHeight;
    }

    /**
     * Gets the north drawn image
     *
     * @return the north drawn image
     */
    public BufferedImage getNorth() {
        return north;
    }

    /**
     * Sets the north drawn image
     *
     * @param north the north drawn image
     */
    public void setNorth(BufferedImage north) {
        this.north = north;
        revalidateImage();
    }

    /**
     * Gets the south drawn image
     *
     * @return the south drawn image
     */
    public BufferedImage getSouth() {
        return south;
    }

    /**
     * Sets the south drawn image
     *
     * @param south the south drawn image
     */
    public void setSouth(BufferedImage south) {
        this.south = south;
        revalidateImage();
    }

    /**
     * Gets the east drawn image
     *
     * @return the east drawn image
     */
    public BufferedImage getEast() {
        return east;
    }

    /**
     * Sets the east drawn image
     *
     * @param east the east drawn image
     */
    public void setEast(BufferedImage east) {
        this.east = east;
        revalidateImage();
    }

    /**
     * Gets the west drawn image
     *
     * @return the west drawn image
     */
    public BufferedImage getWest() {
        return west;
    }

    /**
     * Sets the west drawn image
     *
     * @param west the west drawn image
     */
    public void setWest(BufferedImage west) {
        this.west = west;
        revalidateImage();
    }

    /**
     * Gets the direction this entity is facing
     *
     * @return the direction this entity is facing
     */
    public IsometricDirection getFacing() {
        return facing;
    }

    /**
     * Sets the direction this entity is facing
     *
     * @param facing the direction this entity is facing
     */
    public void setFacing(IsometricDirection facing) {
        this.facing = facing;
        if (facing == IsometricDirection.NORTH || facing == IsometricDirection.SOUTH) {
            setWidth(localWidth);
            setHeight(localHeight);
        } else {
            setWidth(localHeight);
            setHeight(localWidth);
        }
        revalidateImage();
    }

    /**
     * Ensures that this IsometricEntity is using the correct image.
     */
    public void revalidateImage() {
        switch (facing) {
            case NORTH:
                using = north;
                break;
            case SOUTH:
                using = south;
                break;
            case EAST:
                using = east;
                break;
            case WEST:
                using = west;
                break;
        }
    }

    @Override
    public void setY(float y) {
        super.setY(y);
        if (getParent() != null) {
            ((IsometricGameWorld) getParent()).reorderEntity(this);
        }
    }

    /**
     * Gets what image is being used currently
     *
     * @return the currently used image
     */
    public BufferedImage getUsing() {
        return using;
    }

    /**
     * Sets what image this Isometric entity will use to draw
     *
     * @param using what image is being used
     */
    public void setUsing(BufferedImage using) {
        this.using = using;
    }

    @Override
    public void render(Object graphicsObj) {
        Graphics g = (Graphics) graphicsObj;
        //80 y -> +40y -70x
        //80 x -> +40y +70x
        
        float renderX = 70 * ((getY()/80) - (getX()/80));
        float renderY = 40 * ((getX()/80) + (getY()/80));
        InfoLogger.println("renderX: " + renderX + "  renderY: " + renderY);
        InfoLogger.println("x: " + getX() + "  y: " + getY());
        g.drawImage(using, (int) (renderX - getCamera().getX()), 
                (int) (renderY - getCamera().getY()), null);
    }

    public Camera getCamera() {
        return ((HouseState) getParent().getState()).getCamera();
    }
}

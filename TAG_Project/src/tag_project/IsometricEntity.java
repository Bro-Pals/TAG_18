/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tag_project;

import bropals.lib.simplegame.entity.GameWorld;
import bropals.lib.simplegame.entity.block.BlockEntity;
import bropals.lib.simplegame.logger.InfoLogger;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * Represents an isometric entity.
 *
 * @author Jonathon
 */
public class IsometricEntity extends BlockEntity {

    private BufferedImage north, south, east, west;
    private IsometricDirection facing;
    private BufferedImage using = null, drawnImage = null;;
    private final float localWidth;
    private final float localHeight;
    private float renderX, renderY;
    private final float ISO_ANGLE = (float)Math.atan(69.5/40);

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
        if (facing == IsometricDirection.NORTH || facing == IsometricDirection.SOUTH) {
            localWidth = width;
            localHeight = height;
        } else {
            localWidth = height;
            localHeight = width;
        }
        setWidth(localWidth);
        setHeight(localHeight);
        renderX = 0;
        renderY = 0;
        setFacing(facing);
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
        if (!(this instanceof AnimatedIsometricEntity)) {
            rebuildDrawnImage();
        } else {
            drawnImage = using;
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

        if (drawnImage == null) {
            rebuildDrawnImage();
            System.out.println("drawImage is now " + drawnImage);
            return;
        }
        System.out.println("Y:" + getY());
        System.out.println("renderX: " + renderX);
        renderX = ((float)69.5 * ((getY()/80) - (getX()/80))) - 
                (float)(drawnImage.getWidth() * Math.sin(ISO_ANGLE));
        renderY = (40 * ((getX()/80) + (getY()/80))) - drawnImage.getHeight();
        //InfoLogger.println("renderX: " + renderX + "  renderY: " + renderY);
       // InfoLogger.println("x: " + getX() + "  y: " + getY());
        g.drawImage(drawnImage, (int) (renderX - getCamera().getX()), 
                (int) (renderY - getCamera().getY()), null);
        g.setColor(Color.RED);
        int cornerPosX = (int)(renderX - 3 - getCamera().getX() + (float)(drawnImage.getWidth() * Math.sin(ISO_ANGLE)));
        int cornerPosY = (int)(renderY - 3 - getCamera().getY() + drawnImage.getHeight());
        g.fillRect(cornerPosX, cornerPosY, 6, 6);
        g.drawString("(" + getX() + ", " + getY() + ")", cornerPosX + 10, cornerPosY);
        g.drawRect((int)renderX - (int)getCamera().getX(), 
                (int)renderY - (int)getCamera().getY(), (int)drawnImage.getWidth(), (int)drawnImage.getHeight());
    }

    private void rebuildDrawnImage() {
        // make a tiled image for isometric drawing
        if (getLocalWidth() == 0 || getLocalHeight() == 0) {
            InfoLogger.println("This ain't no good size");
            return; // :(
        }
        float width = (float)((getHeight()* Math.sin(ISO_ANGLE)) + 
            (getWidth() * Math.sin(ISO_ANGLE)));
        float height = 120 + (float)((getHeight() * Math.cos(ISO_ANGLE)) + 
            (getWidth() * Math.cos(ISO_ANGLE)));
        //System.out.println("Width: " + width + "  Height: " + height);
        drawnImage = new BufferedImage((int)width, (int)height, BufferedImage.TRANSLUCENT);
        Graphics2D g2img = (Graphics2D) drawnImage.getGraphics();
        float basePointX = (float)(Math.sin(ISO_ANGLE) * getWidth());
        float basePointY = 200f;
        int numTilesDrawn = 0;
        for (float h=0; h < (getHeight()/80); h++) {
            for (float w=0; w < (getWidth()/80); w++) {
                double xPos = basePointX - (69.5 * w) + (69.5 * h);
                double yPos = basePointY + (40 * w) + (40 * h);
                g2img.drawImage(using, (int)(xPos - 69.5), 
                        (int)(yPos - 200), null);
                numTilesDrawn++;
            }
        }
        System.out.println("Tiles drawn: " + numTilesDrawn);
        //System.out.println("Rebuilt an image of size: " + width + ", " + height);
        //System.out.println(drawnImage);
    }
    
    public Camera getCamera() {
        return ((HouseState) getParent().getState()).getCamera();
    }
    
    public float getRenderX() {
        return renderX;
    }
    
    public float getRenderY() {
        return renderY;
    }
}

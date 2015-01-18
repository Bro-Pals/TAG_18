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
    private float renderCoordX, renderCoordY;
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
        renderCoordX = 0;
        renderCoordY = 0;
        setFacing(facing);
    }

    @Override
    public void collideWith(BlockEntity other) {
        if (!(other instanceof BiscuitEntity)) {
            super.collideWith(other);
        }
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
        if (!(this instanceof AnimatedIsometricEntity) &&
                !(this instanceof FurnitureEntity)) {
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
    
    public void setDrawn(BufferedImage drawn) {
        drawnImage = using;
    }

    @Override
    public void render(Object graphicsObj) {
        Graphics g = (Graphics) graphicsObj;
        //80 y -> +40y -70x
        //80 x -> +40y +70x

        if (drawnImage == null && !(this instanceof FurnitureEntity)) {
            rebuildDrawnImage();
            System.out.println("drawImage is now " + drawnImage);
            return;
        }
        if (drawnImage == null) {
            this.revalidateImage();
            drawnImage = using;
        }
        if (drawnImage == null)
            return; // we give up on u drawnImage
        renderCoordX = ((float)69.5 * ((getY()/80) - (getX()/80)));
        renderCoordY = (40 * ((getX()/80) + (getY()/80)));
        int imagePosX = 0;
        int imagePosY = 0;
        if (!(this instanceof FurnitureEntity)) {
            imagePosX = (int) (renderCoordX - (Math.sin(ISO_ANGLE) * getWidth()) - getCamera().getX());
            imagePosY = (int) (renderCoordY - getCamera().getY() - 120);
        } else {
            // find teh bottom right of the image
            float bottomRightX = renderCoordX - 
                    (float)((Math.sin(ISO_ANGLE) * getWidth()));
            float bottomRightY = renderCoordY + 
                    (float)(Math.cos(ISO_ANGLE) * getHeight())
                   + (float)(Math.cos(ISO_ANGLE) * getWidth());
            imagePosX = (int)(bottomRightX - getCamera().getX());
            imagePosY = (int)(bottomRightY - drawnImage.getHeight() - getCamera().getY());
        }   
        g.drawImage(drawnImage, imagePosX, imagePosY , null);
       
        g.setColor(Color.RED);
        int cornerPosX = (int)(renderCoordX - getCamera().getX()); // render coord local (0, 0) (NOT image coordinate)
        int cornerPosY = (int)(renderCoordY - getCamera().getY());
        // draw the renderCoord local (0, 0) point
        g.fillRect(cornerPosX, cornerPosY, 6, 6);
        // display it's world coordinates
        g.drawString("(" + getX() + ", " + getY() + ")", cornerPosX + 10, cornerPosY);
        // draw a box around it
        if (drawnImage != null) {
            g.drawRect(imagePosX, imagePosY, (int)drawnImage.getWidth(), 
                    (int)drawnImage.getHeight());
        }
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
    
    public float getRenderCoordX() {
        return renderCoordX;
    }
    
    public float getRenderCoordY() {
        return renderCoordY;
    }
}

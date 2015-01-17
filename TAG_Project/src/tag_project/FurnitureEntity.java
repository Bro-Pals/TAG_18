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
public class FurnitureEntity extends IsometricEntity {
    
    private final Size size;
    private final boolean defaceable;
    private final boolean moveUnderAble;
    private boolean defaced = false;
    private BufferedImage defacedNorth, defacedSouth, defacedEast, defacedWest;

    public FurnitureEntity(GameWorld parent, float x, float y, float width, float height, boolean anchored, IsometricDirection facing, BufferedImage northNormal, BufferedImage southNormal, BufferedImage eastNormal, BufferedImage westNormal, BufferedImage northDefaced, BufferedImage southDefaced, BufferedImage eastDefaced, BufferedImage westDefaced, Size size, boolean defaceable, boolean moveUnderAble) {
        super(parent, x, y, width, height, anchored, facing, northNormal, southNormal, eastNormal, westNormal);
        this.size = size;
        this.defaceable = defaceable;
        this.moveUnderAble = moveUnderAble;
        defacedNorth = northDefaced;
        defacedSouth = southDefaced;
        defacedEast = eastDefaced;
        defacedWest = westDefaced;
    }
    
    public void ripToShreds() {
        if (defaceable) {
            defaced = true;
            revalidateImage();
        }
    }

    public boolean isRippedToShreds() {
        return defaced;
    }
    
    public Size getSize() {
        return size;
    }

    public boolean isDefaceable() {
        return defaceable;
    }

    public boolean isMoveUnderAble() {
        return moveUnderAble;
    }

    public BufferedImage getDefacedNorth() {
        return defacedNorth;
    }

    public void setDefacedNorth(BufferedImage defacedNorth) {
        this.defacedNorth = defacedNorth;
        revalidateImage();
    }

    public BufferedImage getDefacedSouth() {
        return defacedSouth;
    }

    public void setDefacedSouth(BufferedImage defacedSouth) {
        this.defacedSouth = defacedSouth;
        revalidateImage();
    }

    public BufferedImage getDefacedEast() {
        return defacedEast;
    }

    public void setDefacedEast(BufferedImage defacedEast) {
        this.defacedEast = defacedEast;
        revalidateImage();
    }

    public BufferedImage getDefacedWest() {
        return defacedWest;
    }

    public void setDefacedWest(BufferedImage defacedWest) {
        this.defacedWest = defacedWest;
        revalidateImage();
    }
    
    @Override
    public void revalidateImage() {
        if (!defaced) {
            super.revalidateImage();
        } else {
            switch(getFacing()) {
                case NORTH: setUsing(defacedNorth); break;
                case SOUTH: setUsing(defacedSouth); break;
                case EAST: setUsing(defacedEast); break;
                case WEST: setUsing(defacedWest); break;
            }
        }
    }

    @Override
    public Object clone() {
        FurnitureEntity fe = new FurnitureEntity(
                getParent(), 
                getX(), getY(), 
                getWidth(), 
                getHeight(), 
                isAnchored(), 
                getFacing(), 
                getNorth(), 
                getSouth(), 
                getEast(),
                getWest(), 
                defacedNorth,
                defacedSouth,
                defacedEast,
                defacedWest,
                getSize(),
                isDefaceable(),
                isMoveUnderAble()
        );
        fe.revalidateImage();
        return fe;
    }
}

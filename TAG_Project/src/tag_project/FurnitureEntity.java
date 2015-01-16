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

    public FurnitureEntity(GameWorld parent, float x, float y, float width, float height, boolean anchored, IsometricDirection facing, BufferedImage north, BufferedImage south, BufferedImage east, BufferedImage west, Size size, boolean defaceable, boolean moveUnderAble) {
        super(parent, x, y, width, height, anchored, facing, north, south, east, west);
        this.size = size;
        this.defaceable = defaceable;
        this.moveUnderAble = moveUnderAble;
    }
    
    public void ripToShreds() {
        defaced = false;
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
}

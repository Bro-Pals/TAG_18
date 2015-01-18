/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tag_project;

import bropals.lib.simplegame.animation.Animation;
import bropals.lib.simplegame.animation.Track;
import bropals.lib.simplegame.entity.GameWorld;
import bropals.lib.simplegame.entity.block.BlockEntity;
import java.awt.image.BufferedImage;

/**
 *
 * @author Jonathon
 */
public class BiscuitEntity extends IsometricEntity {
    
    private HouseState houseState;
    private Animation biscuitAnimation;
    
    public BiscuitEntity(GameWorld parent, HouseState houseState, float x, float y) {
        super(parent, x, y, 80, 80, true, IsometricDirection.SOUTH, null, 
                null, 
                null, null);
        this.houseState=houseState;
        setCollidable(false);
        setZ(-5);
        biscuitAnimation = new Animation();
        BufferedImage[] biscuitImages= new Track(houseState.getAssetManager().getImage("biscuitSprites"), 
                    139, 200, 8).getImages();
        biscuitAnimation.addTrack(
                new Track(new BufferedImage[]{
                    biscuitImages[0], biscuitImages[1], biscuitImages[2], biscuitImages[3],
                    biscuitImages[4], biscuitImages[5], biscuitImages[4], biscuitImages[3],
                    biscuitImages[2], biscuitImages[1]
                }, 5));
        biscuitAnimation.setTrack(0);
    }

    @Override
    public void update() {
        biscuitAnimation.update();
        setSouth(biscuitAnimation.getCurrentImage());
    }
    
    @Override
    public void collideWith(BlockEntity other) {
        if (houseState.isTheDog(other)) {
            houseState.biscuitCollected();
            removeParent();
        }
    }    

    @Override
    public Camera getCamera() {
        return houseState.getCamera();
    }
    
    
}

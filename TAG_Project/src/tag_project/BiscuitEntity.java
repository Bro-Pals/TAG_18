/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tag_project;

import bropals.lib.simplegame.animation.Animation;
import bropals.lib.simplegame.entity.GameWorld;
import bropals.lib.simplegame.entity.block.BlockEntity;

/**
 *
 * @author Jonathon
 */
public class BiscuitEntity extends IsometricEntity {
    
    private HouseState houseState;
    
    public BiscuitEntity(GameWorld parent, HouseState houseState, float x, float y) {
        super(parent, x, y, 80, 80, true, IsometricDirection.SOUTH, null, 
                null, 
                null, null);
        this.houseState=houseState;
        setSouth(houseState.getAssetManager().getImage("biscuitSprite"));
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

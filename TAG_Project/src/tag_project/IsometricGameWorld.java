/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tag_project;

import bropals.lib.simplegame.entity.GameWorld;
import bropals.lib.simplegame.state.GameState;

/**
 *
 * @author Jonathon
 */
public class IsometricGameWorld extends GameWorld<IsometricEntity> {

    public IsometricGameWorld(GameState stateIn) {
        super(stateIn);
    }

    @Override
    public void addEntity(IsometricEntity entity) {
        if (!getEntities().isEmpty()) {
            for (int i=0; i<getEntities().size(); i++) {
                if (getEntities().get(i).getY() >= entity.getY()) {
                    getEntities().add(i, entity);
                }
            }
        } else {
            super.addEntity(entity);
        }
    }
    
    public void reorderEntity(IsometricEntity entity) {
        getEntities().remove(entity);
        this.addEntity(entity);
    }
}

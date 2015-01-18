/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tag_project;

import bropals.lib.simplegame.animation.Animation;
import bropals.lib.simplegame.entity.GameWorld;
import bropals.lib.simplegame.logger.InfoLogger;
import bropals.lib.simplegame.state.GameState;
import java.util.List;

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
        /*
        if (entity instanceof FurnitureEntity) {
            if (!getEntities().isEmpty()) {
                for (int i=getEntities().size()-1; i > 0; i--) {
                    // they MUST be at the front
                    if (getEntities().get(i) instanceof DecorationEntity)
                        continue;

                    // only compare furniture with other furniture
                    if (getEntities().get(i) instanceof FurnitureEntity &&
                            !(entity instanceof FurnitureEntity))
                        continue;
                    
                    float depthValue = getEntities().get(i).getDepthValue();
                    if (depthValue >= entity.getDepthValue() &&
                            getEntities().get(i-1).getDepthValue() <= entity.getDepthValue()) {
                        if (entity.getY() == getEntities().get(i).getY()) {
                            if (entity.getX() < getEntities().get(i).getX()) {
                                getEntities().add(i-1, entity);
                            } else {
                                getEntities().add(i, entity);
                            }
                        } else {
                            getEntities().add(i, entity);
                        }
                        return;
                    }
                }
            }
            //go to back to draw last if it don't belong
            //getEntities().add(entity);
        }
        */
        addToWorldWithZSorting(entity);
    }
    
    public void addToWorldWithZSorting(IsometricEntity entity) {
        List<IsometricEntity> list = getEntities();
        if (!list.isEmpty()) {
            IsometricEntity last;
            for (int i=0; i<list.size(); i++) {
                last = list.get(i);
                if (entity.inFrontOf(last) || entity.getZ() == last.getZ()) {
                    entity.setParent(this);
                    list.add(i, entity);
                    return;
                }
            }
        }
        //Temporary
        entity.setParent(this);
        list.add(entity);
    }
    
    public void reorderEntity(IsometricEntity entity) {
        getEntities().remove(entity);
        this.addEntity(entity);
    }
}

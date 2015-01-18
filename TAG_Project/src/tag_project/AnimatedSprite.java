/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tag_project;

import bropals.lib.simplegame.animation.Animation;
import bropals.lib.simplegame.entity.GameWorld;
import bropals.lib.simplegame.util.Counter;
import bropals.lib.simplegame.util.CounterFunction;
import java.awt.image.BufferedImage;

/**
 *
 * @author Owner
 */
public class AnimatedSprite extends IsometricEntity implements CounterFunction {

    Animation animation;
    Counter lifeCounter;
    
    /**
     * Create an animated sprite with a limited lifespan. When it's been animating 
     * for it's framesDuration then it removes itself from its parent.
     * @param parent It's parent world
     * @param x It's x position in world coordinates
     * @param y It's y position in world coordinates
     * @param width It's width
     * @param height It's height
     * @param anim The animation that will play
     * @param framesDuration It's lifespan. If this is a negative number then it 
     * will loop endlessly
     */
    public AnimatedSprite(GameWorld parent, float x, float y, float width, float height, 
            Animation anim, int framesDuration) {
        super(parent, x, y, width, height, true, IsometricDirection.NORTH, 
                null, null, null, null);
        setCollidable(false);
        animation = anim;
        lifeCounter = new Counter(framesDuration, this);
        if (framesDuration < 0) {
            lifeCounter.setLooping(true);
        }
    }
    
    @Override
    public void update() {
        animation.update();
        setNorth(animation.getCurrentImage());
    }

    
    @Override
    public void countFinished() {
        removeParent(); // remove self when time is up
    }

    
}

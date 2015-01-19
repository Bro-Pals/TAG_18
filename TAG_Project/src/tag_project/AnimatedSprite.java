/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tag_project;

import bropals.lib.simplegame.animation.Animation;
import bropals.lib.simplegame.entity.GameWorld;
import bropals.lib.simplegame.logger.InfoLogger;
import bropals.lib.simplegame.util.Counter;
import bropals.lib.simplegame.util.CounterFunction;
import java.awt.Graphics2D;
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
            Animation anim, int framesDuration, float initialZ) {
        super(parent, x, y, width, height, true, IsometricDirection.NORTH, 
                null, null, null, null);
        setCollidable(false);
        animation = anim;
        lifeCounter = new Counter(framesDuration, this);
        if (framesDuration < 0) {
            lifeCounter.setLooping(true);
        }
        setZ(initialZ);
        InfoLogger.println("The parent of this shiny new sprite is : " + getParent());
    }
    
    @Override
    public void update() {
        animation.update();
        lifeCounter.update();
    }
    

    @Override
    public void render(Object graphicsObject) {
        Graphics2D g = (Graphics2D)graphicsObject;
//        System.out.println("I HAVE RENDERED!");
        if ( getParent() == null)
            InfoLogger.println("Could not find the parent");
           
        if (getParent() != null && getCamera() != null) {
            g.drawImage(animation.getCurrentImage(), 
                    (int)(getX() - getCamera().getX()), 
                    (int)(getY() - getCamera().getY()), null);
        }
    }
 
    
    @Override
    public void countFinished() {
        InfoLogger.println("Killed the sprite");
        removeParent(); // remove self when time is up
    }

    
}

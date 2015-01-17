/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tag_project;

import bropals.lib.simplegame.animation.Animation;
import bropals.lib.simplegame.animation.Track;
import bropals.lib.simplegame.entity.BaseEntity;
import bropals.lib.simplegame.entity.block.BlockEntity;
import bropals.lib.simplegame.gui.Gui;
import bropals.lib.simplegame.gui.GuiGroup;
import bropals.lib.simplegame.gui.GuiImage;
import bropals.lib.simplegame.gui.GuiText;
import bropals.lib.simplegame.logger.InfoLogger;
import bropals.lib.simplegame.state.EntityState;
import bropals.lib.simplegame.state.GameState;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import tag_project.factory.HouseLoader;

/**
 *
 * @author Jonathon
 */
public class HouseState extends GameState {

    private Gui gui;
    private IsometricGameWorld world;
    private boolean paused = false;
    private boolean movingUp, movingDown, movingRight, movingLeft;
    private GuiText biscuits, furniture;
    private Camera camera;
    
    /** Super special reference to the player (dog) */
    private AnimatedIsometricEntity dog;
    private float DOG_SPEED_DIAG = 2;
    private float DOG_SPEED = DOG_SPEED_DIAG * (float)Math.sqrt(2);
    
    ///Use R to swap rendering modes
    private boolean developmentRendering = true;

    @Override
    public void update() {
        
        // handle the key presses for dog moving
        if (movingUp && !movingDown) {
            if (dog.getVelocity().getX() == 0)
                dog.getVelocity().setY(-DOG_SPEED);
            else
                dog.getVelocity().setY(-DOG_SPEED_DIAG);
        } else if (movingDown && !movingUp) {
            if (dog.getVelocity().getX() == 0)
                dog.getVelocity().setY(DOG_SPEED);
            else
                dog.getVelocity().setY(DOG_SPEED_DIAG);
        } else if (!movingDown && !movingUp) {
            dog.getVelocity().setY(0);
        }
        if (movingRight && !movingLeft) {
            if (dog.getVelocity().getY() == 0)
                dog.getVelocity().setX(-DOG_SPEED);
            else
                dog.getVelocity().setX(-DOG_SPEED_DIAG);
        } else if (movingLeft && !movingRight) {
            if (dog.getVelocity().getY() == 0)
                dog.getVelocity().setX(DOG_SPEED);
            else
                dog.getVelocity().setX(DOG_SPEED_DIAG);
        } else if (!movingRight && !movingLeft) {
            dog.getVelocity().setX(0);
        }
        
        
        
        Point mp = getWindow().getMousePosition();
        if (!paused) {
            world.updateEntities();
            gui.update(mp.x, mp.y);
        }
    }

    @Override
    public void render(Object o) {
        Graphics g = (Graphics) o;
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWindow().getScreenWidth(), getWindow().getScreenHeight());
        if (!developmentRendering) {
            for (Object be : world.getEntities()) {
                ((BaseEntity) be).render(o);
            }
        } else {
            for (Object be : world.getEntities()) {
                BlockEntity block = ((BlockEntity) be);
                if (block instanceof FurnitureEntity) {
                    g.setColor(Color.RED);
                } else if (block instanceof BiscuitEntity) {
                    g.setColor(Color.ORANGE);
                } else if (block instanceof AnimatedIsometricEntity) {
                    g.setColor(Color.GREEN);
                } else if (block instanceof DecorationEntity) {
                    g.setColor(Color.WHITE);
                } else {
                    g.setColor(Color.BLUE);
                }
                int x = (int)(block.getX()-camera.getX());
                int y = (int)(block.getY()-camera.getY());
                int w = (int)block.getWidth();
                int h = (int)block.getHeight();
                g.fillRect(
                        x, 
                        y, 
                        w, h);
                //g.fillRect(300, 300, 80, 80);
            }
        }
        gui.render(o);
    }

    @Override
    public void onEnter() {
        movingUp = false;
        movingDown = false;
        movingRight = false;
        movingLeft = false;
        ((HouseLoader) (getAssetManager().getAssetLoader(IsometricGameWorld.class))).setHouseState(this);
        getAssetManager().loadAsset("assets/data/house.data", "The House", IsometricGameWorld.class);
        world = getAssetManager().getAsset("The House", IsometricGameWorld.class);

        camera = new Camera();

        gui = new Gui();
        initGUI();

        Animation dogAnimation = new Animation();
        BufferedImage[] dogImages = new Track(
                getAssetManager().getImage("spanielSprites"), 139, 200, 3).getImages();
        getAssetManager().createHorizontialFlipCopy(
                getAssetManager().getImage("spanielSprites"), "reverseSpanielSprites");
        BufferedImage[] dogImagesReverse = new Track(
                getAssetManager().getImage("reverseSpanielSprites"), 139, 200, 3).getImages();
        
        Track northMove = new Track(new BufferedImage[]{
            dogImages[7], dogImages[4], dogImages[5], dogImages[6],
            dogImages[5], dogImages[4]
        }, 2);
        Track eastMove = new Track(new BufferedImage[]{
            dogImagesReverse[4], dogImagesReverse[7], dogImagesReverse[6], dogImagesReverse[5],
            dogImagesReverse[6], dogImagesReverse[7]
        }, 2);
        Track eastStand = new Track(new BufferedImage[]{
            dogImages[8], dogImages[9]}, 5);
        Track northStand = new Track(new BufferedImage[]{
            dogImagesReverse[11], dogImagesReverse[10]}, 5);
        
        Track southMove = new Track(new BufferedImage[]{
            dogImages[3], dogImages[0], dogImages[1], dogImages[2],
            dogImages[1], dogImages[0]
        }, 2);
        Track westMove = new Track(new BufferedImage[]{
            dogImagesReverse[0], dogImagesReverse[3], dogImagesReverse[2], dogImagesReverse[1],
            dogImagesReverse[2], dogImagesReverse[3]
        }, 2);
        Track southStand = new Track(new BufferedImage[]{
            dogImages[8], dogImages[9]}, 5);
        Track westStand = new Track(new BufferedImage[]{
            dogImagesReverse[9], dogImagesReverse[8]}, 5);

        dogAnimation.addTrack(northMove); // 0
        dogAnimation.addTrack(southMove); // 1
        dogAnimation.addTrack(eastMove); // 2
        dogAnimation.addTrack(westMove); // 3
        
        dogAnimation.addTrack(northStand); // 4
        dogAnimation.addTrack(southStand); // 5
        dogAnimation.addTrack(eastStand); // 6
        dogAnimation.addTrack(westStand); // 7

        dog = new AnimatedIsometricEntity(world,
                150, 150, 80, 80, false, IsometricDirection.SOUTH,
                null, null, null, null, dogAnimation);
    }

    public Camera getCamera() {
        return camera;
    }

    /**
     * Reloads the house place. For development purposes.
     */
    private void reloadHousePlan() {
        getAssetManager().unloadAsset("The House", IsometricGameWorld.class);
        getAssetManager().loadAsset("assets/data/house.data", "The House", IsometricGameWorld.class);
        world = getAssetManager().getAsset("The House", IsometricGameWorld.class);
    }

    private void initGUI() {
        GuiGroup main = new GuiGroup();

        GuiImage biscuitIcon = new GuiImage(20, 20, 40, 40,
                getAssetManager().getImage("biscuitIcon"));
        biscuits = new GuiText("0", 70, 20, 80, 40, false);
        GuiImage furnitureIcon = new GuiImage(150, 20, 40, 40,
                getAssetManager().getImage("furnitureIcon"));
        furniture = new GuiText("0", 200, 20, 80, 40, false);

        main.addElement(biscuitIcon);
        main.addElement(biscuits);
        main.addElement(furnitureIcon);
        main.addElement(furniture);

        gui.addGroup("main", main);
        gui.enable("main");
    }

    @Override
    public void onExit() {

    }

    @Override
    public void key(KeyEvent ke, boolean pressed) {
        switch(ke.getKeyCode()) {
            case KeyEvent.VK_UP: movingUp = pressed; break;
            case KeyEvent.VK_DOWN: movingDown = pressed; break;
            case KeyEvent.VK_RIGHT: movingRight = pressed; break;
            case KeyEvent.VK_LEFT: movingLeft = pressed; break;
        }
        
        if (pressed) {            
            if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) {
                getWindow().requestToClose();
            }
            //Development controls; will remove
            else if (ke.getKeyCode() == KeyEvent.VK_G) {
                reloadHousePlan();
                InfoLogger.println("Reloaded house plan");
            } else if (ke.getKeyCode() == KeyEvent.VK_W) {
                camera.move(0, -100);
            } else if (ke.getKeyCode() == KeyEvent.VK_S) {
                camera.move(0, 100);
            } else if (ke.getKeyCode() == KeyEvent.VK_A) {
                camera.move(-100, 0);
            } else if (ke.getKeyCode() == KeyEvent.VK_D) {
                camera.move(100, 0);
            } else if ( ke.getKeyCode() == KeyEvent.VK_R ) {
                developmentRendering = !developmentRendering;
                if (developmentRendering) {
                    InfoLogger.println("Set to development rendering");
                } else {
                    InfoLogger.println("Set to game rendering");
                }
            }
        }
    }

    @Override
    public void mouse(MouseEvent me, boolean pressed) {
        if (pressed) {
            gui.mouseInput(me.getX(), me.getY());
        }
    }
}

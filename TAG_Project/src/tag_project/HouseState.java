/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tag_project;

import bropals.lib.simplegame.animation.Animation;
import bropals.lib.simplegame.animation.Track;
import bropals.lib.simplegame.entity.BaseEntity;
import bropals.lib.simplegame.entity.GameWorld;
import bropals.lib.simplegame.entity.block.TexturedBlock;
import bropals.lib.simplegame.gui.Gui;
import bropals.lib.simplegame.gui.GuiGroup;
import bropals.lib.simplegame.gui.GuiImage;
import bropals.lib.simplegame.gui.GuiText;
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
public class HouseState extends EntityState {

    private Gui gui;
    private IsometricGameWorld world;
    private boolean paused = false;
    private GuiText biscuits, furniture;
    private Camera camera;

    @Override
    public void update() {
        Point mp = getWindow().getMousePosition();
        if (paused) {
            gui.update(mp.x, mp.y);
        }
        super.update();
    }

    @Override
    public void render(Object o) {
        Graphics g = (Graphics) o;
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWindow().getScreenWidth(), getWindow().getScreenHeight());
        gui.render(o);
        for (Object be : getGameWorld().getEntities()) {
            ((BaseEntity) be).render(o);
        }
    }

    @Override
    public void onEnter() {
        ((HouseLoader) (getAssetManager().getAssetLoader(IsometricGameWorld.class))).setHouseState(this);
        getAssetManager().loadAsset("assets/data/house.data", "The House", IsometricGameWorld.class);
        world = getAssetManager().getAsset("The House", IsometricGameWorld.class);

        camera = new Camera();

        gui = new Gui();
        initGUI();

        Animation dogAnimation = new Animation();
        BufferedImage[] dogImages = new Track(
                getAssetManager().getImage("spanielSprites"), 139, 200, 3).getImages();
        Track southMove = new Track(new BufferedImage[]{
            dogImages[3], dogImages[0], dogImages[1], dogImages[2],
            dogImages[1], dogImages[0]
        }, 2);

        dogAnimation.addTrack(southMove);

        AnimatedIsometricEntity dog = new AnimatedIsometricEntity(getGameWorld(),
                300, 300, 10, 10, false, IsometricDirection.SOUTH,
                null, null, null, null, dogAnimation);
    }

    public Camera getCamera() {
        return camera;
    }

    /**
     * Reloads the house place. For development purposes.
     */
    private void reloadHousePlan() {
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
        if (pressed) {
            if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) {
                getWindow().requestToClose();
            } //Development controls; will remove
            else if (ke.getKeyCode() == KeyEvent.VK_G) {
                reloadHousePlan();
            } else if (ke.getKeyCode() == KeyEvent.VK_W) {
                camera.move(0, -100);
            } else if (ke.getKeyCode() == KeyEvent.VK_S) {
                camera.move(0, 100);
            } else if (ke.getKeyCode() == KeyEvent.VK_A) {
                camera.move(-100, 0);
            } else if (ke.getKeyCode() == KeyEvent.VK_D) {
                camera.move(100, 0);
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tag_project;

import bropals.lib.simplegame.animation.Animation;
import bropals.lib.simplegame.animation.Track;
import bropals.lib.simplegame.gui.GuiButton;
import bropals.lib.simplegame.gui.GuiButtonAction;
import bropals.lib.simplegame.gui.GuiGroup;
import bropals.lib.simplegame.gui.GuiImage;
import bropals.lib.simplegame.state.GameState;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 *
 * @author Jonathon
 */
public class MainMenuState extends GameState {

    private GuiGroup gui;
    private Animation spaniel;
    
    @Override
    public void update() {
        Point mp = getWindow().getMousePosition();
        gui.update(mp.x, mp.y);
        spaniel.update();
    }

    @Override
    public void render(Object graphicsObj) {
        gui.render(graphicsObj);
        ((Graphics)graphicsObj).drawImage(spaniel.getCurrentImage(), 440, 200, 278, 380, null);
    }

    @Override
    public void onEnter() {
        gui = new GuiGroup();
        gui.addElement(new GuiImage(0, 0, 800, 600, getAssetManager().getImage("mainMenuBackground")));
        gui.addElement(new GuiButton(80, 270, 323, 145, 
        getAssetManager().getImage("playDown"), getAssetManager().getImage("playUp"),
                getAssetManager().getImage("playDown"),
                new GuiButtonAction() {
                    @Override
                    public void onButtonPress() {
                        getGameStateRunner().setState(new HouseState());
                    }
                }));
        gui.addElement(new GuiButton(80, 425, 323, 145, 
        getAssetManager().getImage("quitDown"), getAssetManager().getImage("quitUp"),
                getAssetManager().getImage("quitDown"),
                new GuiButtonAction() {
                    @Override
                    public void onButtonPress() {
                        getWindow().requestToClose();
                    }
                }));
        gui.addElement(new GuiImage(470, 290, 200, 41, getImage("featuringImage")));
        spaniel = new Animation();
        Track t = new Track(getImage("menuSpaniel"), 139, 190);
        t.setFramesBetweenImages(23);
        spaniel.addTrack(t);
        spaniel.setTrack(0);
    }

    @Override
    public void onExit() {
    }

    @Override
    public void key(KeyEvent key, boolean pressed) {
    }

    @Override
    public void mouse(MouseEvent mouse, boolean pressed) {
        if (pressed) {
            gui.mouseInput(mouse.getX(), mouse.getY());
        }
    }
    
}

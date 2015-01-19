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
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 *
 * @author Owner
 */
public class HowToPlayState extends GameState {

    private GuiGroup gui;
    private Animation spaniel;
    private Animation boy;

    @Override
    public void update() {
        gui.update(getWindow().getMousePosition().x, getWindow().getMousePosition().y);
        spaniel.update();
        boy.update();
    }

    @Override
    public void render(Object graphicsObj) {
        Graphics2D g = (Graphics2D) graphicsObj;
        gui.render(g);
        g.drawImage(spaniel.getCurrentImage(), 140, 74, null);
        g.drawImage(boy.getCurrentImage(), 125, 375, null);
    }

    @Override
    public void onEnter() {
        gui = new GuiGroup();
        gui.addElement(new GuiImage(0, 0, 800, 600, getAssetManager().getImage("howToPlayBackground")));
        gui.addElement(new GuiButton(400, 450, 323, 145,
                getAssetManager().getImage("playDown"), getAssetManager().getImage("playUp"),
                getAssetManager().getImage("playDown"),
                new GuiButtonAction() {
                    @Override
                    public void onButtonPress() {
                        getAssetManager().getSoundEffect("click").play();
                        getGameStateRunner().setState(new HouseState());
                    }
                }));

        spaniel = new Animation();
        Track t = new Track(getImage("menuSpaniel"), 139, 190);
        t.setFramesBetweenImages(23);
        spaniel.addTrack(t);
        spaniel.setTrack(0);

        boy = new Animation();
        Track t2 = new Track(getImage("boyMenu"), 139, 190);
        t2.setFramesBetweenImages(5);
        boy.addTrack(t2);
        boy.setTrack(0);
    }

    @Override
    public void onExit() {
    }

    @Override
    public void key(KeyEvent arg0, boolean arg1) {
    }

    @Override
    public void mouse(MouseEvent mouse, boolean pressed) {
        if (pressed) {
            gui.mouseInput(mouse.getX(), mouse.getY());
        }
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tag_project;

import bropals.lib.simplegame.state.GameState;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

/**
 *
 * @author Jonathon
 */
public class WinState extends GameState {

    private BufferedImage win;
    
    @Override
    public void update() {
    }

    @Override
    public void render(Object graphicsObj) {
        ((Graphics)graphicsObj).drawImage(win, 0, 0, null);
    }

    @Override
    public void onEnter() {
        win = getAssetManager().getImage("winScreen");
    }

    @Override
    public void onExit() {
    }

    @Override
    public void key(KeyEvent arg0, boolean arg1) {
        if (arg1) {
            if (arg0.getKeyCode() == KeyEvent.VK_SPACE) {
                getGameStateRunner().setState(new MainMenuState());
            }
        }
    }

    @Override
    public void mouse(MouseEvent arg0, boolean arg1) {

    }

}

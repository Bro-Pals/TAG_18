/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tag_project;

import bropals.lib.simplegame.gui.GuiGroup;
import bropals.lib.simplegame.gui.GuiImage;
import bropals.lib.simplegame.state.GameState;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 *
 * @author Jonathon
 */
public class GameOverState extends GameState {

    private int biscuits;
    private int furniture;
    private GuiGroup gui;
    private Font font;
    
    public GameOverState(int biscuits, int furniture) {
        this.biscuits = biscuits;
        this.furniture = furniture;
    }
    
    @Override
    public void update() {
    }

    @Override
    public void render(Object graphicsObj) {
    }

    @Override
    public void onEnter() {
        gui = new GuiGroup();
        font = new Font(Font.SANS_SERIF, Font.BOLD, 20);
        gui.addElement(new GuiImage(0, 0, 800, 600, getImage("gameOverBackground")));
    }

    @Override
    public void onExit() {
    }

    @Override
    public void key(KeyEvent kr, boolean pressed) {
        if (kr.getKeyCode() == KeyEvent.VK_SPACE) {
            getGameStateRunner().setState(new MainMenuState());
        }
    }

    @Override
    public void mouse(MouseEvent m, boolean pressed) {
    }
    
}

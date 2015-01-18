/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tag_project;

import bropals.lib.simplegame.gui.GuiGroup;
import bropals.lib.simplegame.gui.GuiImage;
import bropals.lib.simplegame.gui.GuiText;
import bropals.lib.simplegame.state.GameState;
import java.awt.Font;
import java.awt.Graphics;
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
        Graphics g = (Graphics)graphicsObj;
        gui.render(graphicsObj);
    }

    @Override
    public void onEnter() {
        gui = new GuiGroup();
        font = new Font(Font.SANS_SERIF, Font.BOLD, 32);
        gui.addElement(new GuiImage(0, 0, 800, 600, getImage("gameOverBackground")));
        GuiText b, f;
        b = new GuiText("" + biscuits, 160, 380, 0, 0, false);
        b.setFont(font);
        f = new GuiText("" + furniture, 510, 380, 0, 0, false);
        f.setFont(font);
        gui.addElement(b);
        gui.addElement(f);
    }

    @Override
    public void onExit() {
    }

    @Override
    public void key(KeyEvent kr, boolean pressed) {
        if (kr.getKeyCode() == KeyEvent.VK_ESCAPE && pressed) {
            getGameStateRunner().setState(new MainMenuState());
        }
    }

    @Override
    public void mouse(MouseEvent m, boolean pressed) {
    }
    
}

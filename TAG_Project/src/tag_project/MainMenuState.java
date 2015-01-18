/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tag_project;

import bropals.lib.simplegame.gui.GuiGroup;
import bropals.lib.simplegame.state.GameState;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 *
 * @author Jonathon
 */
public class MainMenuState extends GameState {

    private GuiGroup gui;
    
    @Override
    public void update() {
        Point mp = getWindow().getMousePosition();
        gui.update(mp.x, mp.y);
    }

    @Override
    public void render(Object graphicsObj) {
        gui.render(graphicsObj);
    }

    @Override
    public void onEnter() {
        gui = new GuiGroup();
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

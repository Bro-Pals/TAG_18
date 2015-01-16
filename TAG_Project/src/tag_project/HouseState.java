/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tag_project;

import bropals.lib.simplegame.entity.GameWorld;
import bropals.lib.simplegame.gui.Gui;
import bropals.lib.simplegame.state.GameState;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 *
 * @author Jonathon
 */
public class HouseState extends GameState {

    private Gui gui;
    private GameWorld<IsometricEntity> world;
    
    
    @Override
    public void update() {
        
    }

    @Override
    public void render(Object o) {
        Graphics g = (Graphics)o;
    }

    @Override
    public void onEnter() {
        world = new GameWorld<>(this);
        gui = new Gui();
        initGUI();
    }
    
    private void initGUI() {
        
    }

    @Override
    public void onExit() {
        
    }

    @Override
    public void key(KeyEvent ke, boolean bln) {
    }

    @Override
    public void mouse(MouseEvent me, boolean bln) {
    }
}

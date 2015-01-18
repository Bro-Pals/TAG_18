/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tag_project;

import bropals.lib.simplegame.state.GameState;
import bropals.lib.simplegame.util.Counter;
import bropals.lib.simplegame.util.CounterFunction;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 *
 * @author Jonathon
 */
public class WinState extends GameState implements CounterFunction {

    private BufferedImage win, biscuitImage;
    private Color bg;
    private ArrayList<FallingBiscuit> biscuits;
    private Counter biscuitCounter;
    
    @Override
    public void update() {
        biscuitCounter.update();
        for (int i=0; i<biscuits.size(); i++) {
            FallingBiscuit fb = biscuits.get(i);
            fb.y += fb.vel;
            if (fb.y > getWindow().getScreenWidth()) {
                biscuits.remove(i);
                i--;
            }
        }
    }

    @Override
    public void render(Object graphicsObj) {
        Graphics g = (Graphics)graphicsObj;
        g.setColor(bg);
        g.fillRect(0, 0, getWindow().getScreenWidth(), getWindow().getScreenHeight());
        for (FallingBiscuit fb : biscuits) {
            g.drawImage(biscuitImage, (int)fb.x, (int)fb.y, null);
        }
        g.drawImage(win, 0, 0, null);
    }
    
    class FallingBiscuit {
        private float x;
        private float y;
        private float vel;
    }

    @Override
    public void onEnter() {
        win = getAssetManager().getImage("winBackground");
        biscuitImage = getAssetManager().getImage("biscuitsNoSparkle");
        bg = new Color(255, 243, 198);
        biscuits = new ArrayList<>();
        biscuitCounter = new Counter(3, true, this);
    }

    @Override
    public void onExit() {
    }

    @Override
    public void key(KeyEvent arg0, boolean arg1) {
        if (arg1) {
            if (arg0.getKeyCode() == KeyEvent.VK_ESCAPE) {
                getGameStateRunner().setState(new MainMenuState());
            }
        }
    }

    @Override
    public void mouse(MouseEvent arg0, boolean arg1) {

    }

    @Override
    public void countFinished() {
        int spot = 60 + (int)(((double) Math.random() * ((double)getWindow().getScreenWidth()-(double)250) ));
        FallingBiscuit fb = new FallingBiscuit();
        fb.vel = (float)(Math.random()*20) + 3;
        fb.x = spot;
        fb.y = -200;
        biscuits.add(fb);
    }

}

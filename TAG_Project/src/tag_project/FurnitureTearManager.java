/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tag_project;

import bropals.lib.simplegame.gui.GuiProgressBar;
import bropals.lib.simplegame.util.Counter;
import bropals.lib.simplegame.util.CounterFunction;
import java.awt.Color;

/**
 * Manages the values associated with tearing furniture
 * @author Jonathon
 */
public class FurnitureTearManager implements CounterFunction {

    private FurnitureEntity tearing = null;
    private final Counter tearCounter = new Counter(TAGProject.FPS, this);
    private GuiProgressBar guiTearBar;
    private HouseState houseState;
    
    public FurnitureTearManager(GuiProgressBar guiTearBar, HouseState houseState) {
        this.guiTearBar = guiTearBar;
        guiTearBar.setMaxValue(tearCounter.getTargetNumber());
        guiTearBar.setBackgroundColor(Color.WHITE);
        guiTearBar.setProgressBarColor(Color.GREEN);
        this.houseState = houseState;
    }
    
    @Override
    public void countFinished() {
        tearing.ripToShreds();
        houseState.finishedTearing();
        guiTearBar.resetProgress();
        tearing = null;
    }
    
    public void interruptTearing() {
        tearing = null;
        guiTearBar.resetProgress();
        houseState.getAssetManager().getSoundEffect("rippingToShreds").getRaw().stop();
    }
    
    public void startTearing(FurnitureEntity tear) {
        tearing = tear;
        tearCounter.reset();
        houseState.getAssetManager().getSoundEffect("rippingToShreds").play();
    }
    
    public boolean isTearing() {
        return tearing!=null;
    }
    
    public void updateTearing() {
        tearCounter.update();
        guiTearBar.increaseProgress(1);
    }
    
    
}

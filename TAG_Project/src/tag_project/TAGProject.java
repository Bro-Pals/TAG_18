/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tag_project;

import bropals.lib.simplegame.GameStateRunner;
import bropals.lib.simplegame.GameWindow;
import bropals.lib.simplegame.logger.InfoLogger;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.swing.JOptionPane;
import tag_project.factory.EntityFactory;
import tag_project.factory.HouseLoader;

/**
 *
 * @author Jonathon
 */
public class TAGProject {

    public static final String TITLE = "Gary Wenceworth XVIII wants to take revenge on his owner by defacing all the furniture in the house.";
    public static final int FPS = 30;
    
    public static GameStateRunner runner;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int response = JOptionPane.showConfirmDialog(null, "Run the game in fullscreen?", "Want to play in fullscreen?", JOptionPane.YES_NO_CANCEL_OPTION);
        boolean fs = false;
        if (response == JOptionPane.NO_OPTION) {
            fs = false;
        } else if (response == JOptionPane.YES_OPTION) {
            fs = true;
        } else {
            return;
        }
        runner = null;
        GameWindow window = null;
        try {
            window = new GameWindow(TITLE, 
                        800, 600, fs);
            runner = new GameStateRunner(window);
        } catch(Exception e) {
            GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(null);
            JOptionPane.showMessageDialog(null, "Error: " + e.toString(), 
                    "An error has occurred", JOptionPane.ERROR_MESSAGE);
            if (window != null)
                window.destroy();
            System.exit(0);
            return;
        }
        InfoLogger.setSilent(false);
        runner.setFps(FPS);
        ///Loaded the assets
        runner.getAssetManager().loadSoundEffectsInDirectories("assets/soundEffects", true);
        runner.getAssetManager().loadSoundEffect("assets/bad-street-food.wav", "music");
        runner.getAssetManager().loadImagesInDirectories("assets/img", true);
        window.setIcon(runner.getAssetManager().getAsset("crappyIcon", BufferedImage.class));
        ///Need to initialize data
        initData(runner);
        
        runner.getAssetManager().getSoundEffect("music").getRaw().addLineListener(new LineListener() {
            @Override
            public void update(LineEvent event) {
                if (event.getType() == LineEvent.Type.STOP) {
                    runner.getAssetManager().getSoundEffect("music").play();
                }
            }
        });
        runner.getAssetManager().getSoundEffect("music").play();
        
        runner.setState(new MainMenuState());
        runner.loop();
    }
    
    private static void initData(GameStateRunner runner) {
        EntityFactory.loadFurnitureData(runner.getAssetManager());
        runner.getAssetManager().addAssetLoader(new HouseLoader(runner.getAssetManager()), IsometricGameWorld.class);
    }
}

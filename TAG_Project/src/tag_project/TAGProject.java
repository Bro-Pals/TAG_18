/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tag_project;

import bropals.lib.simplegame.GameStateRunner;
import bropals.lib.simplegame.GameWindow;
import javax.swing.JOptionPane;

/**
 *
 * @author Jonathon
 */
public class TAGProject {

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
        GameStateRunner runner = new GameStateRunner(
                new GameWindow("Gary Wenceworth XVIII wants to take revenge on his owner by defacing (eating) all the furniture in the house. The teenage son of his master is trying to stop him (he is gigantic compared to gary)", 
                        800, 600, fs));
        runner.getAssetManager().loadImagesInDirectories("assets/img", true);
        runner.setState(new HouseState());
        runner.loop();
    }
    
}

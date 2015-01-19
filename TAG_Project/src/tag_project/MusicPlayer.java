/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tag_project;

import bropals.lib.simplegame.logger.ErrorLogger;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author Jonathon
 */
public class MusicPlayer implements LineListener {

    private SourceDataLine music;
    private AudioFormat af;

    public MusicPlayer() {
        try {
            AudioInputStream musicStream = AudioSystem.getAudioInputStream(
                    new File("assets/music/music.wav"));
            af = musicStream.getFormat();
            music = AudioSystem.getSourceDataLine(af);
            music.open(af);
            
        } catch (Exception ioe) {
            ErrorLogger.println("" + ioe);
        }
    }

    public void play() {
        music.start();
    }

    @Override
    public void update(LineEvent event) {
        if (LineEvent.Type.STOP == event.getType()) {
            music.close();
            try {
                music.open(af);
            } catch (Exception ioe) {
                ErrorLogger.println("" + ioe);
            }
        }
    }
}

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
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author Jonathon
 */
public class MusicPlayer implements LineListener {

    private SourceDataLine music;
    private TargetDataLine dataSource;
    private AudioFormat af;
    
    private boolean running = false;

    public MusicPlayer() {
        try {
            AudioInputStream musicStream = AudioSystem.getAudioInputStream(
                    new File("assets/bad-street-food.wav"));
            af = musicStream.getFormat();
            music = AudioSystem.getSourceDataLine(af);
            dataSource = AudioSystem.getTargetDataLine(af);
            dataSource.open(af);
            music.open(af);
            music.addLineListener(this);
            dataSource.addLineListener(this);
        } catch (Exception ioe) {
            ErrorLogger.println("" + ioe);
        }
    }

    public void play() {
        running = true;
        music.start();
        dataSource.start();
        byte[] data = new byte[100];
        while (running) {
            dataSource.read(data, 0, 100);
            music.write(data, 0, 100);
            music.drain();
        }
    }
    
    public void stop() {
        running = false;
    }

    @Override
    public void update(LineEvent event) {
        if (LineEvent.Type.STOP == event.getType()) {
            dataSource.flush();
            music.flush();
            this.play();
        }
    }
}

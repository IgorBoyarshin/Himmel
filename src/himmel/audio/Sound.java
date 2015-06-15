package himmel.audio;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALContext;
import org.lwjgl.openal.ALDevice;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.ALC11.*;
import static org.lwjgl.openal.Util.*;

/**
 * Created by Igor on 07-Jun-15.
 */
public class Sound {
    // data

    private int buffer;
    private int source;
    private WaveData waveFile;

    private boolean playing = false;

    public Sound(String path) {
        buffer = alGenBuffers();
        source = alGenSources();

        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File(path));
            waveFile = WaveData.create(ais);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            //copy to buffer
            alBufferData(buffer, waveFile.format, waveFile.data, waveFile.samplerate);
            checkALError();
        } finally {
            waveFile.dispose();
        }

        alSourcei(source, AL_BUFFER, buffer);
//        alSourcei(source, AL_LOOPING, AL_TRUE);
    }

    public boolean isPlaying() {
        return playing;
    }

    public void play() {
        if (!playing) {
            playing = true;
            alSourcePlay(source);
        }
    }

    public void stop() {
        if (playing) {
            playing = false;
            alSourceStop(source);
        }
    }

    public void destroy() {
        alDeleteSources(source);
        alDeleteBuffers(buffer);
    }
}

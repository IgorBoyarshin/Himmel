package himmel.audio;

import org.lwjgl.openal.ALContext;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Igor on 07-Jun-15.
 */
public class SoundManager {
    private Map<String, Sound> sounds;

    private ALContext context;

    public SoundManager() {
        sounds = new HashMap<>();

        context = ALContext.create(null, 48000, 60, false);
        context.makeCurrent();
    }

    public void addSound(String name, String path) {
        sounds.put(name, new Sound(path));
    }

    public void addSound(String name, Sound sound) {
        sounds.put(name, sound);
    }

    public void play(String name) {
        sounds.get(name).play();
    }

    public void stop(String name) {
        sounds.get(name).stop();
    }

    public Sound getSound(String name) {
        return sounds.get(name);
    }

    public void destroy() {
        for (String key : sounds.keySet()) {
            sounds.get(key).destroy();
        }

        context.getDevice().destroy();
        context.destroy();
    }
}

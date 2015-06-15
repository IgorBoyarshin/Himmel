package himmel.utils;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

/**
 * Created by Igor on 01-May-15.
 */
public class InputKeyboard extends GLFWKeyCallback {
    public static final int AMOUNT_OF_KEYS = 65536;

    public boolean[] keys = new boolean[AMOUNT_OF_KEYS];

    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
        keys[key] = action != GLFW.GLFW_RELEASE;
    }
}

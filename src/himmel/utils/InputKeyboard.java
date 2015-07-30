package himmel.utils;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

/**
 * Created by Igor on 01-May-15.
 */
public class InputKeyboard extends GLFWKeyCallback {
//    public static final int AMOUNT_OF_KEYS = 65536;
    public static final int AMOUNT_OF_KEYS = 350;

    public boolean[] keysPress = new boolean[AMOUNT_OF_KEYS];
    public boolean[] keysRepeat = new boolean[AMOUNT_OF_KEYS];

    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
        if (key >= AMOUNT_OF_KEYS || key < 0) {
            System.out.println("RUNTIME ERROR:");
            System.out.println("In InputKeyboard class:");
            System.out.println("Key " + key + " is our of bounds of the array");
            return;
        }

        switch(action) {
            case GLFW.GLFW_RELEASE:
                keysPress[key] = false;
                keysRepeat[key] = false;
                break;
            case GLFW.GLFW_PRESS:
                keysPress[key] = true;
                keysRepeat[key] = false;
                break;
            case GLFW.GLFW_REPEAT:
                keysPress[key] = true; // TODO: should this be false??
                keysRepeat[key] = true;
                break;
        }

//        keys[key] = action != GLFW.GLFW_RELEASE;

//        System.out.print("KEY " + key + " ");
//        System.out.println();
//        if (action == GLFW.GLFW_PRESS) {
//            System.out.println("press");
//        } else if (action == GLFW.GLFW_REPEAT) {
//            System.out.println("repeat");
//        } else {
//            System.out.println("up");
//        }
    }
}

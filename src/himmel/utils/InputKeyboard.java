package himmel.utils;

import himmel.log.Log;
import org.lwjgl.glfw.GLFW;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.glfw.GLFWKeyCallback;

/**
 * Created by Igor on 01-May-15.
 */
public class InputKeyboard extends GLFWKeyCallback {
    public static final int AMOUNT_OF_KEYS = 350;

    private boolean[] keysPress = new boolean[AMOUNT_OF_KEYS];
    private boolean[] keysRepeat = new boolean[AMOUNT_OF_KEYS];

    public static final int HIMMEL_KEY_SPACE = GLFW_KEY_SPACE;
    public static final int HIMMEL_KEY_APOSTROPHE = GLFW_KEY_APOSTROPHE;
    public static final int HIMMEL_KEY_COMMA = GLFW_KEY_COMMA;
    public static final int HIMMEL_KEY_MINUS = GLFW_KEY_MINUS;
    public static final int HIMMEL_KEY_PERIOD = GLFW_KEY_PERIOD;
    public static final int HIMMEL_KEY_SLASH = GLFW_KEY_SLASH;
    public static final int HIMMEL_KEY_0 = GLFW_KEY_0;
    public static final int HIMMEL_KEY_1 = GLFW_KEY_1;
    public static final int HIMMEL_KEY_2 = GLFW_KEY_2;
    public static final int HIMMEL_KEY_3 = GLFW_KEY_3;
    public static final int HIMMEL_KEY_4 = GLFW_KEY_4;
    public static final int HIMMEL_KEY_5 = GLFW_KEY_5;
    public static final int HIMMEL_KEY_6 = GLFW_KEY_6;
    public static final int HIMMEL_KEY_7 = GLFW_KEY_7;
    public static final int HIMMEL_KEY_8 = GLFW_KEY_8;
    public static final int HIMMEL_KEY_9 = GLFW_KEY_9;
    public static final int HIMMEL_KEY_SEMICOLON = GLFW_KEY_SEMICOLON;
    public static final int HIMMEL_KEY_EQUAL = GLFW_KEY_EQUAL;
    public static final int HIMMEL_KEY_A = GLFW_KEY_A;
    public static final int HIMMEL_KEY_B = GLFW_KEY_B;
    public static final int HIMMEL_KEY_C = GLFW_KEY_C;
    public static final int HIMMEL_KEY_D = GLFW_KEY_D;
    public static final int HIMMEL_KEY_E = GLFW_KEY_E;
    public static final int HIMMEL_KEY_F = GLFW_KEY_F;
    public static final int HIMMEL_KEY_G = GLFW_KEY_G;
    public static final int HIMMEL_KEY_H = GLFW_KEY_H;
    public static final int HIMMEL_KEY_I = GLFW_KEY_I;
    public static final int HIMMEL_KEY_J = GLFW_KEY_J;
    public static final int HIMMEL_KEY_K = GLFW_KEY_K;
    public static final int HIMMEL_KEY_L = GLFW_KEY_L;
    public static final int HIMMEL_KEY_M = GLFW_KEY_M;
    public static final int HIMMEL_KEY_N = GLFW_KEY_N;
    public static final int HIMMEL_KEY_O = GLFW_KEY_O;
    public static final int HIMMEL_KEY_P = GLFW_KEY_P;
    public static final int HIMMEL_KEY_Q = GLFW_KEY_Q;
    public static final int HIMMEL_KEY_R = GLFW_KEY_R;
    public static final int HIMMEL_KEY_S = GLFW_KEY_S;
    public static final int HIMMEL_KEY_T = GLFW_KEY_T;
    public static final int HIMMEL_KEY_U = GLFW_KEY_U;
    public static final int HIMMEL_KEY_V = GLFW_KEY_V;
    public static final int HIMMEL_KEY_W = GLFW_KEY_W;
    public static final int HIMMEL_KEY_X = GLFW_KEY_X;
    public static final int HIMMEL_KEY_Y = GLFW_KEY_Y;
    public static final int HIMMEL_KEY_Z = GLFW_KEY_Z;
    public static final int HIMMEL_KEY_LEFT_BRACKET = GLFW_KEY_LEFT_BRACKET;
    public static final int HIMMEL_KEY_BACKSLASH = GLFW_KEY_BACKSLASH;
    public static final int HIMMEL_KEY_RIGHT_BRACKET = GLFW_KEY_RIGHT_BRACKET;
    public static final int HIMMEL_KEY_GRAVE_ACCENT = GLFW_KEY_GRAVE_ACCENT;
    public static final int HIMMEL_KEY_WORLD_1 = GLFW_KEY_WORLD_1;
    public static final int HIMMEL_KEY_WORLD_2 = GLFW_KEY_WORLD_2;
    public static final int HIMMEL_KEY_ESCAPE = GLFW_KEY_ESCAPE;
    public static final int HIMMEL_KEY_ENTER = GLFW_KEY_ENTER;
    public static final int HIMMEL_KEY_TAB = GLFW_KEY_TAB;
    public static final int HIMMEL_KEY_BACKSPACE = GLFW_KEY_BACKSPACE;
    public static final int HIMMEL_KEY_INSERT = GLFW_KEY_INSERT;
    public static final int HIMMEL_KEY_DELETE = GLFW_KEY_DELETE;
    public static final int HIMMEL_KEY_RIGHT = GLFW_KEY_RIGHT;
    public static final int HIMMEL_KEY_LEFT = GLFW_KEY_LEFT;
    public static final int HIMMEL_KEY_DOWN = GLFW_KEY_DOWN;
    public static final int HIMMEL_KEY_UP = GLFW_KEY_UP;
    public static final int HIMMEL_KEY_PAGE_UP = GLFW_KEY_PAGE_UP;
    public static final int HIMMEL_KEY_PAGE_DOWN = GLFW_KEY_PAGE_DOWN;
    public static final int HIMMEL_KEY_HOME = GLFW_KEY_HOME;
    public static final int HIMMEL_KEY_END = GLFW_KEY_END;
    public static final int HIMMEL_KEY_CAPS_LOCK = GLFW_KEY_CAPS_LOCK;
    public static final int HIMMEL_KEY_SCROLL_LOCK = GLFW_KEY_SCROLL_LOCK;
    public static final int HIMMEL_KEY_NUM_LOCK = GLFW_KEY_NUM_LOCK;
    public static final int HIMMEL_KEY_PRINT_SCREEN = GLFW_KEY_PRINT_SCREEN;
    public static final int HIMMEL_KEY_PAUSE = GLFW_KEY_PAUSE;
    public static final int HIMMEL_KEY_F1 = GLFW_KEY_F1;
    public static final int HIMMEL_KEY_F2 = GLFW_KEY_F2;
    public static final int HIMMEL_KEY_F3 = GLFW_KEY_F3;
    public static final int HIMMEL_KEY_F4 = GLFW_KEY_F4;
    public static final int HIMMEL_KEY_F5 = GLFW_KEY_F5;
    public static final int HIMMEL_KEY_F6 = GLFW_KEY_F6;
    public static final int HIMMEL_KEY_F7 = GLFW_KEY_F7;
    public static final int HIMMEL_KEY_F8 = GLFW_KEY_F8;
    public static final int HIMMEL_KEY_F9 = GLFW_KEY_F9;
    public static final int HIMMEL_KEY_F10 = GLFW_KEY_F10;
    public static final int HIMMEL_KEY_F11 = GLFW_KEY_F11;
    public static final int HIMMEL_KEY_F12 = GLFW_KEY_F12;
    public static final int HIMMEL_KEY_F13 = GLFW_KEY_F13;
    public static final int HIMMEL_KEY_F14 = GLFW_KEY_F14;
    public static final int HIMMEL_KEY_F15 = GLFW_KEY_F15;
    public static final int HIMMEL_KEY_F16 = GLFW_KEY_F16;
    public static final int HIMMEL_KEY_F17 = GLFW_KEY_F17;
    public static final int HIMMEL_KEY_F18 = GLFW_KEY_F18;
    public static final int HIMMEL_KEY_F19 = GLFW_KEY_F19;
    public static final int HIMMEL_KEY_F20 = GLFW_KEY_F20;
    public static final int HIMMEL_KEY_F21 = GLFW_KEY_F21;
    public static final int HIMMEL_KEY_F22 = GLFW_KEY_F22;
    public static final int HIMMEL_KEY_F23 = GLFW_KEY_F23;
    public static final int HIMMEL_KEY_F24 = GLFW_KEY_F24;
    public static final int HIMMEL_KEY_F25 = GLFW_KEY_F25;
    public static final int HIMMEL_KEY_KP_0 = GLFW_KEY_KP_0;
    public static final int HIMMEL_KEY_KP_1 = GLFW_KEY_KP_1;
    public static final int HIMMEL_KEY_KP_2 = GLFW_KEY_KP_2;
    public static final int HIMMEL_KEY_KP_3 = GLFW_KEY_KP_3;
    public static final int HIMMEL_KEY_KP_4 = GLFW_KEY_KP_4;
    public static final int HIMMEL_KEY_KP_5 = GLFW_KEY_KP_5;
    public static final int HIMMEL_KEY_KP_6 = GLFW_KEY_KP_6;
    public static final int HIMMEL_KEY_KP_7 = GLFW_KEY_KP_7;
    public static final int HIMMEL_KEY_KP_8 = GLFW_KEY_KP_8;
    public static final int HIMMEL_KEY_KP_9 = GLFW_KEY_KP_9;
    public static final int HIMMEL_KEY_KP_DECIMAL = GLFW_KEY_KP_DECIMAL;
    public static final int HIMMEL_KEY_KP_DIVIDE = GLFW_KEY_KP_DIVIDE;
    public static final int HIMMEL_KEY_KP_MULTIPLY = GLFW_KEY_KP_MULTIPLY;
    public static final int HIMMEL_KEY_KP_SUBTRACT = GLFW_KEY_KP_SUBTRACT;
    public static final int HIMMEL_KEY_KP_ADD = GLFW_KEY_KP_ADD;
    public static final int HIMMEL_KEY_KP_ENTER = GLFW_KEY_KP_ENTER;
    public static final int HIMMEL_KEY_KP_EQUAL = GLFW_KEY_KP_EQUAL;
    public static final int HIMMEL_KEY_LEFT_SHIFT = GLFW_KEY_LEFT_SHIFT;
    public static final int HIMMEL_KEY_LEFT_CONTROL = GLFW_KEY_LEFT_CONTROL;
    public static final int HIMMEL_KEY_LEFT_ALT = GLFW_KEY_LEFT_ALT;
    public static final int HIMMEL_KEY_LEFT_SUPER = GLFW_KEY_LEFT_SUPER;
    public static final int HIMMEL_KEY_RIGHT_SHIFT = GLFW_KEY_RIGHT_SHIFT;
    public static final int HIMMEL_KEY_RIGHT_CONTROL = GLFW_KEY_RIGHT_CONTROL;
    public static final int HIMMEL_KEY_RIGHT_ALT = GLFW_KEY_RIGHT_ALT;
    public static final int HIMMEL_KEY_RIGHT_SUPER = GLFW_KEY_RIGHT_SUPER;
    public static final int HIMMEL_KEY_MENU = GLFW_KEY_MENU;
    public static final int HIMMEL_KEY_LAST = GLFW_KEY_LAST;

    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
        if (!isKeyInRange(key)) {
            Log.logError("<InputKeyboard.invoke>: key " + key + " is out of bounds.");
            return;
        }

        switch (action) {
            case GLFW.GLFW_RELEASE:
                keysPress[key] = false;
                keysRepeat[key] = false;
                break;
            case GLFW.GLFW_PRESS:
                keysPress[key] = true;
                keysRepeat[key] = false;
                break;
            case GLFW.GLFW_REPEAT:
                keysPress[key] = true;
                keysRepeat[key] = true;
                break;
        }
    }

    private boolean isKeyInRange(final int key) {
        return (key >= 0 && key < AMOUNT_OF_KEYS);
    }

    public boolean isKeyPressed(final int key) {
        return isKeyInRange(key) && keysPress[key];
    }

    public boolean isKeyRepeated(final int key) {
        return isKeyInRange(key) && keysRepeat[key];
    }
}

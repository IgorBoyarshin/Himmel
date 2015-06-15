package himmel.utils;

import org.lwjgl.glfw.GLFWCursorPosCallback;

import static org.lwjgl.glfw.GLFW.glfwSetCursorPos;

/**
 * Created by Igor on 01-May-15.
 */
public class InputMouse extends GLFWCursorPosCallback {
    public float dx, dy;

    public void reset(long window) {
        glfwSetCursorPos(window, 0.0d, 0.0d);
        dx = 0.0f;
        dy = 0.0f;
    }

    @Override
    public void invoke(long window, double xPos, double yPos) {
        dx = (float) xPos;
        dy = (float) yPos;
    }
}

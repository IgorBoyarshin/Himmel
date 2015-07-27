package himmel.utils;

import org.lwjgl.glfw.GLFWCursorPosCallback;

import static org.lwjgl.glfw.GLFW.glfwSetCursorPos;

/**
 * Created by Igor on 01-May-15.
 */
public class InputMouse extends GLFWCursorPosCallback {
    public float dx, dy;

    public void reset(long window, float middleX, float middleY) {
        glfwSetCursorPos(window, middleX, middleY);
        dx = middleX;
        dy = middleY;
    }

    @Override
    public void invoke(long window, double xPos, double yPos) {
        dx = (float) xPos;
        dy = (float) yPos;
    }
}

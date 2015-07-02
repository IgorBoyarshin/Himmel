package himmel.graphics;

import himmel.math.Vector4f;
import himmel.utils.InputKeyboard;
import himmel.utils.InputMouse;
import himmel.log.Log;
import himmel.math.Vector2f;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import java.nio.ByteBuffer;

import static org.lwjgl.glfw.Callbacks.glfwSetCallback;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

/**
 * Created by Igor on 24-Apr-15.
 */
public class Window {
    private final int WIDTH, HEIGHT;
    private final String TITLE;

    private long glfwWindow;

    private InputKeyboard keyboard;
    private InputMouse mouse;

    private final boolean RESIZABLE = false;
    private final int SWAP_INTERWAL;
    private final boolean LOG_INFO;

    public Window(String title, int width, int height, boolean vsync, boolean log) {
        this.TITLE = title;
        this.WIDTH = width;
        this.HEIGHT = height;
        this.SWAP_INTERWAL = vsync ? 1 : 0;
        this.LOG_INFO = log;

        if (LOG_INFO) {
            Log.logInfo("Starting Himmel");
        }

        if (!init()) {
            if (LOG_INFO) {
                Log.logError("Could not start the Himmel");
            }
            glfwTerminate();
            System.exit(0);
        }

        if (LOG_INFO) {
            Log.logInfo("OpenGL version: " + getOpenglVersion());
        }
    }

    public Window(String title, int width, int height) {
        this.TITLE = title;
        this.WIDTH = width;
        this.HEIGHT = height;
        this.SWAP_INTERWAL = 1;
        this.LOG_INFO = false;

        if (LOG_INFO) {
            Log.logInfo("Starting Himmel");
        }

        if (!init()) {
            if (LOG_INFO) {
                Log.logError("Could not start the Himmel");
            }
            glfwTerminate();
            System.exit(0);
        }

        if (LOG_INFO) {
            Log.logInfo("OpenGL version: " + getOpenglVersion());
        }
    }

    public void update() {
        glfwPollEvents();
        glfwSwapBuffers(glfwWindow);
    }

    public boolean isKeyDown(final int key) {
        if (key < 0 || key >= InputKeyboard.AMOUNT_OF_KEYS) {
            return false;
        }

        return keyboard.keys[key];
    }

    // Changed center
    public Vector2f getMousePos() {
        return new Vector2f(mouse.dx, HEIGHT - mouse.dy);
    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void terminate() {
        if (LOG_INFO) {
            Log.logInfo("Terminating Himmel");
        }

        glfwDestroyWindow(glfwWindow);
        glfwTerminate();
    }

    public boolean isClosed() {
        return glfwWindowShouldClose(glfwWindow) == GL_TRUE;
    }

    private boolean init() {
//        System.out.println("LWJGL " + Sys.getVersion());

        if (glfwInit() != GL11.GL_TRUE) {
            Log.logError("Unable to init GLFW");
            return false;
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, RESIZABLE ? GL_TRUE : GL_FALSE);

        glfwWindow = glfwCreateWindow(WIDTH, HEIGHT, TITLE, NULL, NULL);

        if (glfwWindow == NULL) {
            Log.logError("Unable to creat GLFW Window");
            return false;
        }

        keyboard = new InputKeyboard();
        mouse = new InputMouse();

        glfwSetCallback(glfwWindow, keyboard);
        glfwSetCursorPosCallback(glfwWindow, mouse);
//        glfwSetInputMode(glfwWindow, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        glfwSetCursorPos(glfwWindow, 0.0d, 0.0d);

        ByteBuffer vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(
                glfwWindow,
                (GLFWvidmode.width(vidmode) - WIDTH) / 2,
                (GLFWvidmode.height(vidmode) - HEIGHT) / 2
        );

        glfwMakeContextCurrent(glfwWindow);
        glfwSwapInterval(SWAP_INTERWAL);
        glfwShowWindow(glfwWindow);

        GLContext.createFromCurrent();

        glEnable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        clearColor(new Vector4f(0.0f, 0.0f, 0.0f, 1.0f));

        return true;
    }

    public void clearColor(Vector4f clearColor) {
        glClearColor(clearColor.x, clearColor.y, clearColor.z, clearColor.w);
    }

    public String getOpenglVersion() {
        return glGetString(GL_VERSION);
    }

    public int getWidth() {
        return WIDTH;
    }

    public int getHeight() {
        return HEIGHT;
    }

    public String getName() {
        return TITLE;
    }
}

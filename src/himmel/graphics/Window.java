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
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.system.MemoryUtil.*;

/**
 * Created by Igor on 24-Apr-15.
 */
public class Window {
    private long glfwWindow;

    private InputKeyboard keyboard;
    private InputMouse mouse;

    private int windowWidth, windowHeight;
    private final String title;
    private final boolean resizable = false;
    private final boolean fullscreen;
    private final int swapInterwal;
    private final AntiAliasing antiAliasing;
    private boolean wireframe;
    private boolean logInfo;
    private boolean cursonEnabled;
    private boolean cullBackFaces;

    public enum AntiAliasing {
        AA_OFF(1),
        AA_2X(2),
        AA_4X(4),
        AA_8X(8);

        final int code;

        AntiAliasing(final int code) {
            this.code = code;
        }
    }

    public Window(String title, int width, int height, AntiAliasing antiAliasing, boolean vsync, boolean fullscreen) {
        this.title = title;
        this.windowWidth = width;
        this.windowHeight = height;
        this.antiAliasing = antiAliasing;
        this.swapInterwal = vsync ? 1 : 0;
        this.fullscreen = fullscreen;
        this.wireframe = false;
        this.logInfo = false;
        this.cursonEnabled = false;
        this.cullBackFaces = false;

        if (logInfo) {
            Log.logInfo("<Window>: Initializing Himmel.");
        }

        if (!init()) {
            if (logInfo) {
                Log.logError("<Window>: Could not initialize the Himmel engine.");
                Log.logError("<Window>: Terminating.");
            }
            glfwTerminate();
            System.exit(0);
        }

        if (logInfo) {
            Log.logInfo("<Window>: OpenGL version: " + getOpenglVersion());
        }
    }

    public Window(String title, int width, int height) {
        this.title = title;
        this.windowWidth = width;
        this.windowHeight = height;
        this.antiAliasing = AntiAliasing.AA_OFF;
        this.swapInterwal = 0;
        this.fullscreen = false;
        this.wireframe = false;
        this.logInfo = false;
        this.cursonEnabled = false;
        this.cullBackFaces = false;

        if (logInfo) {
            Log.logInfo("<Window>: Initializing Himmel.");
        }

        if (!init()) {
            if (logInfo) {
                Log.logError("<Window>: Could not initizlize the Himmel engine.");
                Log.logError("<Window>: Terminating.");
            }
            glfwTerminate();
            System.exit(0);
        }

        if (logInfo) {
            Log.logInfo("<Window>: OpenGL version: " + getOpenglVersion());
        }
    }

    private boolean init() {
        if (glfwInit() != GL11.GL_TRUE) {
            Log.logError("<Window>: Unable to init GLFW.");
            return false;
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GL_TRUE);
        glfwWindowHint(GLFW_RESIZABLE, resizable ? GL_TRUE : GL_FALSE);
        glfwWindowHint(GLFW_SAMPLES, antiAliasing.code);

        ByteBuffer vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        final int monitorWidth = GLFWvidmode.width(vidmode);
        final int monitorHeight = GLFWvidmode.height(vidmode);
        if (fullscreen) {
            this.windowWidth = monitorWidth;
            this.windowHeight = monitorHeight;
        }

        glfwWindow = glfwCreateWindow(windowWidth, windowHeight, title,
                fullscreen ? glfwGetPrimaryMonitor() : NULL, NULL);

        if (glfwWindow == NULL) {
            Log.logError("<Window>: Unable to creat GLFW Window.");
            return false;
        }

        keyboard = new InputKeyboard();
        mouse = new InputMouse();

        glfwSetCallback(glfwWindow, keyboard);
        glfwSetCursorPosCallback(glfwWindow, mouse);
        setShowCursor(cursonEnabled);
        glfwSetCursorPos(glfwWindow, 0.0d, 0.0d);

        if (!fullscreen) {
            glfwSetWindowPos(
                    glfwWindow,
                    (monitorWidth - windowWidth) / 2,
                    (monitorHeight - windowHeight) / 2
            );
        }

        glfwMakeContextCurrent(glfwWindow);
        glfwSwapInterval(swapInterwal);
        glfwShowWindow(glfwWindow);

        GLContext.createFromCurrent();

        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LEQUAL);
        glEnable(GL_BLEND);
        glEnable(GL_MULTISAMPLE);
        if (cullBackFaces) {
            glEnable(GL_CULL_FACE);
            glCullFace(GL_BACK);
        }
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        setWireframe(wireframe);
        setClearColor(new Vector4f(0.0f, 0.0f, 0.0f, 1.0f));

        return true;
    }

    public double getTimeSinceLaunch() {
        return GLFW.glfwGetTime();
    }

    public void setWireframe(boolean wireframe) {
        this.wireframe = wireframe;

        if (this.wireframe) {
            glPolygonMode(GL_FRONT, GL_LINE);
            glPolygonMode(GL_BACK, GL_LINE);
        } else {
            glPolygonMode(GL_FRONT, GL_FILL);
            glPolygonMode(GL_BACK, GL_FILL);
        }
    }

    public void setShowCursor(boolean showCursor) {
        this.cursonEnabled = showCursor;

        glfwSetInputMode(glfwWindow, GLFW_CURSOR, cursonEnabled ? GLFW_CURSOR_NORMAL : GLFW_CURSOR_DISABLED);
    }

    public void setCullBackFaces(boolean cull) {
        this.cullBackFaces = cull;

        if (cullBackFaces) {
            glEnable(GL_CULL_FACE);
            glCullFace(GL_BACK);
        } else {
            glDisable(GL_CULL_FACE);
        }
    }

    public void setLogInfo(boolean logInfo) {
        this.logInfo = logInfo;
    }

    public void pollEvents() {
        glfwPollEvents();
    }

    public void swapBuffers() {
        glfwSwapBuffers(glfwWindow);
    }

    public boolean isKeyPressed(final int key) {
        return keyboard.isKeyPressed(key);
    }

    public boolean isKeyRepeated(final int key) {
        return keyboard.isKeyRepeated(key);
    }

    public Vector2f getMousePos() {
        return new Vector2f(mouse.dx, mouse.dy);
    }

    public void resetMousePos(float resetPosX, float resetPosY) {
        mouse.reset(glfwWindow, resetPosX, resetPosY);
    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void terminate() {
        if (logInfo) {
            Log.logInfo("<Window>: Terminating Himmel.");
        }

        glfwDestroyWindow(glfwWindow);
        glfwTerminate();
    }

    public boolean isClosed() {
        return glfwWindowShouldClose(glfwWindow) == GL_TRUE;
    }

    public void setClearColor(Vector4f clearColor) {
        glClearColor(clearColor.x, clearColor.y, clearColor.z, clearColor.w);
    }

    public String getOpenglVersion() {
        return glGetString(GL_VERSION);
    }

    public int getWidth() {
        return windowWidth;
    }

    public int getHeight() {
        return windowHeight;
    }

    public String getName() {
        return title;
    }
}

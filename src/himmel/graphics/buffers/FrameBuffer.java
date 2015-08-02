package himmel.graphics.buffers;

import himmel.graphics.Texture;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.GL_DEPTH_COMPONENT32;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.glFramebufferTexture;

/**
 * Created by Igor on 02-Aug-15.
 */
public class FrameBuffer {
    private final int WIDTH;
    private final int HEIGHT;

    private int frameBuffer;
    private Texture texture; // TODO: Mb better usual int id?
    private int depthBuffer;
    private boolean isDepthTexture;

    private final int mainFrameBufferWidth;
    private final int mainFrameBufferHeight;

    public FrameBuffer(final int width, final int height) {
        this.WIDTH = width;
        this.HEIGHT = height;
        this.isDepthTexture = false;

        // Getting the prev fbo resolution
        // Will use it when unbinding this one
        IntBuffer params = BufferUtils.createIntBuffer(4);
        glGetIntegerv(GL_VIEWPORT, params);
        mainFrameBufferWidth = params.get(2);
        mainFrameBufferHeight = params.get(3);

        init();
    }

    public FrameBuffer(final int width, final int height, boolean isDepthTexture) {
        this.WIDTH = width;
        this.HEIGHT = height;
        this.isDepthTexture = isDepthTexture;

        // Getting the prev fbo resolution
        // Will use it when unbinding this one
        IntBuffer params = BufferUtils.createIntBuffer(4);
        glGetIntegerv(GL_VIEWPORT, params);
        mainFrameBufferWidth = params.get(2);
        mainFrameBufferHeight = params.get(3);

        init();
    }

    private void init() {
        frameBuffer = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, frameBuffer);
        glDrawBuffer(GL_COLOR_ATTACHMENT0);
        texture = new Texture(createTextureAttachment(WIDTH, HEIGHT));
        depthBuffer = isDepthTexture ? createDepthTextureAttachment(WIDTH, HEIGHT) : createDepthBufferAttachment(WIDTH, HEIGHT);
        unbindFrameBuffer();
    }

    public void bindFrameBuffer() {
        glBindTexture(GL_TEXTURE_2D, 0); // TODO: do I need it?
        glBindFramebuffer(GL_FRAMEBUFFER, frameBuffer);
        glViewport(0, 0, WIDTH, HEIGHT);
    }

    public void unbindFrameBuffer() {
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        glViewport(0, 0, mainFrameBufferWidth, mainFrameBufferHeight);
    }

    public Texture getTexture() {
        return texture;
    }

    public int getDepthBuffer() {
        if (isDepthTexture) {
            return depthBuffer;
        }

        // An error
        System.err.println("<FrameBuffer>:");
        System.err.println("|| Accessing -depthTexture-, but there was has been a depth buffer created instead ||");
        System.err.println();

        return -1;
    }

    private int createTextureAttachment(int width, int height) {
        int texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, (ByteBuffer) null);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, texture, 0);
        return texture;
    }

    private int createDepthTextureAttachment(int width, int height) {
        int texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT32, width, height, 0, GL_DEPTH_COMPONENT, GL_FLOAT, (ByteBuffer) null);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glFramebufferTexture(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, texture, 0);
        return texture;
    }

    private int createDepthBufferAttachment(int width, int height) {
        int depthBuffer = glGenRenderbuffers();
        glBindRenderbuffer(GL_RENDERBUFFER, depthBuffer);
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, width, height);
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, depthBuffer);
        return depthBuffer;
    }

    public void terminate() {
        glDeleteFramebuffers(frameBuffer);
        glDeleteTextures(texture.getTID());
        texture = null; // TODO: is it required?
        glDeleteRenderbuffers(depthBuffer);
    }
}

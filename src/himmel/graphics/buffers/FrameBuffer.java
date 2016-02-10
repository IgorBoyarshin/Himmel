package himmel.graphics.buffers;

import himmel.graphics.textures.Texture2D;
import himmel.graphics.textures.TextureParameters;
import himmel.math.Vector2i;
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
    private final int width;
    private final int height;

    private int frameBufferId;
//    private int textureId;
    private Texture2D texture;
    private int depthBuffer;
    private boolean isDepthTexture;

    private final int mainFrameBufferWidth;
    private final int mainFrameBufferHeight;

    public FrameBuffer(final int width, final int height) {
        this.width = width;
        this.height = height;
        this.isDepthTexture = false;

        Vector2i dimensions = getViewportDimensions();
        mainFrameBufferWidth = dimensions.x;
        mainFrameBufferHeight = dimensions.y;

        init();
    }

    public FrameBuffer(final int width, final int height, boolean isDepthTexture) {
        this.width = width;
        this.height = height;
        this.isDepthTexture = isDepthTexture;

        Vector2i dimensions = getViewportDimensions();
        mainFrameBufferWidth = dimensions.x;
        mainFrameBufferHeight = dimensions.y;

        init();
    }

    private Vector2i getViewportDimensions() {
        IntBuffer params = BufferUtils.createIntBuffer(4);
        glGetIntegerv(GL_VIEWPORT, params);
        return new Vector2i(params.get(2), params.get(3));
    }

    private void init() {
        frameBufferId = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, frameBufferId);
        glDrawBuffer(GL_COLOR_ATTACHMENT0);
        texture = createTextureAttachment(width, height);
        depthBuffer = isDepthTexture ? createDepthTextureAttachment(width, height) : createDepthBufferAttachment(width, height);
        unbindFrameBuffer();
    }

    public void bindFrameBuffer() {
        glBindTexture(GL_TEXTURE_2D, 0); // TODO: do I need it?
        glBindFramebuffer(GL_FRAMEBUFFER, frameBufferId);
        glViewport(0, 0, width, height);
    }

    public void unbindFrameBuffer() {
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        glViewport(0, 0, mainFrameBufferWidth, mainFrameBufferHeight);
    }

    public int getTextureId() {
        return texture.getTID();
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

    private Texture2D createTextureAttachment(int width, int height) {
        Texture2D texture = new Texture2D(width, height, new TextureParameters(TextureParameters.ComponentType.RGB));

//        int texture = glGenTextures();
//        glBindTexture(GL_TEXTURE_2D, texture);
//        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, (ByteBuffer) null);
//        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
//        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, texture.getTID(), 0);
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
        glDeleteFramebuffers(frameBufferId);
        texture.destruct();
        glDeleteRenderbuffers(depthBuffer);
    }
}

package himmel.graphics.buffers;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL15.*;

/**
 * Created by Igor on 03-May-15.
 */
public class Buffer {
    private int bufferID;
    private int componentCount;

    public Buffer(float data[], int componentCount) {
        this.componentCount = componentCount;

        FloatBuffer bufferData = BufferUtils.createFloatBuffer(data.length);
        bufferData.put(data);
        bufferData.flip();

        bufferID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, bufferID);
        glBufferData(GL_ARRAY_BUFFER, bufferData, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public void destruct() {
        glDeleteBuffers(bufferID);
    }

    public void bind() {
        glBindBuffer(GL_ARRAY_BUFFER, bufferID);
    }

    public void unbind() {
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public int getComponentCount() {
        return componentCount;
    }
}

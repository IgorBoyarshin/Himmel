package himmel.graphics.buffers;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Igor on 03-May-15.
 */
public class IndexBuffer {
    private int bufferID;
    private int count;
    private final int type;

    public IndexBuffer(short[] data) {
        count = data.length;
        type = GL_UNSIGNED_SHORT;

        ShortBuffer bufferData = BufferUtils.createShortBuffer(data.length);
        bufferData.put(data);
        bufferData.flip();

        bufferID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, bufferID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, bufferData, GL_STATIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

//    public IndexBuffer(int[] data) {
//        count = data.length;
//        type = GL_UNSIGNED_INT;
//
//        IntBuffer bufferData = BufferUtils.createIntBuffer(data.length);
//        bufferData.put(data);
//        bufferData.flip();
//
//        bufferID = glGenBuffers();
//        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, bufferID);
//        glBufferData(GL_ELEMENT_ARRAY_BUFFER, bufferData, GL_STATIC_DRAW);
//        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
//    }

    public final int getType() {
        return type;
    }

    public void destruct() {
        glDeleteBuffers(bufferID);
    }

    public void bind() {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, bufferID);
    }

    public void unbind() {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    public int getCount() {
        return count;
    }
}

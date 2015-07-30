package himmel.graphics.buffers;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
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

    private final int BYTES_IN_SHORT = 2;
    private final int BYTES_IN_INT = 4;
    // TODO: determine the best multiplier. Usually it is around 3/2
//    private final int BUFFER_SIZE_BYTES = Short.MAX_VALUE * 3 * BYTES_IN_INT;
    private final int BUFFER_SIZE_BYTES;
    private ByteBuffer bufferData;

    private boolean filling;

//    public IndexBuffer(short[] data) {
//        count = data.length;
//        type = GL_UNSIGNED_SHORT;
//
//        bufferData = BufferUtils.createShortBuffer(data.length);
//        bufferData.put(data);
//        bufferData.flip();
//
//        bufferID = glGenBuffers();
//        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, bufferID);
//        glBufferData(GL_ELEMENT_ARRAY_BUFFER, bufferData, GL_STATIC_DRAW);
//        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
//    }

    public IndexBuffer(boolean isShort, boolean isDynamic) {
        type = isShort ? GL_UNSIGNED_SHORT : GL_UNSIGNED_INT;
        filling = false;

        BUFFER_SIZE_BYTES = 20 * Short.MAX_VALUE * 8 * 3 * (isShort ? BYTES_IN_SHORT : BYTES_IN_INT);

        bufferID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, bufferID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, BUFFER_SIZE_BYTES, null, isDynamic ? GL_DYNAMIC_DRAW : GL_STATIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    public void addShort(short index) {
        if (type == GL_UNSIGNED_SHORT) {
            bufferData.putShort(index);
        }
    }

    public void addInt(int index) {
        if (type == GL_UNSIGNED_INT) {
            bufferData.putInt(index);
        }
    }

    public void begin() {
        if (!filling) {
            filling = true;

            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, bufferID);
            bufferData = glMapBuffer(GL_ELEMENT_ARRAY_BUFFER, GL_WRITE_ONLY);
        }
    }

    public void end() {
        if (filling) {
            filling = false;

            glUnmapBuffer(GL_ELEMENT_ARRAY_BUFFER);
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        }
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
        if (!filling) {
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, bufferID);
        }
    }

    public void unbind() {
        if (!filling) {
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        }
    }

    public int getCount() {
        return count;
    }
}

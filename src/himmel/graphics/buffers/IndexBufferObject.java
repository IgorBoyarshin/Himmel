package himmel.graphics.buffers;

import org.lwjgl.BufferUtils;

import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Igor on 03-May-15.
 */
public class IndexBufferObject {

    // TODO: for now only half of the type range (short or int) is used

    private int bufferId;
    private int vertexCount; // Amount of elements
    private ElementType elementType;
    private final boolean dynamicDraw;

    public IndexBufferObject(short data[], boolean dynamicDraw) {
        this.dynamicDraw = dynamicDraw;
        this.elementType = ElementType.UNSIGNED_SHORT;
        vertexCount = data.length;

        bufferId = glGenBuffers();
        putData(data);
    }

    public IndexBufferObject(int data[], boolean dynamicDraw) {
        this.dynamicDraw = dynamicDraw;
        this.elementType = ElementType.UNSIGNED_INT;
        vertexCount = data.length;

        bufferId = glGenBuffers();
        putData(data);
    }

    public void putData(short data[]) {
        ShortBuffer bufferData = BufferUtils.createShortBuffer(data.length);
        bufferData.put(data);
        bufferData.flip();

        bind();
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, bufferData, dynamicDraw ? GL_DYNAMIC_DRAW : GL_STATIC_DRAW);
        unbind();
    }

    public void putData(int data[]) {
        IntBuffer bufferData = BufferUtils.createIntBuffer(data.length);
        bufferData.put(data);
        bufferData.flip();

        bind();
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, bufferData, dynamicDraw ? GL_DYNAMIC_DRAW : GL_STATIC_DRAW);
        unbind();
    }

    public final ElementType getType() {
        return elementType;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public void bind() {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, bufferId);
    }

    public void unbind() {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    public void destruct() {
        glDeleteBuffers(bufferId);
    }

    public enum ElementType {
        UNSIGNED_SHORT(2, GL_UNSIGNED_SHORT),
        UNSIGNED_INT(4, GL_UNSIGNED_INT);

        public final int sizeInBytes;
        public final int typeCode;

        ElementType(int sizeInBytes, int typeCode) {
            this.sizeInBytes = sizeInBytes;
            this.typeCode = typeCode;
        }
    }
}

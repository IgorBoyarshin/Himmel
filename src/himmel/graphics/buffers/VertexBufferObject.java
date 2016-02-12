package himmel.graphics.buffers;

import himmel.log.Log;
import himmel.utils.FloatArray;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL15.*;

/**
 * Created by Igor on 03-May-15.
 */
public class VertexBufferObject {

    // TODO: rename componentCount[]
    // TODO: there might be a better place for BYTES_IN_FLOAT

    private int bufferId;
    private int[] componentCount;
    private int bufferSizeInBytes;
    private final boolean dynamicDraw;
    private int vertexCount;

    public static final int BYTES_IN_FLOAT = 4;

    /**
     * Use this constructor if you want to store a single arrays in this buffer.
     */
    public VertexBufferObject(float[] data, int componentCount, boolean dynamicDraw) {
        this.dynamicDraw = dynamicDraw;

        bufferId = glGenBuffers();
        putData(data, componentCount);
    }

    /**
     * Use this constructor if you want to store multiple arrays in this buffer.
     * The data will be interleaved.
     */
    public VertexBufferObject(FloatArray[] data, int[] componentCount, boolean dynamicDraw) {
        this.dynamicDraw = dynamicDraw;

        bufferId = glGenBuffers();
        putData(data, componentCount);
    }

    /**
     * Resets the buffer and fills it with the given data.
     */
    public void putData(float[] data, int componentCount) {
        this.componentCount = new int[]{componentCount};
        this.vertexCount = data.length / componentCount;
        bufferSizeInBytes = data.length * BYTES_IN_FLOAT;

        FloatBuffer bufferData = BufferUtils.createFloatBuffer(data.length);
        bufferData.put(data);
        bufferData.flip();

        bind();
        glBufferData(GL_ARRAY_BUFFER, bufferData, dynamicDraw ? GL_DYNAMIC_DRAW : GL_STATIC_DRAW);
        unbind();
    }

    /**
     * Resets the buffer and fills it with the given data.
     * The data will be interleaved.
     */
    public void putData(FloatArray[] data, int[] componentCount) {
        this.componentCount = componentCount;
        this.vertexCount = data[0].length() / componentCount[0];
        final int dataSizeInFloats = getArrayLength(data);
        final int newBufferSizeInBytes = dataSizeInFloats * BYTES_IN_FLOAT;

        // No need to re-create the buffer if the size is the same
        if (bufferSizeInBytes != newBufferSizeInBytes) {
            bind();
            glBufferData(GL_ARRAY_BUFFER, newBufferSizeInBytes, null, dynamicDraw ? GL_DYNAMIC_DRAW : GL_STATIC_DRAW);
            unbind();
            bufferSizeInBytes = newBufferSizeInBytes;
        }

        ByteBuffer gpuBuffer = mapBuffer();
        gpuBuffer.clear(); // TODO: check if it is really needed
        final int batches = data[0].length() / componentCount[0];
        for (int batch = 0; batch < batches; batch++) {
            for (int array = 0; array < data.length; array++) {
                for (int component = 0; component < componentCount[array]; component++) {
                    gpuBuffer.putFloat(data[array].elements[batch * componentCount[array] + component]);
                }
            }
        }
        unmapBuffer();
    }

    public void bind() {
        glBindBuffer(GL_ARRAY_BUFFER, bufferId);
    }

    public void unbind() {
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public int getComponentCountForArray(int arrayIndex) {
        if (arrayIndex >= componentCount.length || arrayIndex < 0) {
            Log.logError("<VertexBufferObject.getComponentCountForArray>: Invalid component count access("
                    + arrayIndex + ") in buffer.");
            return 0;
        }

        return componentCount[arrayIndex];
    }

    public int getOverallComponentCount() {
        int count = 0;
        for (int c : componentCount) {
            count += c;
        }
        return count;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public void destruct() {
        glDeleteBuffers(bufferId);
    }

    private ByteBuffer mapBuffer() {
        bind();
        return glMapBuffer(GL_ARRAY_BUFFER, GL_WRITE_ONLY);
    }

    private void unmapBuffer() {
        glUnmapBuffer(GL_ARRAY_BUFFER);
        unbind();
    }

    private int getArrayLength(FloatArray array[]) {
        int length = 0;
        for (FloatArray subArray : array) {
            length += subArray.length();
        }
        return length;
    }
}

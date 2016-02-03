package himmel.graphics.buffers;

import himmel.log.Log;

import java.util.ArrayList;
import java.util.List;

import static himmel.graphics.buffers.IndexBufferObject.ElementType;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

/**
 * Created by Igor on 03-May-15.
 */
public class VertexArrayObject {
    private int arrayId;
    private List<VertexBufferObject> vbos;
    private IndexBufferObject ibo;
    private List<Integer> attribArrayLocations;

    public VertexArrayObject() {
        arrayId = glGenVertexArrays();
        vbos = new ArrayList<>();
        attribArrayLocations = new ArrayList<>();
    }

    public void addVertexBufferObject(VertexBufferObject vbo, int attribLocationsForArrays[]) {
        addAttribArrayLocations(attribLocationsForArrays);

        bind();
        vbo.bind();

        final int amountOfArrays = attribLocationsForArrays.length;
        final int stride = vbo.getOverallComponentCount() * VertexBufferObject.BYTES_IN_FLOAT;
        int offsetAccumulator = 0;

        for (int i = 0; i < amountOfArrays; i++) {
            glEnableVertexAttribArray(attribLocationsForArrays[i]);

            final int componentCount = vbo.getComponentCountForArray(i);
            glVertexAttribPointer(attribLocationsForArrays[i], componentCount, GL_FLOAT, false, stride, offsetAccumulator);
            offsetAccumulator += componentCount * VertexBufferObject.BYTES_IN_FLOAT;

            glDisableVertexAttribArray(attribLocationsForArrays[i]);
        }

        vbo.unbind();
        unbind();

        vbos.add(vbo);
    }

    public void addVertexBufferObjects(VertexBufferObject vbos[], int attribLocationsForArrays[][]) {
        // TODO: Implement with one VAO binding
        for (int i = 0; i < vbos.length; i++) {
            addVertexBufferObject(vbos[i], attribLocationsForArrays[i]);
        }
    }

    public void setIndexBufferObject(IndexBufferObject ibo) {
        this.ibo = ibo;

        bind();
        ibo.bind();
        unbind();
    }

    public void enableAttribArrays() {
        for (int i : attribArrayLocations) {
            glEnableVertexAttribArray(i);
        }
    }

    public void disableAttribArrays() {
        for (int i : attribArrayLocations) {
            glDisableVertexAttribArray(i);
        }
    }

    private void addAttribArrayLocations(int[] attribArrayLocations) {
        for (int attribArrayLocation : attribArrayLocations) {
            if (!this.attribArrayLocations.contains(attribArrayLocation)) {
                this.attribArrayLocations.add(attribArrayLocation);
            }
        }
    }

    public int getVertexCount() {
        if (ibo != null) {
            return ibo.getVertexCount();
        }

        Log.logError("Could not get vertex count: ibo is null.");
        return 0;
    }

    public ElementType getIndexType() {
        if (ibo != null) {
            return ibo.getType();
        }

        Log.logError("Could not get index type: ibo is null.");
        return ElementType.UNSIGNED_SHORT;
    }

    public void bind() {
        glBindVertexArray(arrayId);
    }

    public void unbind() {
        glBindVertexArray(0);
    }

    public void destruct() {
        for (VertexBufferObject vbo : vbos) {
            vbo.destruct();
        }

        ibo.destruct();

        glDeleteVertexArrays(arrayId);
    }
}

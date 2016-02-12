package himmel.graphics.buffers;

import himmel.graphics.textures.Texture;
import himmel.log.Log;
import sun.security.provider.certpath.Vertex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static himmel.graphics.buffers.IndexBufferObject.ElementType;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
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
    private final int arrayId;
    private List<VertexBufferObject> vbos;
    private IndexBufferObject ibo;
    private Map<VertexBufferObject, int[]> attribArrayLocations;
    private final RenderingMode renderingMode;
    private boolean useIndexBuffer;
    private int[] textureIds;

    private final int MAX_TEXTURES = 16;
    private final int TEXTURE_ID_OFF_CODE = -1;

    // TODO: fix vertexCount
    public VertexArrayObject(RenderingMode renderingMode) {
        arrayId = glGenVertexArrays();
        vbos = new ArrayList<>();
        attribArrayLocations = new HashMap<>();
        this.renderingMode = renderingMode;
        useIndexBuffer = false;
        textureIds = new int[MAX_TEXTURES];
        resetTextureIds();
    }

    public void addVertexBufferObject(VertexBufferObject vbo, int attribLocationsForArrays[]) {
        addAttribArrayLocations(attribLocationsForArrays, vbo);
        submitVertexBufferObject(vbo, attribLocationsForArrays);
        vbos.add(vbo);
    }

    private void submitVertexBufferObject(VertexBufferObject vbo, int attribLocationsForArrays[]) {
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
    }

    public void replaceVertexBufferObject(VertexBufferObject oldVbo, VertexBufferObject newVbo, int attribLocationsForArrays[]) {
        removeAttribArrayLocations(oldVbo);
        vbos.remove(oldVbo);

        addVertexBufferObject(newVbo, attribLocationsForArrays);
    }

    public void addVertexBufferObjects(VertexBufferObject vbos[], int attribLocationsForArrays[][]) {
        // TODO: Implement with one VAO binding
        for (int i = 0; i < vbos.length; i++) {
            addVertexBufferObject(vbos[i], attribLocationsForArrays[i]);
        }
    }

    public void setIndexBufferObject(IndexBufferObject ibo) {
        this.ibo = ibo;
        useIndexBuffer = true;

        bind();
        ibo.bind();
        unbind();
    }

    // TODO: replace with tid
    public int addTexture(Texture texture) {
        final int slotIndex = getEmptyTextureIdSlot();
        if (slotIndex == -1) {
            return -1;
        }

        textureIds[slotIndex] = texture.getTID();
        return slotIndex;
    }

    public void setTextureSlots() {
        for (int slot = 0; slot < MAX_TEXTURES; slot++) {
            final int textureId = textureIds[slot];
            if (textureId != TEXTURE_ID_OFF_CODE) {
                glActiveTexture(GL_TEXTURE0 + slot);
                glBindTexture(GL_TEXTURE_2D, textureId);
            }
        }
    }

    private void resetTextureIds() {
        final int OFF_CODE = -1;
        for (int id = 0; id < MAX_TEXTURES; id++) {
            textureIds[id] = OFF_CODE;
        }
    }

    // TODO: implement Exception here for error code
    private int getEmptyTextureIdSlot() {
        for (int slot = 0; slot < MAX_TEXTURES; slot++) {
            if (textureIds[slot] == TEXTURE_ID_OFF_CODE) {
                return slot;
            }
        }

        return -1;
    }

    public void enableAttribArrays() {
        for (VertexBufferObject vbo : attribArrayLocations.keySet()) {
            int[] attribs = attribArrayLocations.get(vbo);
            for (int i : attribs) {
                glEnableVertexAttribArray(i);
            }
        }
    }

    public void disableAttribArrays() {
        for (VertexBufferObject vbo : attribArrayLocations.keySet()) {
            int[] attribs = attribArrayLocations.get(vbo);
            for (int i : attribs) {
                glDisableVertexAttribArray(i);
            }
        }
    }

    private void addAttribArrayLocations(int[] attribArrayLocations, VertexBufferObject vbo) {
        this.attribArrayLocations.put(vbo, attribArrayLocations);
    }

    private void removeAttribArrayLocations(VertexBufferObject vbo) {
        this.attribArrayLocations.remove(vbo);
    }

    public RenderingMode getRenderingMode() {
        return renderingMode;
    }

    public boolean isIndexBufferUsed() {
        return useIndexBuffer;
    }

    public int getVertexCount() {
        return (useIndexBuffer) ?
                (ibo.getVertexCount()) :
                (vbos.get(0).getVertexCount());
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

    public enum RenderingMode {
        TRIANGLES(GL_TRIANGLES),
        POINTS(GL_POINTS);
        // TODO: PATCHES

        public final int code;

        RenderingMode(final int code) {
            this.code = code;
        }
    }
}

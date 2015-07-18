package himmel.graphics.renderers;

import himmel.graphics.renderables.Renderable;
import himmel.graphics.Shader;
import himmel.graphics.buffers.IndexBuffer;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_SHORT;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

/**
 * Created by Igor on 22-May-15.
 */
public class FastRenderer extends Renderer {
    private int vao;
    private int vbo;
    private IndexBuffer ibo;
    private ByteBuffer gpuBuffer;
    private List<Integer> textureSlots;

    private final int FLOAT_SIZE_BYTES = 4;
    private final int COMPONENT_SIZE_BYTES = FLOAT_SIZE_BYTES * (3 + 1 + 2 + 4);
    private final int MAX_VERTICES = Short.MAX_VALUE; // because indices have type Short
    private final int BUFFER_SIZE = COMPONENT_SIZE_BYTES * MAX_VERTICES;

    private int currentVerticesAmount;
    private int currentIndicesAmount;
//    private int currentIndexUsed;

    private boolean filling;

    public FastRenderer() {
        init();
        textureSlots = new ArrayList<>();
    }

    private void terminate() {
        glDeleteBuffers(vbo);
        glDeleteVertexArrays(vao);
        ibo.destruct();
    }

    private void init() {
        vao = glGenVertexArrays();
        vbo = glGenBuffers();
        ibo = new IndexBuffer(true);

        glBindVertexArray(vao);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, BUFFER_SIZE, null, GL_DYNAMIC_DRAW);

        glEnableVertexAttribArray(Shader.ATTR_VERTEX);
        glEnableVertexAttribArray(Shader.ATTR_UV);
        glEnableVertexAttribArray(Shader.ATTR_TID);
        glEnableVertexAttribArray(Shader.ATTR_COLOR);

        glVertexAttribPointer(Shader.ATTR_VERTEX, 3, GL_FLOAT, false, COMPONENT_SIZE_BYTES, 0);
        glVertexAttribPointer(Shader.ATTR_UV, 2, GL_FLOAT, false, COMPONENT_SIZE_BYTES, 12);
        glVertexAttribPointer(Shader.ATTR_TID, 1, GL_FLOAT, false, COMPONENT_SIZE_BYTES, 12 + 8);
        glVertexAttribPointer(Shader.ATTR_COLOR, 4, GL_FLOAT, false, COMPONENT_SIZE_BYTES, 12 + 8 + 4);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    @Override
    public void submit(Renderable renderable) {

    }

    @Override
    public void render() {
        if (!filling) {
            for (int i = 0; i < textureSlots.size(); i++) {
                glActiveTexture(GL_TEXTURE0 + i);
                glBindTexture(GL_TEXTURE_2D, textureSlots.get(i));
            }

            glBindVertexArray(vao);
            ibo.bind();

//            glDrawElements(GL_TRIANGLES, currentIndicesCount, GL_UNSIGNED_SHORT, 0);

            ibo.unbind();
            glBindVertexArray(0);

//            System.out.println("Ind " + currentIndicesCount);
//            System.out.println("Sprite " + currentSpriteCount);
//            currentIndicesCount = 0;
//            currentSpriteCount = 0;
        }
    }

    @Override
    public void begin() {
        if (!filling) {
            filling = true;

            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            gpuBuffer = glMapBuffer(GL_ARRAY_BUFFER, GL_WRITE_ONLY);

            ibo.begin();
//            System.out.println("Capacity " + gpuBuffer.capacity());
        }
    }

    @Override
    public void end() {
        if (filling) {
            filling = false;

            glUnmapBuffer(GL_ARRAY_BUFFER);
            glBindBuffer(GL_ARRAY_BUFFER, 0);

            ibo.end();
        }
    }
}

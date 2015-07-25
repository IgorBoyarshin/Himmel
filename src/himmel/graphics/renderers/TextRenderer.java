package himmel.graphics.renderers;

import himmel.graphics.renderables.Renderable;
import himmel.graphics.Shader;
import himmel.graphics.buffers.IndexBuffer;
import himmel.math.Vector2f;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_SHORT;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

/**
 * Created by Igor on 04-Jun-15.
 */
public class TextRenderer extends Renderer {
    private int vao;
    private int vbo;
    private IndexBuffer ibo;
    private ByteBuffer gpuBuffer;
    private List<Integer> textureSlots;

    private final int SHADER_ATTR_VERTEX = 0;
    private final int SHADER_ATTR_UV = 1;
    private final int SHADER_ATTR_TID = 2;
    private final int SHADER_ATTR_COLOR = 3;

    private final int FLOAT_SIZE_BYTES = 4;

    private final int VERTEX_FLOATS_PER_COMPONENT = 3;
    private final int UV_FLOATS_PER_COMPONENT = 2;
    private final int TID_FLOATS_PER_COMPONENT = 1;
    private final int COLOR_FLOATS_PER_COMPONENT = 4;

    private final int COMPONENT_SIZE = FLOAT_SIZE_BYTES *
            (VERTEX_FLOATS_PER_COMPONENT +
                    UV_FLOATS_PER_COMPONENT +
                    TID_FLOATS_PER_COMPONENT +
                    COLOR_FLOATS_PER_COMPONENT);

    private final int SPRITE_SIZE = COMPONENT_SIZE * 4;
    private final int MAX_INDICES = 32000 / 4 * 6;
    private final int BUFFER_SIZE = SPRITE_SIZE * MAX_INDICES / 6;

    private int currentIndicesCount = 0;
    private int currentSpriteCount = 0;

    public TextRenderer() {
        super();

        init();
        textureSlots = new ArrayList<>();
    }

    private void terminate() {
        glDeleteBuffers(vbo);
    }

    private void init() {
        vao = glGenVertexArrays();
        vbo = glGenBuffers();

        glBindVertexArray(vao);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, BUFFER_SIZE, null, GL_DYNAMIC_DRAW);

        glEnableVertexAttribArray(SHADER_ATTR_VERTEX);
        glEnableVertexAttribArray(SHADER_ATTR_UV);
        glEnableVertexAttribArray(SHADER_ATTR_TID);
        glEnableVertexAttribArray(SHADER_ATTR_COLOR);

        glVertexAttribPointer(SHADER_ATTR_VERTEX, 3, GL_FLOAT, false, COMPONENT_SIZE, 0);
        glVertexAttribPointer(SHADER_ATTR_UV, 2, GL_FLOAT, false, COMPONENT_SIZE, 12);
        glVertexAttribPointer(SHADER_ATTR_TID, 1, GL_FLOAT, false, COMPONENT_SIZE, 12 + 8);
        glVertexAttribPointer(SHADER_ATTR_COLOR, 4, GL_FLOAT, false, COMPONENT_SIZE, 12 + 8 + 4);

        glBindBuffer(GL_ARRAY_BUFFER, 0);

        ibo = new IndexBuffer(true, false);

        ibo.begin();

        // TODO: works only with shorts now
        short offset = 0;
        for (int i = 0; i < MAX_INDICES; i += 6) {
            ibo.addShort((short) (offset + 0));
            ibo.addShort((short) (offset + 1));
            ibo.addShort((short) (offset + 2));

            ibo.addShort((short) (offset + 0));
            ibo.addShort((short) (offset + 2));
            ibo.addShort((short) (offset + 3));

            offset += 4;
        }

        ibo.end();

        glBindVertexArray(0);
    }

    @Override
    public void submit(Renderable text) {
        if (filling) {
            // Not supported
            if (currentSpriteCount > 7998) {
                end();
                render();
                begin();

                currentIndicesCount = 0;
                currentSpriteCount = 0;
            }

            short[] indices = text.getIndices();
            float[] vertices = text.getVertices();
            float[] colors = text.getColors();
            float[] uv = text.getUV();
            int tid = text.getTID();

            float ts = 0.0f;
            if (tid > 0) {
                boolean found = false;
                for (int i = 0; i < textureSlots.size(); i++) {
                    if (textureSlots.get(i) == tid) {
                        ts = (float) (i + 1);
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    if (textureSlots.size() >= 32) {
                        end();
                        render();
                        begin();
                    }
                    textureSlots.add(tid);
                    ts = (float) textureSlots.size();
                }
            } else {
                // Incorrect
            }

            int size = vertices.length / 3;
            for (int i = 0; i < size; i++) {
                gpuBuffer.putFloat(vertices[i * 3 + 0]);
                gpuBuffer.putFloat(vertices[i * 3 + 1]);
                gpuBuffer.putFloat(vertices[i * 3 + 2]);

                gpuBuffer.putFloat(uv[2 * i]);
                gpuBuffer.putFloat(uv[2 * i + 1]);

                gpuBuffer.putFloat(ts);

                if (colors.length == 4) {
                    gpuBuffer.putFloat(colors[0]);
                    gpuBuffer.putFloat(colors[1]);
                    gpuBuffer.putFloat(colors[2]);
                    gpuBuffer.putFloat(colors[3]);
                } else {
                    gpuBuffer.putFloat(colors[i * 4 + 0]);
                    gpuBuffer.putFloat(colors[i * 4 + 1]);
                    gpuBuffer.putFloat(colors[i * 4 + 2]);
                    gpuBuffer.putFloat(colors[i * 4 + 3]);
                }
            }

            currentIndicesCount += indices.length;
            currentSpriteCount++;
        }
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

            glDrawElements(GL_TRIANGLES, currentIndicesCount, ibo.getType(), 0);

            ibo.unbind();
            glBindVertexArray(0);

//            System.out.println("Ind " + currentIndicesCount);
//            System.out.println("Sprite " + currentSpriteCount);
        }
    }

    @Override
    public void begin() {
        if (!filling) {
            filling = true;

            glBindBuffer(GL_ARRAY_BUFFER, vbo);
//            gpuBuffer = null;
            gpuBuffer = glMapBuffer(GL_ARRAY_BUFFER, GL_WRITE_ONLY);
//            System.out.println("Capacity " + gpuBuffer.capacity());

            currentIndicesCount = 0;
            currentSpriteCount = 0;
        }
    }

    @Override
    public void end() {
        if (filling) {
            filling = false;

            glUnmapBuffer(GL_ARRAY_BUFFER);
            glBindBuffer(GL_ARRAY_BUFFER, 0);
        }
    }
}

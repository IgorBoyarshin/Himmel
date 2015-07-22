package himmel.graphics.renderers;

import himmel.graphics.renderables.Renderable;
import himmel.graphics.Shader;
import himmel.graphics.buffers.IndexBuffer;
import himmel.math.Vector2f;
import himmel.math.Vector3f;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Igor on 22-May-15.
 */
public class FastSpriteRenderer extends Renderer {
    private int vao;
    private int vbo;
    private IndexBuffer ibo;
    private ByteBuffer gpuBuffer;
    private List<Integer> textureSlots;

    private final int SHADER_ATTR_VERTEX = 0;
    private final int SHADER_ATTR_COLOR = 1;
    private final int SHADER_ATTR_UV = 2;
    private final int SHADER_ATTR_TID = 3;

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

    public FastSpriteRenderer() {
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
        glEnableVertexAttribArray(SHADER_ATTR_COLOR);
        glEnableVertexAttribArray(SHADER_ATTR_UV);
        glEnableVertexAttribArray(SHADER_ATTR_TID);

        glVertexAttribPointer(SHADER_ATTR_VERTEX, 3, GL_FLOAT, false, COMPONENT_SIZE, 0);
        glVertexAttribPointer(SHADER_ATTR_COLOR, 4, GL_FLOAT, false, COMPONENT_SIZE, 12);
        glVertexAttribPointer(SHADER_ATTR_UV, 2, GL_FLOAT, false, COMPONENT_SIZE, 12 + 16);
        glVertexAttribPointer(SHADER_ATTR_TID, 1, GL_FLOAT, false, COMPONENT_SIZE, 12 + 16 + 8);

        glBindBuffer(GL_ARRAY_BUFFER, 0);

        ibo = new IndexBuffer(false);
        ibo.begin();

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
    public void submit(Renderable renderable) {
        if (filling) {
            if (currentSpriteCount > 7998) {
                end();
                render();
                begin();

                currentIndicesCount = 0;
                currentSpriteCount = 0;
            }

            currentIndicesCount += 6;
            currentSpriteCount++;


            float[] vertices = renderable.getVertices();
            float[] colors = renderable.getColors();
            float[] uv = renderable.getUV();
            int tid = renderable.getTID();

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
//                    System.out.println("t:" + tid + " " + ts);
                }
            } else {
                // render with colors
            }

            Vector3f v1 = transformationStackCash.multiply(new Vector3f(vertices[3 * 0 + 0], vertices[3 * 0 + 1], vertices[3 * 0 + 2]));
            Vector3f v2 = transformationStackCash.multiply(new Vector3f(vertices[3 * 1 + 0], vertices[3 * 1 + 1], vertices[3 * 1 + 2]));
            Vector3f v3 = transformationStackCash.multiply(new Vector3f(vertices[3 * 2 + 0], vertices[3 * 2 + 1], vertices[3 * 2 + 2]));
            Vector3f v4 = transformationStackCash.multiply(new Vector3f(vertices[3 * 3 + 0], vertices[3 * 3 + 1], vertices[3 * 3 + 2]));

            gpuBuffer.putFloat(v1.x);
            gpuBuffer.putFloat(v1.y);
            gpuBuffer.putFloat(v1.z);
            gpuBuffer.putFloat(colors[4 * 0 + 0]);
            gpuBuffer.putFloat(colors[4 * 0 + 1]);
            gpuBuffer.putFloat(colors[4 * 0 + 2]);
            gpuBuffer.putFloat(colors[4 * 0 + 3]);
            if (uv == null) {
                gpuBuffer.putFloat(0.0f);
                gpuBuffer.putFloat(0.0f);
            } else {
                gpuBuffer.putFloat(uv[2 * 0]);
                gpuBuffer.putFloat(uv[2 * 0 + 1]);
            }
            gpuBuffer.putFloat(ts);

            gpuBuffer.putFloat(v2.x);
            gpuBuffer.putFloat(v2.y);
            gpuBuffer.putFloat(v2.z);
            gpuBuffer.putFloat(colors[4 * 1 + 0]);
            gpuBuffer.putFloat(colors[4 * 1 + 1]);
            gpuBuffer.putFloat(colors[4 * 1 + 2]);
            gpuBuffer.putFloat(colors[4 * 1 + 3]);
            if (uv == null) {
                gpuBuffer.putFloat(0.0f);
                gpuBuffer.putFloat(0.0f);
            } else {
                gpuBuffer.putFloat(uv[2 * 1]);
                gpuBuffer.putFloat(uv[2 * 1 + 1]);
            }
            gpuBuffer.putFloat(ts);

            gpuBuffer.putFloat(v3.x);
            gpuBuffer.putFloat(v3.y);
            gpuBuffer.putFloat(v3.z);
            gpuBuffer.putFloat(colors[4 * 2 + 0]);
            gpuBuffer.putFloat(colors[4 * 2 + 1]);
            gpuBuffer.putFloat(colors[4 * 2 + 2]);
            gpuBuffer.putFloat(colors[4 * 2 + 3]);
            if (uv == null) {
                gpuBuffer.putFloat(0.0f);
                gpuBuffer.putFloat(0.0f);
            } else {
                gpuBuffer.putFloat(uv[2 * 2]);
                gpuBuffer.putFloat(uv[2 * 2 + 1]);
            }
            gpuBuffer.putFloat(ts);


            gpuBuffer.putFloat(v4.x);
            gpuBuffer.putFloat(v4.y);
            gpuBuffer.putFloat(v4.z);
            gpuBuffer.putFloat(colors[4 * 3 + 0]);
            gpuBuffer.putFloat(colors[4 * 3 + 1]);
            gpuBuffer.putFloat(colors[4 * 3 + 2]);
            gpuBuffer.putFloat(colors[4 * 3 + 3]);
            if (uv == null) {
                gpuBuffer.putFloat(0.0f);
                gpuBuffer.putFloat(0.0f);
            } else {
                gpuBuffer.putFloat(uv[2 * 3]);
                gpuBuffer.putFloat(uv[2 * 3 + 1]);
            }
            gpuBuffer.putFloat(ts);
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

            glDrawElements(GL_TRIANGLES, currentIndicesCount, GL_UNSIGNED_SHORT, 0);

            ibo.unbind();
            glBindVertexArray(0);

//            System.out.println("Ind " + currentIndicesCount);
//            System.out.println("Sprite " + currentSpriteCount);
            currentIndicesCount = 0;
            currentSpriteCount = 0;
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

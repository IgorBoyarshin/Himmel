package himmel.graphics.renderers;

import himmel.graphics.renderables.Renderable;
import himmel.graphics.Shader;
import himmel.graphics.buffers.IndexBuffer;
import himmel.math.Vector2f;
import himmel.math.Vector3f;

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

    private final int SHADER_ATTR_VERTEX = 0;
    private final int SHADER_ATTR_COLOR = 1;
    private final int SHADER_ATTR_UV = 2;
    private final int SHADER_ATTR_TID = 3;

    private final int FLOAT_SIZE_BYTES = 4;

    private final int VERTEX_FLOATS_PER_COMPONENT = 3;
    private final int UV_FLOATS_PER_COMPONENT = 2;
    private final int TID_FLOATS_PER_COMPONENT = 1;
    private final int COLOR_FLOATS_PER_COMPONENT = 4;

    private final int COMPONENT_SIZE_BYTES = FLOAT_SIZE_BYTES *
            (VERTEX_FLOATS_PER_COMPONENT +
                    UV_FLOATS_PER_COMPONENT +
                    TID_FLOATS_PER_COMPONENT +
                    COLOR_FLOATS_PER_COMPONENT);
    private final int MAX_VERTICES = Short.MAX_VALUE; // because indices have type Short
    private final int BUFFER_SIZE = COMPONENT_SIZE_BYTES * MAX_VERTICES;

    private int currentVerticesAmount;
    private int currentIndicesAmount;

    private boolean filling;

    public FastRenderer() {
        super();

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

        glEnableVertexAttribArray(SHADER_ATTR_VERTEX);
        glEnableVertexAttribArray(SHADER_ATTR_COLOR);
        glEnableVertexAttribArray(SHADER_ATTR_UV);
        glEnableVertexAttribArray(SHADER_ATTR_TID);

        glVertexAttribPointer(SHADER_ATTR_VERTEX, 3, GL_FLOAT, false, COMPONENT_SIZE_BYTES, 0);
        glVertexAttribPointer(SHADER_ATTR_COLOR, 4, GL_FLOAT, false, COMPONENT_SIZE_BYTES, 12);
        glVertexAttribPointer(SHADER_ATTR_UV, 2, GL_FLOAT, false, COMPONENT_SIZE_BYTES, 12 + 16);
        glVertexAttribPointer(SHADER_ATTR_TID, 1, GL_FLOAT, false, COMPONENT_SIZE_BYTES, 12 + 16 + 8);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    @Override
    public void submit(Renderable renderable) {
        if (filling) {
            float[] vertices = renderable.getVertices();
            float[] colors = renderable.getColors();
            float[] uv = renderable.getUV();
            int tid = renderable.getTID();
            short[] indices = renderable.getIndices();

            final int size = vertices.length / 3;

            if (currentVerticesAmount + size >= MAX_VERTICES) {
                // flush
                end();
                render();
                begin();
            }

            // Indices
            for (short index : indices) {
                ibo.addShort((short) (index + currentVerticesAmount));
            }

            currentVerticesAmount += size;
            currentIndicesAmount += indices.length;

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
                    if (textureSlots.size() >= 16) {
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

//            List<Vector3f> transformedVertices = new ArrayList<>();
//            for (int i = 0; i < size; i++) {
//                transformedVertices.add(transformationStackCash.multiply(
//                        new Vector3f(vertices[3 * i + 0], vertices[3 * i + 1], vertices[3 * i + 2])));
////                transformedVertices.add(new Vector3f(vertices[3 * i + 0], vertices[3 * i + 1], vertices[3 * i + 2]));
//            }

            for (int i = 0; i < size; i++) {
//                Vector3f vertex = transformedVertices.get(i);
                Vector3f vertex = transformationStackCash.multiply(
                        new Vector3f(vertices[3 * i + 0], vertices[3 * i + 1], vertices[3 * i + 2]));

                gpuBuffer.putFloat(vertex.x);
                gpuBuffer.putFloat(vertex.y);
                gpuBuffer.putFloat(vertex.z);

                gpuBuffer.putFloat(colors[4 * i + 0]);
                gpuBuffer.putFloat(colors[4 * i + 1]);
                gpuBuffer.putFloat(colors[4 * i + 2]);
                gpuBuffer.putFloat(colors[4 * i + 3]);

                if (uv == null) {
                    gpuBuffer.putFloat(0.0f);
                    gpuBuffer.putFloat(0.0f);
                } else {
                    gpuBuffer.putFloat(uv[2 * i]);
                    gpuBuffer.putFloat(uv[2 * i + 1]);
                }

                gpuBuffer.putFloat(ts);
            }
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

            glDrawElements(GL_TRIANGLES, currentIndicesAmount, GL_UNSIGNED_SHORT, 0);

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

            currentVerticesAmount = 0;
            currentIndicesAmount = 0;
        }
    }

    @Override
    public void end() {
        if (filling) {
            filling = false;

            glUnmapBuffer(GL_ARRAY_BUFFER);
            glBindBuffer(GL_ARRAY_BUFFER, 0);

            ibo.end();

//            System.out.println();
//            System.out.println("Capacity: " + gpuBuffer.capacity());
//            System.out.println("Limit: " + gpuBuffer.position());
//            System.out.println("Vertices: " + currentVerticesAmount);
//            System.out.println("Indices: " + currentIndicesAmount);
//            System.out.println();
        }
    }
}

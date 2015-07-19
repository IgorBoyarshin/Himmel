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

    private final int FLOAT_SIZE_BYTES = 4;
    private final int COMPONENT_SIZE_BYTES = FLOAT_SIZE_BYTES * (3 + 1 + 2 + 4);
    private final int MAX_VERTICES = Short.MAX_VALUE; // because indices have type Short
    private final int BUFFER_SIZE = COMPONENT_SIZE_BYTES * MAX_VERTICES;

    private int currentVerticesAmount;
    private int currentIndicesAmount;
//    private short currentIndexUsed;

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
        if (filling) {
            float[] vertices = renderable.getVertices();
            float[] colors = renderable.getColors();
            List<Vector2f> uv = renderable.getUV();
            int tid = renderable.getTID();

            if (currentVerticesAmount + vertices.length / 3 >= MAX_VERTICES) {
                // flush
                end();
                render();
                begin();
            }

            // Indices
            short[] indices = renderable.getIndices();
            for (short index : indices) {
                ibo.addShort((short) (index + currentVerticesAmount));
            }

            currentVerticesAmount += vertices.length / 3;
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

            List<Vector3f> transformedVertices = new ArrayList<>();
            for (int i = 0; i < vertices.length / 3; i++) {
                transformedVertices.add(transformationStackCash.multiply(new Vector3f(vertices[3 * i + 0], vertices[3 * i + 1], vertices[3 * i + 2])));
//                transformedVertices.add(new Vector3f(vertices[3 * i + 0], vertices[3 * i + 1], vertices[3 * i + 2]));
            }

            for (int i = 0; i < vertices.length / 3; i++) {
                Vector3f vertex = transformedVertices.get(i);

                gpuBuffer.putFloat(vertex.x);
                gpuBuffer.putFloat(vertex.y);
                gpuBuffer.putFloat(vertex.z);
                gpuBuffer.putFloat(uv.get(i).x);
                gpuBuffer.putFloat(uv.get(i).y);
                gpuBuffer.putFloat(tid);
                gpuBuffer.putFloat(colors[4 * i + 0]);
                gpuBuffer.putFloat(colors[4 * i + 1]);
                gpuBuffer.putFloat(colors[4 * i + 2]);
                gpuBuffer.putFloat(colors[4 * i + 3]);
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

package himmel.graphics.renderers;

import himmel.graphics.renderables.Renderable;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL40.GL_PATCHES;
import static org.lwjgl.opengl.GL40.GL_PATCH_VERTICES;
import static org.lwjgl.opengl.GL40.glPatchParameteri;

/**
 * Created by Igor on 27-Aug-15.
 */
public class TerrainTessellationRenderer extends Renderer {
    private int vao;
    private int vbo;
    //    private IndexBuffer ibo;
    private ByteBuffer gpuBuffer;
    private List<Integer> textureSlots;

    private final int SHADER_ATTR_VERTEX = 0;
//    private final int SHADER_ATTR_NORMAL = 1;
//    private final int SHADER_ATTR_UV = 1;

    private final int FLOAT_SIZE_BYTES = 4;

    private final int VERTEX_FLOATS_PER_COMPONENT = 2;
//    private final int NORMAL_FLOATS_PER_COMPONENT = 3;
//    private final int UV_FLOATS_PER_COMPONENT = 2;

    private final int COMPONENT_SIZE_BYTES = FLOAT_SIZE_BYTES *
            (VERTEX_FLOATS_PER_COMPONENT);
//                    NORMAL_FLOATS_PER_COMPONENT +
//                    UV_FLOATS_PER_COMPONENT);

    private final int MAX_VERTICES = (16) * Short.MAX_VALUE;
    private final int BUFFER_SIZE = COMPONENT_SIZE_BYTES * MAX_VERTICES;

    private int currentVerticesAmount;
//    private int currentIndicesAmount;

    private boolean filling;

    public TerrainTessellationRenderer() {
        super();

        init();
        textureSlots = new ArrayList<>();
    }

    private void terminate() {
        glDeleteBuffers(vbo);
        glDeleteVertexArrays(vao);
//        ibo.destruct();
    }

    private void init() {
        vao = glGenVertexArrays();
        vbo = glGenBuffers();
//        ibo = new IndexBuffer(false, false);

        glBindVertexArray(vao);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, BUFFER_SIZE, null, GL_STATIC_DRAW);

        glEnableVertexAttribArray(SHADER_ATTR_VERTEX);
//        glEnableVertexAttribArray(SHADER_ATTR_NORMAL);
//        glEnableVertexAttribArray(SHADER_ATTR_UV);

        glVertexAttribPointer(SHADER_ATTR_VERTEX, VERTEX_FLOATS_PER_COMPONENT, GL_FLOAT, false, COMPONENT_SIZE_BYTES, 0);
//        glVertexAttribPointer(SHADER_ATTR_NORMAL, NORMAL_FLOATS_PER_COMPONENT, GL_FLOAT, false, COMPONENT_SIZE_BYTES, 12);
//        glVertexAttribPointer(SHADER_ATTR_UV, UV_FLOATS_PER_COMPONENT, GL_FLOAT, false, COMPONENT_SIZE_BYTES, 8);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    @Override
    public void submit(Renderable renderable) {
        if (filling) {
            float[] vertices = renderable.getVertices();
//            float[] normals = renderable.getNormals();
//            float[] uv = renderable.getUV();
            int tid = renderable.getTID();

//            Matrix4f modelMatrix = renderable.getModelMatrix();
//            short[] indices = renderable.getIndices();

            final int size = vertices == null ? 0 : vertices.length / 2;

            if (currentVerticesAmount + size >= MAX_VERTICES) {
                // TODO: Was 'flush' before, but now do nothing probably. Just print that the buffer is overflowed
            }

            // Indices
//            if (indices != null) {
//                for (short index : indices) {
//                    if (ibo.getType() == GL_UNSIGNED_SHORT) {
//                        ibo.addShort((short) (index + currentVerticesAmount));
//                    } else {
//                        ibo.addInt(index + currentVerticesAmount);
//                    }
//                }
//            }

            currentVerticesAmount += size;
//            currentIndicesAmount += indices == null ? 0 : indices.length;

            // Texture
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
                }
            } else {
                // render with colors
            }

            for (int i = 0; i < size; i++) {
                // TODO: previously here was transformation stack
//                if (mid > 0.0f) {
//                    gpuBuffer.putFloat(vertices[3 * i + 0]);
//                    gpuBuffer.putFloat(vertices[3 * i + 1]);
//                    gpuBuffer.putFloat(vertices[3 * i + 2]);
//                } else {
//                    if (modelMatrix == null) {
//                        gpuBuffer.putFloat(vertices[3 * i + 0]);
//                        gpuBuffer.putFloat(vertices[3 * i + 1]);
//                        gpuBuffer.putFloat(vertices[3 * i + 2]);
//                    } else {
//                        Vector3f vertex = modelMatrix.multiply(vertices[3 * i + 0], vertices[3 * i + 1], vertices[3 * i + 2]);
//
//                        gpuBuffer.putFloat(vertex.x);
//                        gpuBuffer.putFloat(vertex.y);
//                        gpuBuffer.putFloat(vertex.z);
//                    }
//                }

                gpuBuffer.putFloat(vertices[2 * i + 0]);
                gpuBuffer.putFloat(vertices[2 * i + 1]);
//                gpuBuffer.putFloat(vertices[3 * i + 2]);


//                // TODO: how should I treat normals???
//                if (normals == null) {
//                    gpuBuffer.putFloat(0.0f);
//                    gpuBuffer.putFloat(1.0f);
//                    gpuBuffer.putFloat(0.0f);
//                } else {
//                    gpuBuffer.putFloat(normals[3 * i + 0]);
//                    gpuBuffer.putFloat(normals[3 * i + 1]);
//                    gpuBuffer.putFloat(normals[3 * i + 2]);

//                if (uv == null) {
//                    gpuBuffer.putFloat(0.0f);
//                    gpuBuffer.putFloat(0.0f);
//                } else {
//                    gpuBuffer.putFloat(uv[2 * i]);
//                    gpuBuffer.putFloat(uv[2 * i + 1]);
//                }
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
//            ibo.bind();

            glPatchParameteri(GL_PATCH_VERTICES, 3);
            glDrawArrays(GL_PATCHES, 0, currentVerticesAmount);

//            ibo.unbind();
            glBindVertexArray(0);
        }
    }

    @Override
    public void begin() {
        if (!filling) {
            filling = true;

            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            gpuBuffer = glMapBuffer(GL_ARRAY_BUFFER, GL_WRITE_ONLY);

//            ibo.begin();

            currentVerticesAmount = 0;
//            currentIndicesAmount = 0;
        }
    }

    @Override
    public void end() {
        if (filling) {
            filling = false;

            glUnmapBuffer(GL_ARRAY_BUFFER);
            glBindBuffer(GL_ARRAY_BUFFER, 0);

//            ibo.end();
        }
    }
}
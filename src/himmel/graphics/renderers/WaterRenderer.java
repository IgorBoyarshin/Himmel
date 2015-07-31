package himmel.graphics.renderers;

import himmel.graphics.buffers.IndexBuffer;
import himmel.graphics.renderables.Renderable;
import himmel.math.Matrix4f;
import himmel.math.Vector3f;

import java.nio.ByteBuffer;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

/**
 * Created by Igor on 31-Jul-15.
 */
public class WaterRenderer extends Renderer {
    private int vao;
    private int vbo;
    private IndexBuffer ibo;
    private ByteBuffer gpuBuffer;

    private final int SHADER_ATTR_VERTEX = 0;

    private final int FLOAT_SIZE_BYTES = 4;
    private final int VERTEX_FLOATS_PER_COMPONENT = 2;
    private final int COMPONENT_SIZE = FLOAT_SIZE_BYTES * VERTEX_FLOATS_PER_COMPONENT;
    private final int SPRITE_SIZE = COMPONENT_SIZE * 4;
    private final int MAX_WATER_TILES = 10;
    private final int BUFFER_SIZE = SPRITE_SIZE * MAX_WATER_TILES;

    private int currentVerticesAmount;
    private int currentIndicesAmount;

    public WaterRenderer() {
        init();
    }

    private void init() {
        vao = glGenVertexArrays();
        vbo = glGenBuffers();

        glBindVertexArray(vao);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, BUFFER_SIZE, null, GL_DYNAMIC_DRAW);

        glEnableVertexAttribArray(SHADER_ATTR_VERTEX);

        glVertexAttribPointer(SHADER_ATTR_VERTEX, 3, GL_FLOAT, false, COMPONENT_SIZE, 0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);

        ibo = new IndexBuffer(true, false);
        ibo.begin();

        // TODO: works only with shorts now
        short offset = 0;
        for (int i = 0; i < MAX_WATER_TILES; i++) {
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
            float[] vertices = renderable.getVertices();

            final int size = vertices.length / 2;

            if (currentVerticesAmount + size >= MAX_WATER_TILES * 4) {

                return;
            }

            currentVerticesAmount += size;
            currentIndicesAmount += 6;

//            float ts = 0.0f;
//            if (tid > 0) {
//                boolean found = false;
//                for (int i = 0; i < textureSlots.size(); i++) {
//                    if (textureSlots.get(i) == tid) {
//                        ts = (float) (i + 1);
//                        found = true;
//                        break;
//                    }
//                }
//
//                if (!found) {
//                    if (textureSlots.size() >= 16) {
//                        end();
//                        render();
//                        begin();
//                    }
//                    textureSlots.add(tid);
//                    ts = (float) textureSlots.size();
////                    System.out.println("t:" + tid + " " + ts);
//                }
//            } else {
//                // render with colors
//            }

//            List<Vector3f> transformedVertices = new ArrayList<>();
//            for (int i = 0; i < size; i++) {
//                transformedVertices.add(transformationStackCash.multiply(
//                        new Vector3f(vertices[3 * i + 0], vertices[3 * i + 1], vertices[3 * i + 2])));
////                transformedVertices.add(new Vector3f(vertices[3 * i + 0], vertices[3 * i + 1], vertices[3 * i + 2]));
//            }

            Matrix4f modelMatrix = renderable.getModelMatrix();
            for (int i = 0; i < size; i++) {
                Vector3f vertex = modelMatrix.multiply(
                        new Vector3f(vertices[2 * i + 0], 0.0f, vertices[2 * i + 1]));

                gpuBuffer.putFloat(vertex.x);
                gpuBuffer.putFloat(vertex.z);
            }
        }
    }

    @Override
    public void render() {
        if (!filling) {
            glBindVertexArray(vao);
            ibo.bind();

            glDrawElements(GL_TRIANGLES, currentIndicesAmount, ibo.getType(), 0);

            ibo.unbind();
            glBindVertexArray(0);
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
        }
    }
}

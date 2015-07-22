package himmel.graphics.renderers;

import himmel.graphics.renderables.Renderable;
import himmel.graphics.Shader;
import himmel.graphics.buffers.IndexBuffer;
import himmel.math.FloatArray;
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

    //    private final int COMPONENT_SIZE_BYTES = FLOAT_SIZE_BYTES * (3 + 3 + 1 + 2 + 4);
    private final int COMPONENT_SIZE_BYTES;
    private final int MAX_VERTICES = Short.MAX_VALUE; // because indices have type Short
    //    private final int BUFFER_SIZE = COMPONENT_SIZE_BYTES * MAX_VERTICES;
    private final int BUFFER_SIZE;

    private int currentVerticesAmount;
    private int currentIndicesAmount;
//    private short currentIndexUsed;

    private boolean filling;

    public FastRenderer(List<ShaderComponent> shaderComponents) {
        super(shaderComponents);

        int componentSizeBytes = 0;
        for (ShaderComponent component : shaderComponents) {
            componentSizeBytes += component.getAmount() * component.getType().getBytes();
        }
        this.COMPONENT_SIZE_BYTES = componentSizeBytes;
        this.BUFFER_SIZE = COMPONENT_SIZE_BYTES * MAX_VERTICES;

        init(shaderComponents);
        textureSlots = new ArrayList<>();
    }

    private void terminate() {
        glDeleteBuffers(vbo);
        glDeleteVertexArrays(vao);
        ibo.destruct();
    }

    private void init(List<ShaderComponent> shaderComponents) {
        vao = glGenVertexArrays();
        vbo = glGenBuffers();
        ibo = new IndexBuffer(true);

        glBindVertexArray(vao);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, BUFFER_SIZE, null, GL_DYNAMIC_DRAW);

        // TODO: add support for other component's types
        int offsetForComponent = 0;
        for (ShaderComponent component : shaderComponents) {
            glEnableVertexAttribArray(component.getLocation());
            glVertexAttribPointer(component.getLocation(), component.getAmount(),
                    component.getType().equals(ShaderComponent.TYPE.FLOAT) ? GL_FLOAT : GL_FLOAT, false, COMPONENT_SIZE_BYTES, offsetForComponent);
            offsetForComponent += component.getAmount() * component.getType().getBytes();
        }

//        glEnableVertexAttribArray(Shader.ATTR_VERTEX);
//        glEnableVertexAttribArray(Shader.ATTR_UV);
//        glEnableVertexAttribArray(Shader.ATTR_TID);
//        glEnableVertexAttribArray(Shader.ATTR_COLOR);
//
//        glVertexAttribPointer(Shader.ATTR_VERTEX, 3, GL_FLOAT, false, COMPONENT_SIZE_BYTES, 0);
//        glVertexAttribPointer(Shader.ATTR_UV, 2, GL_FLOAT, false, COMPONENT_SIZE_BYTES, 12);
//        glVertexAttribPointer(Shader.ATTR_TID, 1, GL_FLOAT, false, COMPONENT_SIZE_BYTES, 12 + 8);
//        glVertexAttribPointer(Shader.ATTR_COLOR, 4, GL_FLOAT, false, COMPONENT_SIZE_BYTES, 12 + 8 + 4);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    @Override
    public void submit(Renderable renderable) {
        if (filling) {
//            float[] vertices = renderable.getVertices();
//            float[] colors = renderable.getColors();
//            List<Vector2f> uv = renderable.getUV();
            int tid = renderable.getTID();
            // TODO: Mb make this method in Renderable
            List<FloatArray> floatArrays = new ArrayList<>();
            for (int i = 0; i < renderable.getAmountOfFloatArrays(); i++) {
                floatArrays.add(renderable.getFloatArray(i));
            }
            FloatArray vertices = floatArrays.get(0);

            if (currentVerticesAmount + vertices.size() / 3 >= MAX_VERTICES) {
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

            currentVerticesAmount += vertices.size() / 3;
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
            for (int i = 0; i < vertices.size() / 3; i++) {
//                transformedVertices.add(transformationStackCash.multiply(
//                        new Vector3f(vertices[3 * i + 0], vertices[3 * i + 1], vertices[3 * i + 2])));
                transformedVertices.add(transformationStackCash.multiply(vertices.getVector3f(i)));
            }

            final int amountOfFloatArrays = floatArrays.size();
            for (int i = 0; i < vertices.size() / 3; i++) {
                Vector3f vertex = transformedVertices.get(i);

                gpuBuffer.putFloat(vertex.x);
                gpuBuffer.putFloat(vertex.y);
                gpuBuffer.putFloat(vertex.z);

                for (int j = 1; j < amountOfFloatArrays; j++) {
                    final int count = floatArrays.get(j).getCount();
                    for (int amountOfElements = 0; amountOfElements < count; amountOfElements++) {
                        gpuBuffer.putFloat(floatArrays.get(j).getFloat(i * count + amountOfElements));
                    }
                }

                gpuBuffer.putFloat(ts);

//                gpuBuffer.putFloat(uv.get(i).x);
//                gpuBuffer.putFloat(uv.get(i).y);
//                gpuBuffer.putFloat(ts);
//                gpuBuffer.putFloat(colors[4 * i + 0]);
//                gpuBuffer.putFloat(colors[4 * i + 1]);
//                gpuBuffer.putFloat(colors[4 * i + 2]);
//                gpuBuffer.putFloat(colors[4 * i + 3]);
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

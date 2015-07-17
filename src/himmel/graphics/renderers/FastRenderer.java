package himmel.graphics.renderers;

import himmel.graphics.renderables.Renderable;
import himmel.graphics.Shader;
import himmel.graphics.buffers.IndexBuffer;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
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

    private final int COMPONENT_SIZE = 28;
    private final int MAX_VERTICES = 10 ^ 4;
    private final int MAX_INDICES = MAX_VERTICES / 2 * 3;
    private final int MAX_USED_INDEX = 32000;
    private final int BUFFER_SIZE = COMPONENT_SIZE * MAX_VERTICES;
    private int currentIndicesCount;
    private int currentIndexUsed;
    private int currentVerticesAmount;

    public FastRenderer() {
        init();
    }

    private boolean filling;

    private void terminate() {
        glDeleteBuffers(vbo);
    }

    private void init() {
        vao = glGenVertexArrays();
        vbo = glGenBuffers();

        glBindVertexArray(vao);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, BUFFER_SIZE, null, GL_DYNAMIC_DRAW);
        glEnableVertexAttribArray(Shader.ATTR_VERTEX);
        glEnableVertexAttribArray(Shader.ATTR_COLOR);
        glVertexAttribPointer(Shader.ATTR_VERTEX, 3, GL_FLOAT, false, COMPONENT_SIZE, 0);
        glVertexAttribPointer(Shader.ATTR_COLOR, 4, GL_FLOAT, false, COMPONENT_SIZE, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        short[] indices = new short[MAX_INDICES];


        glBindVertexArray(0);
    }

    @Override
    public void submit(Renderable renderable) {

    }

    @Override
    public void render() {

    }

    public void begin() {

    }

    public void end() {

    }
}

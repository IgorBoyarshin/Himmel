package himmel.graphics.entities;

import himmel.graphics.Shader;
import himmel.graphics.buffers.VertexArrayObject;

/**
 * Created by Igor on 05-Feb-16 at 7:19 PM.
 */
public abstract class Entity {
    protected Shader shader;
    protected VertexArrayObject vao;

    public Entity(Shader shader, VertexArrayObject vao) {
        this.shader = shader;
        this.vao = vao;
    }

    public VertexArrayObject getVertexArrayObject() {
        return vao;
    }

    public Shader getShader() {
        return shader;
    }

    /**
     * Expects the shader program to be enabled.
     */
    public abstract void setShaderParameters();
}

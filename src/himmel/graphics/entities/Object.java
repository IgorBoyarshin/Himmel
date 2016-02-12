package himmel.graphics.entities;

import himmel.graphics.Shader;
import himmel.graphics.buffers.VertexArrayObject;
import himmel.math.Matrix4f;

/**
 * Created by Igor on 07-Feb-16 at 9:01 PM.
 */
public abstract class Object extends Entity {
    protected Matrix4f modelMatrix;

    public Object(Shader shader, VertexArrayObject vao) {
        super(shader, vao);
        this.modelMatrix = Matrix4f.identity();
    }

    public Object(Matrix4f modelMatrix, Shader shader, VertexArrayObject vao) {
        super(shader, vao);
        this.modelMatrix = modelMatrix;
    }

    public void multiplyModelMatrixBy(Matrix4f matrix) {
        modelMatrix = modelMatrix.multiply(matrix);
    }

    public void setModelMatrix(Matrix4f modelMatrix) {
        this.modelMatrix = modelMatrix;
    }

    public Matrix4f getModelMatrix() {
        return modelMatrix;
    }

    @Override
    public void setShaderParameters() {
        shader.setUniformMat4f("ml_matrix", modelMatrix);
    }
}

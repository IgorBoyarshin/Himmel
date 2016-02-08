package himmel.graphics.entities;

import himmel.graphics.Shader;
import himmel.graphics.buffers.IndexBufferObject;
import himmel.graphics.buffers.VertexArrayObject;
import himmel.graphics.buffers.VertexBufferObject;
import himmel.math.Matrix4f;
import himmel.math.Vector3f;
import himmel.math.Vector4f;
import himmel.utils.FloatArray;

/**
 * Created by Igor on 08-Feb-16 at 7:09 PM.
 */
public class Rectangle extends Object {
    public Rectangle(Vector4f color, Shader shader) {
        super(shader, createVao(color));
    }

    public Rectangle(Vector4f color, Matrix4f modelMatrix, Shader shader) {
        super(modelMatrix, shader, createVao(color));
    }

    private static VertexArrayObject createVao(Vector4f color) {
        VertexArrayObject vao = new VertexArrayObject(VertexArrayObject.RenderingMode.TRIANGLES);
        vao.addVertexBufferObject(
                new VertexBufferObject(
                        new FloatArray[]{
                                new FloatArray(generateVertices(
                                        new Vector3f(0.0f, 0.0f, 0.0f),
                                        new Vector3f(1.0f, 1.0f, 1.0f))),
                                new FloatArray(generateNormals()),
                                new FloatArray(generateColors(color))
                        },
                        new int[]{3, 3, 4},
                        false),
                new int[]{0, 1, 2});
        vao.setIndexBufferObject(new IndexBufferObject(generateIndices(), false));

        return vao;
    }

    private static float[] generateVertices(Vector3f center, Vector3f size) {
        return new float[]{
                center.x - size.x / 2.0f, center.y - size.y / 2.0f, center.z,
                center.x - size.x / 2.0f, center.y + size.y / 2.0f, center.z,
                center.x + size.x / 2.0f, center.y + size.y / 2.0f, center.z,
                center.x + size.x / 2.0f, center.y - size.y / 2.0f, center.z
        };
    }

    private static float[] generateNormals() {
        return new float[]{
                0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f
        };
    }

    private static float[] generateColors(Vector4f color) {
        return new float[]{
                color.x, color.y, color.z, color.w,
                color.x, color.y, color.z, color.w,
                color.x, color.y, color.z, color.w,
                color.x, color.y, color.z, color.w
        };
    }

    private static short[] generateIndices() {
        return new short[]{
                0, 1, 2,
                0, 2, 3
        };
    }

    @Override
    public void setShaderParameters() {
        shader.setUniformMat4f("ml_matrix", modelMatrix);
    }
}

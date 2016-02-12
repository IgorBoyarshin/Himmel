package himmel.graphics.entities;

import himmel.graphics.Shader;
import himmel.graphics.textures.Texture;
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
    private Texture texture;

    public Rectangle(Vector4f color, Shader shader) {
        super(shader, createVao(color, null));
        this.texture = null;
    }

    public Rectangle(Vector4f color, Matrix4f modelMatrix, Shader shader, Texture texture) {
        super(modelMatrix, shader, createVao(color, texture));
        this.texture = texture;
    }

    private static VertexArrayObject createVao(Vector4f color, Texture texture) {
        VertexArrayObject vao = new VertexArrayObject(VertexArrayObject.RenderingMode.TRIANGLES);
        final int textureId = vao.addTexture(texture);
        vao.addVertexBufferObject(
                new VertexBufferObject(
                        new FloatArray[]{
                                new FloatArray(generateVertices(
                                        new Vector3f(0.0f, 0.0f, 0.0f),
                                        new Vector3f(1.0f, 1.0f, 1.0f))),
                                new FloatArray(generateNormals()),
                                new FloatArray(generateColors(color)),
                                new FloatArray(generateUvs()),
                                new FloatArray(new float[]{1.0f * textureId + 0.1f}, 4)
                        },
                        new int[]{3, 3, 4, 2, 1},
                        false),
                new int[]{0, 1, 2, 3, 4});
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

    private static float[] generateUvs() {
        return new float[]{
                0.0f, 1.0f,
                0.0f, 0.0f,
                1.0f, 0.0f,
                1.0f, 1.0f
        };
    }

//    @Override
//    public void setShaderParameters() {
//        shader.setUniformMat4f("ml_matrix", modelMatrix);
//    }
}

package himmel.graphics;

import himmel.graphics.buffers.Buffer;
import himmel.graphics.buffers.VertexArray;
import himmel.math.Matrix4f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

/**
 * Created by Igor on 10-Aug-15.
 */
public class Skybox {
    private Shader shader;
    private Texture cubeTexture;

    private String viewMatrixUniformName;
    private String projectionMatrixUniformName;
    private String cubeTextureUniformName;

    private VertexArray vao;

    public Skybox(String[] paths, Shader shader,
                  String projectionMatrixUniformName, String viewMatrixUniformName, String cubeTextureUniformName) {
        this.cubeTexture = new Texture(paths);
        this.shader = shader;
        this.viewMatrixUniformName = viewMatrixUniformName;
        this.projectionMatrixUniformName = projectionMatrixUniformName;
        this.cubeTextureUniformName = cubeTextureUniformName;
        shader.setUniform1i(cubeTextureUniformName, 0); // Change here for multiple

        this.vao = initVao();
    }

    public void render() {
//        glDepthMask(false);
        glDepthFunc(GL_LEQUAL);
        shader.enable();
        vao.bind();
        glActiveTexture(GL_TEXTURE0); // Change here for multiple
        glBindTexture(GL_TEXTURE_CUBE_MAP, cubeTexture.getTID());

        glDrawArrays(GL_TRIANGLES, 0, 36);

        vao.unbind();
        shader.disable();
        glDepthFunc(GL_LESS);
//        glDepthMask(true);
    }

    public void setViewMatrix(Matrix4f viewMatrix) {
        shader.enable();

        Matrix4f withoutTranslation = new Matrix4f();
        withoutTranslation.matrix = viewMatrix.matrix;
        withoutTranslation.matrix[4 * 3 + 0] = 0;
        withoutTranslation.matrix[4 * 3 + 1] = 0;
        withoutTranslation.matrix[4 * 3 + 2] = 0;

        shader.setUniformMat4fv(viewMatrixUniformName, withoutTranslation.toFloatBuffer());
    }

    public void setProjectionMatrix(Matrix4f projectionMatrix) {
        shader.enable();
        shader.setUniformMat4fv(projectionMatrixUniformName, projectionMatrix.toFloatBuffer());
    }

    private static VertexArray initVao() {
        float[] skyboxVertices = {
                -1.0f, 1.0f, -1.0f,
                -1.0f, -1.0f, -1.0f,
                1.0f, -1.0f, -1.0f,
                1.0f, -1.0f, -1.0f,
                1.0f, 1.0f, -1.0f,
                -1.0f, 1.0f, -1.0f,

                -1.0f, -1.0f, 1.0f,
                -1.0f, -1.0f, -1.0f,
                -1.0f, 1.0f, -1.0f,
                -1.0f, 1.0f, -1.0f,
                -1.0f, 1.0f, 1.0f,
                -1.0f, -1.0f, 1.0f,

                1.0f, -1.0f, -1.0f,
                1.0f, -1.0f, 1.0f,
                1.0f, 1.0f, 1.0f,
                1.0f, 1.0f, 1.0f,
                1.0f, 1.0f, -1.0f,
                1.0f, -1.0f, -1.0f,

                -1.0f, -1.0f, 1.0f,
                -1.0f, 1.0f, 1.0f,
                1.0f, 1.0f, 1.0f,
                1.0f, 1.0f, 1.0f,
                1.0f, -1.0f, 1.0f,
                -1.0f, -1.0f, 1.0f,

                -1.0f, 1.0f, -1.0f,
                1.0f, 1.0f, -1.0f,
                1.0f, 1.0f, 1.0f,
                1.0f, 1.0f, 1.0f,
                -1.0f, 1.0f, 1.0f,
                -1.0f, 1.0f, -1.0f,

                -1.0f, -1.0f, -1.0f,
                -1.0f, -1.0f, 1.0f,
                1.0f, -1.0f, -1.0f,
                1.0f, -1.0f, -1.0f,
                -1.0f, -1.0f, 1.0f,
                1.0f, -1.0f, 1.0f
        };

        VertexArray vao = new VertexArray();
        vao.addBuffer(new Buffer(skyboxVertices, 3), 0);
        return vao;
    }
}

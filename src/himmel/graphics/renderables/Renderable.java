package himmel.graphics.renderables;

import himmel.graphics.Shader;
import himmel.graphics.Texture;
import himmel.graphics.renderers.Renderer;
import himmel.math.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Igor on 21-May-15.
 */
public class Renderable {
    protected float[] vertices;
    protected float[] normals;
    protected float[] colors;
    protected Texture texture;
    protected short[] indices;
    //    protected List<Vector2f> uv;
    protected float[] uv;
    protected Renderer renderer;
    protected Shader shader;
    protected Matrix4f modelMatrix;

    protected boolean alive = true;

    public Renderable(float[] vertices, Texture texture, float[] uv, short[] indices, Renderer renderer, Shader shader) {
        this.vertices = vertices;
        this.normals = null;
        this.colors = null;
        this.texture = texture;
        this.uv = uv;
        this.indices = indices;

        this.renderer = renderer;
        this.shader = shader;
    }

    public Renderable(float[] vertices, float[] normals, Texture texture, float[] uv, short[] indices, Renderer renderer, Shader shader) {
        this.vertices = vertices;
        this.normals = normals;
        this.colors = null;
        this.texture = texture;
        this.uv = uv;
        this.indices = indices;

        this.renderer = renderer;
        this.shader = shader;
    }

    public Renderable(float[] vertices, float[] colors, short[] indices, Renderer renderer, Shader shader) {
        this.vertices = vertices;
        this.normals = null;
        this.colors = colors;
        this.texture = null;
        this.uv = null;
        this.indices = indices;

        this.renderer = renderer;
        this.shader = shader;
    }

    public Renderable(float[] vertices, float[] normals, float[] colors, short[] indices, Renderer renderer, Shader shader) {
        this.vertices = vertices;
        this.normals = normals;
        this.colors = colors;
        this.texture = null;
        this.uv = null;
        this.indices = indices;

        this.renderer = renderer;
        this.shader = shader;
    }

    public void submit(Renderer renderer) {
        renderer.submit(this);
    }

    public boolean isAlive() {
        return alive;
    }

    public void die() {
        alive = false;
    }

    public void setModelMatrix(Matrix4f matrix) {
        this.modelMatrix = matrix;
    }

    public Matrix4f getModelMatrix() {
        return modelMatrix;
    }

    public Renderer getRenderer() {
        return renderer;
    }

    public Shader getShader() {
        return shader;
    }

    public int getTID() {
        return texture == null ? 0 : texture.getTID();
    }

    public short[] getIndices() {
        return indices;
    }

    public float[] getVertices() {
        return vertices;
    }

    public float[] getNormals() {
        return normals;
    }

    public float[] getColors() {
        return colors;
    }

    public float[] getUV() {
        return uv;
    }
}

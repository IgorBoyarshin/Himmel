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
    protected short[] indices;
    protected float[] colors;
    protected Texture texture;
    protected List<Vector2f> uv;
    protected Renderer renderer;
    protected Shader shader;
    protected Matrix4f modelMatrix;

    protected boolean alive = true;

    public Renderable(float[] vertices, float[] normals, short[] indices, float[] colors, Renderer renderer, Shader shader) {
        this.vertices = vertices;
        this.normals = normals;
        this.indices = indices;
        this.colors = colors;

        this.renderer = renderer;
        this.shader = shader;

        uv = new ArrayList<>();
//        setDefaultUV();
    }

    public void submit(Renderer renderer) {
//        renderer.push(modelMatrix);
        renderer.submit(this);
//        renderer.pop();
    }

    public List<Vector2f> getUV() {
        return uv;
    }

//    private void setDefaultUV() {
//        uv.add(new Vector2f(0.0f, 1.0f));
//        uv.add(new Vector2f(0.0f, 0.0f));
//        uv.add(new Vector2f(1.0f, 0.0f));
//        uv.add(new Vector2f(1.0f, 1.0f));
//    }

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
//
    public float[] getVertices() {
        return vertices;
    }

    public float[] getNormals() { return normals; }

    public float[] getColors() {
        return colors;
    }

    public void setColors(float[] colors) {
        this.colors = colors;
    }
}

package himmel.graphics;

import himmel.graphics.renderers.Renderer;
import himmel.math.Matrix4f;
import himmel.math.Vector2f;
import himmel.math.Vector3f;
import himmel.math.Vector4f;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Igor on 21-May-15.
 */
public class Renderable {
    protected float[] vertices;
    protected short[] indices;
    protected float[] colors;
    protected Texture texture;
    protected List<Vector2f> uv;
    protected Renderer renderer;
    protected Shader shader;
    protected Matrix4f modelMatrix;

    protected boolean alive = true;

    public static class Content {

    }

    public Renderable(float[] vertices, short[] indices, float[] colors, Renderer renderer, Shader shader) {
        this.vertices = vertices;
        this.indices = indices;
        this.colors = colors;

        this.renderer = renderer;
        this.shader = shader;
//        modelMatrix = Matrix4f.rotation(20.0f, 0.0f, 0.0f, 1.0f);

        uv = new ArrayList<>();
        setDefaultUV();
    }

    public void submit(Renderer renderer) {
//        renderer.push(modelMatrix);
        renderer.submit(this);
//        renderer.pop();
    }

    public List<Vector2f> getUV() {
        return uv;
    }

    private void setDefaultUV() {
        uv.add(new Vector2f(0.0f, 1.0f));
        uv.add(new Vector2f(0.0f, 0.0f));
        uv.add(new Vector2f(1.0f, 0.0f));
        uv.add(new Vector2f(1.0f, 1.0f));
    }

    public boolean isAlive() {
        return alive;
    }

    public void die() {
        alive = false;
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

    public float[] getColors() {
        return colors;
    }

    public void setColors(float[] colors) {
        this.colors = colors;
    }
}

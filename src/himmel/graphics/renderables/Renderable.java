package himmel.graphics.renderables;

import himmel.graphics.Matrixable;
import himmel.graphics.Texture;
import himmel.graphics.renderers.Renderer;
import himmel.graphics.renderers.RenderingSet;
import himmel.math.*;

/**
 * Created by Igor on 21-May-15.
 */
public class Renderable implements Matrixable {
    private float[] vertices;
    private float[] normals;
    private float[] colors;
    private Texture texture;
    private short[] indices;
    private float[] uv;

    private RenderingSet renderingSet;

    private Matrix4f modelMatrix;

    // -1.0f === asking the Renderer to add this matrix to the shader
    // 0.0f === calculate on CPU
    // >0.0f === the is an ID assigned to use with shader
    private float matrixId;

    private boolean alive = true;
    private boolean hasChanged = true;


    public Renderable(float[] vertices, Texture texture, float[] uv, short[] indices, RenderingSet renderingSet) {
        this.vertices = vertices;
        this.normals = null;
        this.colors = null;
        this.texture = texture;
        this.uv = uv;
        this.indices = indices;
        this.matrixId = 0.0f;

        this.renderingSet = renderingSet;
    }

    public Renderable(float[] vertices, float[] normals, Texture texture, float[] uv, short[] indices, RenderingSet renderingSet) {
        this.vertices = vertices;
        this.normals = normals;
        this.colors = null;
        this.texture = texture;
        this.uv = uv;
        this.indices = indices;
        this.matrixId = 0.0f;

        this.renderingSet = renderingSet;
    }

    public Renderable(float[] vertices, float[] colors, short[] indices, RenderingSet renderingSet) {
        this.vertices = vertices;
        this.normals = null;
        this.colors = colors;
        this.texture = null;
        this.uv = null;
        this.indices = indices;
        this.matrixId = 0.0f;

        this.renderingSet = renderingSet;
    }

    public Renderable(float[] vertices, float[] normals, float[] colors, short[] indices, RenderingSet renderingSet) {
        this.vertices = vertices;
        this.normals = normals;
        this.colors = colors;
        this.texture = null;
        this.uv = null;
        this.indices = indices;
        this.matrixId = 0.0f;

        this.renderingSet = renderingSet;
    }

    public void setChanged(boolean changed) {
        hasChanged = changed;
    }

    public void submit(Renderer renderer) {
        renderer.submit(this);
    }

    public boolean isChanged() {
        return hasChanged;
    }

    public boolean isAlive() {
        return alive;
    }

    public void die() {
        alive = false;
        setChanged(true);
    }

    public void setModelMatrix(Matrix4f matrix) {
        this.modelMatrix = matrix;
        if (matrixId <= 0.0f) {
            setChanged(true);
        }
    }

    public Matrix4f getModelMatrix() {
        return modelMatrix;
    }

    public RenderingSet getRenderingSet() {
        return renderingSet;
    }

    // TODO: Decide, whether to avoid it in Text
    public void setTexture(Texture texture) {
        this.texture = texture;
        setChanged(true);
    }

    public int getTID() {
        return texture == null ? 0 : texture.getTID();
    }

    public void setIndices(short[] indices) {
        this.indices = indices;
    }

    public short[] getIndices() {
        return indices;
    }

    // TODO: decide, whether to keep it
    public void setVertices(float[] vertices) {
        this.vertices = vertices;
        setChanged(true);
    }

    public float[] getVertices() {
        return vertices;
    }

    public float[] getNormals() {
        return normals;
    }

    public void setColors(float[] colors) {
        this.colors = colors;
        setChanged(true);
    }

    public float[] getColors() {
        return colors;
    }

    public void setUV(float[] uv) {
        this.uv = uv;
        setChanged(true);
    }

    public float[] getUV() {
        return uv;
    }

    public float getMid() {
        return matrixId;
    }

    public void setMid(float mid) {
        this.matrixId = mid;
        setChanged(true);
    }
}

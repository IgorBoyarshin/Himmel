package himmel.graphics.renderables;

import himmel.graphics.*;
import himmel.graphics.renderers.Renderer;
import himmel.math.FloatArray;
import himmel.math.Vector2f;
import himmel.math.Vector3f;
import himmel.math.Vector4f;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Igor on 22-May-15.
 */
public class Sprite extends Renderable {
    private Vector3f position;
    private Vector2f size;

    public Sprite(Vector3f position, Vector2f size, Vector4f color, Renderer renderer, Shader shader) {
        super(convertVertices(position, size),
                convertColors(color),
                new short[]{0, 1, 2, 0, 2, 3},
                renderer, shader);

        this.position = position;
        this.size = size;
    }

    public Sprite(Vector3f position, Vector2f size, Texture texture, float[] uvs, Renderer renderer, Shader shader) {
        super(convertVertices(position, size),
                texture,
                uvs,
                new short[]{0, 1, 2, 0, 2, 3},
                renderer, shader);

        this.position = position;
        this.size = size;
    }

    private float[] getDefaultUvs() {
        return new float[]{0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f};
    }

    public static void transformIntoFloats(List<Vector2f> vectors) {
        float[] floats = new float[vectors.size() * 2];
        for (int i = 0; i < vectors.size(); i++) {
            floats[i * 2] = vectors.get(i).x;
            floats[i * 2 + 1] = vectors.get(i).y;
        }
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public void setNewPosition(Vector3f position) {
        this.vertices = convertVertices(position, size);
        this.position = position;
    }

    public void setUv(float[] uv) {
        this.uv = uv;
    }

    public void setSize(Vector2f size) {
        this.vertices = convertVertices(position, size);
        this.size = size;
    }

    public void scale(Vector2f scaler) {
        this.size.x *= scaler.x;
        this.size.y *= scaler.y;

        setSize(size);
    }

    public Vector2f getSize() {
        return size;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void move(Vector3f position) {
        setNewPosition(new Vector3f(this.position.x + position.x, this.position.y + position.y, this.position.z + position.z));
    }

    private static float[] convertVertices(Vector3f position, Vector2f size) {
        float[] vertices = new float[]{
                position.x, position.y, position.z,
                position.x, position.y + size.y, position.z,
                position.x + size.x, position.y + size.y, position.z,
                position.x + size.x, position.y, position.z
        };

        return vertices;
    }

    private static float[] convertColors(Vector4f color) {
        float[] colors = new float[]{
                color.x, color.y, color.z, color.w,
                color.x, color.y, color.z, color.w,
                color.x, color.y, color.z, color.w,
                color.x, color.y, color.z, color.w
        };

        return colors;
    }

    public void setColor(Vector4f color) {
        this.colors = convertColors(color);
    }
}
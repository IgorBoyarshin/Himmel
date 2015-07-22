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
//        super(convertVertices(position, size),
//                new float[]{-0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, 0.5f, -0.5f},
//                new short[]{0, 1, 2, 0, 2, 3},
//                convertColors(color), renderer, shader);
        super(prepareFloats(position, size, color),
                new short[]{0, 1, 2, 0, 2, 3}, renderer, shader);
        this.position = position;
        this.size = size;
//        modelMatrix = Matrix4f.translation(new Vector3f(position.x, 10.0f, 0.0f));
    }

//    public Sprite(Vector3f position, Vector2f size, Texture texture, Renderer renderer, Shader shader) {
//        super(convertVertices(position, size),
//                new float[]{-0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, 0.5f, -0.5f},
//                new short[]{0, 1, 2, 0, 2, 3},
//                convertColors(new Vector4f(1.0f, 1.0f, 0.0f, 1.0f)), renderer, shader);
//        this.texture = texture;
//        this.position = position;
//        this.size = size;
////        modelMatrix = Matrix4f.translation(new Vector3f(position.x, 10.0f, 0.0f));
//    }

    private static List<FloatArray> prepareFloats(Vector3f position, Vector2f size, Vector4f color) {
        List<FloatArray> result = new ArrayList<>();
        result.add(new FloatArray(convertVertices(position, size), 3));
        result.add(new FloatArray(getDefaultUvs(), 2));
        result.add(new FloatArray(convertColors(color), 4));
        return result;
    }

    private static float[] getDefaultUvs() {
        return new float[]{0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f};
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public void setNewPosition(Vector3f position) {
        setFloatArray(0, new FloatArray(convertVertices(position, size), 3));
        this.position = position;
    }

//    public static List<Vector2f> getDefaultUv() {
//        List<Vector2f> uv = new ArrayList<>();
//        uv.add(new Vector2f(0.0f, 1.0f));
//        uv.add(new Vector2f(0.0f, 0.0f));
//        uv.add(new Vector2f(1.0f, 0.0f));
//        uv.add(new Vector2f(1.0f, 1.0f));
//
//        return uv;
//    }

//    public List<Vector2f> getUv() {
//        return uv;
//    }

//    public void setUv(List<Vector2f> newUv) {
//        this.uv = newUv;
//    }

    public void setSize(Vector2f size) {
        setFloatArray(1, new FloatArray(convertVertices(position, size), 3));
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
        setFloatArray(1, new FloatArray(convertColors(color), 4));
    }
}

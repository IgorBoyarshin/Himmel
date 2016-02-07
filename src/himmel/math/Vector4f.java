package himmel.math;

/**
 * Created by Igor on 19-May-15.
 */
public class Vector4f {
    public float x, y, z, w;

    public Vector4f() {
        x = 0.0f;
        y = 0.0f;
        z = 0.0f;
        w = 0.0f;
    }

    public Vector4f(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Vector4f normalize() {
        final float length = getLength();
        if (length != 0) {
            x /= length;
            y /= length;
            z /= length;
            w /= length;
        }

        return this;
    }

    public float getLength() {
        return (float) Math.sqrt(x * x + y * y + z * z + w * w);
    }

    public Vector4f copy() {
        return new Vector4f(x, y, z, w);
    }

    public Vector4f add(Vector4f vector) {
        Vector4f result = this.copy();
        result.x += vector.x;
        result.y += vector.y;
        result.z += vector.z;
        result.w += vector.w;

        return result;
    }

    public Vector4f subtract(Vector4f vector) {
        Vector4f result = this.copy();
        result.x -= vector.x;
        result.y -= vector.y;
        result.z -= vector.z;
        result.w -= vector.w;

        return result;
    }

    public float dot(Vector4f vector) {
        return (x * vector.x + y * vector.y + z * vector.z + w * vector.w);
    }

    public static Vector4f add(Vector4f vector1, Vector4f vector2) {
        return vector1.add(vector2);
    }

    public static Vector4f subtract(Vector4f vector1, Vector4f vector2) {
        return vector1.subtract(vector2);
    }

    public static float dot(Vector4f vector1, Vector4f vector2) {
        return vector1.dot(vector2);
    }
}

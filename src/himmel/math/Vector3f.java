package himmel.math;

/**
 * Created by Igor on 01-May-15.
 */
public class Vector3f {
    public float x, y, z;

    public Vector3f() {
        x = 0.0f;
        y = 0.0f;
        z = 0.0f;
    }

    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3f normalize() {
        final float length = getLength();
        if (length != 0) {
            x /= length;
            y /= length;
            z /= length;
        }

        return this;
    }

    public float getLength() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    public Vector3f copy() {
        return new Vector3f(x, y, z);
    }

    public Vector3f add(Vector3f vector) {
        Vector3f result = this.copy();
        result.x += vector.x;
        result.y += vector.y;
        result.z += vector.z;

        return result;
    }

    public Vector3f subtract(Vector3f vector) {
        Vector3f result = this.copy();
        result.x -= vector.x;
        result.y -= vector.y;
        result.z -= vector.z;

        return result;
    }

    public float dot(Vector3f vector) {
        return (x * vector.x + y * vector.y + z * vector.z);
    }

    public static Vector3f add(Vector3f vector1, Vector3f vector2) {
        return vector1.add(vector2);
    }

    public static Vector3f subtract(Vector3f vector1, Vector3f vector2) {
        return vector1.subtract(vector2);
    }

    public static float dot(Vector3f vector1, Vector3f vector2) {
        return vector1.dot(vector2);
    }
}

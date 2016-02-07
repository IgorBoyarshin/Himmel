package himmel.math;

/**
 * Created by Igor on 01-May-15.
 */
public class Vector2f {
    public float x, y;

    public Vector2f() {
        x = 0.0f;
        y = 0.0f;
    }

    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2f normalize() {
        final float length = getLength();
        if (length != 0) {
            x /= length;
            y /= length;
        }

        return this;
    }

    public float getLength() {
        return (float) Math.sqrt(x * x + y * y);
    }

    public Vector2f copy() {
        return new Vector2f(x, y);
    }

    public Vector2f add(Vector2f vector) {
        Vector2f result = this.copy();
        result.x += vector.x;
        result.y += vector.y;

        return result;
    }

    public Vector2f subtract(Vector2f vector) {
        Vector2f result = this.copy();
        result.x -= vector.x;
        result.y -= vector.y;

        return result;
    }

    public float dot(Vector2f vector) {
        return (x * vector.x + y * vector.y);
    }

    public static Vector2f add(Vector2f vector1, Vector2f vector2) {
        return vector1.add(vector2);
    }

    public static Vector2f subtract(Vector2f vector1, Vector2f vector2) {
        return vector1.subtract(vector2);
    }

    public static float dot(Vector2f vector1, Vector2f vector2) {
        return vector1.dot(vector2);
    }
}

package himmel.math;

/**
 * Created by Igor on 01-May-15.
 */
public class Vector2f implements Normalizable{
    public float x, y;

    public Vector2f() {
        x = 0.0f;
        y = 0.0f;
    }

    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void normalize() {
        final float length = (float) Math.sqrt(x * x + y * y);
        if (length != 0) {
            x /= length;
            y /= length;
        }
    }
}

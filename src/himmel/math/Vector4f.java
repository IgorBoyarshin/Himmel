package himmel.math;

/**
 * Created by Igor on 19-May-15.
 */
public class Vector4f implements Normalizable {
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

    @Override
    public void normalize() {
        final float length = (float) Math.sqrt(x * x + y * y + z * z + w * w);
        if (length != 0) {
            x /= length;
            y /= length;
            z /= length;
            w /= length;
        }
    }
}

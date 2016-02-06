package himmel.math;

/**
 * Created by Igor on 19-May-15.
 */
public class Vector4f extends Vector {
    public float x, y, z, w;

    public Vector4f() {
        super(4);
        x = 0.0f;
        y = 0.0f;
        z = 0.0f;
        w = 0.0f;
    }

    public Vector4f(float x, float y, float z, float w) {
        super(4);
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    @Override
    public void normalize() {
        final float length = getLength();
        if (length != 0) {
            x /= length;
            y /= length;
            z /= length;
            w /= length;
        }
    }

    @Override
    public float getLength() {
        return (float) Math.sqrt(x * x + y * y + z * z + w * w);
    }

    @Override
    public Vector copy() {
        return new Vector4f(x, y, z, w);
    }

    @Override
    public Vector add(Vector vector) {
        Vector4f result = (Vector4f) this.copy();
        switch (vector.dimensions) {
            case 4:
                result.w += ((Vector4f) vector).w;
            case 3:
                result.z += ((Vector4f) vector).z;
            case 2:
                result.y += ((Vector4f) vector).y;
            case 1:
                result.x += ((Vector4f) vector).x;
                break;
        }

        return result;
    }

    @Override
    public Vector subtract(Vector vector) {
        Vector4f result = (Vector4f) this.copy();
        switch (vector.dimensions) {
            case 4:
                result.w -= ((Vector4f) vector).w;
            case 3:
                result.z -= ((Vector4f) vector).z;
            case 2:
                result.y -= ((Vector4f) vector).y;
            case 1:
                result.x -= ((Vector4f) vector).x;
                break;
        }

        return result;
    }

    @Override
    public float dot(Vector vector) {
        float result = 0.0f;
        switch (vector.dimensions) {
            case 4:
                result += w * ((Vector4f) vector).w;
            case 3:
                result += z * ((Vector4f) vector).z;
            case 2:
                result += y * ((Vector4f) vector).y;
            case 1:
                result += x * ((Vector4f) vector).x;
                break;
        }

        return result;
    }
}

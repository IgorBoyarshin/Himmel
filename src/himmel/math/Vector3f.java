package himmel.math;

/**
 * Created by Igor on 01-May-15.
 */
public class Vector3f extends Vector {
    public float x, y, z;

    public Vector3f() {
        super(3);
        x = 0.0f;
        y = 0.0f;
        z = 0.0f;
    }

    public Vector3f(float x, float y, float z) {
        super(3);
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void normalize() {
        final float length = getLength();
        if (length != 0) {
            x /= length;
            y /= length;
            z /= length;
        }
    }

    @Override
    public float getLength() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    @Override
    public Vector copy() {
        return new Vector3f(x, y, z);
    }

    @Override
    public Vector add(Vector vector) {
        Vector3f result = (Vector3f) this.copy();
        switch (vector.dimensions) {
            case 3:
                result.z += ((Vector3f) vector).z;
            case 2:
                result.y += ((Vector3f) vector).y;
            case 1:
                result.x += ((Vector3f) vector).x;
                break;
        }

        return result;
    }

    @Override
    public Vector subtract(Vector vector) {
        Vector3f result = (Vector3f) this.copy();
        switch (vector.dimensions) {
            case 3:
                result.z -= ((Vector3f) vector).z;
            case 2:
                result.y -= ((Vector3f) vector).y;
            case 1:
                result.x -= ((Vector3f) vector).x;
                break;
        }

        return result;
    }

    @Override
    public float dot(Vector vector) {
        float result = 0.0f;
        switch (vector.dimensions) {
            case 3:
                result += z * ((Vector3f) vector).z;
            case 2:
                result += y * ((Vector3f) vector).y;
            case 1:
                result += x * ((Vector3f) vector).x;
                break;
        }

        return result;
    }
}

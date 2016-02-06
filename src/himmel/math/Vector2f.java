package himmel.math;

/**
 * Created by Igor on 01-May-15.
 */
public class Vector2f extends Vector {
    public float x, y;

    public Vector2f() {
        super(2);
        x = 0.0f;
        y = 0.0f;
    }

    public Vector2f(float x, float y) {
        super(2);
        this.x = x;
        this.y = y;
    }

    @Override
    public void normalize() {
        final float length = getLength();
        if (length != 0) {
            x /= length;
            y /= length;
        }
    }

    @Override
    public float getLength() {
        return (float) Math.sqrt(x * x + y * y);
    }

    @Override
    public Vector copy() {
        return new Vector2f(x, y);
    }

    @Override
    public Vector add(Vector vector) {
        Vector2f result = (Vector2f) this.copy();
        switch (vector.dimensions) {
            case 2:
                result.y += ((Vector2f) vector).y;
            case 1:
                result.x += ((Vector2f) vector).x;
                break;
        }

        return result;
    }

    @Override
    public Vector subtract(Vector vector) {
        Vector2f result = (Vector2f) this.copy();
        switch (vector.dimensions) {
            case 2:
                result.y -= ((Vector2f) vector).y;
            case 1:
                result.x -= ((Vector2f) vector).x;
                break;
        }

        return result;
    }

    @Override
    public float dot(Vector vector) {
        float result = 0.0f;
        switch (vector.dimensions) {
            case 2:
                result += y * ((Vector2f) vector).y;
            case 1:
                result += x * ((Vector2f) vector).x;
                break;
        }

        return result;
    }
}

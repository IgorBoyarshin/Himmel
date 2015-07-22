package himmel.math;

/**
 * Created by Igor on 22-Jul-15.
 */
public class FloatArray {
    private float[] array;

    // The way floats are combined(vec3, for example)
    private int count;

    public FloatArray(float[] array, int count) {
        this.array = array;
        this.count = count;
    }

    public int size() {
        return array.length;
    }

    public float getFloat(int id) {
        if (id >= array.length) {
            return 0.0f;
        }

        return array[id];
    }

    public Vector2f getVector2f(int id) {
        final int number = 2;
        if (array.length % number != 0 || id >= array.length) {
            return null;
        }

        return new Vector2f(array[number * id], array[number * id + 1]);
    }

    public Vector3f getVector3f(int id) {
        final int number = 3;
        if (array.length % number != 0 || id >= array.length) {
            return null;
        }

        return new Vector3f(array[number * id], array[number * id + 1], array[number * id + 2]);
    }

    public Vector4f getVector4f(int id) {
        final int number = 4;
        if (array.length % number != 0 || id >= array.length) {
            return null;
        }

        return new Vector4f(array[number * id], array[number * id + 1], array[number * id + 2], array[number * id + 3]);
    }

    public int getCount() {
        return count;
    }
}

package himmel.utils;

/**
 * Created by Igor on 03-Feb-16 at 9:04 PM.
 * <p>
 * Class provides an easy way to store a float array.
 */
public class FloatArray {
    public float elements[];

    public FloatArray(float elements[]) {
        this.elements = elements;
    }

    public FloatArray(float[] block, int count) {
        this.elements = new float[block.length * count];
        for (int i = 0; i < count; i++) {
            for (int j = 0; j < block.length; j++) {
                elements[i * block.length + j] = block[j];
            }
        }
    }

    public int length() {
        return elements.length;
    }
}

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

    public FloatArray(float element, int count) {
        this.elements = new float[count];
        for (int i = 0; i < count; i++) {
            elements[i] = element;
        }
    }

    public int length() {
        return elements.length;
    }
}

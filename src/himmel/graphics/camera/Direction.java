package himmel.graphics.camera;

import himmel.math.Vector3f;

/**
 * Created by Igor on 29-Jul-15.
 */
public enum Direction {
    FORWARD(new Vector3f(0.0f, 0.0f, -1.0f)),
    RIGHT(new Vector3f(1.0f, 0.0f, 0.0f)),
    UP(new Vector3f(0.0f, 1.0f, 0.0f));

    private final Vector3f vector;

    public final Vector3f getVector() {
        return vector;
    }

    Direction(Vector3f vector) {
        this.vector = vector;
    }
}

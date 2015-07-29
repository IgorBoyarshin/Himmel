package himmel.graphics.camera;

import himmel.graphics.Matrixable;
import himmel.math.Matrix4f;
import himmel.math.Vector3f;

/**
 * Created by Igor on 29-Jul-15.
 */
public class ProCamera extends Camera {

    public ProCamera(Vector3f position) {
        super(position);
    }

    public ProCamera(Vector3f position, float pitch, float yaw, float roll) {
        super(position, pitch, yaw, roll);
    }

    @Override
    public Matrix4f getViewMatrix() {
        return null;
    }

    @Override
    public void attachTo(Matrixable object) {

    }

    @Override
    public void move(Direction direction, float length) {

    }

    @Override
    public Matrix4f getModelMatrix() {
        return null;
    }
}

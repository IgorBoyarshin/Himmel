package himmel.graphics.camera;

import himmel.graphics.Matrixable;
import himmel.math.Matrix4f;
import himmel.math.Vector3f;

/**
 * Created by Igor on 29-Jul-15.
 */
public class SimpleCamera extends Camera{

    public SimpleCamera(Vector3f position) {
        super(position);
    }

    public SimpleCamera(Vector3f position, float pitch, float yaw) {
        super(position, pitch, yaw, 0.0f);
    }

    @Override
    public Matrix4f getViewMatrix() {
        return null;
    }

    @Override
    public void attachTo(Matrixable object) {

    }

    @Override
    public void move(Vector3f direction, float length) {

    }

    @Override
    public Matrix4f getModelMatrix() {
        return null;
    }
}

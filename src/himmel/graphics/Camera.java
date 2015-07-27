package himmel.graphics;

import himmel.math.Matrix4f;
import himmel.math.Vector3f;

/**
 * Created by Igor on 20-Jul-15.
 */
public class Camera {
    private Vector3f cameraPosition;
    private float pitch; // tangazh
    private float yaw; // riskanie
    private float roll; // kren

    public Camera(Vector3f position) {
        this.cameraPosition = position;
        this.pitch = 0.0f;
        this.yaw = 0.0f;
    }

    public Camera(Vector3f position, float pitch, float yaw, float roll) {
        this.cameraPosition = position;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    public Matrix4f getMatrix() {
        return new Matrix4f(1.0f)
                .multiply(Matrix4f.rotation(yaw, 0.0f, 1.0f, 0.0f))
//                .multiply(Matrix4f.rotation(pitch, 1.0f, 0.0f, 0.0f))
                .multiply(Matrix4f.translation(cameraPosition));
    }

    // TODO
    // Parameter: Object
    public void attachTo() {

    }

    // TODO
    public void move(Vector3f direction, float length) {

    }

    // TODO
    public Vector3f getDirection() {
        return null;
    }


    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void setRoll(float roll) {
        this.roll = roll;
    }

    public float getRoll() {
        return roll;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setNewPosition(Vector3f position) {
        this.cameraPosition = position;
    }

    public void shift(Vector3f shift) {
        this.cameraPosition.x += shift.x;
        this.cameraPosition.y += shift.y;
        this.cameraPosition.z += shift.z;
    }

    public Vector3f getPosition() {
        return cameraPosition;
    }
}

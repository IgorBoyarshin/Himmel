package himmel.graphics;

import himmel.math.Vector3f;

/**
 * Created by Igor on 20-Jul-15.
 */
public class Camera {
    private Vector3f cameraPosition;
    private float pitch; // tangazh
    private float yaw; // riskanie

    public Camera(Vector3f position) {
        this.cameraPosition = position;
        this.pitch = 0.0f;
        this.yaw = 0.0f;
    }

    public Camera(Vector3f position, float pitch, float yaw) {
        this.cameraPosition = position;
        this.pitch = pitch;
        this.yaw = yaw;
    }


    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
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

    public void move(Vector3f shift) {
        this.cameraPosition.x += shift.x;
        this.cameraPosition.y += shift.y;
        this.cameraPosition.z += shift.z;
    }

    public Vector3f getPosition() {
        return cameraPosition;
    }
}

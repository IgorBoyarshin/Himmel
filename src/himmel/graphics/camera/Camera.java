package himmel.graphics.camera;

import himmel.graphics.Matrixable;
import himmel.math.Matrix4f;
import himmel.math.Vector3f;

/**
 * Created by Igor on 20-Jul-15.
 */
public abstract class Camera implements Matrixable {
    protected Vector3f cameraPosition;
    protected float pitch; // tangazh
    protected float yaw; // riskanie
    protected float roll; // kren

    public Camera(Vector3f position) {
        this.cameraPosition = position;
        this.pitch = 0.0f;
        this.yaw = 0.0f;
        this.roll = 0.0f;
    }

    public Camera(Vector3f position, float pitch, float yaw, float roll) {
        this.cameraPosition = position;
        this.pitch = pitch;
        this.yaw = yaw;
        this.roll = roll;
    }

    public abstract Matrix4f getViewMatrix();

    public abstract void attachTo(Matrixable object);

    public abstract void move(Direction direction, float length);

    public void setYaw(float yaw) {
        if (yaw >= 360.0f) {
            yaw -= 360.0f;
        }
        if (yaw < 0.0f) {
            yaw += 360.0f;
        }
        this.yaw = yaw;
    }

    public void setPitch(float pitch) {
        if (pitch >= 360.0f) {
            pitch -= 360.0f;
        }
        if (pitch < 0.0f) {
            pitch += 360.0f;
        }
        this.pitch = pitch;
    }

    public void setRoll(float roll) {
        if (roll >= 360.0f) {
            roll -= 360.0f;
        }
        if (roll < 0.0f) {
            roll += 360.0f;
        }
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

    public void setPosition(Vector3f position) {
        this.cameraPosition = position;
    }

    public Vector3f getPosition() {
        return cameraPosition;
    }

    public void shift(Vector3f shift) {
        this.cameraPosition.x += shift.x;
        this.cameraPosition.y += shift.y;
        this.cameraPosition.z += shift.z;
    }

    public void printPosition() {
        System.out.println("Camera position (" + cameraPosition.x + ";" + cameraPosition.y + ";" + cameraPosition.z + ")");
    }
}

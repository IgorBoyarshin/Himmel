package himmel.graphics.camera;

import himmel.graphics.Matrixable;
import himmel.math.Matrix4f;
import himmel.math.Vector3f;

/**
 * Created by Igor on 29-Jul-15.
 */
public class SimpleCamera extends Camera {

    private final float MAX_PITCH = 0.98f * 90.0f;
    private final float MIN_PITCH = -0.98f * 90.0f;

    public SimpleCamera(Vector3f position) {
        super(position);
    }

    public SimpleCamera(Vector3f position, float pitch, float yaw) {
        super(position, pitch, yaw, 0.0f);
    }

    @Override
    public void setPitch(float pitch) {
        if (pitch >= MAX_PITCH) {
            pitch = MAX_PITCH;
        }
        if (pitch <= MIN_PITCH) {
            pitch = MIN_PITCH;
        }

        this.pitch = pitch;
    }

    @Override
    public Matrix4f getViewMatrix() {
        Matrix4f rot1 = Matrix4f.rotation(-pitch, 1.0f, 0.0f, 0.0f);
        Matrix4f rot2 = Matrix4f.rotation(-yaw, 0.0f, 1.0f, 0.0f);
        Matrix4f pos = Matrix4f.translation(new Vector3f(-cameraPosition.x, -cameraPosition.y, -cameraPosition.z));
        return rot1.multiply(rot2).multiply(pos);
    }

    @Override
    public void attachTo(Matrixable object) {

    }

    @Override
    public void move(Vector3f direction, float length) {
        // Assuming that the vector is normalized.
        // May and probably will behave inappropriately if the vector is not parallel with one of the axis.

        // Forward
        cameraPosition.x += direction.z * length * (float) Math.sin(Math.toRadians(yaw));
        cameraPosition.y += direction.z * -length * (float) Math.sin(Math.toRadians(pitch));
        cameraPosition.z += direction.z * length * (float) (Math.cos(Math.toRadians(pitch)) * Math.cos(Math.toRadians(yaw)));
        // Right
        cameraPosition.x += direction.x * -length * (float) Math.cos(Math.toRadians(yaw));
        cameraPosition.z += direction.x * length * (float) (Math.sin(Math.toRadians(yaw)));
        // Up
        cameraPosition.y += direction.y * length;

    }

    @Override
    public Matrix4f getModelMatrix() {
        Matrix4f rot1 = Matrix4f.rotation(pitch, 1.0f, 0.0f, 0.0f);
        Matrix4f rot2 = Matrix4f.rotation(yaw, 0.0f, 1.0f, 0.0f);
        Matrix4f pos = Matrix4f.translation(new Vector3f(cameraPosition.x, cameraPosition.y, cameraPosition.z));
        return rot1.multiply(rot2).multiply(pos);
    }
}

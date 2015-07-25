package himmel.math;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;

/**
 * Created by Igor on 01-May-15.
 */
public class Matrix4f {
    private final int SIZE = 4 * 4;
    public float[] matrix = new float[SIZE];

    public Matrix4f() {
        for (int i = 0; i < SIZE; i++) {
            matrix[i] = 0.0f;
        }
    }

    public Matrix4f(float diagonal) {
        for (int i = 0; i < SIZE; i++) {
            matrix[i] = 0.0f;
        }

        matrix[0 + 0 * 4] = diagonal;
        matrix[1 + 1 * 4] = diagonal;
        matrix[2 + 2 * 4] = diagonal;
        matrix[3 + 3 * 4] = diagonal;
    }

    public static Matrix4f orthographic(float left, float right, float bottom, float top, float near, float far) {
        Matrix4f result = new Matrix4f(1.0f);

        result.matrix[0 + 0 * 4] = 2.0f / (right - left);

        result.matrix[1 + 1 * 4] = 2.0f / (top - bottom);

        result.matrix[2 + 2 * 4] = 2.0f / (near - far);

        result.matrix[0 + 3 * 4] = (left + right) / (left - right);
        result.matrix[1 + 3 * 4] = (bottom + top) / (bottom - top);
        result.matrix[2 + 3 * 4] = (far + near) / (far - near);

        return result;
    }

    public static Matrix4f perspective(final int WINDOW_WIDTH, final int WINDOW_HEIGHT,
                                       float fFovDeg, float fzNear, float fzFar) {
        Matrix4f result = new Matrix4f(1.0f);

        float degToRad = 3.14159f * 2.0f / 360.0f;
        float fFovRad = fFovDeg * degToRad;
        float fFrustumScale = 1.0f / (float) Math.tan(fFovRad / 2.0f);

        result.matrix[0] = fFrustumScale / (WINDOW_WIDTH * 1.0f / (float) WINDOW_HEIGHT);
        result.matrix[5] = fFrustumScale;
        result.matrix[10] = (fzFar + fzNear) / (fzNear - fzFar);
        result.matrix[14] = (2 * fzFar * fzNear) / (fzNear - fzFar);
        result.matrix[11] = -1.0f;

        return result;
    }

    public static Matrix4f identity() {
        Matrix4f result = new Matrix4f(1.0f);

        return result;
    }

    public Vector3f multiply(float x, float y, float z) {
        float xx = matrix[0 + 0 * 4] * x + matrix[0 + 1 * 4] * y + matrix[0 + 2 * 4] * z + matrix[0 + 3 * 4];
        float yy = matrix[1 + 0 * 4] * x + matrix[1 + 1 * 4] * y + matrix[1 + 2 * 4] * z + matrix[1 + 3 * 4];
        float zz = matrix[2 + 0 * 4] * x + matrix[2 + 1 * 4] * y + matrix[2 + 2 * 4] * z + matrix[2 + 3 * 4];

        return new Vector3f(xx, yy, zz);
    }

    public Vector3f multiply(Vector3f vector) {
        float x = matrix[0 + 0 * 4] * vector.x + matrix[0 + 1 * 4] * vector.y + matrix[0 + 2 * 4] * vector.z + matrix[0 + 3 * 4];
        float y = matrix[1 + 0 * 4] * vector.x + matrix[1 + 1 * 4] * vector.y + matrix[1 + 2 * 4] * vector.z + matrix[1 + 3 * 4];
        float z = matrix[2 + 0 * 4] * vector.x + matrix[2 + 1 * 4] * vector.y + matrix[2 + 2 * 4] * vector.z + matrix[2 + 3 * 4];

        return new Vector3f(x, y, z);
    }

    public Matrix4f multiply(Matrix4f matrixB) {
        Matrix4f result = new Matrix4f(1.0f);

        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                float sum = 0.0f;
                for (int e = 0; e < 4; e++) {
                    sum += this.matrix[x + e * 4] * matrixB.matrix[e + y * 4];
                }
                result.matrix[x + y * 4] = sum;
            }
        }
//        this.matrix = result.matrix;

        return result;
    }

    public Matrix4f scale(Vector3f vector) {
        matrix[0 + 0 * 4] *= vector.x;
        matrix[1 + 1 * 4] *= vector.y;
        matrix[2 + 2 * 4] *= vector.z;

        return this;
    }

    public static Matrix4f scaling(Vector3f vector) {
        Matrix4f result = identity();

        result.matrix[0 + 0 * 4] = vector.x;
        result.matrix[1 + 1 * 4] = vector.y;
        result.matrix[2 + 2 * 4] = vector.z;

        return result;
    }

    public Matrix4f translate(Vector3f vector) {
        matrix[0 + 3 * 4] += vector.x;
        matrix[1 + 3 * 4] += vector.y;
        matrix[2 + 3 * 4] += vector.z;

        return this;
    }

    public static Matrix4f translation(Vector3f vector) {
        Matrix4f result = identity();

        result.matrix[0 + 3 * 4] = vector.x;
        result.matrix[1 + 3 * 4] = vector.y;
        result.matrix[2 + 3 * 4] = vector.z;

        return result;
    }

    public Matrix4f rotate(float angle, float x, float y, float z) {
        this.matrix = rotation(angle, x, y, z).matrix;
        return this;
    }

    /**
     * Angle in Degrees
     */
    public static Matrix4f rotation(float angle, float x, float y, float z) {
        Matrix4f result = identity();
        float r = (float) toRadians(angle);
        float cos = (float) cos(r);
        float sin = (float) sin(r);
        float omc = 1.0f - cos;

        result.matrix[0 + 0 * 4] = x * omc + cos;
        result.matrix[1 + 0 * 4] = y * x * omc + z * sin;
        result.matrix[2 + 0 * 4] = x * z * omc - y * sin;

        result.matrix[0 + 1 * 4] = x * y * omc - z * sin;
        result.matrix[1 + 1 * 4] = y * omc + cos;
        result.matrix[2 + 1 * 4] = y * z * omc + x * sin;

        result.matrix[0 + 2 * 4] = x * z * omc + y * sin;
        result.matrix[1 + 2 * 4] = y * z * omc - x * sin;
        result.matrix[2 + 2 * 4] = z * omc + cos;

        return result;
    }

    public FloatBuffer toFloatBuffer() {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(matrix.length);
        buffer.put(matrix);
        buffer.flip();
        return buffer;
    }

    public void print() {
        System.out.print(matrix[0 + 0 * 4] + " ");
        System.out.print(matrix[0 + 1 * 4] + " ");
        System.out.print(matrix[0 + 2 * 4] + " ");
        System.out.print(matrix[0 + 3 * 4] + " ");
        System.out.println();
        System.out.print(matrix[1 + 0 * 4] + " ");
        System.out.print(matrix[1 + 1 * 4] + " ");
        System.out.print(matrix[1 + 2 * 4] + " ");
        System.out.print(matrix[1 + 3 * 4] + " ");
        System.out.println();
        System.out.print(matrix[2 + 0 * 4] + " ");
        System.out.print(matrix[2 + 1 * 4] + " ");
        System.out.print(matrix[2 + 2 * 4] + " ");
        System.out.print(matrix[2 + 3 * 4] + " ");
        System.out.println();
        System.out.print(matrix[3 + 0 * 4] + " ");
        System.out.print(matrix[3 + 1 * 4] + " ");
        System.out.print(matrix[3 + 2 * 4] + " ");
        System.out.print(matrix[3 + 3 * 4] + " ");
        System.out.println();
    }
}

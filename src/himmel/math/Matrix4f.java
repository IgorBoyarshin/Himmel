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
    private final int DIMENTION = 4;
    private final int SIZE = DIMENTION * DIMENTION;

    // Uses column-major ordering
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

    public static Matrix4f perspective(final float width, final float height,
                                       float fFovDeg, float fzNear, float fzFar) {
        Matrix4f result = new Matrix4f(1.0f);

        final float fFovRad = (float) Math.toRadians(fFovDeg);
        final float fFrustumScale = 1.0f / (float) Math.tan(fFovRad / 2.0f);

        result.matrix[0] = fFrustumScale / (width / height);
        result.matrix[5] = fFrustumScale;
        result.matrix[10] = (fzFar + fzNear) / (fzNear - fzFar);
        result.matrix[14] = (2 * fzFar * fzNear) / (fzNear - fzFar);
        result.matrix[11] = -1.0f;

        return result;
    }

    public static Matrix4f identity() {
        return new Matrix4f(1.0f);
    }

    // The following two methods are disabled for now because I have not decided yet
    // whether the vector should be interpreted as (x,y,z,0) or (x,y,z,1).

//    public Vector3f multiply(float x, float y, float z) {
//        final float xx = matrix[0 + 0 * 4] * x + matrix[0 + 1 * 4] * y + matrix[0 + 2 * 4] * z + matrix[0 + 3 * 4];
//        final float yy = matrix[1 + 0 * 4] * x + matrix[1 + 1 * 4] * y + matrix[1 + 2 * 4] * z + matrix[1 + 3 * 4];
//        final float zz = matrix[2 + 0 * 4] * x + matrix[2 + 1 * 4] * y + matrix[2 + 2 * 4] * z + matrix[2 + 3 * 4];
//
//        return new Vector3f(xx, yy, zz);
//    }
//
//    public Vector3f multiply(Vector3f vector) {
//        final float x = matrix[0 + 0 * 4] * vector.x + matrix[0 + 1 * 4] * vector.y + matrix[0 + 2 * 4] * vector.z + matrix[0 + 3 * 4];
//        final float y = matrix[1 + 0 * 4] * vector.x + matrix[1 + 1 * 4] * vector.y + matrix[1 + 2 * 4] * vector.z + matrix[1 + 3 * 4];
//        final float z = matrix[2 + 0 * 4] * vector.x + matrix[2 + 1 * 4] * vector.y + matrix[2 + 2 * 4] * vector.z + matrix[2 + 3 * 4];
//
//        return new Vector3f(x, y, z);
//    }

    public Vector4f multiply(float x, float y, float z, float w) {
        final float xx = matrix[0 + 0 * 4] * x + matrix[0 + 1 * 4] * y
                + matrix[0 + 2 * 4] * z + matrix[0 + 3 * 4] * w;
        final float yy = matrix[1 + 0 * 4] * x + matrix[1 + 1 * 4] * y
                + matrix[1 + 2 * 4] * z + matrix[1 + 3 * 4] * w;
        final float zz = matrix[2 + 0 * 4] * x + matrix[2 + 1 * 4] * y
                + matrix[2 + 2 * 4] * z + matrix[2 + 3 * 4] * w;
        final float ww = matrix[3 + 0 * 4] * x + matrix[3 + 1 * 4] * y
                + matrix[3 + 2 * 4] * z + matrix[3 + 3 * 4] * w;

        return new Vector4f(xx, yy, zz, ww);
    }

    public Vector4f multiply(Vector4f vector) {
        final float x = matrix[0 + 0 * 4] * vector.x + matrix[0 + 1 * 4] * vector.y
                + matrix[0 + 2 * 4] * vector.z + matrix[0 + 3 * 4] * vector.w;
        final float y = matrix[1 + 0 * 4] * vector.x + matrix[1 + 1 * 4] * vector.y
                + matrix[1 + 2 * 4] * vector.z + matrix[1 + 3 * 4] * vector.w;
        final float z = matrix[2 + 0 * 4] * vector.x + matrix[2 + 1 * 4] * vector.y
                + matrix[2 + 2 * 4] * vector.z + matrix[2 + 3 * 4] * vector.w;
        final float w = matrix[3 + 0 * 4] * vector.x + matrix[3 + 1 * 4] * vector.y
                + matrix[3 + 2 * 4] * vector.z + matrix[3 + 3 * 4] * vector.w;

        return new Vector4f(x, y, z, w);
    }

    /**
     * Does not alter this matrix.
     *
     * @return A new matrix that is the result of multiplication of this matrix over the argument.
     */
    public Matrix4f multiply(Matrix4f otherMatrix) {
        Matrix4f result = new Matrix4f();

        for (int row = 0; row < DIMENTION; row++) {
            for (int column = 0; column < DIMENTION; column++) {
                float element = 0.0f;
                for (int index = 0; index < DIMENTION; index++) {
                    element += this.matrix[row + index * DIMENTION] * otherMatrix.matrix[index + column * DIMENTION];
                }
                result.matrix[row + column * DIMENTION] = element;
            }
        }

        return result;
    }

    /**
     * Does not alter this matrix.
     */
    public Matrix4f scale(Vector3f vector) {
        return this.multiply(scaling(vector));
    }

    public static Matrix4f scaling(Vector3f vector) {
        Matrix4f result = identity();

        result.matrix[0 + 0 * 4] = vector.x;
        result.matrix[1 + 1 * 4] = vector.y;
        result.matrix[2 + 2 * 4] = vector.z;

        return result;
    }

    /**
     * Does not alter this matrix.
     */
    public Matrix4f translate(Vector3f vector) {
        return this.multiply(translation(vector));
    }

    public static Matrix4f translation(Vector3f vector) {
        Matrix4f result = identity();

        result.matrix[0 + 3 * 4] = vector.x;
        result.matrix[1 + 3 * 4] = vector.y;
        result.matrix[2 + 3 * 4] = vector.z;

        return result;
    }

    /**
     * Does not alter this matrix.
     */
    public Matrix4f rotate(float angle, float x, float y, float z) {
        return this.multiply(rotation(angle, x, y, z));
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

    /**
     * Does not alter this matrix.
     */
    public Matrix4f rotateAboutAxis(float angle, Vector3f axis) {
        return this.multiply(rotationAboutAxis(angle, axis));
    }

    /**
     * Angle in Degrees
     */
    public static Matrix4f rotationAboutAxis(float angle, Vector3f axis) {
        Matrix4f result = identity();

        final float r = (float) toRadians(angle);
        final float argument = r / 2.0f;
        final float sin = (float) sin(argument);
        final float q0 = (float) cos(argument);
        final float q1 = sin * axis.x;
        final float q2 = sin * axis.y;
        final float q3 = sin * axis.z;
        final float q02 = q0 * q0;
        final float q12 = q1 * q1;
        final float q22 = q2 * q2;
        final float q32 = q3 * q3;

        result.matrix[0 + 0 * 4] = q02 + q12 - q22 - q32;
        result.matrix[1 + 0 * 4] = 2.0f * (q2 * q1 + q0 * q3);
        result.matrix[2 + 0 * 4] = 2.0f * (q3 * q1 - q0 * q2);

        result.matrix[0 + 1 * 4] = 2.0f * (q1 * q2 - q0 * q3);
        result.matrix[1 + 1 * 4] = q02 - q12 + q22 - q32;
        result.matrix[2 + 1 * 4] = 2.0f * (q3 * q2 + q0 * q1);

        result.matrix[0 + 2 * 4] = 2.0f * (q1 * q3 + q0 * q2);
        result.matrix[1 + 2 * 4] = 2.0f * (q2 * q3 - q0 * q1);
        result.matrix[2 + 2 * 4] = q02 - q12 - q22 + q32;

        return result;
    }

    public FloatBuffer toFloatBuffer() {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(matrix.length);
        buffer.put(matrix);
        buffer.flip();
        return buffer;
    }
}

package himmel.graphics;

import himmel.math.Matrix4f;
import himmel.math.Vector2f;
import himmel.math.Vector3f;
import himmel.math.Vector4f;
import himmel.utils.ShaderUtils;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glUseProgram;

/**
 * Created by Igor on 01-May-15.
 */
public class Shader {
//    public static final int ATTR_VERTEX = 0;
//    public static final int ATTR_NORMAL = 1;
//    public static final int ATTR_UV = 2;
//    public static final int ATTR_TID = 3;
//    public static final int ATTR_COLOR = 4;

    private Map<String, Integer> locationCache = new HashMap<String, Integer>();

    private final int ID;

    public Shader(String vertex, String fragment) {
        ID = ShaderUtils.load(vertex, fragment);
    }

    public int getUniform(String name) {
        if (locationCache.containsKey(name)) {
            return locationCache.get(name);
        }

        int result = glGetUniformLocation(ID, name);
        if (result == -1) {
            System.err.println(":> Could not find uniform variable '" + name + "'!");
        } else {
            locationCache.put(name, result);
        }

        return result;
    }

    public void setUniform1i(String name, int i) {
        glUniform1i(getUniform(name), i);
    }

    public void setUniform1iv(String name, IntBuffer i) {
        glUniform1iv(getUniform(name), i);
    }

    public void setUniform1fv(String name, FloatBuffer i) {
        glUniform1fv(getUniform(name), i);
    }

    public void setUniform1f(String name, float f) {
        glUniform1f(getUniform(name), f);
    }

    public void setUniform2f(String name, Vector2f vector) {
        glUniform2f(getUniform(name), vector.x, vector.y);
    }

    public void setUniform3f(String name, Vector3f vector) {
        glUniform3f(getUniform(name), vector.x, vector.y, vector.z);
    }

    public void setUniform4f(String name, Vector4f vector) {
        glUniform4f(getUniform(name), vector.x, vector.y, vector.z, vector.w);
    }

    public void setUniformMat4f(String name, Matrix4f matrix) {
        glUniformMatrix4fv(getUniform(name), false, matrix.toFloatBuffer());
    }

    public void setUniformMat4fv(String name, FloatBuffer matrices) {
        glUniformMatrix4fv(getUniform(name), false, matrices);
    }

    public void enable() {
        glUseProgram(ID);
    }

    public void disable() {
        glUseProgram(0);
    }
}

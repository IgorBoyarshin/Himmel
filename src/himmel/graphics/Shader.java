package himmel.graphics;

import himmel.math.Matrix4f;
import himmel.math.Vector2f;
import himmel.math.Vector3f;
import himmel.math.Vector4f;
import himmel.utils.ShaderUtils;

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

    private final int shaderId;
    private final String id;

    private static final String idSeparator = "*";

    public Shader(String vertex, String fragment) {
        shaderId = ShaderUtils.load(vertex, fragment);
        this.id = getFileName(vertex) + idSeparator + getFileName(fragment);
    }

    public Shader(String vertex, String fragment, String geometry) {
        shaderId = ShaderUtils.load(vertex, fragment, geometry);
        this.id = getFileName(vertex) + idSeparator + getFileName(fragment) + idSeparator + getFileName(geometry);
    }

    public String getId() {
        return id;
    }

    public int getUniform(String name) {
        if (locationCache.containsKey(name)) {
            return locationCache.get(name);
        }

        int result = glGetUniformLocation(shaderId, name);
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
        glUseProgram(shaderId);
    }

    public void disable() {
        glUseProgram(0);
    }

    private static String getFileName(String fileName) {
        int dotIndex = fileName.indexOf(".");
        int curIndex = dotIndex - 1;
        while (curIndex > 0 && fileName.charAt(curIndex) != '/') {
            curIndex--;
        }
        return fileName.substring(curIndex + 1, dotIndex);
    }
}

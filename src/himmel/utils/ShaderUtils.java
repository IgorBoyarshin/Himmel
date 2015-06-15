package himmel.utils;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glDeleteShader;

/**
 * Created by Igor on 01-May-15.
 */
public class ShaderUtils {
    public static int load(String vertPath, String fragPath) {
        String vert = FileLoader.loadAsString(vertPath);
        String frag = FileLoader.loadAsString(fragPath);

        return create(vert, frag);
    }

    public static int create(String vert, String frag) {
        int program = glCreateProgram();
        int vertID = glCreateShader(GL_VERTEX_SHADER);
        int fragID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(vertID, vert);
        glShaderSource(fragID, frag);

        glCompileShader(vertID);
        if (glGetShaderi(vertID, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("Failed to compile Vertex Shader");
            System.err.println(glGetShaderInfoLog(vertID, 2048));
        }

        glCompileShader(fragID);
        if (glGetShaderi(fragID, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("Failed to compile Fragment Shader");
            System.err.println(glGetShaderInfoLog(fragID, 2048));
        }

        glAttachShader(program, vertID);
        glAttachShader(program, fragID);
        glLinkProgram(program);
        glValidateProgram(program);

        glDeleteShader(fragID);
        glDeleteShader(vertID);

        return program;
    }
}

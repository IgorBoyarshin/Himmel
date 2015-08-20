package himmel.utils;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER;
import static org.lwjgl.opengl.GL40.GL_TESS_CONTROL_SHADER;
import static org.lwjgl.opengl.GL40.GL_TESS_EVALUATION_SHADER;

/**
 * Created by Igor on 01-May-15.
 */
public class ShaderUtils {
    public static int load(String vertPath, String fragPath) {
        String vert = FileLoader.loadAsString(vertPath);
        String frag = FileLoader.loadAsString(fragPath);

        return create(vert, frag);
    }

    public static int load(String vertPath, String fragPath, String geomPath) {
        String vert = FileLoader.loadAsString(vertPath);
        String frag = FileLoader.loadAsString(fragPath);
        String geom = FileLoader.loadAsString(geomPath);

        return create(vert, frag, geom);
    }

    public static int load(String vertPath, String fragPath, String geomPath, String tessControlPath, String tessEvaluationPath) {
        String vert = FileLoader.loadAsString(vertPath);
        String frag = FileLoader.loadAsString(fragPath);
        String geom = FileLoader.loadAsString(geomPath);
        String tcs = FileLoader.loadAsString(tessControlPath);
        String tes = FileLoader.loadAsString(tessEvaluationPath);

        return create(vert, frag, geom);
    }

    public static int create(String vert, String frag, String geom, String tcs, String tes) {
        int program = glCreateProgram();
        int vertID = glCreateShader(GL_VERTEX_SHADER);
        int fragID = glCreateShader(GL_FRAGMENT_SHADER);
        int geomId = glCreateShader(GL_GEOMETRY_SHADER);
        int tcsId = glCreateShader(GL_TESS_CONTROL_SHADER);
        int tesId = glCreateShader(GL_TESS_EVALUATION_SHADER);
        glShaderSource(vertID, vert);
        glShaderSource(fragID, frag);
        glShaderSource(geomId, geom);
        glShaderSource(tcsId, tcs);
        glShaderSource(tesId, tes);

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

        glCompileShader(geomId);
        if (glGetShaderi(geomId, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("Failed to compile Geometry Shader");
            System.err.println(glGetShaderInfoLog(geomId, 2048));
        }

        glCompileShader(tcsId);
        if (glGetShaderi(tcsId, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("Failed to compile Tessellation Control Shader");
            System.err.println(glGetShaderInfoLog(tcsId, 2048));
        }

        glCompileShader(tesId);
        if (glGetShaderi(tesId, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("Failed to compile Tessellation Evaluation Shader");
            System.err.println(glGetShaderInfoLog(tesId, 2048));
        }

        glAttachShader(program, vertID);
        glAttachShader(program, fragID);
        glAttachShader(program, geomId);
        glAttachShader(program, tcsId);
        glAttachShader(program, tesId);
        glLinkProgram(program);
        glValidateProgram(program);

        glDeleteShader(fragID);
        glDeleteShader(vertID);
        glDeleteShader(geomId);
        glDeleteShader(tcsId);
        glDeleteShader(tesId);

        return program;
    }

    public static int create(String vert, String frag, String geom) {
        int program = glCreateProgram();
        int vertID = glCreateShader(GL_VERTEX_SHADER);
        int fragID = glCreateShader(GL_FRAGMENT_SHADER);
        int geomId = glCreateShader(GL_GEOMETRY_SHADER);
        glShaderSource(vertID, vert);
        glShaderSource(fragID, frag);
        glShaderSource(geomId, geom);

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

        glCompileShader(geomId);
        if (glGetShaderi(geomId, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("Failed to compile Geometry Shader");
            System.err.println(glGetShaderInfoLog(geomId, 2048));
        }

        glAttachShader(program, vertID);
        glAttachShader(program, fragID);
        glAttachShader(program, geomId);
        glLinkProgram(program);
        glValidateProgram(program);

        glDeleteShader(fragID);
        glDeleteShader(vertID);
        glDeleteShader(geomId);

        return program;
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

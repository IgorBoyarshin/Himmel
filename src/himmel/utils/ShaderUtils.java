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

    public static int load(String vertPath, String geomPath, String fragPath) {
        String vert = FileLoader.loadAsString(vertPath);
        String geom = FileLoader.loadAsString(geomPath);
        String frag = FileLoader.loadAsString(fragPath);

        return create(vert, geom, frag);
    }

    public static int load(String vertPath, String tessControlPath, String tessEvaluationPath, String fragPath) {
        String vert = FileLoader.loadAsString(vertPath);
        String tcs = FileLoader.loadAsString(tessControlPath);
        String tes = FileLoader.loadAsString(tessEvaluationPath);
        String frag = FileLoader.loadAsString(fragPath);

        return create(vert, tcs, tes, frag);
    }

    public static int load(String vertPath, String tessControlPath, String tessEvaluationPath, String geomPath, String fragPath) {
        String vert = FileLoader.loadAsString(vertPath);
        String tcs = FileLoader.loadAsString(tessControlPath);
        String tes = FileLoader.loadAsString(tessEvaluationPath);
        String geom = FileLoader.loadAsString(geomPath);
        String frag = FileLoader.loadAsString(fragPath);

        return create(vert, tcs, tes, geom, frag);
    }

    public static int create(String vert, String tcs, String tes, String frag) {
        int program = glCreateProgram();
        int vertID = glCreateShader(GL_VERTEX_SHADER);
        int tcsId = glCreateShader(GL_TESS_CONTROL_SHADER);
        int tesId = glCreateShader(GL_TESS_EVALUATION_SHADER);
        int fragID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(vertID, vert);
        glShaderSource(tcsId, tcs);
        glShaderSource(tesId, tes);
        glShaderSource(fragID, frag);

        glCompileShader(vertID);
        if (glGetShaderi(vertID, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("Failed to compile Vertex Shader");
            System.err.println(glGetShaderInfoLog(vertID, 2048));
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

        glCompileShader(fragID);
        if (glGetShaderi(fragID, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("Failed to compile Fragment Shader");
            System.err.println(glGetShaderInfoLog(fragID, 2048));
        }

        glAttachShader(program, vertID);
        glAttachShader(program, tcsId);
        glAttachShader(program, tesId);
        glAttachShader(program, fragID);
        glLinkProgram(program);
        glValidateProgram(program);

        glDeleteShader(fragID);
        glDeleteShader(tesId);
        glDeleteShader(tcsId);
        glDeleteShader(vertID);

        return program;
    }

    public static int create(String vert, String tcs, String tes, String geom, String frag) {
        int program = glCreateProgram();
        int vertID = glCreateShader(GL_VERTEX_SHADER);
        int tcsId = glCreateShader(GL_TESS_CONTROL_SHADER);
        int tesId = glCreateShader(GL_TESS_EVALUATION_SHADER);
        int geomId = glCreateShader(GL_GEOMETRY_SHADER);
        int fragID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(vertID, vert);
        glShaderSource(tcsId, tcs);
        glShaderSource(tesId, tes);
        glShaderSource(geomId, geom);
        glShaderSource(fragID, frag);

        glCompileShader(vertID);
        if (glGetShaderi(vertID, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("Failed to compile Vertex Shader");
            System.err.println(glGetShaderInfoLog(vertID, 2048));
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

        glCompileShader(geomId);
        if (glGetShaderi(geomId, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("Failed to compile Geometry Shader");
            System.err.println(glGetShaderInfoLog(geomId, 2048));
        }

        glCompileShader(fragID);
        if (glGetShaderi(fragID, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("Failed to compile Fragment Shader");
            System.err.println(glGetShaderInfoLog(fragID, 2048));
        }

        glAttachShader(program, vertID);
        glAttachShader(program, tcsId);
        glAttachShader(program, tesId);
        glAttachShader(program, geomId);
        glAttachShader(program, fragID);
        glLinkProgram(program);
        glValidateProgram(program);

        glDeleteShader(fragID);
        glDeleteShader(geomId);
        glDeleteShader(tesId);
        glDeleteShader(tcsId);
        glDeleteShader(vertID);

        return program;
    }

    public static int create(String vert, String geom, String frag) {
        int program = glCreateProgram();
        int vertID = glCreateShader(GL_VERTEX_SHADER);
        int geomId = glCreateShader(GL_GEOMETRY_SHADER);
        int fragID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(vertID, vert);
        glShaderSource(geomId, geom);
        glShaderSource(fragID, frag);

        glCompileShader(vertID);
        if (glGetShaderi(vertID, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("Failed to compile Vertex Shader");
            System.err.println(glGetShaderInfoLog(vertID, 2048));
        }

        glCompileShader(geomId);
        if (glGetShaderi(geomId, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("Failed to compile Geometry Shader");
            System.err.println(glGetShaderInfoLog(geomId, 2048));
        }

        glCompileShader(fragID);
        if (glGetShaderi(fragID, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("Failed to compile Fragment Shader");
            System.err.println(glGetShaderInfoLog(fragID, 2048));
        }

        glAttachShader(program, vertID);
        glAttachShader(program, geomId);
        glAttachShader(program, fragID);
        glLinkProgram(program);
        glValidateProgram(program);

        glDeleteShader(fragID);
        glDeleteShader(geomId);
        glDeleteShader(vertID);

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

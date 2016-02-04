package himmel.utils;

import himmel.log.Log;

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

    public static int loadShaderId(String[] paths) {
        String[] shadersAsStrings = new String[paths.length];
        for (int i = 0; i < paths.length; i++) {
            shadersAsStrings[i] = FileLoader.loadAsString(paths[i]);
        }

        return createShaderProgram(shadersAsStrings);
    }

    private static int createShaderProgram(String[] shaders) {
        final int program = glCreateProgram();
        int[] shadersIds = new int[shaders.length];

        for (int i = 0; i < shaders.length; i++) {
            ShaderType shaderType = ShaderType.getShaderTypeFromIndex(shaders.length, i);
            final int shaderId = glCreateShader(shaderType.code);

            glShaderSource(shaderId, shaders[i]);
            glCompileShader(shaderId);
            if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == GL_FALSE) {
                Log.logError("<ShaderUtils.createShaderProgram>: Failed to compile " + shaderType.name + " shader.");
                Log.logError(glGetShaderInfoLog(shaderId, 2048));
            }
            glAttachShader(program, shaderId);

            shadersIds[i] = shaderId;
        }

        glLinkProgram(program);
        glValidateProgram(program);

        for (int i = 0; i < shaders.length; i++) {
            glDeleteShader(shadersIds[i]);
        }

        return program;
    }

    private enum ShaderType {
        VERTEX("Vertex", GL_VERTEX_SHADER),
        TESSELLATION_CONTROL("Tessellation Control", GL_TESS_CONTROL_SHADER),
        TESSELLATION_EVALUATION("Tessellation Evaluation", GL_TESS_EVALUATION_SHADER),
        GEOMETRY("Geometry", GL_GEOMETRY_SHADER),
        FRAGMENT("Fragment", GL_FRAGMENT_SHADER);

        public final String name;
        public final int code;

        ShaderType(String name, final int code) {
            this.name = name;
            this.code = code;
        }

        public static ShaderType getShaderTypeFromIndex(int size, int id) {
            switch (size) {
                case 2:
                    switch (id) {
                        case 0:
                            return VERTEX;
                        case 1:
                            return FRAGMENT;
                        default:
                            Log.logError("<ShaderUtils.ShaderType.getShaderTypeFromIndex>: wrong id argument(" + id + ").");
                    }
                    break;
                case 3:
                    switch (id) {
                        case 0:
                            return VERTEX;
                        case 1:
                            return GEOMETRY;
                        case 2:
                            return FRAGMENT;
                        default:
                            Log.logError("<ShaderUtils.ShaderType.getShaderTypeFromIndex>: wrong id argument(" + id + ").");
                    }
                    break;
                case 4:
                    switch (id) {
                        case 0:
                            return VERTEX;
                        case 1:
                            return TESSELLATION_CONTROL;
                        case 2:
                            return TESSELLATION_EVALUATION;
                        case 3:
                            return FRAGMENT;
                        default:
                            Log.logError("<ShaderUtils.ShaderType.getShaderTypeFromIndex>: wrong id argument(" + id + ").");
                    }
                    break;
                case 5:
                    switch (id) {
                        case 0:
                            return VERTEX;
                        case 1:
                            return TESSELLATION_CONTROL;
                        case 2:
                            return TESSELLATION_EVALUATION;
                        case 3:
                            return GEOMETRY;
                        case 4:
                            return FRAGMENT;
                        default:
                            Log.logError("<ShaderUtils.ShaderType.getShaderTypeFromIndex>: wrong id argument(" + id + ").");
                    }
                    break;
                default:
                    Log.logError("<ShaderUtils.ShaderType.getShaderTypeFromIndex>: wrong size argument(" + size + ").");
                    return null;
            }

            return null;
        }
    }
}

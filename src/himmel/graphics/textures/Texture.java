package himmel.graphics.textures;

import himmel.log.Log;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL42.*;
import static org.lwjgl.opengl.GL30.*;

/**
 * Created by Igor on 28-May-15.
 */
public abstract class Texture {
    private final int textureID;
    private final TextureType textureType;

    protected Texture(TextureType textureType) {
        this.textureType = textureType;

        textureID = glGenTextures();

    }

    protected void createTexture(final boolean mutable, final int bindingPoint, final int width, final int height,
                                      TextureParameters.ComponentType componentType, ByteBuffer data) {
        bind();

        if (mutable) {
            glTexImage2D(bindingPoint, 0, componentType.codeFormat, width, height, 0,
                    componentType.codeFormat, GL_UNSIGNED_BYTE, data);
        } else {
            glTexStorage2D(bindingPoint, 6, componentType.codeInternalFormat, width, height);
            glTexSubImage2D(bindingPoint, 0, 0, 0, width, height,
                    componentType.codeFormat, GL_UNSIGNED_BYTE, data);
        }
    }

//    protected void createTexture() {
//        int id = glGenTextures();
//        glBindTexture(GL_TEXTURE_2D, id);
//
//        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, (ByteBuffer) null);
//        glTexStorage2D(GL_TEXTURE_2D, 6, settings.componentType.codeInternalFormat, imageData.width, imageData.height);
//
//        // That's filling
//        glTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, imageData.width, imageData.height,
//                settings.componentType.codeFormat, GL_UNSIGNED_BYTE, imageData.imageBuffer);
//    }

    protected void setTextureParameters(TextureParameters parameters) {
        glBindTexture(textureType.code, textureID);

        if (parameters.filteringTypeMin.generateMipmaps || parameters.filteringTypeMag.generateMipmaps) {
            glGenerateMipmap(textureType.code);
            glTexParameterf(textureType.code, GL_TEXTURE_LOD_BIAS, parameters.lodBias);
        }
        glTexParameteri(textureType.code, GL_TEXTURE_MIN_FILTER, parameters.filteringTypeMin.code);
        glTexParameteri(textureType.code, GL_TEXTURE_MAG_FILTER, parameters.filteringTypeMag.code);
        glTexParameteri(textureType.code, GL_TEXTURE_WRAP_S, parameters.textureWrapping.codeS);
        glTexParameteri(textureType.code, GL_TEXTURE_WRAP_T, parameters.textureWrapping.codeT);
        glTexParameteri(textureType.code, GL_TEXTURE_WRAP_R, parameters.textureWrapping.codeR);

        glBindTexture(textureType.code, 0);
    }

//    public Texture(int tid) {
//        textureID = tid;
//    }

//    public Texture(Settings settings, final int width, final int height) {
//        textureID = createImageTexture(settings, width, height);
//    }


//    public Texture(String path, Settings settings) {
//        textureID = createTexture(path, settings);
//    }
//
//    public Texture(String paths[], Settings settings) {
//        textureID = createCubeTexture(paths, settings);
//    }

//    private int createImageTexture(Settings settings, final int width, final int height) {
//        int id = glGenTextures();
//        glBindTexture(GL_TEXTURE_2D, id);
//        glEnable(GL_TEXTURE_2D);
//
////        ImageData imageData = loadImageDataFromFile(path, settings.componentType);
//        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, (ByteBuffer) null);
//
//        glTexStorage2D(GL_TEXTURE_2D, 6, settings.componentType.codeInternalFormat, imageData.width, imageData.height);
//        glTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, imageData.width, imageData.height,
//                settings.componentType.codeFormat, GL_UNSIGNED_BYTE, imageData.imageBuffer);
//
//
//        if (settings.filteringType.useMipmaps) {
//            glGenerateMipmap(GL_TEXTURE_2D);
//            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_LOD_BIAS, settings.lodBias);
//        }
//        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, settings.filteringType.code);
//        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, settings.filteringType.code);
//        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, settings.textureWrapping.codeS);
//        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, settings.textureWrapping.codeT);
//        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_R, settings.textureWrapping.codeR);
//
//        glBindTexture(GL_TEXTURE_2D, 0);
//        return id;
//    }

//    private int createTexture(String path, Settings settings) {
//        int id = glGenTextures();
//        glBindTexture(GL_TEXTURE_2D, id);
//        glEnable(GL_TEXTURE_2D);
//
//        ImageData imageData = loadImageDataFromFile(path, settings.componentType);
//
//        glTexStorage2D(GL_TEXTURE_2D, 6, settings.componentType.codeInternalFormat, imageData.width, imageData.height);
//        glTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, imageData.width, imageData.height,
//                settings.componentType.codeFormat, GL_UNSIGNED_BYTE, imageData.imageBuffer);
//
//
//        if (settings.filteringTypeMin.useMipmaps && settings.filteringTypeMag.useMipmaps) {
//            glGenerateMipmap(GL_TEXTURE_2D);
//            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_LOD_BIAS, settings.lodBias);
//        }
//        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, settings.filteringTypeMin.code);
//        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, settings.filteringTypeMag.code);
//        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, settings.textureWrapping.codeS);
//        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, settings.textureWrapping.codeT);
//        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_R, settings.textureWrapping.codeR);
//
//        glBindTexture(GL_TEXTURE_2D, 0);
//        return id;
//    }

//    private int createCubeTexture(String[] paths, Settings settings) {
//        int id = glGenTextures();
//        glBindTexture(GL_TEXTURE_CUBE_MAP, id);
//
//        for (int i = 0; i < 6; i++) {
//            ImageData imageData = loadImageDataFromFile(paths[i], settings.componentType);
//            glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, settings.componentType.codeFormat,
//                    imageData.width, imageData.height, 0, settings.componentType.codeFormat, GL_UNSIGNED_BYTE, imageData.imageBuffer);
//        }
//
////        if (settings.filteringType.useMipmaps) {
////            glGenerateMipmap(GL_TEXTURE_CUBE_MAP);
////        }
//        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, settings.filteringTypeMin.code);
//        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, settings.filteringTypeMag.code);
//        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_S, settings.textureWrapping.codeS);
//        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_T, settings.textureWrapping.codeT);
//        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_R, settings.textureWrapping.codeR);
//
//        glBindTexture(GL_TEXTURE_CUBE_MAP, 0);
//        return id;
//    }


//    private int loadCubeTexture(String[] paths) {
//        int id = glGenTextures();
//        glBindTexture(GL_TEXTURE_CUBE_MAP, id);
//
//        for (int i = 0; i < 6; i++) {
//            ImageData imageData = loadImageDataFromFile(paths[i], TYPE_RGB);
//            glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL_RGB,
//                    imageData.width, imageData.height, 0, GL_RGB, GL_UNSIGNED_BYTE, imageData.imageBuffer);
//        }
//
//        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
//        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
//        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
//        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
//        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);
//        glBindTexture(GL_TEXTURE_CUBE_MAP, 0);
//
//        return id;
//    }

//    private int loadTexture(String path, int type, int minFilter, int magFilter, boolean genMipmaps) {
//        ImageData imageData = loadImageDataFromFile(path, type);
//
//        int id = glGenTextures();
//        glBindTexture(GL_TEXTURE_2D, id);
//        glEnable(GL_TEXTURE_2D);
//
//        glTexStorage2D(GL_TEXTURE_2D, 6, type == TYPE_RGB ? GL_RGB8 : GL_RGBA8, imageData.width, imageData.height);
//        glTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, imageData.width, imageData.height,
//                type == TYPE_RGB ? GL_RGB : GL_RGBA, GL_UNSIGNED_BYTE, imageData.imageBuffer);
//
//        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
//        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
//        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);
//
////        glTexImage2D(GL_TEXTURE_2D, 0, type == TYPE_RGB ? GL_RGB : GL_RGBA,
////                imageData.width, imageData.height, 0, type == TYPE_RGB ? GL_RGB : GL_RGBA, GL_UNSIGNED_BYTE, imageData.imageBuffer);
//
//        if (genMipmaps) {
//            glGenerateMipmap(GL_TEXTURE_2D);
//            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER,
//                    minFilter == FILTER_LINEAR ? GL_LINEAR_MIPMAP_LINEAR : GL_NEAREST_MIPMAP_NEAREST);
//            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER,
//                    magFilter == FILTER_LINEAR ? GL_LINEAR : GL_NEAREST);
//            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_LOD_BIAS, -0.2f);
//        } else {
//            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER,
//                    minFilter == FILTER_LINEAR ? GL_LINEAR : GL_NEAREST);
//            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER,
//                    magFilter == FILTER_LINEAR ? GL_LINEAR : GL_NEAREST);
//        }
////        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, minFilter == FILTER_LINEAR ? GL_LINEAR : GL_NEAREST);
////        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
////        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
////        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, magFilter == FILTER_LINEAR ? GL_LINEAR : GL_NEAREST);
////        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, minFilter == FILTER_LINEAR ? GL_LINEAR_MIPMAP_NEAREST : GL_NEAREST_MIPMAP_NEAREST);
////        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, magFilter == FILTER_LINEAR ? GL_LINEAR_MIPMAP_LINEAR : GL_NEAREST_MIPMAP_NEAREST);
//
//        glBindTexture(GL_TEXTURE_2D, 0);
//
//        return id;
//    }

    protected ImageData loadImageDataFromFile(String path, TextureParameters.ComponentType componentType) {
        BufferedImage i = null;
        try {
            i = ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (i == null) {
            Log.logError("<Texture.loadImageDataFromFile>: could not read image from " + path);
            return null;
        }
        byte[] data = ((DataBufferByte) i.getRaster().getDataBuffer()).getData();

        if (componentType.equals(TextureParameters.ComponentType.RGB)) {
            for (int j = 0; j < data.length; j += 3) {
                byte temp = data[j];
                data[j] = data[j + 2];
                data[j + 2] = temp;
            }
        } else {
            for (int j = 0; j < data.length; j += 4) {
                byte t1 = data[j];
                byte t2 = data[j + 1];
                data[j] = data[j + 3];
                data[j + 1] = data[j + 2];
                data[j + 2] = t2;
                data[j + 3] = t1;
            }
        }

        ByteBuffer imageBuffer = ByteBuffer.allocateDirect(data.length);
        imageBuffer.order(ByteOrder.nativeOrder());
        imageBuffer.put(data, 0, data.length);
        imageBuffer.flip();

        return new ImageData(imageBuffer, i.getWidth(), i.getHeight());
    }

    public void bind() {
        glBindTexture(textureType.code, textureID);
    }

    public void unbind() {
        glBindTexture(textureType.code, 0);
    }

    public int getTID() {
        return textureID;
    }

    public void destruct() {
        glDeleteTextures(textureID);
    }


    protected class ImageData {
        public ByteBuffer imageBuffer;
        public int width;
        public int height;

        public ImageData(ByteBuffer imageBuffer, int width, int height) {
            this.imageBuffer = imageBuffer;
            this.width = width;
            this.height = height;
        }
    }

    protected enum TextureType {
        TEXTURE_2D(GL_TEXTURE_2D),
        CUBE_TEXTURE(GL_TEXTURE_CUBE_MAP);

        public final int code;

        TextureType(final int code) {
            this.code = code;
        }
    }
}

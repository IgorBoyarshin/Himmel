package himmel.graphics;

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
public class Texture {
    private int textureID;

    public static final int TYPE_RGB = 0;
    public static final int TYPE_RGBA = 1;

    public static final int FILTER_NEAREST = 0;
    public static final int FILTER_LINEAR = 1;

    public Texture(int tid) {
        textureID = tid;
    }

    public Texture(String path, final int type) {
        textureID = loadTexture(path, type);
    }

    public Texture(String path, final int type, final int minFilter, final int magFilter, final boolean genMipmaps) {
        textureID = loadTexture(path, type, minFilter, magFilter, genMipmaps);
    }

    public Texture(String paths[]) {
        textureID = loadCubeTexture(paths);
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, textureID);
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public int getTID() {
        return textureID;
    }

    private int loadCubeTexture(String[] paths) {
        int id = glGenTextures();
        glBindTexture(GL_TEXTURE_CUBE_MAP, id);

        for (int i = 0; i < 6; i++) {
            ImageData imageData = loadFile(paths[i], TYPE_RGB);
            glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL_RGB,
                    imageData.width, imageData.height, 0, GL_RGB, GL_UNSIGNED_BYTE, imageData.imageBuffer);
        }

        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);
        glBindTexture(GL_TEXTURE_CUBE_MAP, 0);

        return id;
    }

    private int loadTexture(String path, int type) {
        return loadTexture(path, type, FILTER_LINEAR, FILTER_LINEAR, false);
    }

    private int loadTexture(String path, int type, int minFilter, int magFilter, boolean genMipmaps) {
        ImageData imageData = loadFile(path, type);

        int id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, id);
        glEnable(GL_TEXTURE_2D);

        glTexStorage2D(GL_TEXTURE_2D, 6, type == TYPE_RGB ? GL_RGB8 : GL_RGBA8, imageData.width, imageData.height);
        glTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, imageData.width, imageData.height,
                type == TYPE_RGB ? GL_RGB : GL_RGBA, GL_UNSIGNED_BYTE, imageData.imageBuffer);

//        glTexImage2D(GL_TEXTURE_2D, 0, type == TYPE_RGB ? GL_RGB : GL_RGBA,
//                imageData.width, imageData.height, 0, type == TYPE_RGB ? GL_RGB : GL_RGBA, GL_UNSIGNED_BYTE, imageData.imageBuffer);

        if (genMipmaps) {
            glGenerateMipmap(GL_TEXTURE_2D);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER,
                    minFilter == FILTER_LINEAR ? GL_LINEAR_MIPMAP_LINEAR : GL_NEAREST_MIPMAP_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER,
                    magFilter == FILTER_LINEAR ? GL_LINEAR : GL_NEAREST);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_LOD_BIAS, -0.2f);
        } else {
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER,
                    minFilter == FILTER_LINEAR ? GL_LINEAR  : GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER,
                    magFilter == FILTER_LINEAR ? GL_LINEAR : GL_NEAREST);
        }
//        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, minFilter == FILTER_LINEAR ? GL_LINEAR : GL_NEAREST);
//        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
//        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
//        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, magFilter == FILTER_LINEAR ? GL_LINEAR : GL_NEAREST);
//        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, minFilter == FILTER_LINEAR ? GL_LINEAR_MIPMAP_NEAREST : GL_NEAREST_MIPMAP_NEAREST);
//        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, magFilter == FILTER_LINEAR ? GL_LINEAR_MIPMAP_LINEAR : GL_NEAREST_MIPMAP_NEAREST);

        glBindTexture(GL_TEXTURE_2D, 0);

        return id;
    }

    private ImageData loadFile(String path, int type) {
        BufferedImage i = null;
        try {
            i = ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] data = ((DataBufferByte) i.getRaster().getDataBuffer()).getData();

        if (type == TYPE_RGB) {
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

    private class ImageData {
        public ByteBuffer imageBuffer;
        public int width;
        public int height;

        public ImageData(ByteBuffer imageBuffer, int width, int height) {
            this.imageBuffer = imageBuffer;
            this.width = width;
            this.height = height;
        }
    }
}

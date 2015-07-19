package himmel.graphics;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Igor on 28-May-15.
 */
public class Texture {
    private int textureID;

    public static final int TYPE_RGB = 0;
    public static final int TYPE_RGBA = 1;

    public static final int FILTER_NEAREST = 0;
    public static final int FILTER_LINEAR = 1;
//    private final int TYPE;

    public Texture(String path, final int type, final int minFilter, final int magFilter) {
//        TYPE = type;

        textureID = loadTexture(path, type, minFilter, magFilter);
//        this.TYPE = TYPE_RGB;
    }

//    public final int getType() {
//        return TYPE;
//    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, textureID);
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public int getTID() {
        return textureID;
    }

    private int loadTexture(String path, int type, int minFilter, int magFilter) {
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

        int id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, id);
//        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
//        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, minFilter == FILTER_LINEAR ? GL_LINEAR : GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, magFilter == FILTER_LINEAR ? GL_LINEAR : GL_NEAREST);

//        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, i.getWidth(), i.getHeight(), 0, GL_RGB, GL_UNSIGNED_BYTE, imageBuffer);
        glTexImage2D(GL_TEXTURE_2D, 0, type == TYPE_RGB ? GL_RGB : GL_RGBA,
                i.getWidth(), i.getHeight(), 0, type == TYPE_RGB ? GL_RGB : GL_RGBA, GL_UNSIGNED_BYTE, imageBuffer);
//        if (TYPE == TYPE_RGB) {
//            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, i.getWidth(), i.getHeight(), 0, GL_RGB, GL_UNSIGNED_BYTE, imageBuffer);
//        } else {
//            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, i.getWidth(), i.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, imageBuffer);
//        }
        glBindTexture(GL_TEXTURE_2D, 0);

        return id;
    }
}

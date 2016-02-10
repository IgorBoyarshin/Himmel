package himmel.graphics.textures;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL42.*;
import static org.lwjgl.opengl.GL30.*;

/**
 * Created by Igor on 10-Feb-16 at 12:08 PM.
 */
public class Texture2D extends Texture {

    // Mutable
    public Texture2D(final int width, final int height, TextureParameters textureParameters) {
        super(TextureType.TEXTURE_2D);

        bind();
        createTexture(false, TextureType.TEXTURE_2D.code, width, height, textureParameters.componentType, null);
        setTextureParameters(textureParameters);
        unbind();
    }

    // Immutable
    public Texture2D(String path, TextureParameters textureParameters) {
        super(TextureType.TEXTURE_2D);

        bind();
        ImageData imageData = loadImageDataFromFile(path, textureParameters.componentType);
        createTexture(false, TextureType.TEXTURE_2D.code, imageData.width, imageData.height,
                textureParameters.componentType, imageData.imageBuffer);
        setTextureParameters(textureParameters);
        unbind();
    }
}

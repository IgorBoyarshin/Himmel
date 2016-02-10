package himmel.graphics.textures;

import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X;

/**
 * Created by Igor on 10-Feb-16 at 12:08 PM.
 */
public class CubeTexture extends Texture{

    // Mutable
    public CubeTexture(final int width, final int height, TextureParameters textureParameters) {
        super(Texture.TextureType.CUBE_TEXTURE);

        bind();
        for (int i = 0; i < 6; i++) {
//            ImageData imageData = loadImageDataFromFile(paths[i], textureParameters.componentType);
            createTexture(true, GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, width, height,
                    textureParameters.componentType, null);
        }
        setTextureParameters(textureParameters);
        unbind();
    }

    public CubeTexture(String paths[], TextureParameters textureParameters) {
        super(Texture.TextureType.CUBE_TEXTURE);

        bind();
        for (int i = 0; i < 6; i++) {
            ImageData imageData = loadImageDataFromFile(paths[i], textureParameters.componentType);
            createTexture(true, GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, imageData.width, imageData.height,
                    textureParameters.componentType, imageData.imageBuffer);
        }
        setTextureParameters(textureParameters);
        unbind();
    }
}

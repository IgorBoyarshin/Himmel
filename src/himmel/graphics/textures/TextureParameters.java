package himmel.graphics.textures;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_RGBA8;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL13.GL_CLAMP_TO_BORDER;
import static org.lwjgl.opengl.GL14.GL_MIRRORED_REPEAT;

/**
 * Created by Igor on 10-Feb-16 at 12:14 PM.
 */
public class TextureParameters {

    public ComponentType componentType;
    public FilteringType filteringTypeMin;
    public FilteringType filteringTypeMag;
    public TextureWrapping textureWrapping;
    public float lodBias;

    public TextureParameters(ComponentType componentType) {
        this.componentType = componentType;
        this.filteringTypeMin = FilteringType.LINEAR_MIPMAP_LINEAR;
        this.filteringTypeMag = FilteringType.LINEAR_MIPMAP_LINEAR;
        this.textureWrapping = TextureWrapping.CLAMP_TO_EDGE;
        this.lodBias = -0.2f;
    }

    public TextureParameters(ComponentType componentType, FilteringType filteringTypeMin, FilteringType filteringTypeMag,
                             TextureWrapping textureWrapping, float lodBias) {
        this.componentType = componentType;
        this.filteringTypeMin = filteringTypeMin;
        this.filteringTypeMag = filteringTypeMag;
        this.textureWrapping = textureWrapping;
        this.lodBias = lodBias;
    }


    public enum TextureWrapping {
        CLAMP_TO_EDGE(GL_CLAMP_TO_EDGE),
        CLAMP_TO_BORDER(GL_CLAMP_TO_BORDER),
        REPEAT(GL_REPEAT),
        MIRRORED_REPEEAT(GL_MIRRORED_REPEAT);

        public final int codeS;
        public final int codeT;
        public final int codeR;

        TextureWrapping(final int code) {
            this.codeS = code;
            this.codeT = code;
            this.codeR = code;
        }
    }

//    public enum FilteringTypeWithMipmaps {
//        LINEAR_LINEAR(GL_LINEAR_MIPMAP_LINEAR, true),
//        LINEAR_NEAREST(GL_LINEAR_MIPMAP_NEAREST, true),
//        NEAREST_LINEAR(GL_NEAREST_MIPMAP_LINEAR, true),
//        NEAREST_NEAREST(GL_NEAREST_MIPMAP_NEAREST, true);
//
//        public final int code;
//        public final boolean useMipmaps;
//
//        FilteringType(final int code, final boolean useMipmaps) {
//            this.code = code;
//            this.useMipmaps = useMipmaps;
//        }
//    }
//
//    public enum FilteringTypeWithoutMipmaps {
//        LINEAR(GL_LINEAR, false),
//        NEAREST(GL_NEAREST, false);
//
//        public final int code;
//        public final boolean useMipmaps;
//
//        FilteringType(final int code, final boolean useMipmaps) {
//            this.code = code;
//            this.useMipmaps = useMipmaps;
//        }
//    }

    public enum FilteringType {
        LINEAR_MIPMAP_LINEAR(GL_LINEAR_MIPMAP_LINEAR, true),
        LINEAR_MIPMAP_NEAREST(GL_LINEAR_MIPMAP_NEAREST, true),
        NEAREST_MIPMAP_LINEAR(GL_NEAREST_MIPMAP_LINEAR, true),
        NEAREST_MIPMAP_NEAREST(GL_NEAREST_MIPMAP_NEAREST, true),
        LINEAR(GL_LINEAR, false),
        NEAREST(GL_NEAREST, false);

        public final int code;
        public final boolean generateMipmaps;

        FilteringType(final int code, final boolean generateMipmaps) {
            this.code = code;
            this.generateMipmaps = generateMipmaps;
        }
    }

    public enum ComponentType {
        RGB(GL_RGB, GL_RGB8),
        RGBA(GL_RGBA, GL_RGBA8);

        public final int codeFormat;
        public final int codeInternalFormat;

        ComponentType(final int codeFormat, final int codeInternalFormat) {
            this.codeFormat = codeFormat;
            this.codeInternalFormat = codeInternalFormat;
        }
    }
}

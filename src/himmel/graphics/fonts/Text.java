package himmel.graphics.fonts;

import com.sun.prism.impl.VertexBuffer;
import himmel.graphics.Shader;
import himmel.graphics.buffers.IndexBufferObject;
import himmel.graphics.buffers.VertexArrayObject;
import himmel.graphics.buffers.VertexBufferObject;
import himmel.graphics.entities.EntityWithMatrix;
import himmel.log.Log;
import himmel.math.Matrix4f;
import himmel.math.Vector2f;
import himmel.math.Vector3f;
import himmel.math.Vector4f;
import himmel.utils.FloatArray;

/**
 * Created by Igorek on 12-Feb-16.
 */
public class Text extends EntityWithMatrix {

    // For now only window-space text

    private String text;
    private Font font; // TODO: needed??
    private int fontSize;
    private static final int defaultFontSize = 100;
    private final boolean dynamic;
//    private final float distanceBetweenLines = 10.0f;

    /**
     * For windows-space - in range from 0.0f to 1.0f;
     * For worlds-space  - in project length units.
     * <p>
     * !!! Actually, always in units !!!
     */
    //private float lineTerminationLength;

    private static Font defaultFont = null;

    // TODO: fontSize
    public Text(String text, Font font, float lineLength, Shader shader, boolean dynamic) {
        super(shader, constructVao(text, font, defaultFontSize, lineLength, new Vector4f(0.0f, 1.0f, 0.0f, 1.0f), dynamic));
        this.text = text;
        this.font = font;
        this.fontSize = defaultFontSize;
        this.dynamic = dynamic;
    }

    public Text(String text, Matrix4f modelMatrix, Font font, float lineLength, int fontSize,
                Vector4f color, Shader shader, boolean dynamic) {
        super(modelMatrix, shader, constructVao(text, font, fontSize, lineLength, color, dynamic));
        this.text = text;
        this.font = font;
        this.fontSize = fontSize;
        this.dynamic = dynamic;
    }

//    public void setText(String text) {
//        this.text = text;
//    }

    public static void setDefaultFont(Font font) {
        defaultFont = font;
    }

    private static VertexArrayObject constructVao(String text, Font font, int fontSize, float lineLength,
                                                  Vector4f color, boolean dynamic) {
        VertexArrayObject vao = new VertexArrayObject(VertexArrayObject.RenderingMode.TRIANGLES);
        final int textureId = vao.addTexture(font.getTexture());
        vao.addVertexBufferObject(constructVbo(text, font, fontSize, lineLength, color, textureId),
                new int[]{0, 1, 2, 3});
        vao.setIndexBufferObject(constructIbo(text, dynamic));

        return vao;
    }

    // OpenGL CS is from bottom-left
    // Texture's and Font's CS is from upper-left
    private static VertexBufferObject constructVbo(String text, Font font, int fontSize, float lineLength,
                                                   Vector4f color, final int textureId) {
        final int textLength = text.length();
        float[] vertices = new float[textLength * 4 * 3]; // 4 - per rect, 3 - xyz
        float[] uvs = new float[textLength * 4 * 2];

        final float startingCursorOffsetX = fontSize * font.getPadding() / font.getLineHeight();
        final float startingCursorOffsetY = -fontSize * font.getPadding() / font.getLineHeight() + fontSize;
        Vector2f currentOffset = new Vector2f(startingCursorOffsetX, startingCursorOffsetY);
        int vertexCounter = 0;
        for (char symbol : text.toCharArray()) {
            FontCharacter currentCharacter = font.getCharacter(symbol);
            if (currentCharacter == null) {
                Log.logError("<Text.constructVbo>: the font does not support character '" + symbol + "'.");
                continue;
            }

            // Next line
            if (currentCharacter.c == '\n') {
                currentOffset.x = startingCursorOffsetX;
                currentOffset.y -= fontSize * 0.85f;
                continue;
            }

            // Next line
            if (lineLength - currentOffset.x < currentCharacter.width / font.getLineHeight() * fontSize) {
                currentOffset.x = startingCursorOffsetX;
                currentOffset.y -= fontSize * 0.85f; // 1 = lineH / lineH
            }

            // lb
            vertices[vertexCounter * 3 + 0] = currentOffset.x + currentCharacter.xOffset / font.getLineHeight() * fontSize;
            vertices[vertexCounter * 3 + 1] = currentOffset.y -
                    (currentCharacter.yOffset + currentCharacter.height) / font.getLineHeight() * fontSize;
            vertices[vertexCounter * 3 + 2] = 0.0f;
            uvs[vertexCounter * 2 + 0] = currentCharacter.x;
            uvs[vertexCounter * 2 + 1] = currentCharacter.y + currentCharacter.height;
            vertexCounter++;

            // lu
            vertices[vertexCounter * 3 + 0] = currentOffset.x + currentCharacter.xOffset / font.getLineHeight() * fontSize;
            vertices[vertexCounter * 3 + 1] = currentOffset.y - currentCharacter.yOffset / font.getLineHeight() * fontSize;
            vertices[vertexCounter * 3 + 2] = 0.0f;
            uvs[vertexCounter * 2 + 0] = currentCharacter.x;
            uvs[vertexCounter * 2 + 1] = currentCharacter.y;
            vertexCounter++;

            //ru
            vertices[vertexCounter * 3 + 0] = currentOffset.x +
                    (currentCharacter.xOffset + currentCharacter.width) / font.getLineHeight() * fontSize;
            vertices[vertexCounter * 3 + 1] = currentOffset.y - currentCharacter.yOffset / font.getLineHeight() * fontSize;
            vertices[vertexCounter * 3 + 2] = 0.0f;
            uvs[vertexCounter * 2 + 0] = currentCharacter.x + currentCharacter.width;
            uvs[vertexCounter * 2 + 1] = currentCharacter.y;
            vertexCounter++;

            //rb
            vertices[vertexCounter * 3 + 0] = currentOffset.x +
                    (currentCharacter.xOffset + currentCharacter.width) / font.getLineHeight() * fontSize;
            vertices[vertexCounter * 3 + 1] = currentOffset.y -
                    (currentCharacter.yOffset + currentCharacter.height) / font.getLineHeight() * fontSize;
            vertices[vertexCounter * 3 + 2] = 0.0f;
            uvs[vertexCounter * 2 + 0] = currentCharacter.x + currentCharacter.width;
            uvs[vertexCounter * 2 + 1] = currentCharacter.y + currentCharacter.height;
            vertexCounter++;

            currentOffset.x += currentCharacter.xAdvance / font.getLineHeight() * fontSize;
        }

        return new VertexBufferObject(
                new FloatArray[]{
                        new FloatArray(vertices),
                        new FloatArray(new float[]{color.x, color.y, color.z, color.w}, vertices.length / 3),
                        new FloatArray(uvs),
                        new FloatArray(new float[]{textureId}, vertices.length / 3)},
                new int[]{3, 4, 2, 1},
                false);
    }

    // TODO: Don't allow strings larger than for ShortBuffer
    private static IndexBufferObject constructIbo(String text, boolean dynamic) {
        final int textLength = text.length();
        short[] indices = new short[textLength * 6];
        short counter = 0;
        for (int symbol = 0; symbol < textLength; symbol++) {
            indices[6 * symbol + 0] = (short) (counter);
            indices[6 * symbol + 1] = (short) (counter + 2);
            indices[6 * symbol + 2] = (short) (counter + 1);
            indices[6 * symbol + 3] = (short) (counter);
            indices[6 * symbol + 4] = (short) (counter + 3);
            indices[6 * symbol + 5] = (short) (counter + 2);

//            indices[6 * symbol + 0] = (short) (counter);
//            indices[6 * symbol + 1] = (short) (counter + 1);
//            indices[6 * symbol + 2] = (short) (counter + 2);
//            indices[6 * symbol + 3] = (short) (counter);
//            indices[6 * symbol + 4] = (short) (counter + 2);
//            indices[6 * symbol + 5] = (short) (counter + 3);

            counter += 4;
        }

        return new IndexBufferObject(indices, dynamic);
    }
}

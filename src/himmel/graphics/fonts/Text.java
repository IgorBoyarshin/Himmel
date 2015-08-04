package himmel.graphics.fonts;

import himmel.graphics.renderables.Renderable;
import himmel.graphics.renderers.RenderingSet;
import himmel.math.Matrix4f;
import himmel.math.Vector2f;
import himmel.math.Vector3f;
import himmel.math.Vector4f;

/**
 * Created by Igor on 02-Jun-15.
 */
public class Text extends Renderable {

    /*
    Should UVs match the number of vertices or indices??
     */

    //    private Vector2f size;
    private Vector4f color;
    private static final float FILE_SIZE = 512.0f;

    //    private Font font;
    private String text;

    private int fontSize;
    private static final int MIN_FONT_SIZE = 1;
    private static final int MAX_FONT_SIZE = 1000;
    private static final int DEFAULT_FONT_SIZE = 20;

    private static Font font;
    //    private static Font font = new Font("src//himmel//resources//FontCalibri");
//    private static Renderer textRenderer = new TextRenderer();
//    private static Shader shader;
    private static RenderingSet renderingSet;

    public Text(String text, Vector4f mainColor) {
        super(null, null, null, renderingSet);
        this.text = text;
        this.color = mainColor;
        setModelMatrix(Matrix4f.identity());
        checkAndSetFontSize(DEFAULT_FONT_SIZE);
        compileSentence(text);
    }

    public Text(String text, int fontSize, Vector4f mainColor) {
        super(null, null, null, renderingSet);
        this.text = text;
        this.color = mainColor;
        setModelMatrix(Matrix4f.identity());
        checkAndSetFontSize(fontSize);
        compileSentence(text);
    }

    public Text(String text, Vector4f color, int fontSize) {
        super(null, null, null, renderingSet);
        this.text = text;
        this.color = color;
        setModelMatrix(Matrix4f.identity());
        checkAndSetFontSize(fontSize);
        compileSentence(text);
    }

    public Text(String text, int fontSize) {
        super(null, null, null, renderingSet);
//        font = new Font("src//himmel//resources//FontCalibri");
        this.text = text;
        color = new Vector4f(0.0f, 0.0f, 0.0f, 1.0f);
        setModelMatrix(Matrix4f.identity());
        checkAndSetFontSize(fontSize);
        compileSentence(text);
    }

    private void checkAndSetFontSize(int size) {
        if (size >= MIN_FONT_SIZE && size <= MAX_FONT_SIZE) {
            this.fontSize = size;
        } else {
            this.fontSize = DEFAULT_FONT_SIZE;
        }

        recalculateVertices();
    }

    public void setFontSize(int size) {
        checkAndSetFontSize(size);
    }

    public static void setFont(Font theFont) {
        font = theFont;
    }

    public static void setRenderingSet(RenderingSet theRenderingSet) {
        renderingSet = theRenderingSet;
    }

    public void transform(Matrix4f model) {
        setModelMatrix(model);
        recalculateVertices();
    }

    private void recalculateVertices() {
        float height = FILE_SIZE;
        float sizeX = 0.0f;
        float sizeY = text.length() == 0 ? 0.0f : fontSize * font.getCharacterSize(text.charAt(0)).y / height;
        float[] vertices = new float[12 * text.length()];
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            Vector2f size = font.getCharacterSize(c);

            // Vertices
            Vector3f pos1 = getModelMatrix().multiply(new Vector3f(sizeX, 0.0f, 0.0f));
            Vector3f pos2 = getModelMatrix().multiply(new Vector3f(sizeX, sizeY, 0.0f));
            Vector3f pos3 = getModelMatrix().multiply(new Vector3f(sizeX + fontSize * size.x / height, sizeY, 0.0f));
            Vector3f pos4 = getModelMatrix().multiply(new Vector3f(sizeX + fontSize * size.x / height, 0.0f, 0.0f));

            vertices[12 * i + 0] = pos1.x;
            vertices[12 * i + 1] = pos1.y;
            vertices[12 * i + 2] = pos1.z;

            vertices[12 * i + 3] = pos2.x;
            vertices[12 * i + 4] = pos2.y;
            vertices[12 * i + 5] = pos2.z;

            vertices[12 * i + 6] = pos3.x;
            vertices[12 * i + 7] = pos3.y;
            vertices[12 * i + 8] = pos3.z;

            vertices[12 * i + 9] = pos4.x;
            vertices[12 * i + 10] = pos4.y;
            vertices[12 * i + 11] = pos4.z;

            sizeX += fontSize * size.x / height;
        }

        setVertices(vertices);
    }

    private void compileSentence(String text) {
        int length = text.length();
        float[] vertices = new float[4 * 3 * length];
        short[] indices = new short[6 * length];
        float[] colors = new float[4];
        float[] uv = new float[8 * length];
        setTexture(font.getTexture());
        float height = FILE_SIZE;
        float sizeX = 0.0f;
        float sizeY = text.length() == 0 ? 0.0f : fontSize * font.getCharacterSize(text.charAt(0)).y / height;

        short indicesOffset = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            Vector4f parameter = font.getCharacterParameter(c);
            Vector2f size = font.getCharacterSize(c);

            // UV
            uv[i * 8 + 0] = parameter.x / height;
            uv[i * 8 + 1] = parameter.w / height;
            uv[i * 8 + 2] = parameter.x / height;
            uv[i * 8 + 3] = parameter.y / height;
            uv[i * 8 + 4] = parameter.z / height;
            uv[i * 8 + 5] = parameter.y / height;
            uv[i * 8 + 6] = parameter.z / height;
            uv[i * 8 + 7] = parameter.w / height;

            // Vertices
            Vector3f pos1 = getModelMatrix().multiply(new Vector3f(sizeX, 0.0f, 0.0f));
            Vector3f pos2 = getModelMatrix().multiply(new Vector3f(sizeX, sizeY, 0.0f));
            Vector3f pos3 = getModelMatrix().multiply(new Vector3f(sizeX + fontSize * size.x / height, sizeY, 0.0f));
            Vector3f pos4 = getModelMatrix().multiply(new Vector3f(sizeX + fontSize * size.x / height, 0.0f, 0.0f));

            vertices[12 * i + 0] = pos1.x;
            vertices[12 * i + 1] = pos1.y;
            vertices[12 * i + 2] = pos1.z;

            vertices[12 * i + 3] = pos2.x;
            vertices[12 * i + 4] = pos2.y;
            vertices[12 * i + 5] = pos2.z;

            vertices[12 * i + 6] = pos3.x;
            vertices[12 * i + 7] = pos3.y;
            vertices[12 * i + 8] = pos3.z;

            vertices[12 * i + 9] = pos4.x;
            vertices[12 * i + 10] = pos4.y;
            vertices[12 * i + 11] = pos4.z;

            sizeX += fontSize * size.x / height;

            // Indices
            indices[6 * i + 0] = (short) (indicesOffset + 0);
            indices[6 * i + 1] = (short) (indicesOffset + 1);
            indices[6 * i + 2] = (short) (indicesOffset + 2);
            indices[6 * i + 3] = (short) (indicesOffset + 0);
            indices[6 * i + 4] = (short) (indicesOffset + 2);
            indices[6 * i + 5] = (short) (indicesOffset + 3);

            indicesOffset += 4;
        }

        colors[0] = color.x;
        colors[1] = color.y;
        colors[2] = color.z;
        colors[3] = color.w;

        setVertices(vertices);
        setIndices(indices);
        setColors(colors);
        setUV(uv);
    }

    public void setText(String newText) {
        // Maybe implement by-letter changing
        text = newText;
        compileSentence(text);
    }
}

package himmel.graphics.fonts;

import himmel.graphics.Renderable;
import himmel.graphics.Shader;
import himmel.graphics.renderers.Renderer;
import himmel.graphics.renderers.TextRenderer;
import himmel.math.Matrix4f;
import himmel.math.Vector2f;
import himmel.math.Vector3f;
import himmel.math.Vector4f;

import java.util.ArrayList;

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
    private static Renderer textRenderer = new TextRenderer();
    private static Shader shader;

    public Text(String text, Vector4f mainColor) {
        super(null, null, null, textRenderer, shader);
        this.text = text;
        this.color = mainColor;
        modelMatrix = Matrix4f.identity();
        checkAndSetFontSize(DEFAULT_FONT_SIZE);
        compileSentence(text);
    }

    public Text(String text, int fontSize, Vector4f mainColor) {
        super(null, null, null, textRenderer, shader);
        this.text = text;
        this.color = mainColor;
        modelMatrix = Matrix4f.identity();
        checkAndSetFontSize(fontSize);
        compileSentence(text);
    }

    public Text(String text, Vector4f color, int fontSize, Shader shader) {
        super(null, null, null, textRenderer, shader);
        this.text = text;
        this.color = color;
        modelMatrix = Matrix4f.identity();
        checkAndSetFontSize(fontSize);
        compileSentence(text);
    }

    public Text(String text, int fontSize, Shader shader) {
        super(null, null, null, textRenderer, shader);
//        font = new Font("src//himmel//resources//FontCalibri");
        this.text = text;
        color = new Vector4f(0.0f, 0.0f, 0.0f, 1.0f);
        modelMatrix = Matrix4f.identity();
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

    public static void setShader(Shader newShader) {
        shader = newShader;
    }

    public void transform(Matrix4f model) {
        this.modelMatrix = model;
        recalculateVertices();
    }

    private void recalculateVertices() {
        float height = FILE_SIZE;
        float sizeX = 0.0f;
        float sizeY = text.length() == 0 ? 0.0f : fontSize * font.getCharacterSize(text.charAt(0)).y / height;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            Vector2f size = font.getCharacterSize(c);

            // Vertices
            Vector3f pos1 = modelMatrix.multiply(new Vector3f(sizeX, 0.0f, 0.0f));
            Vector3f pos2 = modelMatrix.multiply(new Vector3f(sizeX, sizeY, 0.0f));
            Vector3f pos3 = modelMatrix.multiply(new Vector3f(sizeX + fontSize * size.x / height, sizeY, 0.0f));
            Vector3f pos4 = modelMatrix.multiply(new Vector3f(sizeX + fontSize * size.x / height, 0.0f, 0.0f));

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
    }

    private void compileSentence(String text) {
        int length = text.length();
        vertices = new float[4 * 3 * length];
        indices = new short[6 * length];
        colors = new float[4];
        uv = new ArrayList<>();
        texture = font.getTexture();
        float height = FILE_SIZE;
        float sizeX = 0.0f;
        float sizeY = text.length() == 0 ? 0.0f : fontSize * font.getCharacterSize(text.charAt(0)).y / height;

        short indicesOffset = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            Vector4f parameter = font.getCharacterParameter(c);
            Vector2f size = font.getCharacterSize(c);

            // UV
            uv.add(new Vector2f(parameter.x / height, parameter.w / height));
            uv.add(new Vector2f(parameter.x / height, parameter.y / height));
            uv.add(new Vector2f(parameter.z / height, parameter.y / height));
            uv.add(new Vector2f(parameter.z / height, parameter.w / height));

            // Vertices
            Vector3f pos1 = modelMatrix.multiply(new Vector3f(sizeX, 0.0f, 0.0f));
            Vector3f pos2 = modelMatrix.multiply(new Vector3f(sizeX, sizeY, 0.0f));
            Vector3f pos3 = modelMatrix.multiply(new Vector3f(sizeX + fontSize * size.x / height, sizeY, 0.0f));
            Vector3f pos4 = modelMatrix.multiply(new Vector3f(sizeX + fontSize * size.x / height, 0.0f, 0.0f));

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
    }

    public void setText(String newText) {
        // Maybe implement by-letter changing
        text = newText;
        compileSentence(text);
    }
}

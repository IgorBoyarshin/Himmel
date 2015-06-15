package himmel.graphics.fonts;

import himmel.graphics.Renderable;
import himmel.graphics.Texture;
import himmel.math.Vector2f;
import himmel.math.Vector4f;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by Igor on 02-Jun-15.
 */
public class Font {
    private static final int MIN_FONT_SIZE = 8;
    private static final int MAX_FONT_SIZE = 6000;
    private static final int DEFAULT_FONT_SIZE = 100;

    private Texture fontTexture;
    private Map<Character, Vector4f> parameters;
    private int size;


    public Font(String fontName) {
        fontTexture = new Texture(fontName + ".png", Texture.TYPE_RGB);
        parameters = loadParameters(fontName + ".hfp");
        checkAndSetFontSize(DEFAULT_FONT_SIZE);
    }

    public Font(String fontName, int size) {
        fontTexture = new Texture(fontName + ".png", Texture.TYPE_RGB);
        parameters = loadParameters(fontName + ".hfp");
        checkAndSetFontSize(size);
    }

    public Texture getTexture() {
        return fontTexture;
    }

    public Vector2f getCharacterSize(char c) {
        Vector4f parameter = parameters.get(c);
        return new Vector2f(parameter.z - parameter.x, parameter.w - parameter.y);
    }

    public Vector4f getCharacterParameter(char c) {
        return parameters.get(c);
    }

    private Map<Character, Vector4f> loadParameters(String fileName) {
        Map<Character, Vector4f> parameters = new HashMap<>();

        try {
            BufferedReader in = new BufferedReader(new FileReader(fileName));

            // First we read ' '
            float xx1 = Float.parseFloat(in.readLine());
            float yy1 = Float.parseFloat(in.readLine());
            float xx2 = Float.parseFloat(in.readLine());
            float yy2 = Float.parseFloat(in.readLine());
            parameters.put(' ', new Vector4f(xx1, yy1, xx2, yy2));

            // Now all other characters
            String line;
            while (in.ready()) {
                line = in.readLine();
                StringTokenizer st = new StringTokenizer(line, " ");
                char c = st.nextToken().charAt(0);
                float x1 = Float.parseFloat(st.nextToken());
                float y1 = Float.parseFloat(st.nextToken());
                float x2 = Float.parseFloat(st.nextToken());
                float y2 = Float.parseFloat(st.nextToken());
                Vector4f v = new Vector4f(x1, y1, x2, y2);

                parameters.put(c, v);
            }
            // eof

            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return parameters;
    }

    private void checkAndSetFontSize(int size) {
        if (size >= MIN_FONT_SIZE && size <= MAX_FONT_SIZE) {
            this.size = size;
        } else {
            this.size = DEFAULT_FONT_SIZE;
        }
    }

    public int getFontSize() {
        return size;
    }

    public void setFontSize(int size) {
        checkAndSetFontSize(size);
    }
}

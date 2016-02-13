package himmel.graphics.fonts;

import himmel.graphics.textures.Texture;
import himmel.graphics.textures.Texture2D;
import himmel.graphics.textures.TextureParameters;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Igor on 12-Feb-16 at 9:57 AM.
 */
public class FontManager {

    private static FontManager instance;

    public static Font calibriFont;

    private FontManager() {
        calibriFont = loadFont(System.getProperty("user.dir") + "//res//fonts//Calibri");
    }

    public static void initialize() {
        if (instance == null) {
            instance = new FontManager();
        }
    }

    private Font loadFont(String pathToFont) {
        Texture fontTexure = new Texture2D(pathToFont + ".png", new TextureParameters(TextureParameters.ComponentType.RGBA));
        List<FontCharacter> characters = new ArrayList<>();
        final float padding;
        final float imageSize;
        final float lineHeight;

        try (BufferedReader reader = new BufferedReader(new FileReader(pathToFont + ".fnt"))) {
            // Header
            padding = Integer.valueOf(reader.readLine().split(" ")[10].split("=")[1].split(",")[0]); // line 1
            String[] line2Pairs = reader.readLine().split(" ");
            imageSize = Integer.valueOf(line2Pairs[3].split("=")[1]); // line 2
            lineHeight = Integer.valueOf(line2Pairs[1].split("=")[1]); // line 2
            reader.readLine(); // line 3
            final int charactersAmount = Integer.valueOf(reader.readLine().substring(12)); // line 4

            // Characters array
            for (int charactedNumber = 0; charactedNumber < charactersAmount; charactedNumber++) {
                String[] fields = reader.readLine().split(" {1,}"); // The regex matches one or more spaces
                final int character = Integer.valueOf(fields[1].split("=")[1]);
                final float x = 1.0f * Integer.valueOf(fields[2].split("=")[1]) / imageSize;
                final float y = 1.0f * Integer.valueOf(fields[3].split("=")[1]) / imageSize;
                final float width = 1.0f * Integer.valueOf(fields[4].split("=")[1]) / imageSize;
                final float height = 1.0f * Integer.valueOf(fields[5].split("=")[1]) / imageSize;
                final float xOffset = 1.0f * Integer.valueOf(fields[6].split("=")[1]) / imageSize;
                final float yOffset = 1.0f * Integer.valueOf(fields[7].split("=")[1]) / imageSize;
                final float xAdvance = 1.0f * Integer.valueOf(fields[8].split("=")[1]) / imageSize;

                characters.add(new FontCharacter((char) character, x, y, width, height, xOffset, yOffset, xAdvance));
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return new Font(fontTexure, characters, padding / imageSize, lineHeight / imageSize);
    }
}

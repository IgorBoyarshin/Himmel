package himmel.graphics.fonts;

import himmel.graphics.textures.Texture;

import java.util.List;

/**
 * Created by Igorek on 12-Feb-16 at 3:02 PM.
 */
public class Font {

    private Texture texture;
    private List<FontCharacter> presentCharacters;
    private final float padding;
    private final float lineHeight;

    protected Font(Texture texture, List<FontCharacter> presentCharacters, float padding, float lineHeight) {
        this.texture = texture;
        this.presentCharacters = presentCharacters;
        this.padding = padding;
        this.lineHeight = lineHeight;
    }

    public Texture getTexture() {
        return texture;
    }

    public float getPadding() {
        return padding;
    }

    public float getLineHeight() {
        return lineHeight;
    }

    public FontCharacter getCharacter(char c) {
        return presentCharacters.stream()
                .filter(fontCharacter -> fontCharacter.c == c)
                .findFirst()
                .get();
    }

    public boolean isCharacterPresent(char c) {
        return !presentCharacters.stream()
                .noneMatch(fontCharacter -> fontCharacter.c == c);
    }
}

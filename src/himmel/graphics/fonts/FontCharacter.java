package himmel.graphics.fonts;

/**
 * Created by Igorek on 12-Feb-16.
 */
public class FontCharacter {

    public final char c;
    public final float x;
    public final float y;
    public final float width;
    public final float height;
    public final float xOffset;
    public final float yOffset;
    public final float xAdvance;

    public FontCharacter(char c, float x, float y, float width, float height, float xOffset, float yOffset, float xAdvance) {
        this.c = c;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.xAdvance = xAdvance;
    }
}

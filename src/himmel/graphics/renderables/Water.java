package himmel.graphics.renderables;

import himmel.graphics.renderers.RenderingSet;
import himmel.math.Vector2f;

/**
 * Created by Igor on 31-Jul-15.
 */
public class Water extends Renderable {
    public Water(Vector2f size, RenderingSet renderingSet) {
        super(convertVertices(size), null, null, renderingSet);
    }

    private static float[] convertVertices(Vector2f size) {
        return new float[]{
                0.0f, 0.0f,
                0.0f + size.x, 0.0f,
                0.0f + size.x, 0.0f + size.y,
                0.0f, 0.0f + size.y
        };
    }
}

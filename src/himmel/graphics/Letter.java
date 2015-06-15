package himmel.graphics;

import himmel.graphics.renderers.Renderer;

/**
 * Created by Igor on 04-Jun-15.
 */
public class Letter extends Renderable{
    public Letter(float[] vertices, short[] indices, float[] colors, Renderer renderer, Shader shader) {
        super(vertices, indices, colors, renderer, shader);
    }
}

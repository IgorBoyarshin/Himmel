package himmel.graphics.layers;

import himmel.graphics.Shader;
import himmel.graphics.renderers.Renderer;

/**
 * Created by Igor on 03-Jun-15.
 */
public class RenderingSet {
    private Renderer renderer;
    private Shader shader;

    public RenderingSet(Renderer renderer, Shader shader) {
        this.renderer = renderer;
        this.shader = shader;
    }

    public Renderer getRenderer() {
        return renderer;
    }

    public Shader getShader() {
        return shader;
    }
}

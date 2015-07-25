package himmel.graphics.renderers;

import himmel.graphics.Shader;

/**
 * Created by Igor on 7/25/2015.
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

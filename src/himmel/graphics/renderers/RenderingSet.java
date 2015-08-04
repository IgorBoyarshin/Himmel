package himmel.graphics.renderers;

import himmel.graphics.Shader;

/**
 * Created by Igor on 7/25/2015.
 */
public class RenderingSet implements Instancer {
    private Renderer renderer;
    private Instancer rendererInstancer;
    private Shader shader;
    private final String id;

    private static final String idSeparator = "-";

    // Creates an instance
    public RenderingSet(Instancer rendererInstancer, Shader shader) {
        this.rendererInstancer = rendererInstancer;
        this.renderer = (Renderer) rendererInstancer.createInstance();
        this.shader = shader;
        this.id = generateNewId(renderer, shader);
    }

    // Does not create an instance
    public RenderingSet(Instancer rendererInstancer, String rendererClassName, Shader shader) {
        this.rendererInstancer = rendererInstancer;
        this.shader = shader;
        this.id = generateNewId(rendererClassName, shader);
    }

    public Renderer getRenderer() {
        return renderer == null ? (Renderer) rendererInstancer.createInstance() : renderer;
    }

    public Shader getShader() {
        return shader;
    }

    public final String getId() {
        return id;
    }

    private static String generateNewId(Renderer renderer, Shader shader) {
        return renderer.getId() + idSeparator + shader.getId();
    }

    private static String generateNewId(String rendererId, Shader shader) {
        return rendererId + idSeparator + shader.getId();
    }

    @Override
    public RenderingSet createInstance() {
        return new RenderingSet(rendererInstancer, shader);
    }
}

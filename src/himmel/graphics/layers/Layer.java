package himmel.graphics.layers;

import himmel.graphics.renderables.Renderable;
import himmel.graphics.Shader;
import himmel.graphics.renderers.Renderer;
import himmel.graphics.renderers.RenderingSet;

import java.util.*;

/**
 * Created by Igor on 26-May-15.
 */
public class Layer {
    protected Map<RenderingSet, List<Renderable>> objects;
    protected int count;
    protected boolean reSubmit = true;

    public Layer() {
        objects = new HashMap<>();
        count = 0;
    }

    public void add(Renderable renderable) {
        count++;
        RenderingSet renderingSet = renderable.getRenderingSet();

        if (!objects.containsKey(renderingSet)) {
            List<Renderable> renderables = new ArrayList<>();

            renderables.add(renderable);
            objects.put(renderingSet, renderables);
        } else {
            objects.get(renderingSet).add(renderable);
        }
    }

    public void update() {
        for (RenderingSet renderingSet : objects.keySet()) {
            for (Renderable renderable : objects.get(renderingSet)) {
                if (renderable.isChanged()) {
                    reSubmit = true;
                    return;
                }
            }
        }
    }

    public void submit() {
        if (reSubmit) {
            for (RenderingSet renderingSet : objects.keySet()) {
                Shader shader = renderingSet.getShader();
                Renderer renderer = renderingSet.getRenderer();

                shader.enable();
                renderer.begin();
                for (Renderable renderable : objects.get(renderingSet)) {
                    if (renderable.isAlive()) {
                        renderable.submit(renderer);
                        renderable.setChanged(false);
                    } else {
                        objects.get(renderingSet).remove(renderable);
                    }
                }
                renderer.end();
            }

            reSubmit = false;
        }
    }

    public void render() {
        for (RenderingSet renderingSet : objects.keySet()) {
            Shader shader = renderingSet.getShader();
            Renderer renderer = renderingSet.getRenderer();

            shader.enable();
            renderer.render();
        }
    }

    public int getSize() {
        return count;
    }
}

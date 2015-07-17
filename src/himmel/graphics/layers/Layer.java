package himmel.graphics.layers;

import himmel.graphics.Renderable;
import himmel.graphics.Shader;
import himmel.graphics.renderers.FastSpriteRenderer;
import himmel.graphics.renderers.Renderer;
import himmel.math.Matrix4f;

import java.util.*;

/**
 * Created by Igor on 26-May-15.
 */
public class Layer {

    private Map<Shader, Map<Renderer, List<Renderable>>> objects;
    private int count;

    public Layer() {
        objects = new HashMap<>();
        count = 0;
    }

    public void add(Renderable renderable) {
        count++;
        Shader shader = renderable.getShader();
        Renderer renderer = renderable.getRenderer();

        if (!objects.containsKey(shader)) {
            Map<Renderer, List<Renderable>> map = new HashMap<>();
            List<Renderable> renderables = new ArrayList<>();

            renderables.add(renderable);
            map.put(renderer, renderables);
            objects.put(shader, map);
        } else {
            Map<Renderer, List<Renderable>> map = objects.get(shader);
            if (!map.containsKey(renderer)) { // if more than 1 renderer for a shader
                // TODO
            } else {
                List<Renderable> renderables = map.get(renderer);
                renderables.add(renderable);
            }
        }
    }

    public void render() {
        Set<Shader> setShader = objects.keySet();
        for (Shader shader : setShader) {
            Map<Renderer, List<Renderable>> map = objects.get(shader);
            Set<Renderer> setRenderer = map.keySet();
            shader.enable();
            for (Renderer renderer : setRenderer) {
                List<Renderable> renderables = map.get(renderer);
                renderer.begin();
                for (Renderable renderable : renderables) {
//                    renderer.push(renderable.getModelMatrix());
                    if (renderable.isAlive()) {
                        renderable.submit(renderer);
                    } else{
                        renderables.remove(renderable);
                    }
//                    renderer.pop();
                }
                renderer.end();
                renderer.render();
            }
        }
    }

    public int getSize() {
        return count;
    }
}

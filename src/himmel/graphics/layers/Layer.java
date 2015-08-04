package himmel.graphics.layers;

import himmel.graphics.renderables.Renderable;
import himmel.graphics.Shader;
import himmel.graphics.renderers.Renderer;
import himmel.graphics.renderers.RenderingSet;
import org.lwjgl.glfw.GLFW;

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
        reSubmit = true;

        RenderingSet renderingSet = renderable.getRenderingSet();
        String renderingSetId = renderingSet.getId();

        boolean found = objects.keySet()
                .stream()
                .anyMatch(rs -> rs.getId().equals(renderingSetId));

//        boolean found = false;
//        for (RenderingSet rs : objects.keySet()) {
//            if (rs.getId().equals(renderingSetId)) {
//                found = true;
//                break;
//            }
//        }

        if (!found) {
            List<Renderable> renderables = new ArrayList<>();

            renderables.add(renderable);
            objects.put(renderingSet.createInstance(), renderables);
        } else {
//            objects.get(renderingSet).add(renderable);
            objects.get(getRenderingSet(renderingSetId)).add(renderable);
        }

//        if (!objects.containsKey(renderingSet)) {
//            List<Renderable> renderables = new ArrayList<>();
//
//            renderables.add(renderable);
//            objects.put(renderingSet, renderables);
//        } else {
//            objects.get(renderingSet).add(renderable);
//        }
    }

    private RenderingSet getRenderingSet(String id) {
        return objects.keySet()
                .stream()
                .filter(renderingSet -> renderingSet.getId().equals(id))
                .findFirst()
                .get();
    }

    public void update() {
        for (RenderingSet renderingSet : objects.keySet()) {
            for (Renderable renderable : objects.get(renderingSet)) {
                if (renderable.isChanged()) {
                    reSubmit = true;
                    break;
                }
            }
            if (reSubmit) {
                break;
            }
        }

        submit();
    }

    protected void submit() {
        if (reSubmit) {
            for (RenderingSet renderingSet : objects.keySet()) {
                Shader shader = renderingSet.getShader();
                Renderer renderer = renderingSet.getRenderer();

                shader.enable();
                renderer.begin();
                for (Renderable renderable : objects.get(renderingSet)) {
                    if (renderable.isAlive()) {
                        renderable.setChanged(false);
                        renderable.submit(renderer);
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

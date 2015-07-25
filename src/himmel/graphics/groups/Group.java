package himmel.graphics.groups;

import himmel.graphics.renderables.Renderable;
import himmel.graphics.renderers.Renderer;
import himmel.math.Matrix4f;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Igor on 27-May-15.
 */
public class Group extends Renderable {

    private List<Renderable> children;
    private Matrix4f transformationMatrix;

    public Group(Matrix4f transform) {
        super(null, null, null, null);

        children = new ArrayList<>();
        transformationMatrix = transform;
    }

    public void add(Renderable renderable) {
        children.add(renderable);
    }

    @Override
    public void submit(Renderer renderer) {
        renderer.push(transformationMatrix);
        for (Renderable renderable : children) {
            renderable.submit(renderer);
        }
        renderer.pop();
    }
}

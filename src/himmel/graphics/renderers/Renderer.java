package himmel.graphics.renderers;

import himmel.graphics.renderables.Renderable;
import himmel.math.Matrix4f;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Igor on 26-May-15.
 */
public abstract class Renderer{
    protected List<Matrix4f> transformationStack;
    protected volatile Matrix4f transformationStackCash;

    protected boolean filling = false;

    public Renderer() {
        transformationStack = new ArrayList<>();
        transformationStack.add(Matrix4f.identity());
        transformationStackCash = transformationStack.get(0);
    }

    public void pushOverride(Matrix4f matrix) {
        transformationStack.add(matrix);

        transformationStackCash = matrix;
    }

    public void push(Matrix4f matrix) {
        Matrix4f newMatrix = transformationStackCash.multiply(matrix);
        transformationStack.add(newMatrix);

        transformationStackCash = newMatrix;
    }

    public void pop() {
        int size = transformationStack.size();

        if (size > 1) {
            transformationStack.remove(size - 1);
            size--;
            transformationStackCash = transformationStack.get(size - 1);
        }
    }

    public abstract void begin();

    public abstract void end();

    public abstract void submit(Renderable renderable);

    public abstract void render();

    public String getId() {
        String classNameFull = this.getClass().getName();
        int curIndex = classNameFull.length() - 1;
        while (curIndex > 0 && classNameFull.charAt(curIndex) != '.') {
            curIndex--;
        }
        return classNameFull.substring(curIndex + 1);
    }
}

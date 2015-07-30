package himmel.graphics.layers;

import himmel.graphics.renderables.Renderable;
import himmel.graphics.renderers.Renderer3D;
import himmel.graphics.renderers.RenderingSet;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.util.List;

/**
 * Created by Igor on 7/26/2015.
 */
public class LayerWithMatrices extends Layer {
    private List<Renderable> renderablesWithMatricesInShader;

    public LayerWithMatrices() {
        super();
    }

    @Override
    public void update() {
        for (RenderingSet renderingSet : objects.keySet()) {
            // TODO: get rid of Renderer3D.AMOUNT_OF_MATRICES
            FloatBuffer newMatrices = BufferUtils.createFloatBuffer(16 * Renderer3D.AMOUNT_OF_MATRICES);
            boolean found = false;

            for (Renderable renderable : objects.get(renderingSet)) {
                if (!renderable.isAlive()) {
                    objects.remove(renderable);
                    continue;
                }
                if (renderable.isChanged()) {
                    reSubmit = true;
                }
                if (renderable.getMid() > 0.0f) {
                    found = true;
                    newMatrices.put(renderable.getModelMatrix().toFloatBuffer());
                }
            }

            if (found) {
                newMatrices.rewind();
                renderingSet.getShader().enable();
                renderingSet.getShader().setUniformMat4fv("modelMatrices", newMatrices);
            }
        }

        submit();
    }
}

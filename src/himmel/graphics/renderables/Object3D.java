package himmel.graphics.renderables;

import himmel.graphics.Texture;
import himmel.graphics.renderers.RenderingSet;
import himmel.math.Vector4f;

/**
 * Created by Igor on 7/25/2015.
 */
public class Object3D extends Renderable {


    public Object3D(float[] vertices, float[] normals, float[] colors, short[] indices, RenderingSet renderingSet) {
        super(vertices, normals, colors, indices, renderingSet);
    }

    public Object3D(float[] vertices, float[] normals, Texture texture, float[] uv, short[] indices, RenderingSet renderingSet) {
        super(vertices, normals, texture, uv, indices, renderingSet);
    }

    // TODO: reformat. Or not...
    public Object3D(Model model, Vector4f color, RenderingSet renderingSet) {
        super(model.getVertices(), model.getNormals(),
                getColors(color, model.getVertices().length / 3), model.getIndices(), renderingSet);
    }

    private static float[] getColors(Vector4f color, int amount) {
        float[] colors = new float[amount * 4];

        for (int i = 0; i < amount; i++) {
            colors[4 * i + 0] = color.x;
            colors[4 * i + 1] = color.y;
            colors[4 * i + 2] = color.z;
            colors[4 * i + 3] = color.w;
        }

        return colors;
    }
}

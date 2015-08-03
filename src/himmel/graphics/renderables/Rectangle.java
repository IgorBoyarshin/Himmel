package himmel.graphics.renderables;

import himmel.graphics.Texture;
import himmel.graphics.renderers.RenderingSet;
import himmel.math.Vector3f;
import himmel.math.Vector4f;

/**
 * Created by Igor on 02-Aug-15.
 */
public class Rectangle extends Renderable {
    private static RenderingSet renderingSet;

    public Rectangle(Vector3f position1, Vector3f position2, Vector4f color) {
        super(convertVertices(position1, position2), convertColors(color), new short[]{0, 1, 2, 0, 2, 3}, renderingSet);
    }

    public Rectangle(Vector3f position1, Vector3f position2, Texture texture) {
        super(convertVertices(position1, position2), texture, getUVs(), new short[]{0, 1, 2, 0, 2, 3}, renderingSet);
    }

    public static void setRenderingSet(RenderingSet theRenderingSet) {
        renderingSet = theRenderingSet;
    }

    private static final float[] getUVs() {
        return new float[]{
                0.0f, 0.0f,
                0.0f, 1.0f,
                1.0f, 1.0f,
                1.0f, 0.0f
        };
    }

    private static final float[] convertVertices(Vector3f position1, Vector3f position2) {
        return new float[] {
                position1.x, position1.y, position1.z,
                position1.x, position2.y, position1.z,
                position2.x, position2.y, position2.z,
                position2.x, position1.y, position2.z
        };
    }

    private static final float[] convertColors(Vector4f color) {
        return new float[] {
                color.x, color.y, color.z, color.w,
                color.x, color.y, color.z, color.w,
                color.x, color.y, color.z, color.w,
                color.x, color.y, color.z, color.w
        };
    }
}

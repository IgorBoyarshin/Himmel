package himmel.graphics.renderables;

import himmel.graphics.Texture;
import himmel.graphics.renderers.RenderingSet;
import himmel.math.Vector3f;
import himmel.math.Vector4f;

/**
 * Created by Igor on 02-Aug-15.
 */
public class Cube extends Renderable {
//    private static RenderingSet renderingSet;

    public Cube(Vector3f center, Vector3f size, Vector4f color, RenderingSet renderingSet) {
        super(convertVertices(center, size), convertNormals(), convertColors(color), convertIndices(), renderingSet);
    }

//    public Cube(Vector3f center, Vector3f size, Texture texture, RenderingSet renderingSet) {
//        super(convertVertices(center, size), texture, getUVs(), new short[]{0, 1, 2, 0, 2, 3}, renderingSet);
//    }

//    public static void setRenderingSet(RenderingSet theRenderingSet) {
//        renderingSet = theRenderingSet;
//    }

//    private static final float[] getUVs() {
//        return new float[]{
//                0.0f, 0.0f,
//                0.0f, 1.0f,
//                1.0f, 1.0f,
//                1.0f, 0.0f
//        };
//    }

    private static final float[] convertNormals() {
        return new float[]{
                0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f,

                1.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.0f,

                0.0f, 0.0f, -1.0f,
                0.0f, 0.0f, -1.0f,
                0.0f, 0.0f, -1.0f,
                0.0f, 0.0f, -1.0f,

                -1.0f, 0.0f, 0.0f,
                -1.0f, 0.0f, 0.0f,
                -1.0f, 0.0f, 0.0f,
                -1.0f, 0.0f, 0.0f,

                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f,

                0.0f, -1.0f, 0.0f,
                0.0f, -1.0f, 0.0f,
                0.0f, -1.0f, 0.0f,
                0.0f, -1.0f, 0.0f
        };
    }

    private static final short[] convertIndices() {
        return new short[]{
                4 * 0 + 0, 4 * 0 + 1, 4 * 0 + 2, 4 * 0 + 0, 4 * 0 + 2, 4 * 0 + 3,
                4 * 1 + 0, 4 * 1 + 1, 4 * 1 + 2, 4 * 1 + 0, 4 * 1 + 2, 4 * 1 + 3,
                4 * 2 + 0, 4 * 2 + 1, 4 * 2 + 2, 4 * 2 + 0, 4 * 2 + 2, 4 * 2 + 3,
                4 * 3 + 0, 4 * 3 + 1, 4 * 3 + 2, 4 * 3 + 0, 4 * 3 + 2, 4 * 3 + 3,
                4 * 4 + 0, 4 * 4 + 1, 4 * 4 + 2, 4 * 4 + 0, 4 * 4 + 2, 4 * 4 + 3,
                4 * 5 + 0, 4 * 5 + 1, 4 * 5 + 2, 4 * 5 + 0, 4 * 5 + 2, 4 * 5 + 3
        };
    }

    private static final float[] convertVertices(Vector3f center, Vector3f size) {
        return new float[]{
                // Front
                center.x - size.x / 2.0f, center.y - size.y / 2.0f, center.z + size.z / 2.0f,
                center.x - size.x / 2.0f, center.y + size.y / 2.0f, center.z + size.z / 2.0f,
                center.x + size.x / 2.0f, center.y + size.y / 2.0f, center.z + size.z / 2.0f,
                center.x + size.x / 2.0f, center.y - size.y / 2.0f, center.z + size.z / 2.0f,

                // Right
                center.x + size.x / 2.0f, center.y - size.y / 2.0f, center.z + size.z / 2.0f,
                center.x + size.x / 2.0f, center.y + size.y / 2.0f, center.z + size.z / 2.0f,
                center.x + size.x / 2.0f, center.y + size.y / 2.0f, center.z - size.z / 2.0f,
                center.x + size.x / 2.0f, center.y - size.y / 2.0f, center.z - size.z / 2.0f,

                // Back
                center.x + size.x / 2.0f, center.y - size.y / 2.0f, center.z - size.z / 2.0f,
                center.x + size.x / 2.0f, center.y + size.y / 2.0f, center.z - size.z / 2.0f,
                center.x - size.x / 2.0f, center.y + size.y / 2.0f, center.z - size.z / 2.0f,
                center.x - size.x / 2.0f, center.y - size.y / 2.0f, center.z - size.z / 2.0f,

                // Left
                center.x - size.x / 2.0f, center.y - size.y / 2.0f, center.z - size.z / 2.0f,
                center.x - size.x / 2.0f, center.y + size.y / 2.0f, center.z - size.z / 2.0f,
                center.x - size.x / 2.0f, center.y + size.y / 2.0f, center.z + size.z / 2.0f,
                center.x - size.x / 2.0f, center.y - size.y / 2.0f, center.z + size.z / 2.0f,

                // Up
                center.x - size.x / 2.0f, center.y + size.y / 2.0f, center.z + size.z / 2.0f,
                center.x - size.x / 2.0f, center.y + size.y / 2.0f, center.z - size.z / 2.0f,
                center.x + size.x / 2.0f, center.y + size.y / 2.0f, center.z - size.z / 2.0f,
                center.x + size.x / 2.0f, center.y + size.y / 2.0f, center.z + size.z / 2.0f,

                // Down
                center.x + size.x / 2.0f, center.y - size.y / 2.0f, center.z + size.z / 2.0f,
                center.x + size.x / 2.0f, center.y - size.y / 2.0f, center.z - size.z / 2.0f,
                center.x - size.x / 2.0f, center.y - size.y / 2.0f, center.z - size.z / 2.0f,
                center.x - size.x / 2.0f, center.y - size.y / 2.0f, center.z + size.z / 2.0f,
        };
    }

    private static final float[] convertColors(Vector4f color) {
        return new float[]{
                color.x, color.y, color.z, color.w,
                color.x, color.y, color.z, color.w,
                color.x, color.y, color.z, color.w,
                color.x, color.y, color.z, color.w,

                color.x, color.y, color.z, color.w,
                color.x, color.y, color.z, color.w,
                color.x, color.y, color.z, color.w,
                color.x, color.y, color.z, color.w,

                color.x, color.y, color.z, color.w,
                color.x, color.y, color.z, color.w,
                color.x, color.y, color.z, color.w,
                color.x, color.y, color.z, color.w,

                color.x, color.y, color.z, color.w,
                color.x, color.y, color.z, color.w,
                color.x, color.y, color.z, color.w,
                color.x, color.y, color.z, color.w,

                color.x, color.y, color.z, color.w,
                color.x, color.y, color.z, color.w,
                color.x, color.y, color.z, color.w,
                color.x, color.y, color.z, color.w,

                color.x, color.y, color.z, color.w,
                color.x, color.y, color.z, color.w,
                color.x, color.y, color.z, color.w,
                color.x, color.y, color.z, color.w
        };
    }
}

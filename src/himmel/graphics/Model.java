package himmel.graphics;

import himmel.math.Vector2f;
import himmel.math.Vector3f;
import himmel.math.Vector3i;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Igor on 20-Jul-15.
 */
public class Model {
    private float[] vertices;
    private float[] normals;
    private float[] uvs;
    private int[] indicesInts;
    private short[] indicesShorts;
    private boolean indicesAsShorts;

    private final int MAX_SHORT_VALUE = Short.MAX_VALUE;

    public Model(String pathToModel) {
        loadModel(pathToModel);
    }

    private void loadModel(String path) {
        long startLoading = System.currentTimeMillis();

        // Load data

        List<Vector3f> positionsArray = new ArrayList<>();
        List<Vector2f> uvsArray = new ArrayList<>();
        List<Vector3f> normalsArray = new ArrayList<>();
        List<Face> facesArray = new ArrayList<>();
        boolean uvsPresent = false;
        boolean normalsPresent = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("v ")) {
                    String[] values = line.split(" ");

                    final float x = Float.valueOf(values[1]);
                    final float y = Float.valueOf(values[2]);
                    final float z = Float.valueOf(values[3]);

                    positionsArray.add(new Vector3f(x, y, z));
                } else if (line.startsWith("vt ")) {
                    String[] values = line.split(" ");

                    final float x = Float.valueOf(values[1]);
                    final float y = Float.valueOf(values[2]);

                    uvsArray.add(new Vector2f(x, y));
                    uvsPresent = true;
                } else if (line.startsWith("vn ")) {
                    String[] values = line.split(" ");

                    final float x = Float.valueOf(values[1]);
                    final float y = Float.valueOf(values[2]);
                    final float z = Float.valueOf(values[3]);

                    normalsArray.add(new Vector3f(x, y, z));
                    normalsPresent = true;
                } else if (line.startsWith("f ")) {
                    String[] groups = line.split(" ");
                    String[] vertex0 = groups[1].split("/");
                    String[] vertex1 = groups[2].split("/");
                    String[] vertex2 = groups[3].split("/");

                    // -1 because .obj format starts with 1, not 0
                    facesArray.add(new Face(
                            new Vector3i(
                                    Integer.valueOf(emptyCheck(vertex0[0])) - 1,
                                    Integer.valueOf(emptyCheck(vertex0[1])) - 1,
                                    Integer.valueOf(emptyCheck(vertex0[2])) - 1),
                            new Vector3i(
                                    Integer.valueOf(emptyCheck(vertex1[0])) - 1,
                                    Integer.valueOf(emptyCheck(vertex1[1])) - 1,
                                    Integer.valueOf(emptyCheck(vertex1[2])) - 1),
                            new Vector3i(
                                    Integer.valueOf(emptyCheck(vertex2[0])) - 1,
                                    Integer.valueOf(emptyCheck(vertex2[1])) - 1,
                                    Integer.valueOf(emptyCheck(vertex2[2])) - 1)
                    ));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        long finishLoad = System.currentTimeMillis();
        // Process data

        List<Vector3i> verticesAsIndices = new ArrayList<>(); // as <v,vt,vn> triples
        List<Integer> indices = new ArrayList<>();

        for (Face face : facesArray) {
            for (Vector3i vertex : face.verticesIndices) {
                indices.add(addElementAndReturnIndex(vertex, verticesAsIndices));
            }
        }

        long finishProcess = System.currentTimeMillis();
        // Return data

        indicesAsShorts = (verticesAsIndices.size() <= MAX_SHORT_VALUE);
        this.vertices = new float[verticesAsIndices.size() * 3];
        this.uvs = (uvsPresent) ? (new float[verticesAsIndices.size() * 2]) : null;
        this.normals = (normalsPresent) ? (new float[verticesAsIndices.size() * 3]) : null;
        if (indicesAsShorts) {
            this.indicesShorts = new short[indices.size()];
        } else {
            this.indicesInts = new int[indices.size()];
        }

        for (int i = 0; i < verticesAsIndices.size(); i++) {
            Vector3i vertexIndices = verticesAsIndices.get(i);

            Vector3f position = positionsArray.get(vertexIndices.x);
            Vector2f uv = (uvsPresent) ? uvsArray.get(vertexIndices.y) : null;
            Vector3f normal = (normalsPresent) ? normalsArray.get(vertexIndices.z) : null;

            this.vertices[3 * i + 0] = position.x;
            this.vertices[3 * i + 1] = position.y;
            this.vertices[3 * i + 2] = position.z;

            if (uvsPresent) {
                this.uvs[2 * i + 0] = uv.x;
                this.uvs[2 * i + 1] = uv.y;
            }

            if (normalsPresent) {
                this.normals[3 * i + 0] = normal.x;
                this.normals[3 * i + 1] = normal.y;
                this.normals[3 * i + 2] = normal.z;
            }
        }

        if (indicesAsShorts) {
            for (int i = 0; i < indices.size(); i++) {
                this.indicesShorts[i] = indices.get(i).shortValue();
            }
        } else {
            for (int i = 0; i < indices.size(); i++) {
                this.indicesInts[i] = indices.get(i);
            }
        }


        long endLoading = System.currentTimeMillis();
//        System.out.println("Load time: " + (finishLoad - startLoading));
//        System.out.println("Process time: " + (finishProcess - finishLoad));
//        System.out.println("Return time: " + (endLoading - finishProcess));
//        System.out.println("Total loading time: " + (endLoading - startLoading));
    }

    private String emptyCheck(String string) {
        final String safetyString = "1";
        return (string.length() == 0) ? (safetyString) : (string);
    }

    /**
     * Adds the vertex if the array does not contain such int triple(not object!!).
     */
    private int addElementAndReturnIndex(Vector3i vertex, List<Vector3i> array) {
        for (int elementIndex = 0; elementIndex < array.size(); elementIndex++) {
            if (vectorsEqual(vertex, array.get(elementIndex))) {
                return elementIndex;
            }
        }

        array.add(vertex);
        return (array.size() - 1);
    }

    private boolean vectorsEqual(Vector3i v1, Vector3i v2) {
        return (v1.x == v2.x &&
                v1.y == v2.y &&
                v1.z == v2.z);
    }

    public float[] getVertices() {
        return vertices;
    }

    public float[] getNormals() {
        return normals;
    }

    public float[] getUvs() {
        return uvs;
    }

    public int[] getIndicesInts() {
        return indicesInts;
    }

    public short[] getIndicesShorts() {
        return indicesShorts;
    }

    public boolean areIndicesAsShorts() {
        return indicesAsShorts;
    }

    private class Face {
        public Vector3i[] verticesIndices = new Vector3i[3]; // <v, vt, vn> 3 times

        public Face(Vector3i v1, Vector3i v2, Vector3i v3) {
            verticesIndices[0] = v1;
            verticesIndices[1] = v2;
            verticesIndices[2] = v3;
        }
    }
}

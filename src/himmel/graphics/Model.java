package himmel.graphics;

import himmel.math.Vector3f;
import himmel.math.Vector3s;

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
    private short[] indices;

    public Model(String pathToModel) {
        try {
            loadModel(pathToModel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadModel(String path) throws IOException {
        List<Vector3f> verticesArray = new ArrayList<>();
        List<Vector3f> normalsArray = new ArrayList<>();
        List<Face> facesArray = new ArrayList<>();

        // Load data
        BufferedReader reader = new BufferedReader(new FileReader(path));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("v ")) {
                float x = Float.valueOf(line.split(" ")[1]);
                float y = Float.valueOf(line.split(" ")[2]);
                float z = Float.valueOf(line.split(" ")[3]);
                verticesArray.add(new Vector3f(x, y, z));
            } else if (line.startsWith("vn ")) {
                float x = Float.valueOf(line.split(" ")[1]);
                float y = Float.valueOf(line.split(" ")[2]);
                float z = Float.valueOf(line.split(" ")[3]);
                normalsArray.add(new Vector3f(x, y, z));
            } else if (line.startsWith("f ")) {
                Vector3s vertexIndex = new Vector3s(
                        (short) (Short.valueOf(line.split(" ")[1].split("/")[0]) - 1),
                        (short) (Short.valueOf(line.split(" ")[2].split("/")[0]) - 1),
                        (short) (Short.valueOf(line.split(" ")[3].split("/")[0]) - 1));
                Vector3s normalIndex = new Vector3s(
                        (short) (Short.valueOf(line.split(" ")[1].split("/")[2]) - 1),
                        (short) (Short.valueOf(line.split(" ")[2].split("/")[2]) - 1),
                        (short) (Short.valueOf(line.split(" ")[3].split("/")[2]) - 1));
                facesArray.add(new Face(vertexIndex, normalIndex));
            }
        }

        // Process data
        List<Vertex> vertices = new ArrayList<>();
        List<Short> indices = new ArrayList<>();
        for (short face = 0; face < facesArray.size(); face++) {
            Vertex vertex1 = new Vertex(verticesArray.get(facesArray.get(face).vertices.x),
                    normalsArray.get(facesArray.get(face).normals.x));
            Vertex vertex2 = new Vertex(verticesArray.get(facesArray.get(face).vertices.y),
                    normalsArray.get(facesArray.get(face).normals.y));
            Vertex vertex3 = new Vertex(verticesArray.get(facesArray.get(face).vertices.z),
                    normalsArray.get(facesArray.get(face).normals.z));

            boolean matchFound = false;

            // V1
            for (short i = 0; i < vertices.size(); i++) {
                if (vertices.get(i).equals(vertex1)) {
                    matchFound = true;
                    indices.add(i);
                    break;
                }
            }
            if (!matchFound) {
                vertices.add(vertex1);
                indices.add((short) (vertices.size() - 1));
            }
            matchFound = false;

            // V2
            for (short i = 0; i < vertices.size(); i++) {
                if (vertices.get(i).equals(vertex2)) {
                    matchFound = true;
                    indices.add(i);
                    break;
                }
            }
            if (!matchFound) {
                vertices.add(vertex2);
                indices.add((short) (vertices.size() - 1));
            }
            matchFound = false;

            // V3
            for (short i = 0; i < vertices.size(); i++) {
                if (vertices.get(i).equals(vertex3)) {
                    matchFound = true;
                    indices.add(i);
                    break;
                }
            }
            if (!matchFound) {
                vertices.add(vertex3);
                indices.add((short) (vertices.size() - 1));
            }
        }

        // Return data
        this.vertices = new float[vertices.size() * 3];
        this.normals = new float[vertices.size() * 3];
        this.indices = new short[indices.size()];

        for (int i = 0; i < vertices.size(); i++) {
            Vertex vertex = vertices.get(i);
            this.vertices[3 * i + 0] = vertex.position.x;
            this.vertices[3 * i + 1] = vertex.position.y;
            this.vertices[3 * i + 2] = vertex.position.z;

            this.normals[3 * i + 0] = vertex.normal.x;
            this.normals[3 * i + 1] = vertex.normal.y;
            this.normals[3 * i + 2] = vertex.normal.z;
        }

        for (int i = 0; i < indices.size(); i++) {
            this.indices[i] = indices.get(i);
        }

//        System.out.println("verts");
//        int counter = 0;
//        for (float f : this.vertices) {
//            System.out.print(f + " ");
//            counter++;
//            if (counter == 3) {
//                counter = 0;
//                System.out.println();
//            }
//        }
//        System.out.println("ind");
//        counter = 0;
//        for (short f : this.indices) {
//            System.out.print(f + " ");
//        }
    }

    public short[] getIndices() {
        return indices;
    }

    public float[] getNormals() {
        return normals;
    }

    public float[] getVertices() {
        return vertices;
    }

    private class Vertex {
        public Vector3f position;
        public Vector3f normal;

        public Vertex(Vector3f position, Vector3f normal) {
            this.position = position;
            this.normal = normal;
        }

        public boolean equals(Vertex otherVertex) {
            if (this.position.x == otherVertex.position.x &&
                    this.position.y == otherVertex.position.y &&
                    this.position.z == otherVertex.position.z) {
                if (this.normal.x == otherVertex.normal.x &&
                        this.normal.y == otherVertex.normal.y &&
                        this.normal.z == otherVertex.normal.z) {
                    return true;
                }
            }

            return false;
        }
    }

    private class Face {
        public Vector3s vertices;
        public Vector3s normals;

        public Face(Vector3s vertices, Vector3s normals) {
            this.vertices = vertices;
            this.normals = normals;
        }

//        public List<Vertex> getArray() {
//            List<Vertex> list = new ArrayList<>();
//            list.add(new Vertex(vertices.x));
//        }
    }
}

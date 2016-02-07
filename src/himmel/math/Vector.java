package himmel.math;

/**
 * The class provides functionality for a float vector.
 * Created by Igor on 06-Feb-16 at 3:13 PM.
 */
public abstract class Vector {

    protected final int dimensions;

    protected Vector(final int dimensions) {
        this.dimensions = dimensions;
    }

    public abstract Vector normalize();
    public abstract float getLength();
    public abstract Vector copy();

    /**
     * Does not alter this vector.
     * @return The sum of two vectors, provided that
     * the argument has fewer or the same number of dimensions, does nothing otherwise.
     */
    public abstract Vector add(Vector vector);

    /**
     * Does not alter this vector.
     * @return The result of subtraction the argument from this vector, provided that
     * the argument has fewer or the same number of dimensions, does nothing otherwise.
     */
    public abstract Vector subtract(Vector vector);

    /**
     * Does not alter this vector.
     * @return The product of two vectors, provided that
     * the argument has fewer or the same number of dimensions, 0.0f otherwise.
     */
    public abstract float dot(Vector vector);

//    /**
//     * Performs a cross product and assigns the result to this vector, provided that
//     * the argument has fewer or the same number of dimensions, does nothing otherwise.
//     */
//    public abstract void cross(Vector vector);

    /**
     * Does not alter the first or the second argument.
     * @return A new vector that is the sum of the given two, provided that
     * vector2 has fewer or the same number of dimensions than vector1, null otherwise.
     */
    public static Vector add(Vector vector1, Vector vector2) {
        if (vector2.dimensions <= vector1.dimensions) {
            Vector newVector = vector1.copy();
            newVector.add(vector2);
            return newVector;
        }

        return null;
    }

    /**
     * Does not alter the first or the second argument.
     * @return A new vector that is the result of subtraction of vector2 from vector1, provided that
     * vector2 has fewer or the same number of dimensions than vector1, null otherwise.
     */
    public static Vector subtract(Vector vector1, Vector vector2) {
        if (vector2.dimensions <= vector1.dimensions) {
            Vector newVector = vector1.copy();
            newVector.subtract(vector2);
            return newVector;
        }

        return null;
    }

    /**
     * Does not alter the first or the second argument.
     * @return The dot product of vector1 over vector2, provided that
     * vector2 has fewer or the same number of dimensions than vector1, 0.0f otherwise.
     */
    public static float dot(Vector vector1, Vector vector2) {
        if (vector2.dimensions <= vector1.dimensions) {
            return vector1.dot(vector2);
        }

        return 0.0f;
    }

//    /**
//     * Does not alter the first or the second argument.
//     * @return A new vector that is the cross product of vector1 over vector2, provided that
//     * the dimension of both vectors is 3, null otherwise.
//     */
//    public static Vector cross(Vector vector1, Vector vector2) {
//        if (vector2.dimensions == 3 && vector2.dimensions == vector1.dimensions) {
//            Vector newVector = vector1.copy();
//            newVector.cross(vector2);
//            return newVector;
//        }
//
//        return null;
//    }
}

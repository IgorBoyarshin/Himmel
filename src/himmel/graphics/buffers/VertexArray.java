package himmel.graphics.buffers;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

/**
 * Created by Igor on 03-May-15.
 */
public class VertexArray {
    private int arrayID;
    private List<Buffer> buffers;

    public VertexArray() {
        arrayID = glGenVertexArrays();
        buffers = new ArrayList<>();
    }

    public void destruct() {
        for (Buffer buffer : buffers) {
            buffer.destruct();
        }

        glDeleteVertexArrays(arrayID);
    }

    public void addBuffer(Buffer buffer, int index) {
        bind();
        buffer.bind();

        glEnableVertexAttribArray(index);
        glVertexAttribPointer(index, buffer.getComponentCount(), GL_FLOAT, false, 0, 0);

        buffer.unbind();
        unbind();

        buffers.add(buffer);
    }

    public void bind() {
        glBindVertexArray(arrayID);
    }

    public void unbind() {
        glBindVertexArray(0);
    }
}

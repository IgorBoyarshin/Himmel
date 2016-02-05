package himmel.graphics.layers;

import himmel.graphics.buffers.VertexArrayObject;
import himmel.graphics.entities.Entity;
import himmel.graphics.renderables.Renderable;
import himmel.graphics.Shader;
import himmel.graphics.renderers.Renderer;
import himmel.graphics.renderers.RenderingSet;
import org.lwjgl.glfw.GLFW;

import java.util.*;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawElements;

/**
 * Created by Igor on 26-May-15.
 */
public class Layer {
    private Map<Shader, Map<VertexArrayObject, List<Entity>>> objects;

    public Layer() {
        objects = new HashMap<>();
    }

    public void add(Entity entity) {
        addToShader(entity);
    }

    public void remove(Entity entity) {
        Map<VertexArrayObject, List<Entity>> vaos = objects.get(entity.getShader());
        if (vaos == null) {
            return;
        }

        List<Entity> entities = vaos.get(entity.getVertexArrayObject());
        if (entities == null) {
            return;
        }

        entities.remove(entity);
    }

    private void addToShader(Entity entity) {
        boolean shaderMatchFound = objects.keySet()
                .stream()
                .anyMatch(shader -> shader.equals(entity.getShader()));

        if (!shaderMatchFound) {
            objects.put(entity.getShader(), new HashMap<>());
        }
        addToVertexArrayObject(entity);
    }

    private void addToVertexArrayObject(Entity entity) {
        Map<VertexArrayObject, List<Entity>> vaoMap = objects.get(entity.getShader());
        boolean vaoMatchFound = vaoMap.keySet()
                .stream()
                .anyMatch(vao -> vao.equals(entity.getVertexArrayObject()));

        if (!vaoMatchFound) {
            vaoMap.put(entity.getVertexArrayObject(), new ArrayList<>());
        }
        vaoMap.get(entity.getVertexArrayObject()).add(entity);
    }

    public void render() {
        for (Shader shader : objects.keySet()) {
            Map<VertexArrayObject, List<Entity>> vaos = objects.get(shader);
            shader.enable();
            for (VertexArrayObject vao : vaos.keySet()) {
                List<Entity> entities = vaos.get(vao);
                vao.bind();
                vao.enableAttribArrays();
                for (Entity entity : entities) {
                    entity.setShaderParameters();
                    glDrawElements(GL_TRIANGLES, vao.getVertexCount(), vao.getIndexType().typeCode, 0);
                }
                vao.disableAttribArrays();
                vao.unbind();
            }
            shader.disable();
        }
    }
}

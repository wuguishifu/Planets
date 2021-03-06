package com.planets.engine.graphics;

import com.planets.engine.io.window.Window;
import com.planets.engine.math.Matrix4f;
import com.planets.engine.math.Vector3f;
import com.planets.engine.objects.RenderObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

public class Renderer {
    // the shader program
    private Shader shader;

    // the window to render to
    private Window window;

    // the light color
    private Vector3f lightColor = new Vector3f(1.0f, 1.0f, 1.0f);

    /**
     * default constructor
     * @param window - the specified window to render to
     * @param shader - the shader program to use to render
     */
    public Renderer(Window window, Shader shader) {
        this.shader = shader;
        this.window = window;
    }

    /**
     * renders the mesh
     * @param object - the object to be rendered
     * @param camera - the camera perspective
     */
    public void renderMesh(RenderObject object, Camera camera, Vector3f lightPosition) {
        GL30.glBindVertexArray(object.getMesh().getVAO());
        GL30.glEnableVertexAttribArray(0);
        GL30.glEnableVertexAttribArray(1);
        GL30.glEnableVertexAttribArray(2);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, object.getMesh().getIBO());
        shader.bind();
        shader.setUniform("model", Matrix4f.transform(object.getPosition(), object.getRotation(), object.getScale()));
        shader.setUniform("view", Matrix4f.view(camera.getPosition(), camera.getRotation()));
        shader.setUniform("projection", window.getProjectionMatrix());
        shader.setUniform("lightPos", lightPosition);
        shader.setUniform("lightLevel", 0.1f);
        shader.setUniform("viewPos", camera.getPosition());
        shader.setUniform("lightColor", lightColor);
        GL11.glDrawElements(GL11.GL_TRIANGLES, object.getMesh().getIndices().length, GL11.GL_UNSIGNED_INT, 0);
        shader.unbind();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL30.glDisableVertexAttribArray(0);
        GL30.glDisableVertexAttribArray(1);
        GL30.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

    /**
     * sets the light color
     * @param lightColor - the new color
     */
    public void setLightColor(Vector3f lightColor) {
        this.lightColor = lightColor;
    }

    /**
     * getter method
     * @return - the light color
     */
    public Vector3f getLightColor() {
        return this.lightColor;
    }
}
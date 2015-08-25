package com.jagers.spelet.level;

import static org.lwjgl.glfw.GLFW.*;

import com.jagers.spelet.graphics.Shader;
import com.jagers.spelet.graphics.Texture;
import com.jagers.spelet.graphics.VertexArray;
import com.jagers.spelet.math.Matrix4f;
import com.jagers.spelet.math.Vector3f;
import com.jagers.spelet.input.Input;

public class Player {
	private float SIZE = 1.0f;
	private VertexArray mesh;
	private Texture texture;
	
	private Vector3f position = new Vector3f();
	private float rot;
	private float delta = 0.0f;
	
	public Player() {
		float[] vertices = new float[] {
			-SIZE / 2.0f, -SIZE / 2.0f, 0.2f,
			-SIZE / 2.0f,  SIZE / 2.0f, 0.2f,
			 SIZE / 2.0f,  SIZE / 2.0f, 0.2f,
			 SIZE / 2.0f, -SIZE / 2.0f, 0.2f
		};
			
		byte[] indices = new byte[] {
			0, 1, 2,
			2, 3, 0
		};
		
		float[] tcs = new float[] {
			0, 1,
			0, 0,
			1, 0,
			1, 1
		};
		
		mesh = new VertexArray(vertices, indices, tcs);
		texture = new Texture("res/bird.png");
	}
	
	public void update() {
		position.y -= delta;
		if (Input.isKeyDown(GLFW_KEY_SPACE)) 
			delta = -0.15f;
		else
			delta += 0.01f;
		
		rot = -delta * 90.0f;
	}
	
	public void fall() {
		delta = -0.15f;
	}
	
	public void render() {
		Shader.PLAYER.enable();
		Shader.PLAYER.setUniformMat4f("ml_matrix", Matrix4f.translate(position).multiply(Matrix4f.rotate(rot)));
		texture.bind();
		mesh.render();
		Shader.PLAYER.disable();
	}

	public float getY() {
		return position.y;
	}

	public float getSize() {
		return SIZE;
	}
}

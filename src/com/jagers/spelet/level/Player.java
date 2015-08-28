package com.jagers.spelet.level;

import static org.lwjgl.glfw.GLFW.*;

import com.jagers.spelet.graphics.Shader;
import com.jagers.spelet.graphics.Texture;
import com.jagers.spelet.graphics.VertexArray;
import com.jagers.spelet.math.Matrix4f;
import com.jagers.spelet.math.Vector3f;
import com.jagers.spelet.models.MPPlayer;
import com.jagers.spelet.input.Input;

public class Player {
	private float SIZE = 1.0f;
	private VertexArray mesh;
	private Texture texture;
	private MPPlayer self;
	
	private Vector3f position = new Vector3f();
	private float rot;
	private float delta = 0.0f;
	private float alpha = 0.0f;
	
	public Player(MPPlayer inSelf, MPPlayer inOpponent) {
		this.self = inSelf;
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
		position.x -= alpha;
		delta = 0.0f;
		alpha = 0.0f;
		if(Input.isKeyDown(GLFW_KEY_W) || Input.isKeyDown(GLFW_KEY_UP))
		{
			delta = -0.25f;
		}
		if(Input.isKeyDown(GLFW_KEY_S) || Input.isKeyDown(GLFW_KEY_DOWN))
		{
			if(position.y > -3.65f)
			{
				delta = +0.25f;
			}
		}
		if(Input.isKeyDown(GLFW_KEY_D) || Input.isKeyDown(GLFW_KEY_RIGHT))
		{
			if(position.x < 9.7f)
			{
				alpha = -0.10f;
			}
		}
		if(Input.isKeyDown(GLFW_KEY_A) || Input.isKeyDown(GLFW_KEY_LEFT))
		{
			if(position.x > -9.5f)
			{
				alpha = +0.10f;
			}
		}
		if(Input.isKeyDown(GLFW_KEY_ESCAPE))
		{
			
		}
		if (Input.isKeyDown(GLFW_KEY_SPACE)) 
		{
			delta = -0.15f;
		}
		if(position.y > -3.65f)
		{
			delta += 0.1f;
		}
		self.changeX(position.x);
		self.changeY(position.y);
		//System.out.println("Alpha: " + position.x);
		//System.out.println("Delta: " + position.y);
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

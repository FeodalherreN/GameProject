package com.jagers.spelet.level;
import com.jagers.spelet.graphics.Shader;
import com.jagers.spelet.graphics.Texture;
import com.jagers.spelet.graphics.VertexArray;
import com.jagers.spelet.math.Matrix4f;
import com.jagers.spelet.math.Vector3f;
import com.jagers.spelet.models.MPPlayer;

public class Opponent {
	private float SIZE = 1.0f;
	private VertexArray mesh;
	private Texture texture;
	private MPPlayer self;
	
	private Vector3f position = new Vector3f();
	private float rot;
	private float delta = 0.0f;
	private float alpha = 0.0f;
	
	public Opponent(MPPlayer inSelf) {
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
		texture = new Texture("res/opponent.png");
	}
	
	public void update() {
		position.x = self.getX();
		position.y = self.getY();
		//System.out.println(position.x + " " + position.y);
		rot = -delta * 90.0f;
	}
	
	public void fall() {
		delta = -0.15f;
	}
	
	public void render() {
		Shader.OPPONENT.enable();
		Shader.OPPONENT.setUniformMat4f("ml_matrix", Matrix4f.translate(position).multiply(Matrix4f.rotate(rot)));
		texture.bind();
		mesh.render();
		Shader.OPPONENT.disable();
	}

	public float getY() {
		return position.y;
	}

	public float getSize() {
		return SIZE;
	}
}

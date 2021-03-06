package com.jagers.spelet.level;

import com.jagers.spelet.graphics.Shader;
import com.jagers.spelet.graphics.Texture;
import com.jagers.spelet.graphics.VertexArray;
import com.jagers.spelet.input.Input;
import com.jagers.spelet.math.Matrix4f;
import com.jagers.spelet.math.Vector3f;
import com.jagers.spelet.models.MPPlayer;

import static org.lwjgl.glfw.GLFW.*;

public class Level {
	private VertexArray background, fade;
	private Texture bgTexture;
	
	private int xScroll = 0;
	private int map = 0;
	
	private Player player;
	private Opponent opponent;
	
	private boolean control = true, reset = false;
	
	
	private float time = 0.0f;
	
	public Level(MPPlayer inSelf, MPPlayer inOpponent) {
		float[] vertices = new float[] {
			-10.0f, -10.0f * 9.0f / 16.0f, 0.0f,
			-10.0f,  10.0f * 9.0f / 16.0f, 0.0f,
			  0.0f,  10.0f * 9.0f / 16.0f, 0.0f,
			  0.0f, -10.0f * 9.0f / 16.0f, 0.0f
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
		
		fade = new VertexArray(6);
		background = new VertexArray(vertices, indices, tcs);
		bgTexture = new Texture("res/bg.jpeg");
		player = new Player(inSelf, inOpponent);
		opponent = new Opponent(inOpponent);
		
	}
	
	
	public void update() {
		
		player.update();
		opponent.update();
		
		/*if (control && collision()) {
			player.fall();
			control = false;
		}*/
		
		if (!control && Input.isKeyDown(GLFW_KEY_SPACE))	
			reset = true;
		
		time += 0.01f;
	}
	
	
	private boolean collision() {
		for (int i = 0; i < 5 * 2; i++) {
			float bx = -xScroll * 0.05f;
			float by = player.getY();
			
			float bx0 = bx - player.getSize() / 2.0f;
			float bx1 = bx + player.getSize() / 2.0f;
			float by0 = by - player.getSize() / 2.0f;
			float by1 = by + player.getSize() / 2.0f;
		}
		return false;
	}
	
	public boolean isGameOver() {
		return reset;
	}
	
	public void render() {
		bgTexture.bind();
		Shader.BG.enable();
		Shader.BG.setUniform2f("player", 0, player.getY());
		Shader.BG.setUniform2f("opponent", 0, opponent.getY());
		background.bind();
		for (int i = map; i < map + 4; i++) {
			Shader.BG.setUniformMat4f("vw_matrix", Matrix4f.translate(new Vector3f(i * 10 + xScroll * 0.03f, 0.0f, 0.0f)));
			background.draw();
		}
		Shader.BG.disable();
		bgTexture.unbind();
		
 		player.render();
 		opponent.render();
		
		Shader.FADE.enable();
		Shader.FADE.setUniform1f("time", time);
		fade.render();
		Shader.FADE.disable();
	}
}

package com.jagers.spelet;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.nio.ByteBuffer;

import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.GLContext;

import com.jagers.spelet.graphics.Shader;
import com.jagers.spelet.input.Input;
import com.jagers.spelet.level.Level;
import com.jagers.spelet.level.Player;
import com.jagers.spelet.math.Matrix4f;
import com.jagers.spelet.models.MPPlayer;
import com.jagers.spelet.network.Client;
import com.jagers.spelet.network.Server;

public class StartGame implements Runnable {
	private int width = 1280;
	private int height = 720;
	
	private Thread gameThread;
	private Thread conThread;
	private Client client;
	private Server server;
	private MPPlayer self;
	private MPPlayer oponent;
	private boolean running = false;
	
	private long window;
	
	private Level level;
	public void hostGame() {
		running = true;
		gameThread = new Thread(this, "Game");
		gameThread.start();
	}
	
	
	public void start() {
		running = true;
		gameThread = new Thread(this, "Game");
		self = new MPPlayer();
		oponent = new MPPlayer();
		gameThread.start();
		Server server = new Server(self, oponent); 
		Thread myThread = new Thread(server);
		myThread.setDaemon(true); // important, otherwise JVM does not exit at end of main()
		myThread.start();
	}
	
	private void init() {
		if (glfwInit() != GL_TRUE) {
			System.err.println("Could not initialize GLFW!");
			return;
		}
		
		glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
		window = glfwCreateWindow(width, height, "Game", NULL, NULL);
		if (window == NULL) {
			System.err.println("Could not create GLFW window!");
			return;
		}
		
		ByteBuffer vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(window, (GLFWvidmode.width(vidmode) - width) / 2, (GLFWvidmode.height(vidmode) - height) / 2);
		
		Input.init(window);
		glfwSetKeyCallback(window, Input.getKeyCallback());
		
		glfwMakeContextCurrent(window);
		glfwShowWindow(window);
		GLContext.createFromCurrent();
		
 		glEnable(GL_DEPTH_TEST);
		glActiveTexture(GL_TEXTURE1);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		System.out.println("OpenGL: " + glGetString(GL_VERSION));
		Shader.loadAll();
		
		Matrix4f pr_matrix = Matrix4f.orthographic(-10.0f, 10.0f, -10.0f * 9.0f / 16.0f, 10.0f * 9.0f / 16.0f, -1.0f, 1.0f);
		Shader.BG.setUniformMat4f("pr_matrix", pr_matrix);
		Shader.BG.setUniform1i("tex", 1);
		
		Shader.PLAYER.setUniformMat4f("pr_matrix", pr_matrix);
		Shader.PLAYER.setUniform1i("tex", 1);
		
		level = new Level();
	}
	
	public void run() {
		init();
		
		long lastTime = System.nanoTime();
		double delta = 0.0;
		double ns = 1000000000.0 / 60.0;
		long timer = System.currentTimeMillis();
		int updates = 0;
		int frames = 0;
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if (delta >= 1.0) {
				update();
				updates++;
				delta--;
			}
			render();
			frames++;
			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				System.out.println(updates + " ups, " + frames + " fps");
				updates = 0;
				frames = 0;
			}
			if (glfwWindowShouldClose(window) == GL_TRUE)
				running = false;
		}
		
		glfwDestroyWindow(window);
		glfwTerminate();
	}
	
	private void update() {
		glfwPollEvents();
		level.update();
		if (level.isGameOver()) {
			level = new Level();
		}
	}
	
	private void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		level.render();
		
		int error = glGetError();
		if (error != GL_NO_ERROR)
			System.out.println(error);
		
		glfwSwapBuffers(window);
	}
}

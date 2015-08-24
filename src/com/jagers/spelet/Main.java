package com.jagers.spelet;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.nio.ByteBuffer;

import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.GLContext;

import com.jagers.spelet.graphics.Shader;
import com.jagers.spelet.input.input;
import com.jagers.spelet.level.Level;
import com.jagers.spelet.math.Matrix4f;

public class Main implements Runnable {
	private int width = 1280;
	private int height = 720;
	
	private Thread thread;
	private boolean running = false;
	private long window;
	private Level level;
	
	public void start(){
		running = false;
		thread = new Thread(this, "Spelet");
		thread.start();
	}
	
	private void init()
	{
		if(glfwInit() != GL_TRUE)
		{
			//TODO: Handle it
		}
		glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
		window = glfwCreateWindow(width, height, "Spelet", NULL, NULL);
		if(window == NULL)
		{
			//TODO: handle
			return;
		}
		ByteBuffer vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(window, (GLFWvidmode.width(vidmode) - width) / 2, (GLFWvidmode.height(vidmode) - height) / 2);
		
		glfwSetKeyCallback(window, new input());
		
		glfwMakeContextCurrent(window);
		glfwShowWindow(window);
		GLContext.createFromCurrent();
		
		glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		glEnable(GL_DEPTH_TEST);
		System.out.println("OpenGL: " + glGetString(GL_VERSION));
		Shader.loadAll();
		
		Matrix4f pr_matrix = Matrix4f.orthographic(-10.0f, 10.0f, -10.0f * 9.0f / 16.0f, 10.0f * -9.0f / 16.0f, -1.0f, 1.0f);
		Shader.BG.setUniformMat4f("pr_matrix", pr_matrix);
		
		level = new Level();
		
	}
	public void run(){
		init();
		while(running)
		{
			update();
			render();
			
			if(glfwWindowShouldClose(window) == GL_TRUE){ 
				running = false;
			}
		}
	}
	private void update(){
		glfwPollEvents();
	}
	private void render(){
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		level.render();
		int error = glGetError();
		if(error != GL_NO_ERROR)
		{
			System.out.println(error);
		}
		glfwSwapBuffers(window);
	}
	
	public static void main(String[] args) {
		new Main().start();

	}

}

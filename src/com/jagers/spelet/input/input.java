package com.jagers.spelet.input;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;

public class Input extends GLFWKeyCallback{

	 public static boolean[] keys = new boolean [65536];
	    private static  GLFWErrorCallback errorCallback;
	    private static GLFWKeyCallback   keyCallback;
	    
	    public static boolean isKeyDown(int keycode){
	        return keys[keycode];
	    }

	    public static void init(long window) {
	        keyCallback = new GLFWKeyCallback(){
	            @Override
	            public void invoke(long window, int key, int scancode, int action, int mods) {
	                keys[key] = action != GLFW.GLFW_RELEASE;
	            }
	        };
	    }
	    
	    public static GLFWKeyCallback getKeyCallback(){
	        return keyCallback;
	    }
	    
	    private Input(){}

		@Override
		public void invoke(long window, int key, int scancode, int action, int mods) {
			
		}

}

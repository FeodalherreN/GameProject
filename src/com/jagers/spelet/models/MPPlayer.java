package com.jagers.spelet.models;

public class MPPlayer {
	
	private float ivX = 1;
	private float ivY = 1;
	private final Object lock = new Object();	
	
    public void changeX(float x) {
        synchronized (lock) {
        	ivX = x;
        }
    }

    public float getX() {
        synchronized (lock) {
            float temp = ivX;
            return temp;
        }
    }
    public void changeY(float y) {
        synchronized (lock) {
        	ivY = y;
        }
    }

    public float getY() {
        synchronized (lock) {
            float temp = ivY;
            return temp;
        }
    }
}

package entities;

import org.lwjgl.glfw.GLFW;

import input.Keyinput;
import maths.Vector3f;

public class Camera {
	Vector3f postion =new Vector3f(0,0,0);
	float pitch;
	float yaw;
	float roll;
	
	
	public void move(){
		if(Keyinput.keys[GLFW.GLFW_KEY_W])
			postion.z-=0.02f;
		if(Keyinput.keys[GLFW.GLFW_KEY_D])
			postion.x+=0.02f;
		if(Keyinput.keys[GLFW.GLFW_KEY_A])
			postion.x-=0.02f;
		if(Keyinput.keys[GLFW.GLFW_KEY_S])
			postion.z+=0.02f;
	}
	
	public Vector3f getPostion() {
		return postion;
	}
	public float getPitch() {
		return pitch;
	}
	public float getYaw() {
		return yaw;
	}
	public float getRoll() {
		return roll;
	}
	
	
}

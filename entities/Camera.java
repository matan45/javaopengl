package entities;

import org.lwjgl.glfw.GLFW;

import input.Keyinput;
import input.MouseScroll;
import input.MouseScroll.Scroll;
import maths.Vector3f;

public class Camera {
	Vector3f postion =new Vector3f(0,0,0);
	float pitch;
	float yaw;
	float roll;
	
	
	public void move(){
		if(Keyinput.keyDown(GLFW.GLFW_KEY_W))
			postion.z-=0.1f;
		if(Keyinput.keyDown(GLFW.GLFW_KEY_D))
			postion.x+=0.1f;
		if(Keyinput.keyDown(GLFW.GLFW_KEY_A))
			postion.x-=0.1f;
		if(Keyinput.keyDown(GLFW.GLFW_KEY_S))
			postion.z+=0.1f;
		
		MouseScroll.setS(new Scroll() {

			@Override
			public void ScrollUp() {
				postion.y+=0.5f;

			}

			@Override
			public void ScrollDown() {
				postion.y-=0.5f;

			}
		});
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

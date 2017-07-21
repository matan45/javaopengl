package entities;

import org.lwjgl.glfw.GLFW;

import input.Keyinput;
import input.MouseScroll;
import input.MouseScroll.Scroll;
import maths.Vector3f;

public class Camera {
	Vector3f postion = new Vector3f(5, 8, -3);
	float pitch = 14.0f;
	float yaw = 36.0f;
	float roll;
	float speed = 0.1f;

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

	public void move() {

		if (Keyinput.keyDown(GLFW.GLFW_KEY_X))
			roll--;
		if (Keyinput.keyDown(GLFW.GLFW_KEY_Z))
			roll++;
		if (Keyinput.keyDown(GLFW.GLFW_KEY_C))
			roll = 0;

		if (Keyinput.keyDown(GLFW.GLFW_KEY_LEFT_CONTROL))
			postion.y -= (float) (Math.cos(-yaw * Math.PI / 180) * speed);

		if (Keyinput.keyDown(GLFW.GLFW_KEY_SPACE))
			postion.y += (float) (Math.cos(-yaw * Math.PI / 180) * speed);

		if (Keyinput.keyDown(GLFW.GLFW_KEY_A))
			postion.x += (float) (Math.sin((-yaw - 90) * Math.PI / 180) * speed);

		if (Keyinput.keyDown(GLFW.GLFW_KEY_D))
			postion.x += (float) (Math.sin((-yaw + 90) * Math.PI / 180) * speed);

		if (Keyinput.keyDown(GLFW.GLFW_KEY_S))
			postion.z += (float) (Math.cos(-yaw * Math.PI / 180) * speed);

		if (Keyinput.keyDown(GLFW.GLFW_KEY_W))
			postion.z -= (float) (Math.cos(-yaw * Math.PI / 180) * speed);

		if (Keyinput.keyDown(GLFW.GLFW_KEY_Q))
			yaw--;

		if (Keyinput.keyDown(GLFW.GLFW_KEY_E))
			yaw++;

		MouseScroll.setS(new Scroll() {

			@Override
			public void ScrollUp(double speed) {
				if (pitch < 70 && speed >= 0)
					pitch += speed;

			}

			@Override
			public void ScrollDown(double speed) {
				if (pitch > -70 && speed < 0)
					pitch += speed;

			}
		});

	}

}

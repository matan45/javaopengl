package entities;

import org.lwjgl.glfw.GLFW;

import input.Keyinput;
import input.MouseCursor;
import input.MouseScroll;
import input.MouseScroll.Scroll;
import input.Mouseinput;
import maths.Vector3f;

public class Camera {
	float distanceFromPlayer = 15;
	float angleAroundPlayer = 0;

	Vector3f position = new Vector3f(0, 0, 0);
	float pitch = 20;
	float yaw = 0;
	float roll;
	float speed = 0.1f;

	private Player player;

	public Camera(Player player) {
		this.player = player;
	}

	public Vector3f getPosition() {
		return position;
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

	public void FirstPerson() {

		if (Keyinput.keyDown(GLFW.GLFW_KEY_R)) {
			position = new Vector3f(5, 8, -3);
			pitch = 14.0f;
			yaw = 36.0f;
			roll = 0;
		}

		if (Keyinput.keyDown(GLFW.GLFW_KEY_X))
			roll--;
		if (Keyinput.keyDown(GLFW.GLFW_KEY_Z))
			roll++;
		if (Keyinput.keyDown(GLFW.GLFW_KEY_C))
			roll = 0;

		if (Keyinput.keyDown(GLFW.GLFW_KEY_LEFT_CONTROL))
			position.y -= (float) (Math.cos(-yaw * Math.PI / 180) * speed);

		if (Keyinput.keyDown(GLFW.GLFW_KEY_SPACE))
			position.y += (float) (Math.cos(-yaw * Math.PI / 180) * speed);

		if (Keyinput.keyDown(GLFW.GLFW_KEY_A))
			position.x += (float) (Math.sin((-yaw - 90) * Math.PI / 180) * speed);

		if (Keyinput.keyDown(GLFW.GLFW_KEY_D))
			position.x += (float) (Math.sin((-yaw + 90) * Math.PI / 180) * speed);

		if (Keyinput.keyDown(GLFW.GLFW_KEY_S))
			position.z += (float) (Math.cos(-yaw * Math.PI / 180) * speed);

		if (Keyinput.keyDown(GLFW.GLFW_KEY_W))
			position.z -= (float) (Math.cos(-yaw * Math.PI / 180) * speed);

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

	private void calculateZoom() {
		MouseScroll.setS(new Scroll() {

			@Override
			public void ScrollUp(double speed) {
				distanceFromPlayer -= speed;
				if (distanceFromPlayer < 1)
					distanceFromPlayer = 1;

			}

			@Override
			public void ScrollDown(double speed) {
				distanceFromPlayer -= speed;
				if (distanceFromPlayer > 22)
					distanceFromPlayer = 22;

			}
		});
	}

	public void Person3D() {
		calculateZoom();
		calculatePitch();
		calculateAngleAoundPlayer();
		float HorizontalDistance = calculateHorizontalDistance();
		float VerticalDistance = calculateVerticalDistance();
		calculateCameraPosition(HorizontalDistance, VerticalDistance);
		this.yaw = 180 - (player.getRotY() + angleAroundPlayer);
	}

	private void calculatePitch() {
		if (Mouseinput.mouseButtonDown(GLFW.GLFW_MOUSE_BUTTON_2)) {
			float pitchChange = (float) (MouseCursor.DY() * 0.1f);
			pitch -= pitchChange;
			if (pitch < 1)
				pitch = 1;
			else if (pitch > 90)
				pitch = 90;
		}
	}

	private void calculateAngleAoundPlayer() {
		if (Mouseinput.mouseButtonDown(GLFW.GLFW_MOUSE_BUTTON_2)) {
			float angleChange = (float) (MouseCursor.DX() * 0.1f);
			angleAroundPlayer -= angleChange;
		}
	}

	private float calculateHorizontalDistance() {
		float hD = (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
		if (hD < 0)
			hD = 0;
		return hD;
	}

	private float calculateVerticalDistance() {
		float vD = (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
		if (vD < 0)
			vD = 0;
		return vD;

	}

	private void calculateCameraPosition(float HorizontalDistance, float VerticalDistance) {
		float theta = player.getRotY() + angleAroundPlayer;
		float offsetX = (float) (HorizontalDistance * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float) (HorizontalDistance * Math.cos(Math.toRadians(theta)));
		position.x = player.getPosition().x - offsetX;
		position.z = player.getPosition().z - offsetZ;
		position.y = player.getPosition().y + VerticalDistance;
	}
}

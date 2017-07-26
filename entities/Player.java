package entities;

import org.lwjgl.glfw.GLFW;

import input.Keyinput;
import maths.Vector3f;
import terrains.Terrain;
import texture.TexturedModel;
import utill.Time;

public class Player extends Entity {
	static final float RUN_SPEED = 15;
	static final float TURN_SPEED = 160;
	static final float GRAVITY = -50;
	static final float JUMP_POWER = 20;

	float currentSpeed = 0;
	float currentTurnSpeed = 0;
	float upwardsSpeed = 0;
	boolean isInAir = false;

	public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
	}

	public void move(Terrain terrain) {
		checkInputs();
		super.increaseRotation(0, currentTurnSpeed * Time.getDeltaTime(), 0);
		float distance = currentSpeed * Time.getDeltaTime();
		float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
		float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
		super.increasePosition(dx, 0, dz);
		upwardsSpeed += GRAVITY * Time.getDeltaTime();
		super.increasePosition(0, upwardsSpeed * Time.getDeltaTime(), 0);
		float terrainHeight = terrain.getHeightOfTerrain(super.getPosition().x, super.getPosition().z);
		if (super.getPosition().y < terrainHeight) {
			upwardsSpeed = 0;
			isInAir = false;
			super.getPosition().y = terrainHeight;
		}
	}

	private void jump() {
		if (!isInAir) {
			this.upwardsSpeed = JUMP_POWER;
			isInAir = true;
		}
	}

	private void checkInputs() {
		if (Keyinput.keyDown(GLFW.GLFW_KEY_KP_8)) {
			this.currentSpeed = RUN_SPEED;
		} else if (Keyinput.keyDown(GLFW.GLFW_KEY_KP_2)) {
			this.currentSpeed = -RUN_SPEED;
		} else {
			this.currentSpeed = 0;
		}
		if (Keyinput.keyDown(GLFW.GLFW_KEY_KP_6)) {
			this.currentTurnSpeed = -TURN_SPEED;
		} else if (Keyinput.keyDown(GLFW.GLFW_KEY_KP_4)) {
			this.currentTurnSpeed = TURN_SPEED;
		} else {
			this.currentTurnSpeed = 0;
		}
		if (Keyinput.keyDown(GLFW.GLFW_KEY_KP_5)) {
			jump();
		}
	}

}
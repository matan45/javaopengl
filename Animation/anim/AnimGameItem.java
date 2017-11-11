package Animation.anim;

import java.util.Map;
import java.util.Optional;

import Animation.graph.Mesh;

public class AnimGameItem {
	private Map<String, Animation> animations;

	private Animation currentAnimation;
	Mesh[] meshes;

	public AnimGameItem(Mesh[] meshes, Map<String, Animation> animations) {
		this.meshes = meshes;
		this.animations = animations;
		Optional<Map.Entry<String, Animation>> entry = animations.entrySet().stream().findFirst();
		currentAnimation = entry.isPresent() ? entry.get().getValue() : null;
	}

	public Animation getAnimation(String name) {
		return animations.get(name);
	}

	public Animation getCurrentAnimation() {
		return currentAnimation;
	}

	public void setCurrentAnimation(Animation currentAnimation) {
		this.currentAnimation = currentAnimation;
	}

	public Mesh[] getMeshes() {
		return meshes;
	}
	
}


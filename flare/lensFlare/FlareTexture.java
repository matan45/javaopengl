package flare.lensFlare;



import flare.textures.Texture;
import maths.Vector2f;

public class FlareTexture {
	
	private final Texture texture;
	private final float scale;
	
	private Vector2f screenPos = new Vector2f();

	public FlareTexture(Texture texture, float scale){
		this.texture = texture;
		this.scale = scale;
	}
	
	public void setScreenPos(Vector2f newPos){
		this.screenPos.set(newPos);
	}
	
	public Texture getTexture() {
		return texture;
	}

	public float getScale() {
		return scale;
	}

	public Vector2f getScreenPos() {
		return screenPos;
	}
	
}

package flare.lensFlare;



import entities.Camera;
import maths.Maths;
import maths.Matrix4f;
import maths.Vector2f;
import maths.Vector3f;
import maths.Vector4f;

public class FlareManager {

	private static final Vector2f CENTER_SCREEN = new Vector2f(0.5f, 0.5f);

	private final FlareTexture[] flareTextures;
	private final float spacing;

	private FlareRenderer renderer;

	public FlareManager(float spacing, FlareTexture... textures) {
		this.spacing = spacing;
		this.flareTextures = textures;
		this.renderer = new FlareRenderer();
	}

	public void render(Camera camera, Matrix4f projectionMat, Vector3f sunWorldPos) {
		Vector2f sunCoords = convertToScreenSpace(sunWorldPos, camera, projectionMat);
		if(sunCoords == null){
			return;
		}
		Vector2f sunToCenter = Vector2f.sub(CENTER_SCREEN, sunCoords, null);
		float brightness = 1 - (sunToCenter.length() / 0.7f);
		if(brightness > 0){
			calcFlarePositions(sunToCenter, sunCoords);
			renderer.render(sunCoords,flareTextures, brightness);
		}
	}
	
	private void calcFlarePositions(Vector2f sunToCenter, Vector2f sunCoords){
		for(int i=0;i<flareTextures.length;i++){
			Vector2f direction = new Vector2f(sunToCenter);
			direction.scale(i * spacing);
			Vector2f flarePos = Vector2f.add(sunCoords, direction, null);
			flareTextures[i].setScreenPos(flarePos);
		}
	}

	private Vector2f convertToScreenSpace(Vector3f worldPos, Camera camera, Matrix4f projectionMat) {
		Vector4f coords = new Vector4f(worldPos.x, worldPos.y, worldPos.z, 1f);
		Matrix4f viewMat=new Matrix4f();
		viewMat=Maths.createViewMatrix(camera);
		Matrix4f.transform(viewMat, coords, coords);
		Matrix4f.transform(projectionMat, coords, coords);
		if (coords.w <= 0) {
			return null;
		}
		float x = (coords.x / coords.w + 1) / 2f;
		float y = 1 - ((coords.y / coords.w + 1) / 2f);
		return new Vector2f(x, y);
	}

	public void cleanUp() {
		renderer.cleanUp();
	}

}

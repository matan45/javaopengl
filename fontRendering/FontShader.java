package fontRendering;

import maths.Vector2f;
import maths.Vector3f;
import shader.ShaderProgram;

public class FontShader extends ShaderProgram {

	private int location_colour;
	private int location_translation;
	private int location_outlineColour;

	public FontShader() {
		super("font.vs", "font.frag");
	}

	@Override
	protected void getAllUniformLocations() {
		location_colour = super.getUniformLocation("colour");
		location_translation = super.getUniformLocation("translation");
		location_outlineColour = super.getUniformLocation("outlineColour");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
	}

	protected void loadColour(Vector3f colour) {
		super.loadVector(location_colour, colour);
	}

	protected void loadOutlineColour(Vector3f outlineColour) {
		super.loadVector(location_outlineColour, outlineColour);
	}

	protected void loadTranslation(Vector2f translation) {
		super.load2DVector(location_translation, translation);
	}

}

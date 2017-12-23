package bloom;

import shader.ShaderProgram;

public class BrightFilterShader extends ShaderProgram {
	private int location_shades;
	private int location_scale;
	
	public BrightFilterShader(String vs, String frag) {
		super(vs, frag);
	}

	@Override
	protected void getAllUniformLocations() {
		location_shades = super.getUniformLocation("SHADES");
		location_scale = super.getUniformLocation("scale");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}
	
	public void loadshades(int shades) {
		super.loadInt(location_shades, shades);
	}
	protected void scale(float scale){
		super.loadFloat(location_scale, scale);
	}

}

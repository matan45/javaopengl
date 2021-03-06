package particles;

public class ParticleTexture {
	int textureID;
	int numberOfRows;
	boolean additive;

	public ParticleTexture(int textureID, int numberOfRows, boolean additive) {
		this.textureID = textureID;
		this.numberOfRows = numberOfRows;
		this.additive = additive;
	}

	public int getTextureID() {
		return textureID;
	}

	public int getNumberOfRows() {
		return numberOfRows;
	}

	public boolean isAdditive() {
		return additive;
	}

}

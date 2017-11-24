package PBR;

import maths.Vector3f;

public class Material {
	Vector3f albedo;
	float metallic;
	float ao;
	float roughness;

	int albedoMap;
	int metallicMap;
	int roughnessMap;
	int normalMap;
	int aoMap;

	boolean hasalbedoMap;
	boolean hasmetallicMap;
	boolean hasroughnessMap;
	boolean hasaoMap;
	boolean hasnormalMap;

	public Vector3f getAlbedo() {
		return albedo;
	}

	public void setAlbedo(Vector3f albedo) {
		this.albedo = albedo;
	}

	public float getMetallic() {
		return metallic;
	}

	public void setMetallic(float metallic) {
		this.metallic = metallic;
	}

	public float getAo() {
		return ao;
	}

	public void setAo(float ao) {
		this.ao = ao;
	}

	public float getRoughness() {
		return roughness;
	}

	public void setRoughness(float roughness) {
		this.roughness = roughness;
	}

	public int getAlbedoMap() {
		return albedoMap;
	}

	public void setAlbedoMap(int albedoMap) {
		this.albedoMap = albedoMap;
		hasalbedoMap=true;
	}

	public int getMetallicMap() {
		return metallicMap;
	}

	public void setMetallicMap(int metallicMap) {
		this.metallicMap = metallicMap;
		hasmetallicMap=true;
	}

	public int getRoughnessMap() {
		return roughnessMap;
	}

	public void setRoughnessMap(int roughnessMap) {
		this.roughnessMap = roughnessMap;
		hasroughnessMap=true;
	}

	public int getNormalMap() {
		return normalMap;
	}

	public void setNormalMap(int normalMap) {
		this.normalMap = normalMap;
		hasnormalMap=true;
	}

	public int getAoMap() {
		return aoMap;
	}

	public void setAoMap(int aoMap) {
		this.aoMap = aoMap;
		hasaoMap=true;
	}

	public boolean isHasalbedoMap() {
		return hasalbedoMap;
	}

	public boolean isHasmetallicMap() {
		return hasmetallicMap;
	}

	public boolean isHasroughnessMap() {
		return hasroughnessMap;
	}

	public boolean isHasaoMap() {
		return hasaoMap;
	}

	public boolean isHasnormalMap() {
		return hasnormalMap;
	}

}

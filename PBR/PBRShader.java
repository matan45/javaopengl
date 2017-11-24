package PBR;

import java.util.List;

import entities.Light;
import maths.Matrix4f;
import maths.Vector3f;
import shader.ShaderProgram;

public class PBRShader extends ShaderProgram {

	private static final int MAX_LIGHTS = 4;

	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;

	private int location_lightposition[];
	private int location_lightColour[];
	private int location_attenuation[];
	
	private int location_camera;

	private int location_albedoMap;
	private int location_metallicMap;
	private int location_roughnessMap;
	private int location_normalMap;
	private int location_aoMap;

	private int location_hasalbedoMap;
	private int location_hasMetallicMap;
	private int location_hasroughnessMap;
	private int location_hasnormalMap;
	private int location_hasaoMap;

	private int location_Salbedo;
	private int location_Smetallic;
	private int location_Sroughness;
	private int location_Sao;

	public PBRShader(String vertexFile, String fragmentFile) {
		super(vertexFile, fragmentFile);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		
		location_camera=super.getUniformLocation("camPos");

		location_albedoMap = super.getUniformLocation("AlbedoMap");
		location_metallicMap = super.getUniformLocation("metallicMap");
		location_roughnessMap = super.getUniformLocation("roughnessMap");
		location_normalMap = super.getUniformLocation("NormalMap");
		location_aoMap = super.getUniformLocation("aoMap");

		location_hasalbedoMap = super.getUniformLocation("UsingAlbedoMap");
		location_hasMetallicMap = super.getUniformLocation("UsingMetallicMap");
		location_hasroughnessMap = super.getUniformLocation("UsingRoughnessMap");
		location_hasnormalMap = super.getUniformLocation("UsingNormalMap");
		location_hasaoMap = super.getUniformLocation("UsingAoMap");

		location_Salbedo = super.getUniformLocation("AlbedoColor");
		location_Smetallic = super.getUniformLocation("metallicColor");
		location_Sroughness = super.getUniformLocation("roughnessColor");
		location_Sao = super.getUniformLocation("aoColor");

		location_lightposition = new int[MAX_LIGHTS];
		location_lightColour = new int[MAX_LIGHTS];
		location_attenuation = new int[MAX_LIGHTS];
		for (int i = 0; i < MAX_LIGHTS; i++) {
			location_lightposition[i] = super.getUniformLocation("lightPosition[" + i + "]");
			location_lightColour[i] = super.getUniformLocation("lightColour[" + i + "]");
			location_attenuation[i] = super.getUniformLocation("attenuation[" + i + "]");
		}
		
		
		
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoordinates");
		super.bindAttribute(2, "normal");

	}

	protected void connectTextureUnits() {
		super.loadInt(location_albedoMap, 0);
		super.loadInt(location_metallicMap, 1);
		super.loadInt(location_roughnessMap, 2);
		super.loadInt(location_normalMap, 3);
		super.loadInt(location_aoMap, 4);
	}

	protected void loadTransformationMatrix(Matrix4f matrix) {
		super.loadMatrix(location_transformationMatrix, matrix);
	}

	protected void loadLights(List<Light> lights) {
		for (int i = 0; i < MAX_LIGHTS; i++) {
			if (i < lights.size()) {
				super.loadVector(location_lightposition[i], lights.get(i).getPosition());
				super.loadVector(location_lightColour[i], lights.get(i).getColour());
				super.loadVector(location_attenuation[i], lights.get(i).getAttenuation());
			} else {
				super.loadVector(location_lightposition[i], new Vector3f(0, 0, 0));
				super.loadVector(location_lightColour[i], new Vector3f(0, 0, 0));
				super.loadVector(location_attenuation[i], new Vector3f(1, 0, 0));
			}
		}
	}
	
	protected void loadcamera(Vector3f camera) {
		super.loadVector(location_camera, camera);
	}

	protected void loadViewMatrix(Matrix4f viewMatrix) {
		super.loadMatrix(location_viewMatrix, viewMatrix);
	}

	protected void loadProjectionMatrix(Matrix4f projection) {
		super.loadMatrix(location_projectionMatrix, projection);
	}

	protected void loadhasalbedoMap(boolean useMap) {
		super.loadBoolean(location_hasalbedoMap, useMap);
	}

	protected void loadhasMetallicMap(boolean useMap) {
		super.loadBoolean(location_hasMetallicMap, useMap);
	}

	protected void loadhasroughnessMap(boolean useMap) {
		super.loadBoolean(location_hasroughnessMap, useMap);
	}

	protected void loadhasnormalMap(boolean useMap) {
		super.loadBoolean(location_hasnormalMap, useMap);
	}

	protected void loadhasaoMap(boolean useMap) {
		super.loadBoolean(location_hasaoMap, useMap);
	}

	protected void loadroughness(float roughness) {
		super.loadFloat(location_Sroughness, roughness);
	}

	protected void loadalbedo(Vector3f albedo) {
		super.loadVector(location_Salbedo, albedo);
	}

	protected void loadao(float ao) {
		super.loadFloat(location_Sao, ao);
	}

	protected void loadmetallic(float metallic) {
		super.loadFloat(location_Smetallic, metallic);
	}

}

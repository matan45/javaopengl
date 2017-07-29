package renderer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import maths.Vector2f;
import maths.Vector3f;

public class OBJLoader {
	static Vector3f OBJLength = new Vector3f();

	public static RawModel loadObjModel(String fileName, Loader loader) {
		FileReader fr = null;

		try {
			fr = new FileReader(new File("src/resources/3D model/" + fileName + ".obj"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		BufferedReader reader = new BufferedReader(fr);
		String line;
		List<Vector3f> vertices = new ArrayList<Vector3f>();
		List<Vector2f> textures = new ArrayList<Vector2f>();
		List<Vector3f> normals = new ArrayList<Vector3f>();
		List<Integer> indices = new ArrayList<Integer>();
		float[] verticesArray = null;
		float[] normalsArray = null;
		float[] textureArray = null;
		int[] indicesArray = null;

		try {
			while (true) {
				line = reader.readLine();
				String[] currentLine = line.split(" ");
				if (line.startsWith("v ")) {
					Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]),
							Float.parseFloat(currentLine[3]));
					vertices.add(vertex);
				} else if (line.startsWith("vt ")) {
					Vector2f texture = new Vector2f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]));
					textures.add(texture);
				} else if (line.startsWith("vn ")) {
					Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]),
							Float.parseFloat(currentLine[3]));
					normals.add(normal);

				} else if (line.startsWith("f ")) {
					textureArray = new float[vertices.size() * 2];
					normalsArray = new float[vertices.size() * 3];
					break;
				}
			}
			float maxX = 0, minX = 0;
			float maxY = 0, minY = 0;
			float maxZ = 0, minZ = 0;
			for (Vector3f vertex : vertices) {
				minX = Math.min(minX, vertex.x);
				minY = Math.min(minY, vertex.y);
				minZ = Math.min(minZ, vertex.z);
				maxX = Math.max(maxX, vertex.x);
				maxY = Math.max(maxY, vertex.y);
				maxZ = Math.max(maxZ, vertex.z);
			}
			OBJLength.x = maxX - minX;
			OBJLength.y = maxY - minY;
			OBJLength.z = maxZ - minZ;
			while (line != null) {
				if (!line.startsWith("f ")) {
					line = reader.readLine();
					continue;
				}
				String[] currentLine = line.split(" ");
				String[] vertex1 = currentLine[1].split("/");
				String[] vertex2 = currentLine[2].split("/");
				String[] vertex3 = currentLine[3].split("/");

				processVertex(vertex1, indices, textures, normals, textureArray, normalsArray);
				processVertex(vertex2, indices, textures, normals, textureArray, normalsArray);
				processVertex(vertex3, indices, textures, normals, textureArray, normalsArray);
				line = reader.readLine();
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		verticesArray = new float[vertices.size() * 3];
		indicesArray = new int[indices.size()];

		int vertexPointer = 0;
		for (Vector3f vertex : vertices) {
			verticesArray[vertexPointer++] = vertex.x;
			verticesArray[vertexPointer++] = vertex.y;
			verticesArray[vertexPointer++] = vertex.z;
		}

		for (int i = 0; i < indices.size(); i++) {
			indicesArray[i] = indices.get(i);
		}

		return loader.loadToVAO(verticesArray, textureArray, normalsArray, indicesArray);

	}

	private static void processVertex(String[] vertexData, List<Integer> indices, List<Vector2f> textures,
			List<Vector3f> normals, float[] textureArray, float[] normalsArray) {
		int currentVertxPointer = Integer.parseInt(vertexData[0]) - 1;
		indices.add(currentVertxPointer);
		Vector2f currentTex = textures.get(Integer.parseInt(vertexData[1]) - 1);
		textureArray[currentVertxPointer * 2] = currentTex.x;
		textureArray[currentVertxPointer * 2 + 1] = 1 - currentTex.y;
		Vector3f currenNorm = normals.get(Integer.parseInt(vertexData[2]) - 1);
		normalsArray[currentVertxPointer * 3] = currenNorm.x;
		normalsArray[currentVertxPointer * 3 + 1] = currenNorm.y;
		normalsArray[currentVertxPointer * 3 + 2] = currenNorm.z;
	}

	public static Vector3f getOBJLength() {
		return OBJLength;
	}

}

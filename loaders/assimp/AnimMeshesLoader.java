package loaders.assimp;

import static org.lwjgl.assimp.Assimp.aiImportFile;
import static org.lwjgl.assimp.Assimp.aiProcess_FixInfacingNormals;
import static org.lwjgl.assimp.Assimp.aiProcess_GenSmoothNormals;
import static org.lwjgl.assimp.Assimp.aiProcess_JoinIdenticalVertices;
import static org.lwjgl.assimp.Assimp.aiProcess_LimitBoneWeights;
import static org.lwjgl.assimp.Assimp.aiProcess_Triangulate;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.AIAnimation;
import org.lwjgl.assimp.AIBone;
import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIMatrix4x4;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AINode;
import org.lwjgl.assimp.AINodeAnim;
import org.lwjgl.assimp.AIQuatKey;
import org.lwjgl.assimp.AIQuaternion;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIVector3D;
import org.lwjgl.assimp.AIVectorKey;
import org.lwjgl.assimp.AIVertexWeight;

import Animation.Texture.Texture;
import utill.Util;
import Animation.anim.AnimGameItem;
import Animation.anim.AnimatedFrame;
import Animation.anim.Animation;
import Animation.graph.Mesh;
import maths.Matrix4f;
import maths.Quaternion;
import maths.Vector3f;

public class AnimMeshesLoader {
	
	static Vector3f OBJLength = new Vector3f();
	
	private static void buildTransFormationMatrices(AINodeAnim aiNodeAnim, Node node) {
		int numFrames = aiNodeAnim.mNumPositionKeys();
		AIVectorKey.Buffer positionKeys = aiNodeAnim.mPositionKeys();
		AIVectorKey.Buffer scalingKeys = aiNodeAnim.mScalingKeys();
		AIQuatKey.Buffer rotationKeys = aiNodeAnim.mRotationKeys();

		for (int i = 0; i < numFrames; i++) {
			AIVectorKey aiVecKey = positionKeys.get(i);
			AIVector3D vec = aiVecKey.mValue();

			Matrix4f transfMat = new Matrix4f();
			Matrix4f.translate(new Vector3f(vec.x(), vec.y(), vec.z()), transfMat, transfMat);

			AIQuatKey quatKey = rotationKeys.get(i);
			AIQuaternion aiQuat = quatKey.mValue();
			Quaternion quat = new Quaternion(aiQuat.x(), aiQuat.y(), aiQuat.z(), aiQuat.w());
			transfMat.rotateGeneric(quat, transfMat);

			if (i < aiNodeAnim.mNumScalingKeys()) {
				aiVecKey = scalingKeys.get(i);
				vec = aiVecKey.mValue();
				Matrix4f.scale(new Vector3f(vec.x(), vec.y(), vec.z()), transfMat, transfMat);
			}

			node.addTransformation(transfMat);
		}
	}

	public static AnimGameItem loadAnimGameItem(String resourcePath, String fileName) {
		return loadAnimGameItem(resourcePath, fileName, aiProcess_GenSmoothNormals | aiProcess_JoinIdenticalVertices
				| aiProcess_Triangulate | aiProcess_FixInfacingNormals | aiProcess_LimitBoneWeights);
	}

	public static AnimGameItem loadAnimGameItem(String resourcePath, String fileName, int flags) {
		AIScene aiScene = aiImportFile(resourcePath, flags);
		if (aiScene == null) {
			System.err.println("Error loading model");
		}

		List<Bone> boneList = new ArrayList<>();
		int numMeshes = aiScene.mNumMeshes();
		PointerBuffer aiMeshes = aiScene.mMeshes();
		Mesh[] meshes = new Mesh[numMeshes];
		for (int i = 0; i < numMeshes; i++) {
			AIMesh aiMesh = AIMesh.create(aiMeshes.get(i));
			Mesh mesh = processMesh(aiMesh, boneList, fileName);
			meshes[i] = mesh;
		}

		AINode aiRootNode = aiScene.mRootNode();
		Matrix4f rootTransfromation = AnimMeshesLoader.toMatrix(aiRootNode.mTransformation());
		Node rootNode = processNodesHierarchy(aiRootNode, null);
		Map<String, Animation> animations = processAnimations(aiScene, boneList, rootNode, rootTransfromation);
		AnimGameItem item = new AnimGameItem(meshes, animations);

		return item;
	}

	private static List<AnimatedFrame> buildAnimationFrames(List<Bone> boneList, Node rootNode,
			Matrix4f rootTransformation) {

		int numFrames = rootNode.getAnimationFrames();
		List<AnimatedFrame> frameList = new ArrayList<>();
		for (int i = 0; i < numFrames; i++) {
			AnimatedFrame frame = new AnimatedFrame();
			frameList.add(frame);

			int numBones = boneList.size();
			for (int j = 0; j < numBones; j++) {
				Bone bone = boneList.get(j);
				Node node = rootNode.findByName(bone.getBoneName());
				Matrix4f boneMatrix = Node.getParentTransforms(node, i);
				Matrix4f.mul(boneMatrix, bone.getOffsetMatrix(), boneMatrix);
				Matrix4f.mul(rootTransformation, boneMatrix, boneMatrix);
				frame.setMatrix(j, boneMatrix);
			}
		}

		return frameList;
	}

	private static Map<String, Animation> processAnimations(AIScene aiScene, List<Bone> boneList, Node rootNode,
			Matrix4f rootTransformation) {
		Map<String, Animation> animations = new HashMap<>();

		// Process all animations
		int numAnimations = aiScene.mNumAnimations();
		PointerBuffer aiAnimations = aiScene.mAnimations();
		for (int i = 0; i < numAnimations; i++) {
			AIAnimation aiAnimation = AIAnimation.create(aiAnimations.get(i));

			// Calculate transformation matrices for each node
			int numChanels = aiAnimation.mNumChannels();
			PointerBuffer aiChannels = aiAnimation.mChannels();
			for (int j = 0; j < numChanels; j++) {
				AINodeAnim aiNodeAnim = AINodeAnim.create(aiChannels.get(j));
				String nodeName = aiNodeAnim.mNodeName().dataString();
				Node node = rootNode.findByName(nodeName);
				buildTransFormationMatrices(aiNodeAnim, node);
			}

			List<AnimatedFrame> frames = buildAnimationFrames(boneList, rootNode, rootTransformation);
			Animation animation = new Animation(aiAnimation.mName().dataString(), frames, aiAnimation.mDuration());
			animations.put(animation.getName(), animation);
		}
		return animations;
	}

	private static void processBones(AIMesh aiMesh, List<Bone> boneList, List<Integer> boneIds, List<Float> weights) {
		Map<Integer, List<VertexWeight>> weightSet = new HashMap<>();
		int numBones = aiMesh.mNumBones();
		PointerBuffer aiBones = aiMesh.mBones();
		for (int i = 0; i < numBones; i++) {
			AIBone aiBone = AIBone.create(aiBones.get(i));
			int id = boneList.size();
			Bone bone = new Bone(id, aiBone.mName().dataString(), toMatrix(aiBone.mOffsetMatrix()));

			boneList.add(bone);
			int numWeights = aiBone.mNumWeights();
			AIVertexWeight.Buffer aiWeights = aiBone.mWeights();
			for (int j = 0; j < numWeights; j++) {
				AIVertexWeight aiWeight = aiWeights.get(j);
				VertexWeight vw = new VertexWeight(bone.getBoneId(), aiWeight.mVertexId(), aiWeight.mWeight());
				List<VertexWeight> vertexWeightList = weightSet.get(vw.getVertexId());
				if (vertexWeightList == null) {
					vertexWeightList = new ArrayList<>();
					weightSet.put(vw.getVertexId(), vertexWeightList);
				}
				vertexWeightList.add(vw);
			}
		}

		int numVertices = aiMesh.mNumVertices();
		for (int i = 0; i < numVertices; i++) {
			List<VertexWeight> vertexWeightList = weightSet.get(i);
			int size = vertexWeightList != null ? vertexWeightList.size() : 0;
			for (int j = 0; j < Mesh.MAX_WEIGHTS; j++) {
				if (j < size) {
					VertexWeight vw = vertexWeightList.get(j);
					weights.add(vw.getWeight());
					boneIds.add(vw.getBoneId());
				} else {
					weights.add(0.0f);
					boneIds.add(0);
				}
			}
		}
	}

	private static Mesh processMesh(AIMesh aiMesh, List<Bone> boneList, String fileName) {
		List<Float> vertices = new ArrayList<>();
		List<Float> textures = new ArrayList<>();
		List<Float> normals = new ArrayList<>();
		List<Integer> indices = new ArrayList<>();
		List<Integer> boneIds = new ArrayList<>();
		List<Float> weights = new ArrayList<>();

		processVertices(aiMesh, vertices);
		processNormals(aiMesh, normals);
		processTextCoords(aiMesh, textures);
		processIndices(aiMesh, indices);
		processBones(aiMesh, boneList, boneIds, weights);

		Mesh mesh = new Mesh(Util.listToArray(vertices), Util.listToArray(textures), Util.listToArray(normals),
				Util.listIntToArray(indices), Util.listIntToArray(boneIds), Util.listToArray(weights),
				loadTexture(fileName));

		return mesh;
	}

	private static Node processNodesHierarchy(AINode aiNode, Node parentNode) {
		String nodeName = aiNode.mName().dataString();
		Node node = new Node(nodeName, parentNode);

		int numChildren = aiNode.mNumChildren();
		PointerBuffer aiChildren = aiNode.mChildren();
		for (int i = 0; i < numChildren; i++) {
			AINode aiChildNode = AINode.create(aiChildren.get(i));
			Node childNode = processNodesHierarchy(aiChildNode, node);
			node.addChild(childNode);
		}

		return node;
	}

	private static Matrix4f toMatrix(AIMatrix4x4 aiMatrix4x4) {
		Matrix4f result = new Matrix4f();
		result.m00 = aiMatrix4x4.a1();
		result.m10 = aiMatrix4x4.a2();
		result.m20 = aiMatrix4x4.a3();
		result.m30 = aiMatrix4x4.a4();
		result.m01 = aiMatrix4x4.b1();
		result.m11 = aiMatrix4x4.b2();
		result.m21 = aiMatrix4x4.b3();
		result.m31 = aiMatrix4x4.b4();
		result.m02 = aiMatrix4x4.c1();
		result.m12 = aiMatrix4x4.c2();
		result.m22 = aiMatrix4x4.c3();
		result.m32 = aiMatrix4x4.c4();
		result.m03 = aiMatrix4x4.d1();
		result.m13 = aiMatrix4x4.d2();
		result.m23 = aiMatrix4x4.d3();
		result.m33 = aiMatrix4x4.d4();

		return result;
	}

	private static void processVertices(AIMesh aiMesh, List<Float> vertices) {
		AIVector3D.Buffer aiVertices = aiMesh.mVertices();
		float maxX = 0, minX = 0;
		float maxY = 0, minY = 0;
		float maxZ = 0, minZ = 0;
		while (aiVertices.remaining() > 0) {
			AIVector3D aiVertex = aiVertices.get();
			vertices.add(aiVertex.x());
			vertices.add(aiVertex.y());
			vertices.add(aiVertex.z());
			minX = Math.min(minX, aiVertex.x());
			minY = Math.min(minY, aiVertex.y());
			minZ = Math.min(minZ, aiVertex.z());
			maxX = Math.max(maxX, aiVertex.x());
			maxY = Math.max(maxY, aiVertex.y());
			maxZ = Math.max(maxZ, aiVertex.z());
		}
		OBJLength.x = maxX - minX;
		OBJLength.y = maxY - minY;
		OBJLength.z = maxZ - minZ;
	}

	private static void processTextCoords(AIMesh aiMesh, List<Float> textures) {
		AIVector3D.Buffer textCoords = aiMesh.mTextureCoords(0);
		int numTextCoords = textCoords != null ? textCoords.remaining() : 0;
		for (int i = 0; i < numTextCoords; i++) {
			AIVector3D textCoord = textCoords.get();
			textures.add(textCoord.x());
			textures.add(1 - textCoord.y());
		}
	}

	private static void processNormals(AIMesh aiMesh, List<Float> normals) {
		AIVector3D.Buffer aiNormals = aiMesh.mNormals();
		while (aiNormals != null && aiNormals.remaining() > 0) {
			AIVector3D aiNormal = aiNormals.get();
			normals.add(aiNormal.x());
			normals.add(aiNormal.y());
			normals.add(aiNormal.z());
		}
	}

	private static void processIndices(AIMesh aiMesh, List<Integer> indices) {
		int numFaces = aiMesh.mNumFaces();
		AIFace.Buffer aiFaces = aiMesh.mFaces();
		for (int i = 0; i < numFaces; i++) {
			AIFace aiFace = aiFaces.get(i);
			IntBuffer buffer = aiFace.mIndices();
			while (buffer.remaining() > 0) {
				indices.add(buffer.get());
			}
		}
	}

	private static Texture loadTexture(String textureFile) {
		Texture diffuseTexture = Texture.newTexture(textureFile).anisotropic().create();
		return diffuseTexture;
	}

	public static Vector3f getOBJLength() {
		return OBJLength;
	}

}

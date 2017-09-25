package maths;

import java.io.Serializable;
import java.nio.FloatBuffer;

/**
 *
 * Holds a 3-tuple vector.
 *
 * @author cix_foo <cix_foo@users.sourceforge.net>
 * @version $Revision$ $Id$
 */

public class Vector3f extends Vector implements Serializable, ReadableVector3f, WritableVector3f {

	private static final long serialVersionUID = 1L;

	public float x, y, z;

	/**
	 * Constructor for Vector3f.
	 */
	public Vector3f() {
		super();
	}

	/**
	 * Constructor
	 */
	public Vector3f(ReadableVector3f src) {
		set(src);
	}

	/**
	 * Constructor
	 */
	public Vector3f(float x, float y, float z) {
		set(x, y, z);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lwjgl.util.vector.WritableVector2f#set(float, float)
	 */
	public void set(float x, float y) {
		this.x = x;
		this.y = y;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lwjgl.util.vector.WritableVector3f#set(float, float, float)
	 */
	public void set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Load from another Vector3f
	 * 
	 * @param src
	 *            The source vector
	 * @return this
	 */
	public Vector3f set(ReadableVector3f src) {
		x = src.getX();
		y = src.getY();
		z = src.getZ();
		return this;
	}

	/**
	 * @return the length squared of the vector
	 */
	public float lengthSquared() {
		return x * x + y * y + z * z;
	}

	/**
	 * Translate a vector
	 * 
	 * @param x
	 *            The translation in x
	 * @param y
	 *            the translation in y
	 * @return this
	 */
	public Vector3f translate(float x, float y, float z) {
		this.x += x;
		this.y += y;
		this.z += z;
		return this;
	}

	/**
	 * Add a vector to another vector and place the result in a destination
	 * vector.
	 * 
	 * @param left
	 *            The LHS vector
	 * @param right
	 *            The RHS vector
	 * @param dest
	 *            The destination vector, or null if a new vector is to be
	 *            created
	 * @return the sum of left and right in dest
	 */
	public static Vector3f add(Vector3f left, Vector3f right, Vector3f dest) {
		if (dest == null)
			return new Vector3f(left.x + right.x, left.y + right.y, left.z + right.z);
		else {
			dest.set(left.x + right.x, left.y + right.y, left.z + right.z);
			return dest;
		}
	}

	/**
	 * Subtract a vector from another vector and place the result in a
	 * destination vector.
	 * 
	 * @param left
	 *            The LHS vector
	 * @param right
	 *            The RHS vector
	 * @param dest
	 *            The destination vector, or null if a new vector is to be
	 *            created
	 * @return left minus right in dest
	 */
	public static Vector3f sub(Vector3f left, Vector3f right, Vector3f dest) {
		if (dest == null)
			return new Vector3f(left.x - right.x, left.y - right.y, left.z - right.z);
		else {
			dest.set(left.x - right.x, left.y - right.y, left.z - right.z);
			return dest;
		}
	}

	/**
	 * The cross product of two vectors.
	 *
	 * @param left
	 *            The LHS vector
	 * @param right
	 *            The RHS vector
	 * @param dest
	 *            The destination result, or null if a new vector is to be
	 *            created
	 * @return left cross right
	 */
	public static Vector3f cross(Vector3f left, Vector3f right, Vector3f dest) {

		if (dest == null)
			dest = new Vector3f();

		dest.set(left.y * right.z - left.z * right.y, right.x * left.z - right.z * left.x,
				left.x * right.y - left.y * right.x);

		return dest;
	}

	/**
	 * Negate a vector
	 * 
	 * @return this
	 */
	public Vector negate() {
		x = -x;
		y = -y;
		z = -z;
		return this;
	}

	/**
	 * Negate a vector and place the result in a destination vector.
	 * 
	 * @param dest
	 *            The destination vector or null if a new vector is to be
	 *            created
	 * @return the negated vector
	 */
	public Vector3f negate(Vector3f dest) {
		if (dest == null)
			dest = new Vector3f();
		dest.x = -x;
		dest.y = -y;
		dest.z = -z;
		return dest;
	}

	/**
	 * Normalise this vector and place the result in another vector.
	 * 
	 * @param dest
	 *            The destination vector, or null if a new vector is to be
	 *            created
	 * @return the normalised vector
	 */
	public Vector3f normalise(Vector3f dest) {
		float l = length();

		if (dest == null)
			dest = new Vector3f(x / l, y / l, z / l);
		else
			dest.set(x / l, y / l, z / l);

		return dest;
	}

	/**
	 * The dot product of two vectors is calculated as v1.x * v2.x + v1.y * v2.y
	 * + v1.z * v2.z
	 * 
	 * @param left
	 *            The LHS vector
	 * @param right
	 *            The RHS vector
	 * @return left dot right
	 */
	public static float dot(Vector3f left, Vector3f right) {
		return left.x * right.x + left.y * right.y + left.z * right.z;
	}

	/**
	 * Calculate the angle between two vectors, in radians
	 * 
	 * @param a
	 *            A vector
	 * @param b
	 *            The other vector
	 * @return the angle between the two vectors, in radians
	 */
	public static float angle(Vector3f a, Vector3f b) {
		float dls = dot(a, b) / (a.length() * b.length());
		if (dls < -1f)
			dls = -1f;
		else if (dls > 1.0f)
			dls = 1.0f;
		return (float) Math.acos(dls);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lwjgl.vector.Vector#load(FloatBuffer)
	 */
	public Vector load(FloatBuffer buf) {
		x = buf.get();
		y = buf.get();
		z = buf.get();
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lwjgl.vector.Vector#scale(float)
	 */
	public Vector scale(float scale) {

		x *= scale;
		y *= scale;
		z *= scale;

		return this;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lwjgl.vector.Vector#store(FloatBuffer)
	 */
	public Vector store(FloatBuffer buf) {

		buf.put(x);
		buf.put(y);
		buf.put(z);

		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder(64);

		sb.append("Vector3f[");
		sb.append(x);
		sb.append(", ");
		sb.append(y);
		sb.append(", ");
		sb.append(z);
		sb.append(']');
		return sb.toString();
	}

	/**
	 * @return x
	 */
	public final float getX() {
		return x;
	}

	/**
	 * @return y
	 */
	public final float getY() {
		return y;
	}

	/**
	 * Set X
	 * 
	 * @param x
	 */
	public final void setX(float x) {
		this.x = x;
	}

	/**
	 * Set Y
	 * 
	 * @param y
	 */
	public final void setY(float y) {
		this.y = y;
	}

	/**
	 * Set Z
	 * 
	 * @param z
	 */
	public void setZ(float z) {
		this.z = z;
	}

	/*
	 * (Overrides)
	 * 
	 * @see org.lwjgl.vector.ReadableVector3f#getZ()
	 */
	public float getZ() {
		return z;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vector3f other = (Vector3f) obj;

		if (x == other.x && y == other.y && z == other.z)
			return true;

		return false;
	}

	// return the max values of the 2 vectors
	public Vector3f Max(Vector3f other) {
		Vector3f result = new Vector3f();
		result.x = this.x > other.x ? this.x : other.x;
		result.y = this.y > other.y ? this.y : other.y;
		result.z = this.z > other.z ? this.x : other.z;

		return result;
	}

	public float MaxValue() {
		float max = 0;
		max = Math.max(Math.max(this.x, this.y), this.z);
		return max;
	}

	public Vector3f reflected(Vector3f normal) {
		float a = Vector3f.dot(this, normal) * (-2);
		normal.x = normal.x * a;
		normal.y = normal.y * a;
		normal.z = normal.z * a;
		Vector3f dest = new Vector3f();
		Vector3f.add(normal, this, dest);
		return dest;
	}

	public boolean isZero() {
		return (this.x == 0 && this.y == 0 && this.z == 0);
	}

	public boolean equls(Vector3f other) {
		return (this.x == other.x && this.y == other.y && this.z == other.z);
	}
	
	public Vector3f MaxVector(Vector3f other){
		Vector3f max=new Vector3f();
		max.x=Math.max(this.x, other.x);
		max.y=Math.max(this.y, other.y);
		max.z=Math.max(this.z, other.z);
		return max;
	}
	
	public Vector3f rotate(Vector3f rotation){
		Matrix4f matrix=new Matrix4f();
		matrix.setIdentity();
		Matrix4f.rotate((float) Math.toRadians(rotation.x), new Vector3f(1, 0, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rotation.y), new Vector3f(0, 1, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rotation.z), new Vector3f(0, 0, 1), matrix, matrix);
		Matrix4f ro=new Matrix4f();
		ro.setZero();
		ro.m00=this.x;
		ro.m01=this.y;
		ro.m02=this.z;
		Matrix4f.mul(matrix, ro, matrix);
		
		return new Vector3f(matrix.m00,matrix.m01,matrix.m02);
	}
	
}
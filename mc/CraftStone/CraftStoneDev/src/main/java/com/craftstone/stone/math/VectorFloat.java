package com.craftstone.stone.math;

/**
 * Holds a float vector
 * @author kmate
 *
 */
public class VectorFloat {
	private float x, y, z = 0f;
	
	/**
	 * Creates a vector3f
	 * @param x the x
	 * @param y the y
	 * @param z the z
	 */
	public VectorFloat(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Creates a vector2f
	 * @param x the x
	 * @param y the y
	 */
	public VectorFloat(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Returns the x of the vector
	 * @return the x coordinate
	 */
	public float getX() {
		return x;
	}

	/**
	 * Returns the y of the vector
	 * @return the y coordinate
	 */
	public float getY() {
		return y;
	}
	
	/**
	 * Returns the z of the vector
	 * @return the z coordinate
	 */
	public float getZ() {
		return z;
	}
}

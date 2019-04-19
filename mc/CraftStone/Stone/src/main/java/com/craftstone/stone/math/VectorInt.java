package com.craftstone.stone.math;

/**
 * Holds an int vector
 * @author kmate
 *
 */
public class VectorInt {
	private int x, y, z = 0;
	
	/**
	 * Creates a vector3i
	 * @param x the x
	 * @param y the y
	 * @param z the z
	 */
	public VectorInt(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Creates a vector2i
	 * @param x the x
	 * @param y the y
	 */
	public VectorInt(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Returns the x of the vector
	 * @return the x coordinate
	 */
	public int getX() {
		return x;
	}

	/**
	 * Returns the y of the vector
	 * @return the y coordinate
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * Returns the z of the vector
	 * @return the z coordinate
	 */
	public int getZ() {
		return z;
	}
}

package com.craftstone.stone.math;

/**
 * Holds a double vector
 * @author kmate
 *
 */
public class VectorDouble {
	private double x, y, z = 0;
	
	/**
	 * Creates a vector3d
	 * @param x the x
	 * @param y the y
	 * @param z the z
	 */
	public VectorDouble(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Creates a vector2d
	 * @param x the x
	 * @param y the y
	 */
	public VectorDouble(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Returns the x of the vector
	 * @return the x coordinate
	 */
	public double getX() {
		return x;
	}

	/**
	 * Returns the y of the vector
	 * @return the y coordinate
	 */
	public double getY() {
		return y;
	}
	
	/**
	 * Returns the z of the vector
	 * @return the z coordinate
	 */
	public double getZ() {
		return z;
	}
}

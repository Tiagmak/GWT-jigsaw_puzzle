package mpjp.shared.geom;

import java.io.Serializable;

public class Point implements Serializable{
	private static final long serialVersionUID = 1L;
	private double x, y;

	/***
	 * Create point from coordinates
	 * @param x The horizontal coordinate of this point
	 * @param y The vertical coordinate of this point
	 */
	public Point(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}

	/**
	 * Create point at origin
	 */
	public Point() {
		super();
	}

	/**
	 * The X coordinate of this point
	 * 
	 * @return x
	 */
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	/**
	 * The Y coordinate of this point
	 * 
	 * @return y
	 */
	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	@Override
	public String toString() {
		return "Point [x=" + x + ", y=" + y + "]";
	}

	/**
	 * overrides hashCode in class java.lang.Object 
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	/**
	 * override equals in class java.lang.Object
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Point other = (Point) obj;
		if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
			return false;
		if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
			return false;
		return true;
	}
}

package mpjp.shared.geom;

import java.io.Serializable;

public class CurveTo implements Segment, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * controlPoint1 - First control point in this Bezier line
	 * controlPoint2 - Second control point in this Bezier line
	 *  	endPoint - End point in this Bezier line
	 */
	private Point controlPoint1, controlPoint2, endPoint;

	/**
	 * An empty instance
	 */
	public CurveTo() {
		
	}

	/**
	 * A Bezier segment with given control points and end point
	 * @param controlPoint1 - control point 1
	 * @param controlPoint2 - control point 2
	 * @param endPoint - end point
	 */
	public CurveTo(Point controlPoint1, Point controlPoint2, Point endPoint) {
		this.controlPoint1 = controlPoint1;
		this.controlPoint2 = controlPoint2;
		this.endPoint = endPoint;
	}

	/**
	 * Current first control point
	 * @return - controlPoint1
	 */
	public Point getControlPoint1() {
		return controlPoint1;
	}

	/**
	 * Change first control point
	 * @param controlPoint1 - first control point
	 */
	public void setControlPoint1(Point controlPoint1) {
		this.controlPoint1 = controlPoint1;
	}

	/**
	 * Current second control point
	 * @return - controlPoint2
	 */
	public Point getControlPoint2() {
		return controlPoint2;
	}

	/**
	 * Change second control point
	 * @param controlPoint2 - second control point
	 */
	public void setControlPoint2(Point controlPoint2) {
		this.controlPoint2 = controlPoint2;
	}

	/**
	 * The current end point
	 * @return - endPoint
	 */
	public Point getEndPoint() {
		return endPoint;
	}

	/**
	 * Change the end point
	 * @param endPoint - end point
	 */
	public void setEndPoint(Point endPoint) {
		this.endPoint = endPoint;
	}
}

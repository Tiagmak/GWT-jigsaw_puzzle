package mpjp.shared.geom;

public class QuadTo implements Segment{
	/**
	 * controlPoint - Create point at origin
	 * endPoint - The end point of this quadratic curve segment
	 */
	private Point controlPoint, endPoint;

	/**
	 * An empty instance
	 */
	public QuadTo() {
		
	}

	/**
	 * A quadratic segment from given control point and end point
	 * @param controlPoint
	 * @param endPoint
	 */
	public QuadTo(Point controlPoint, Point endPoint) {
		this.controlPoint = controlPoint;
		this.endPoint = endPoint;
	}

	/**
	 * Current control point
	 * @return - controlPoint
	 */
	public Point getControlPoint() {
		return controlPoint;
	}

	/**
	 * Create point at origin
	 * @param controlPoint - control point
	 */
	public void setControlPoint(Point controlPoint) {
		this.controlPoint = controlPoint;
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

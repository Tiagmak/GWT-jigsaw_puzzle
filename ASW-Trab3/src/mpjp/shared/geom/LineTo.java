package mpjp.shared.geom;

public class LineTo implements Segment{
	/**
	 * End point in this line segment
	 */
	private Point endPoint;

	/**
	 * An empty instance
	 */
	public LineTo() {
		
	}

	/**
	 * A line segment to the given end point
	 * @param endPoint - end point
	 */
	public LineTo(Point endPoint) {
		this.endPoint = endPoint;
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

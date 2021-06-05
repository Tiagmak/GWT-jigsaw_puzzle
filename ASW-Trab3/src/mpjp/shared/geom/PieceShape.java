package mpjp.shared.geom;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PieceShape implements Serializable{

	private static final long serialVersionUID = 1L;
	Point startPoint;
	List<Segment> segments;
	
	/**
	 * Create a complete shape from an initial point as a list of segments
	 * @param startPoint
	 * @param segments
	 */
	public PieceShape(Point startPoint, List<Segment> segments) {
		this.startPoint = startPoint;
		this.segments = segments;
	}

	/**
	 * Create an empty piece shape
	 */
	public PieceShape() {
		this.segments =  new ArrayList<Segment>();
	}

	/**
	 * Create a piece shape with an initial point.
	 * @param startPoint
	 */
	public PieceShape(Point startPoint) {
		this.startPoint = startPoint;
		this.segments =  new ArrayList<Segment>();
	}
	
	/**
	 * The current starting point
	 * @return - point
	 */
	public Point getStartPoint() {
		return startPoint;
	}
	
	/**
	 * Change the starting point
	 * @param startPoint - start point
	 */
	public void setStartPoint(Point startPoint) {
		this.startPoint = startPoint;
	}

	/**
	 * The current list of segments
	 * @return segments
	 */
	public List<Segment> getSegments() {
		return segments;
	}

	/**
	 * Change the list of segments
	 * @param segments - list of Segment
	 */
	public void setSegments(List<Segment> segments) {
		this.segments = segments;
	}

	/**
	 * Add a segment to this shape
	 * @param segment - to be added
	 * @return this instance, to allow chaining
	 */
	public PieceShape addSegment(Segment segment) {
		this.segments.add(segment);
		return this;
	}

}
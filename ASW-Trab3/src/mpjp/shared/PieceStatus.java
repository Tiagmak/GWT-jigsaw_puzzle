package mpjp.shared;
import java.io.Serializable;

import mpjp.shared.geom.Point;


public class PieceStatus implements HasPoint, Serializable{
	
	static final long serialVersionUID = 1L;
	
	/**
	 * block - ID of the block this piece belongs to
	 * id - ID of a piece, a non-negative integer less than the number of pieces
	 * position - Current position of this piece
	 * rotation - Rotation of this piece (unused in this prototype)
	 */
	int block, id;
	Point position;
	double rotation;
	
	public PieceStatus(int id, Point position) {
		super();
		this.id = id;
		this.position = position;
	}

	public PieceStatus() {
		super();
	}
	
	/**
	 * The ID block of connected pieces that this currently piece belongs to.
	 * @return - block
	 */
	public int getBlock() {
		return block;
	}

	/**
	 * Change the block ID this piece currently belongs to
	 * @param block - id of this pieces's block
	 */
	public void setBlock(int block) {
		this.block = block;
	}

	/**
	 * The immutable id of this piece (i.e. cannot be changed)
	 * @return - id of this piece
	 */
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/**
	 * The current position of the piece "center" in the workspace
	 * @return - center
	 */
	public Point getPosition() {
		return position;
	}

	/**
	 * Change the position of the piece "center" in the workspace
	 * @param position - of the piece's "center"
	 */
	public void setPosition(Point position) {
		this.position = position;
	}

	/**
	 * Current rotation of this piece, in radians.
	 * @return - rotation
	 */
	public double getRotation() {
		return rotation;
	}

	/**
	 * Change the rotation of this piece.
	 * @param rotation - of the piece
	 */
	public void setRotation(double rotation) {
		this.rotation = rotation;
	}

	public double getX() {
		return position.getX();
	}
	
	public double getY() {
		return position.getY();
	}
}

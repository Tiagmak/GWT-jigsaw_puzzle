package mpjp.game;


public enum Direction {
	NORTH, SOUTH, WEST, EAST;

	/**
	 * Signal of X axis variation in this direction, one of { -1, 0, 1 }
	 * @return delta
	 */
	int getSignalX() {
		switch (this) {
		case EAST:
			return 1;
		case WEST:
			return -1;
		default:
			return 0;
		}
	}
	
	/**
	 * Signal of Y axis variation in this direction, one of { -1, 0, 1 }
	 * @return delta
	 */
	int getSignalY() {
		switch (this) {
		case SOUTH:
			return 1;
		case NORTH:
			return -1;
		default:
			return 0;
		}
	}
}

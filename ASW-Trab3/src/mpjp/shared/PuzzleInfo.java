package mpjp.shared;
import java.io.Serializable;

/**
 * Information required to create a jigsaw puzzle. 
 * This class groups data that needs to be redirected to different methods and classes, following a design pattern known as Collecting Parameter.
 */
public class PuzzleInfo implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * columns - Number of columns in the puzzle
	 * cuttingName - Name of the cutting used for producing pieces
	 * height - Height of the jigsaw puzzle
	 * imageName - Name of the file with the jigsaw puzzle's background
	 * rows - Number of rows in the puzzle
	 * width - Width of the jigsaw puzzle
	 */
	int columns, rows;
	java.lang.String cuttingName, imageName;
	double height, width;
	
	public PuzzleInfo() {
		super();
	}
	
	public PuzzleInfo(String imageName, String cuttingName, int rows, int columns, double width, double height) {
		super();
		this.columns = columns;
		this.rows = rows;
		this.cuttingName = cuttingName;
		this.imageName = imageName;
		this.height = height;
		this.width = width;
	}
	
	public int getColumns() {
		return columns;
	}
	
	public void setColumns(int columns) {
		this.columns = columns;
	}
	
	public int getRows() {
		return rows;
	}
	
	public void setRows(int rows) {
		this.rows = rows;
	}
	
	public java.lang.String getCuttingName() {
		return cuttingName;
	}
	
	public void setCuttingName(java.lang.String cuttingName) {
		this.cuttingName = cuttingName;
	}
	
	public java.lang.String getImageName() {
		return imageName;
	}
	
	public void setImageName(java.lang.String imageName) {
		this.imageName = imageName;
	}
	
	public double getHeight() {
		return height;
	}
	
	public void setHeight(double height) {
		this.height = height;
	}
	
	public double getWidth() {
		return width;
	}
	
	public void setWidth(double width) {
		this.width = width;
	}	
}

package ocr.info;

/**
 * Representation of a Grid
 *
 * @author Jonathan Reimels
 * @version 1.0.0
 */
public class Grid {
	private boolean[][] _grid;
	private int _size = 8;

	/**
	 * Constructor - Set the size of the grid (grids are square, the size of a single side)
	 * @param size
	 */
	public Grid(int size) {
		_size = size;
		_grid = new boolean[_size][_size];
		clear();
	}

	/**
	 * Get the size of the grid (grids are square, the size of a single side)
	 * @return size
	 */
	public int getSize() {
		return _size;
	}

	/**
	 * Get the value of a coordinate
	 * @param coord - The coordinate to get
	 * @return boolean value of coordinate
	 * @throws Exception
	 */
	public boolean getValue(Coordinate coord) throws Exception {
		// check that coordinate is within the grid
		if (coord.getRow() < 0 || coord.getRow() >= _size || coord.getCol() < 0 || coord.getCol() >= _size) {
			throw new Exception("Invalid row or col number");
		}

		// return the value at the coordinate
		return _grid[coord.getRow()][coord.getCol()];
	}

	/**
	 * Set the value at a coordinate
	 * @param coord - The coordinate to set
	 * @param value - The boolean value to set
	 * @throws Exception
	 */
	public void setValue(Coordinate coord, boolean value) throws Exception {
		// set value at coordinate
		_grid[coord.getRow()][coord.getCol()] = value;
	}

	/**
	 * Clear all values in grid
	 */
	public void clear() {
		for (int i = 0; i < _size; i++) {
			for (int j = 0; j < _size; j++) {
				_grid[i][j] = false;
			}
		}
	}
}


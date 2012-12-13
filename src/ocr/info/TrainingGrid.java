package ocr.info;

import java.io.Serializable;

/**
 * Container for a Grid and it's expected value when processed through a Neural Net
 *
 * @author Jonathan Reimels
 * @version 1.0.0
 */
public class TrainingGrid implements Serializable {
    /**
     *  generated Serial Version UID
     */
    private static final long serialVersionUID = 7962173105892065911L;

    // instance variables
    private Grid _grid;
    private char _value;

    /**
     * Constructor
     */
    public TrainingGrid() {}

    /**
     * Constructor, set grid and expected value
     * @param grid - Grid to set
     * @param value - Expected value of grid
     */
    public TrainingGrid(Grid grid, char value) {
        _grid = grid;
        _value = value;
    }

    /**
     * Get the Grid
     * @return Grid
     */
    public Grid getGrid() {
        return _grid;
    }

    /**
     * Set the Grid
     * @param grid - Grid to set
     */
    public void setGrid(Grid grid) {
        _grid = grid;
    }

    /**
     * Get the expected value for the grid
     * @return Expected value
     */
    public char getValue() {
        return _value;
    }

    /**
     * Set the expected value for the grid
     * @param value - Expected value to set
     */
    public void setValue(char value) {
        _value = value;
    }
}

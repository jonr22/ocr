package ocr.info;

/**
 * Container for constants
 * @author Jonathan Reimels
 * @version 1.0.0
 */
public class Constants {
	/**
	 * Array of output characters
	 */
	public static final Character[] OUTPUT = new Character[] {0, 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 
		'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

	/**
	 * Size of a side of the grid
	 */
	public static final int GRID_SIZE = 8;

	/**
	 * Offset for number of neurons in hidden layer (normally  (input size) * (2/3) + (output size)
	 */
	public static final int HIDDEN_LAYER_OFFSET = -45;

	/**
	 * Confidence threshold for each letter (should be between 0 and 1)
	 */
	public static final double CONFIDENCE = .8;
	
	/**
	 * Number of epochs to run through in training
	 */
	public static final int EPOCH_COUNT = 10000;
}

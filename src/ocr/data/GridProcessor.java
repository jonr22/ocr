package ocr.data;

import neural.net.Network;
import ocr.info.Constants;
import ocr.info.Coordinate;
import ocr.info.Grid;

/**
 * Collection of static methods to process a Grid through a Network and to
 * process input and output
 * @author Jonathan Reimels
 * @version 1.0.0
 */
public class GridProcessor {

	// constants
	private static final double TRUE = 1.0;
	private static final double FALSE = 0.0;

	/**
	 * Convert a Grid into the proper format to pass to a Network
	 * @param grid - Grid to convert
	 * @return input to pass to a Network
	 * @throws Exception
	 */
	public static double[] convertGrid(Grid grid) throws Exception {
		int size = grid.getSize();
		double[] inputs = new double[size * size];

		for (int row = 0; row < size; row++) {
			for (int col = 0; col < size; col++) {
				if (grid.getValue(new Coordinate(row, col))) {
					inputs[row * size + col] = TRUE;
				} else {
					inputs[row * size + col] = FALSE;
				}
			}
		}

		return inputs;
	}

	/**
	 * Convert an 'Expected Output' char into a form that can be passed to the trainer
	 * @param output - char to convert
	 * @return Expected Output to pass to the trainer
	 */
	public static double[] convertExpectedOutput(char output) {
		// TODO: check output is 0 or a capital letter - else throw error
		double[] expected = new double[NetworkManager.OUTPUT_SIZE];
		for (int i = 0; i < NetworkManager.OUTPUT_SIZE; i++) {
			expected[i] = FALSE;
		}

		if (Character.isLetter(output)) {
			int index = output - 'A';
			expected[index] = TRUE;
		}

		return expected;
	}

	/**
	 * Convert the output from a Neural Network into the appropriate char
	 * @param output - Neural Network output
	 * @return char
	 */
	public static char convertOutput(double[] output) {
		for (int i = 0; i < output.length; i++) {
			if (output[i] > Constants.CONFIDENCE) {
				return (char)(i + 'A');
			}
		}
		return 0;
	}

	/**
	 * Process a Grid through a Network
	 * @param grid - Grid to process
	 * @param network - Network to process Grid through
	 * @return char result
	 * @throws Exception
	 */
	public static char process(Grid grid, Network network) throws Exception {
		double[] inputs = convertGrid(grid);
		double[] outputs = network.fire(inputs);
		return convertOutput(outputs);
	}
}


package ocr.data;

import neural.net.Network;
import ocr.info.Coordinate;
import ocr.info.Grid;

public class GridProcessor {

	private static final double TRUE = 1.0;
	private static final double FALSE = 0.0;
	private static final double CUTOFF = 0.8;

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

	public static double[] convertExpectedOutput(char output) {
	    // TODO: check output is 0 or a capital letter
	    double[] expected = new double[NetworkManager.OUTPUT_SIZE];
	    for (int i = 0; i < NetworkManager.OUTPUT_SIZE; i++) {
	        expected[i] = FALSE;
	    }

	    if (Character.isLetter(output)) {
    	    int index = Character.getNumericValue(output) - Character.getNumericValue('A');
    	    expected[index] = TRUE;
	    }

	    return expected;
	}
	
	public static char convertOutput(double[] output) {
		for (int i = 0; i < output.length; i++) {
			if (output[i] > CUTOFF) {
				return (char)(i + 'A');
			}
		}
		return 0;
	}

	public static char process(Grid grid, Network network) throws Exception {
		double[] inputs = convertGrid(grid);
		double[] outputs = network.fire(inputs);
		return convertOutput(outputs);
	}
}

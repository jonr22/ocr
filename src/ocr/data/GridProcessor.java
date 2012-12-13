package ocr.data;

import neural.net.Network;
import ocr.info.Coordinate;
import ocr.info.Grid;

public class GridProcessor {
	private static final int HIDDEN_NEURON_COUNT = 25;
	private static final int OUTPUT_COUNT = 1;
	private static final double TRUE = 1.0;
	private static final double FALSE = 0.0;
	private static final double CUTOFF = 0.8;
	
	private Network _network;
	private Grid _grid;
	
	public GridProcessor(Grid grid) {
		_grid = grid;
		_network = new Network(_grid.getSize() * _grid.getSize(), OUTPUT_COUNT, HIDDEN_NEURON_COUNT);
	}
	
	public GridProcessor(Grid grid, String filename) throws Exception {
		_grid = grid;
		_network = Network.load(filename);
	}
	
	public void save(String filename) throws Exception {
		_network.save(filename);
	}
	
	public char process() throws Exception {
		int size = _grid.getSize();
		double[] inputs = new double[size * size];
		
		for (int row = 0; row < size; row++) {
			for (int col = 0; col < size; col++) {
				if (_grid.getValue(new Coordinate(row, col))) {
					inputs[row * size + col] = TRUE;
				} else {
					inputs[row * size + col] = FALSE;
				}
			}
		}
		
		double[] outputs = _network.fire(inputs);
		for (int i = 0; i < outputs.length; i++) {
			if (outputs[i] > CUTOFF) {
				return (char)(i + Character.getNumericValue('A'));
			}
		}
		
		return 0;
	}
	
	// train
	
}

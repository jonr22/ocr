package ocr.data;

import java.io.File;

import neural.net.Network;
import ocr.info.Constants;

/**
 * Manage a Network, handle creating and saving the network for OCR
 * 
 * @author Jonathan Reimels
 * @version 1.0.0
 */
public class NetworkManager {
	// public constants
	public static final int OUTPUT_SIZE = Constants.OUTPUT.length;
	public static final int INPUT_SIZE = Constants.GRID_SIZE * Constants.GRID_SIZE;

	// private constants
	private static final int HIDDEN_NEURON_COUNT = (INPUT_SIZE * 2) / 3 + OUTPUT_SIZE + Constants.HIDDEN_LAYER_OFFSET;

	// instance variables
	private Network _network;
	private File _file = null;

	/**
	 * Constructor - creates Neural Network
	 */
	public NetworkManager() {
		_network = new Network(INPUT_SIZE, OUTPUT_SIZE, HIDDEN_NEURON_COUNT);
	}

	/**
	 * Constructor - loads Neural Network from File
	 * @param file - File to load Neural Network from
	 * @throws Exception
	 */
	public NetworkManager(File file) throws Exception {
		_network = Network.load(file);
		_file = file;
	}

	/**
	 * Check if a file has been set for the Neural Network to save to
	 * @return true if a file is set
	 */
	public boolean isFileSet() {
		return _file != null;
	}

	/**
	 * Get the Network being used
	 * @return the Network
	 */
	public Network getNetwork() {
		return _network;
	}

	/**
	 * Save a Neural Network to a file
	 * @param file - File to save Neural Network to
	 * @throws Exception
	 */
	public void saveAs(File file) throws Exception {
		_network.save(file);
		_file = file;
	}

	/**
	 * Save Neural network to set file
	 * @throws Exception
	 */
	public void save() throws Exception {
		_network.save(_file);
	}
}


package neural.net;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Representation of an Artificial Neural Network
 * The number of inputs, outputs, and neurons in the hidden layer are
 * configurable and the Activation Function can be configured too with
 * any class that implements the ActivationFunction interface.
 *
 * This Network is restricted to use exactly one hidden layer
 *
 * @author Jonathan Reimels
 * @version 1.0.0
 */
public class Network implements Serializable  {
	/**
	 * generated Serial Version UID
	 */
	private static final long serialVersionUID = 7848387727961706621L;

	// instance variables
	private Layer _hiddenLayer;
	private Layer _outputLayer;
	private double[] _lastOutputs;
	private int _inputCount = 0;
	private int _outputCount = 0;
	private int _hiddenLayerNeuronCount = 0;
	private ActivationFunction _activationFunction;

	/**
	 * Constructor
	 * @param inputCount - number of inputs
	 * @param outputCount - number of output neurons
	 * @param hiddenLayerNeuronCount - number of neurons in the hidden layer
	 * @param activationFunction - class implementing ActivationFunction, used to calculate activation value of neuron
	 */
	public Network(int inputCount, int outputCount, int hiddenLayerNeuronCount, ActivationFunction activationFunction) {
		_inputCount = inputCount;
		_outputCount = outputCount;
		_hiddenLayerNeuronCount = hiddenLayerNeuronCount;
		_activationFunction = activationFunction;
		generateLayers();
	}

	/**
	 * Constructor - uses default ActivationFunction (Sigmoid)
	 * @param inputCount - number of inputs
	 * @param outputCount - number of output neurons
	 * @param hiddenLayerNeuronCount - number of neurons in the hidden layer
	 */
	public Network(int inputCount, int outputCount, int hiddenLayerNeuronCount) {
		_inputCount = inputCount;
		_outputCount = outputCount;
		_hiddenLayerNeuronCount = hiddenLayerNeuronCount;
		_activationFunction = new ActivationFunctionSigmoid();
		generateLayers();
	}

	/**
	 * Get number of inputs
	 * @return input count
	 */
	public int getInputCount() {
		return _inputCount;
	}

	/**
	 * Get number of outputs
	 * @return output count
	 */
	public int getOutputCount() {
		return _outputCount;
	}

	/**
	 * Get last output values
	 * @return outputs
	 */
	public double[] getLastOutputs() {
		return _lastOutputs;
	}

	/**
	 * Get the instance of the ActivationFunction being used
	 * @return ActivationFunction
	 */
	public ActivationFunction getActivationFunction() {
		return _activationFunction;
	}

	/**
	 * Get the hidden layer
	 * @return Layer
	 */
	public Layer getHiddenLayer() {
		return _hiddenLayer;
	}

	/**
	 * Get the output layer
	 * @return Layer
	 */
	public Layer getOutputLayer() {
		return _outputLayer;
	}

	/**
	 * Fire each layer in the Network
	 * @param inputs - inputs to be passed to the first layer
	 * @return output
	 * @throws Exception - throws an exception if input length is incorrect
	 */
	public double[] fire(double[] inputs) throws Exception {
		if (inputs.length != _inputCount) {
			throw new Exception(String.format(
					"Fire called on Network with invalid inputs, expected inputs to be of size %d not size %d",
					_inputCount,
					inputs.length));
		}

		// fire hidden layer
		double[] hiddenOutput = _hiddenLayer.fire(inputs);

		// fire output layer with output from hidden layer
		_lastOutputs = _outputLayer.fire(hiddenOutput);

		// return the output from the output layer
		return _lastOutputs;
	}

	/**
	 * Save the neural network to a file, this should be used after
	 * training is complete so that the Network can be reloaded and
	 * training will not need to be redone
	 * @param file - File to save
	 * @throws FileNotFoundException, IOException
	 */
	public void save(File file) throws Exception {
		FileOutputStream fs = new FileOutputStream(file);
		ObjectOutputStream os = new ObjectOutputStream(fs);
		os.writeObject(this);
		os.close();
	}

	/**
	 * Load a Network from a file
	 * @param file - File to load
	 * @return Network
	 * @throws FileNotFoundException, IOException, ClassNotFoundException
	 */
	public static Network load(File file) throws Exception {
		FileInputStream fs = new FileInputStream(file);
		ObjectInputStream os = new ObjectInputStream(fs);
		Object obj = os.readObject();
		os.close();

		return (Network)obj;
	}

	/**
	 * Create the hidden and output layers
	 */
	private void generateLayers() {
		_hiddenLayer = new Layer(_hiddenLayerNeuronCount, _inputCount, _activationFunction);
		_outputLayer = new Layer(_outputCount, _hiddenLayerNeuronCount, _activationFunction);
	}
}


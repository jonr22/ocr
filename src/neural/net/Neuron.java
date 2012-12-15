package neural.net;

import java.io.Serializable;
import java.util.Random;

/**
 * Represents a single neuron within an Artificial Neural Net
 * 
 * @author Jonathan Reimels
 * @version 1.0.0
 */
public class Neuron implements Serializable {
	/**
	 * generated Serial Version UID
	 */
	private static final long serialVersionUID = 3346040118277176478L;

	/**
	 * The offset input value
	 */
	public static final int OFFSET = -1;

	// instance variables
	private double[] _weights;
	private int _inputCount = 0;
	private double _lastOutput = -1.0;
	private double[] _lastInput = null;
	private ActivationFunction _activationFunction;
	private Random rnd = new Random();

	/**
	 * Constructor
	 * @param inputCount - number of inputs the neuron accepts
	 * @param activationFunction - class implementing ActivationFunction, used to calculate activation value of neuron
	 */
	public Neuron(int inputCount, ActivationFunction activationFunction) {
		_activationFunction = activationFunction;
		_inputCount = inputCount;
		generateWeights();
	}

	/**
	 * Set a weight for one of the input values
	 * @param id - weight to set, id must be less than the number of inputs + 1 (the offset gets a weight)
	 * @param weight - value of the weight
	 * @throws Exception - throws an exception for invalid id
	 */
	public void setWeight(int id, double weight) throws Exception {
		if (id < 0 || id > _inputCount) { // there is a last offset weight at _inputCount
			throw new Exception(String.format(
					"setWeight called on Neuron with invalid id, expected id to be within %d not %d",
					_inputCount,
					id));
		}

		_weights[id] = weight;
	}

	/**
	 * Get a weight for one of the input values
	 * @param id - weight to set, id must be less than the number of inputs + 1 (the offset gets a weight)
	 * @return value of weight
	 * @throws Exception - throws an exceptionf for invalid id
	 */
	public double getWeight(int id) throws Exception {
		if (id < 0 || id > _inputCount) { // there is a last offset weight at _inputCount
			throw new Exception(String.format(
					"getWeight called on Neuron with invalid id, expected id to be within %d not %d",
					_inputCount,
					id));
		}

		return _weights[id];
	}

	/**
	 * Get number of inputs for the neuron
	 * @return number of inputs
	 */
	public int getInputCount() {
		return _inputCount;
	}

	/**
	 * Get an input from the last set of input operated on
	 * @param id - input to retrieve
	 * @return value of input
	 * @throws Exception - throws an exception for invalid id, or if fire() has not been executed yet
	 */
	public double getInput(int id) throws Exception {
		if (id < 0 || id >= _inputCount) {
			throw new Exception(String.format(
					"getInput called on Neuron with invalid id, expected id to be within %d not %d",
					_inputCount,
					id));
		}

		if (_lastInput == null) {
			throw new Exception("getInput called on Neuron before fire was called");
		}

		return _lastInput[id];
	}

	/**
	 * Get the last output returned by fire()
	 * @return last output value
	 */
	public double getLastOutput() {
		return _lastOutput;
	}

	/**
	 * Fire the neuron, ie process the input and return the output
	 * @param inputs - an array of inputs to act on
	 * @return output value of neuron
	 * @throws Exception - throws an exception for invalid input size
	 */
	public double fire(double[] inputs) throws Exception {
		if (inputs.length != _inputCount) {
			throw new Exception(String.format(
					"Fire called on Neuron with invalid inputs, expected inputs to be of size %d not size %d",
					_inputCount,
					inputs.length));
		}

		// set the last input
		_lastInput = inputs;

		// calculate the dot product of inputs * weights and
		//   pass this to the activation function to get the output
		double dot = dotProduct(inputs);
		_lastOutput = _activationFunction.activate(dot);

		return _lastOutput;
	}

	/**
	 * Generate random weights, should be used to initialize weight values
	 */
	private void generateWeights() {
		// the last weight is the Threshold
		_weights = new double[_inputCount + 1];
		for (int i = 0; i <= _inputCount; i++) {
			_weights[i] = rnd.nextDouble();
		}
	}

	/**
	 * Calculate dot product of inputs (and offset) * weights
	 * @param inputs
	 * @return dot product
	 */
	private double dotProduct(double[] inputs) {
		double sum = 0.0;
		for (int i = 0; i < _inputCount; i++) {
			sum += inputs[i] * _weights[i];
		}
		sum += OFFSET * _weights[_inputCount];
		return sum;
	}
}


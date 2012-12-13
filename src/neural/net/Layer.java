package neural.net;

import java.io.Serializable;

/**
 * Represents a layer of neurons within an Artificial Neural Net
 * 
 * @author Jonathan Reimels
 * @version 1.0.0
 */
public class Layer implements Serializable  {
	/**
	 * generated Serial Version UID
	 */
	private static final long serialVersionUID = -5933031753552996252L;

	private Neuron[] _neurons;
	private double[] _lastOutputs;
	private int _neuronCount = 0;
	private int _inputCount = 0;
	private ActivationFunction _activationFunction;

	/**
	 * Constructor
	 * @param neuronCount - number of neurons in the layer
	 * @param inputCountPerNeuron - number of inputs for each neuron in the layer
	 * @param activationFunction - class implementing ActivationFunction, used to calculate activation value of neuron
	 */
	public Layer(int neuronCount, int inputCountPerNeuron, ActivationFunction activationFunction) {
		_neuronCount = neuronCount;
		_inputCount = inputCountPerNeuron;
		_activationFunction = activationFunction;
		generateNeurons();
	}

	/**
	 * Get number of neurons in the layer
	 * @return neuron count
	 */
	public int getNeuronCount() {
		return _neuronCount;
	}

	/**
	 * Get a specific neuron in the layer
	 * @param id - the neuron to get
	 * @return Neuron
	 * @throws Exception - throws an exception if id is out of bounds
	 */
	public Neuron getNeuron(int id) throws Exception {
		if (id < 0 || id >= _neuronCount) {
			throw new Exception(String.format(
					"getNeuron called on Neuron with invalid id, expected id to be within %d not %d",
					_neuronCount,
					id));
		}

		return _neurons[id];
	}

	/**
	 * Get the output from the last fire()
	 * @return last output
	 */
	public double[] getLastOutputs() {
		return _lastOutputs;
	}

	/**
	 * Fire the layer of neurons, loops through each neuron in the layer and fires it
	 * @param inputs - input to pass to each neuron
	 * @return output from each neuron
	 * @throws Exception - throws an exception if inputs length is invalid
	 */
	public double[] fire(double[] inputs) throws Exception {
		if (inputs.length != _inputCount) {
			throw new Exception(String.format(
					"Fire called on Layer with invalid inputs, expected inputs to be of size %d not size %d",
					_inputCount,
					inputs.length));
		}

		// re-initialize output
		_lastOutputs = new double[_neuronCount];

		// loop through each neuron, fire it and store its output
		for (int i = 0; i < _neuronCount; i++) {
			_lastOutputs[i] = _neurons[i].fire(inputs);
		}

		// return the output
		return _lastOutputs;
	}

	/**
	 * Create all neurons in the layer
	 */
	private void generateNeurons() {
		_neurons = new Neuron[_neuronCount];
		for (int i = 0; i < _neuronCount; i++) {
			_neurons[i] = new Neuron(_inputCount, _activationFunction);
		}
	}
}


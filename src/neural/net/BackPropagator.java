package neural.net;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class performs back-propagation on a Network to update/train the
 * weights of the Neurons within each Layer in the Network
 *
 * @author Jonathan Reimels
 * @version 1.0.0
 */
public class BackPropagator implements Serializable  { //TODO: have this extend class called Trainer
	// TODO: this probably doesn't need to be Serializable
	/**
	 * generated Serial Version UID
	 */
	private static final long serialVersionUID = -8960781685209341841L;

	// instance variables
	private ArrayList<double[]> _inputs = new ArrayList<double[]>();
	private ArrayList<double[]> _expectedOutputs = new ArrayList<double[]>();
	private ArrayList<Double> _averageErrors = new ArrayList<Double>();
	private double _learningRate;
	private Network _network;

	/**
	 * Constructor
	 * @param network - The Network to operate on
	 * @param learningRate - The learning rate to use (should be between 0 and 1)
	 */
	public BackPropagator(Network network, double learningRate) {
		_network = network;
		_learningRate = learningRate;
	}

	/**
	 * The averages from each input within the last run
	 * @return averages
	 */
	public ArrayList<Double> getAverageErrors() {
		return _averageErrors;
	}

	/**
	 * Add an input and the expected output for this input
	 * @param inputs - input to use
	 * @param expectedOutputs - output expected from this input
	 * @throws Exception - throws an exception if the input size doesn't match that expected by
	 * the Network, or the output size doesn't match that returned by the Network
	 */
	public void addInputOutput(double[] inputs, double[] expectedOutputs) throws Exception {
		if (inputs.length != _network.getInputCount()) {
			throw new Exception(String.format(
					"addInputOutput called on BackPropagator with invalid inputs, expected inputs to be of size %d not size %d",
					_network.getInputCount(),
					inputs.length));
		}

		if (expectedOutputs.length != _network.getOutputCount()) {
			throw new Exception(String.format(
					"addInputOutput called on BackPropagator with invalid expectedOutputs, expected expectedOutputs to be of size %d not size %d",
					_network.getOutputCount(),
					expectedOutputs.length));
		}

		// add the input and output to their respective lists
		_inputs.add(inputs);
		_expectedOutputs.add(expectedOutputs);
	}

	/**
	 * Run the input through an iteration, and update the weights of each neuron after the run of each input
	 * @return average error across running all inputs
	 * @throws Exception
	 */
	public double runAndUpdate() throws Exception {
		// initialize the average list
		_averageErrors = new ArrayList<Double>();

		// loop through all input/output
		for (int inputIndex = 0; inputIndex < _inputs.size(); inputIndex++) {
			// get the actual output
			double[] outputs = _network.fire(_inputs.get(inputIndex));

			// get the error values (expected - output) and add their average to the average errors list
			double[] errors = calculateInitialError(outputs, _expectedOutputs.get(inputIndex));
			_averageErrors.add(calcArrayAverage(errors));

			// get the hidden layer and the neuron count within it
			Layer layer = _network.getHiddenLayer();
			int neuronCount = layer.getNeuronCount();

			// loop through all the neurons in the hidden layer
			for (int i = 0; i < neuronCount; i++) {
				Neuron neuron = layer.getNeuron(i);

				// calculate the delta values for the hidden layer neurons and update their weights
				double sum = calculateWeightedDeltaSum(i, _network.getOutputLayer(), errors);
				double delta = neuron.getLastOutput() * (1 - neuron.getLastOutput()) * sum;
				updateNeuronWeights(neuron, delta);
			}

			// get the output layer and it's neuron count
			layer = _network.getOutputLayer();
			neuronCount = layer.getNeuronCount();

			// loop through all neurons in the output layer and update their weights
			for (int i = 0; i < neuronCount; i++) {
				// the deltas for the output layer are the errors calculated for it
				Neuron neuron = layer.getNeuron(i);
				updateNeuronWeights(neuron, errors[i]);
			}
		}

		// calculate the average of all the runs and return it
		return calcListAverage(_averageErrors);
	}

	/**
	 * Calculate the error within the output layer (expected output - actual output)
	 * @param output - the actual output
	 * @param expectedOutput - the output that is expected/wanted
	 * @return array of errors
	 */
	private double[] calculateInitialError(double[] output, double[] expectedOutput) {
		double[] error = new double[_network.getOutputCount()];
		for (int i = 0; i < output.length; i++) {
			error[i] = output[i] * (1 - output[i]) * (expectedOutput[i] - output[i]);
		}
		return error;
	}

	/**
	 * Calculate the average of an array full of doubles
	 * @param arr - Array containing values to be averaged
	 * @return average
	 */
	private double calcArrayAverage(double[] arr) {
		double sum = 0.0;
		for (int i = 0; i < arr.length; i++) {
			sum += arr[i];
		}
		return (sum / arr.length);
	}

	/**
	 * Calculate the average of an ArrayList of doubles
	 * @param list - List containing values to be averaged
	 * @return average
	 */
	private double calcListAverage(ArrayList<Double> list) {
		double sum = 0.0;
		for (int i = 0; i < list.size(); i++) {
			sum += list.get(i);
		}
		return (sum / list.size());
	}

	/**
	 * Calculate the delta using the back-propagation algorithm
	 * @param parentNeuronId - neuron (from hidden layer) to calculate the delta for
	 * @param base - previous layer
	 * @param deltas - deltas from base layer
	 * @return delta
	 * @throws Exception
	 */
	private double calculateWeightedDeltaSum(int parentNeuronId, Layer base, double[] deltas) throws Exception {
		int neuronCount = base.getNeuronCount();
		double sum = 0.0;
		for (int i = 0; i < neuronCount; i++) {
			Neuron neuron = base.getNeuron(i);
			sum += neuron.getWeight(parentNeuronId) * deltas[i];
		}
		return sum;
	}

	/**
	 * Update neuron weights
	 * @param neuron - neuron to update
	 * @param delta - delta value to use
	 * @throws Exception
	 */
	private void updateNeuronWeights(Neuron neuron, double delta) throws Exception {
		int inputCount = neuron.getInputCount();
		for (int i = 0; i < inputCount; i++) {
			neuron.setWeight(i, neuron.getWeight(i) + (_learningRate * delta * neuron.getInput(i)));
		}
		neuron.setWeight(inputCount, neuron.getWeight(inputCount) + (_learningRate * delta * Neuron.OFFSET));
	}
}


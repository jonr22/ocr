package ocr.data;

import neural.net.BackPropagator;
import neural.net.Network;
import ocr.info.TrainingGrid;

/**
 * Manage training of a Neural Network
 * 
 * @author Jonathan Reimels
 * @version 1.0.0
 */
public class TrainingManager {
	// instance variables
	private TrainingSetManager _trainingSet;
	private Network _network; // TODO: should this be NetworkManager
	private double _learningRate = 0.7;

	/**
	 * Set the Network to use for training
	 * @param network - the Network to set
	 */
	public void setNetwork(Network network) {
		_network = network;
	}

	/**
	 * Set the TrainingSet to use for training
	 * @param trainingSet - the TrainingSet to set
	 */
	public void setTrainingSet(TrainingSetManager trainingSet) {
		_trainingSet = trainingSet;
	}

	/**
	 * Set the learning rate to use for training
	 * @param learningRate - the learning rate to use
	 */
	public void setLearningRate(double learningRate) {
		_learningRate = learningRate;
	}

	/**
	 * Train the set Network with the set TrainingSet and learning rate
	 * @param epochCount - number of times to run the training
	 * @throws Exception
	 */
	public void train(int epochCount) throws Exception {
		BackPropagator trainer = initializeTrainer();

		for (int i = 0; i < epochCount; i++) {
			trainer.runAndUpdate();
		}
	}

	/**
	 * Initialize back propagator with the appropriate settings
	 * @return an initialized BackPropagator
	 * @throws Exception
	 */
	private BackPropagator initializeTrainer() throws Exception {
		BackPropagator trainer = new BackPropagator(_network, _learningRate);
		int trainingCount = _trainingSet.getCount();
		for (int i = 0; i < trainingCount; i++) {
			TrainingGrid t = _trainingSet.getGrid(i);
			trainer.addInputOutput(
					GridProcessor.convertGrid(t.getGrid()),
					GridProcessor.convertExpectedOutput(t.getValue()));
		}

		return trainer;
	}
}


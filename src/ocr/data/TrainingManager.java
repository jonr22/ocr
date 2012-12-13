package ocr.data;

import ocr.info.TrainingGrid;
import neural.net.BackPropagator;
import neural.net.Network;

public class TrainingManager {
    private TrainingSetManager _trainingSet;
    private Network _network; // TODO: should this be NetworkManager
    private double _learningRate = 0.7;

    public void setNetwork(Network network) {
        _network = network;
    }

    public void setTrainingSet(TrainingSetManager trainingSet) {
        _trainingSet = trainingSet;
    }

    public void setLearningRate(int learningRate) {
        _learningRate = learningRate;
    }

    public void train(int epochCount) throws Exception {
        BackPropagator trainer = initializeTrainer();

        for (int i = 0; i < epochCount; i++) {
            trainer.runAndUpdate();
        }
    }

    private BackPropagator initializeTrainer() throws Exception {
        // TODO: update use of BackPropagator when it extends Trainer
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

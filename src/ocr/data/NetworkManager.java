package ocr.data;

import neural.net.Network;

public class NetworkManager {
    // public constants
    public static final int OUTPUT_SIZE = 1;
    public static final int INPUT_SIZE = 8 * 8;

    // private constants
    private static final int HIDDEN_NEURON_COUNT = 25;

    // instance variables
    private Network _network;

    public NetworkManager() {
        _network = new Network(INPUT_SIZE, OUTPUT_SIZE, HIDDEN_NEURON_COUNT);
    }

    public NetworkManager(String filename) throws Exception {
        _network = Network.load(filename);
    }

    public Network getNetwork() {
        return _network;
    }

    public void save(String filename) throws Exception {
        _network.save(filename);
    }
}

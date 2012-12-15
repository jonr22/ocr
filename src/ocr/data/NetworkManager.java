package ocr.data;

import java.io.File;

import ocr.info.OutputValues;

import neural.net.Network;

public class NetworkManager {
    // public constants
    public static final int OUTPUT_SIZE = OutputValues.OUTPUT.length;
    public static final int INPUT_SIZE = 8 * 8;

    // private constants
    private static final int HIDDEN_NEURON_COUNT = (INPUT_SIZE * 2) / 3 + OUTPUT_SIZE;

    // instance variables
    private Network _network;
    private File _file = null;

    public NetworkManager() {
        _network = new Network(INPUT_SIZE, OUTPUT_SIZE, HIDDEN_NEURON_COUNT);
    }

    public NetworkManager(File file) throws Exception {
        _network = Network.load(file);
        _file = file;
    }
    
    public boolean getHasFileSet() {
    	return _file != null;
    }

    public Network getNetwork() {
        return _network;
    }

    public void saveAs(File file) throws Exception {
        _network.save(file);
        _file = file;
    }
    
    public void save() throws Exception {
        _network.save(_file);
    }
}

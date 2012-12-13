package neural.net;

import java.io.Serializable;

/**
 * Interface to implement for any Activation Function to be used with a Neural Network
 * 
 * @author Jonathan Reimels
 * @version 1.0.0
 */
public interface ActivationFunction extends Serializable {
	/**
	 * The activation function to execute within a neuron
	 * @param input - the input to process, should be the sum of weights and input to a neuron
	 * @return output
	 */
	double activate(double input);

	/**
	 * The derivative of the Activation Function to use in Back-Propogation
	 * @param input - the input to process through the derivative function
	 * @return output
	 */
	double derivative(double input);
}

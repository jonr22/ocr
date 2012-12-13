package neural.net;

/**
 * Implementation of the Sigmoid Activation Function
 * 
 * @author Jonathan Reimels
 * @version 1.0.0
 */
public class ActivationFunctionSigmoid implements ActivationFunction {
	/**
	 * generated Serial Version UID
	 */
	private static final long serialVersionUID = -1261043327479245211L;

	/**
	 * The sigmoid activation function
	 */
	@Override
	public double activate(double input) {
		return 1.0 / (1 + Math.exp(-1.0 * input));
	}

	/**
	 * The derivative of the sigmoid function
	 */
	@Override
	public double derivative(double input) {
		return input * (1.0 - input);
	}
}

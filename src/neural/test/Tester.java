package neural.test;

import neural.net.BackPropagator;
import neural.net.Network;

/**
 * Test the Artificial Neural Network wit a simple XOR-like example.
 * The test takes 3 input of 1 or 0, all 1s and all 0s are equal to 0,
 * all other combinations are equal to 1
 * 
 * @author Jonathan Reimels
 * @version 1.0.0
 */
public class Tester {

	/**
	 * Run the test
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// set-up network and trainer
			Network network = new Network(3, 1, 2);
			BackPropagator prop = new BackPropagator(network, 0.8);

			// add test data
			prop.addInputOutput(new double[] {1.0, 1.0, 1.0}, new double[] {0.0});
			prop.addInputOutput(new double[] {1.0, 1.0, 0.0}, new double[] {1.0});
			prop.addInputOutput(new double[] {1.0, 0.0, 1.0}, new double[] {1.0});
			prop.addInputOutput(new double[] {1.0, 0.0, 0.0}, new double[] {1.0});
			prop.addInputOutput(new double[] {0.0, 1.0, 1.0}, new double[] {1.0});
			prop.addInputOutput(new double[] {0.0, 1.0, 0.0}, new double[] {1.0});
			prop.addInputOutput(new double[] {0.0, 0.0, 1.0}, new double[] {1.0});
			prop.addInputOutput(new double[] {0.0, 0.0, 0.0}, new double[] {0.0});

			// run trainer
			int count = 0;
			while (count < 500000) {
				prop.runAndUpdate();
				count++;
			}

			// test and print results
			System.out.println("--------------TEST--------------");
			System.out.println(String.format("Input: 1.0, 1.0, 1.0\tOutput: %f", network.fire(new double[] {1.0, 1.0, 1.0})[0]));
			System.out.println(String.format("Input: 1.0, 1.0, 0.0\tOutput: %f", network.fire(new double[] {1.0, 1.0, 0.0})[0]));
			System.out.println(String.format("Input: 1.0, 0.0, 1.0\tOutput: %f", network.fire(new double[] {1.0, 0.0, 1.0})[0]));
			System.out.println(String.format("Input: 1.0, 0.0, 0.0\tOutput: %f", network.fire(new double[] {1.0, 0.0, 0.0})[0]));
			System.out.println(String.format("Input: 0.0, 1.0, 1.0\tOutput: %f", network.fire(new double[] {0.0, 1.0, 1.0})[0]));
			System.out.println(String.format("Input: 0.0, 1.0, 0.0\tOutput: %f", network.fire(new double[] {0.0, 1.0, 0.0})[0]));
			System.out.println(String.format("Input: 0.0, 0.0, 1.0\tOutput: %f", network.fire(new double[] {0.0, 0.0, 1.0})[0]));
			System.out.println(String.format("Input: 0.0, 0.0, 0.0\tOutput: %f", network.fire(new double[] {0.0, 0.0, 0.0})[0]));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}


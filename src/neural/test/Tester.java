package neural.test;

import neural.net.*;

public class Tester {

    public static void main(String[] args) {
        try {
            Network network = new Network(3, 1, 2);
            BackPropagator prop = new BackPropagator(network, 0.8);

            prop.addInputOutput(new double[] {1.0, 1.0, 1.0}, new double[] {0.0});
            prop.addInputOutput(new double[] {1.0, 1.0, 0.0}, new double[] {1.0});
            prop.addInputOutput(new double[] {1.0, 0.0, 1.0}, new double[] {1.0});
            prop.addInputOutput(new double[] {1.0, 0.0, 0.0}, new double[] {1.0});
            prop.addInputOutput(new double[] {0.0, 1.0, 1.0}, new double[] {1.0});
            prop.addInputOutput(new double[] {0.0, 1.0, 0.0}, new double[] {1.0});
            prop.addInputOutput(new double[] {0.0, 0.0, 1.0}, new double[] {1.0});
            prop.addInputOutput(new double[] {0.0, 0.0, 0.0}, new double[] {0.0});

            int count = 0;
            double error = 1.0;
            while (count < 500000) { //&& Math.abs(error) > 0.0001) {
                error = prop.runAndUpdate();
                count++;
                //System.out.println(String.format("Epoch: %d, Average Error: %f", count, error));
            }

            System.out.println("--------------TEST--------------");
            System.out.println(String.format("Input: 1.0, 1.0, 1.0\tOutput: %f", network.fire(new double[] {1.0, 1.0, 1.0})[0]));
            System.out.println(String.format("Input: 1.0, 1.0, 0.0\tOutput: %f", network.fire(new double[] {1.0, 1.0, 0.0})[0]));
            System.out.println(String.format("Input: 1.0, 0.0, 1.0\tOutput: %f", network.fire(new double[] {1.0, 0.0, 1.0})[0]));
            System.out.println(String.format("Input: 1.0, 0.0, 0.0\tOutput: %f", network.fire(new double[] {1.0, 0.0, 0.0})[0]));
            System.out.println(String.format("Input: 0.0, 1.0, 1.0\tOutput: %f", network.fire(new double[] {0.0, 1.0, 1.0})[0]));
            System.out.println(String.format("Input: 0.0, 1.0, 0.0\tOutput: %f", network.fire(new double[] {0.0, 1.0, 0.0})[0]));
            System.out.println(String.format("Input: 0.0, 0.0, 1.0\tOutput: %f", network.fire(new double[] {0.0, 0.0, 1.0})[0]));
            System.out.println(String.format("Input: 0.0, 0.0, 0.0\tOutput: %f", network.fire(new double[] {0.0, 0.0, 0.0})[0]));

            /*network.save("d:\\NeuralNet.ser");

            Network work = Network.load("d:\\NeuralNet.ser");
            System.out.println("\n--------------TEST LOAD--------------");
            System.out.println(String.format("Input: 1.0, 1.0, 1.0\tOutput: %f", work.fire(new double[] {1.0, 1.0, 1.0})[0]));
            System.out.println(String.format("Input: 1.0, 1.0, 0.0\tOutput: %f", work.fire(new double[] {1.0, 1.0, 0.0})[0]));
            System.out.println(String.format("Input: 1.0, 0.0, 1.0\tOutput: %f", work.fire(new double[] {1.0, 0.0, 1.0})[0]));
            System.out.println(String.format("Input: 1.0, 0.0, 0.0\tOutput: %f", work.fire(new double[] {1.0, 0.0, 0.0})[0]));
            System.out.println(String.format("Input: 0.0, 1.0, 1.0\tOutput: %f", work.fire(new double[] {0.0, 1.0, 1.0})[0]));
            System.out.println(String.format("Input: 0.0, 1.0, 0.0\tOutput: %f", work.fire(new double[] {0.0, 1.0, 0.0})[0]));
            System.out.println(String.format("Input: 0.0, 0.0, 1.0\tOutput: %f", work.fire(new double[] {0.0, 0.0, 1.0})[0]));
            System.out.println(String.format("Input: 0.0, 0.0, 0.0\tOutput: %f", work.fire(new double[] {0.0, 0.0, 0.0})[0]));*/

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

package NeuralNet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Network implements Serializable {
    //    stores all a layers
    List<HiddenLayer> hiddenLayers;

    //    initial constructor
    public Network() {
        hiddenLayers = new ArrayList<>();
    }

    //    adds layers to network
    public void add(HiddenLayer hiddenLayer) {
        hiddenLayers.add(hiddenLayer);
    }

    //    forward propagation to get utility values after training
    public List<Double> predict(int[] X) {
        Matrix output = Matrix.arrayToMatrix(X);
        for (HiddenLayer hl : hiddenLayers) {
            output = hl.forwardPropagation(output);
        }
        return output.toArray();

    }

    public List<Double> predictPartial(double[] X) {
        Matrix output = Matrix.arrayToMatrix(X);
        for (HiddenLayer hl : hiddenLayers) {
            output = hl.forwardPropagation(output);
        }
        return output.toArray();

    }

    //    fit/train
    public void fit(int[][] states, double[][] values, int epochs, double learningRate) {
        long total = System.nanoTime();
        for (int x = 0; x < epochs; x++) {
            double error = 0.0;
            for (int y = 0; y < states.length; y++) {
//                initializes output
                Matrix output = Matrix.arrayToMatrix(states[y]);
//                forward propagation through all the hidden layers
                for (HiddenLayer hiddenLayer : hiddenLayers) {
                    output = hiddenLayer.forwardPropagation(output);
                }
//                calculates error for display
                error += Loss.mse(values[y], output.toArray());

//                calculates gradient for back propagation
                Matrix gradient = Loss.gradient(Matrix.arrayToMatrix(values[y]), output);
//                  back propagation through all layers in reverse
                for (int i = hiddenLayers.size() - 1; i >= 0; i--) {
                    HiddenLayer hiddenLayer = hiddenLayers.get(i);
                    gradient = hiddenLayer.backwardPropagation(gradient, learningRate);
                }

            }
            error /= states.length;

            long endTime = System.nanoTime();
            long duration = (endTime - total) / (long) Math.pow(10, 9);
            System.out.println("Epoch: " + x + "; error: " + error + "; Time: " + duration);
        }
    }


    public void fitPartial(double[][] states, double[][] values, int epochs, double learningRate) {
        long total = System.nanoTime();
        for (int x = 0; x < epochs; x++) {

            double error = 0.0;
            for (int y = 0; y < states.length; y++) {
//                initializes output
                Matrix output = Matrix.arrayToMatrix(states[y]);
//                forward propagation through all the layers
                for (HiddenLayer hiddenLayer : hiddenLayers) {
                    output = hiddenLayer.forwardPropagation(output);
                }
//                calculates error for display
                error += Loss.mse(values[y], output.toArray());

//                calculates gradient for back propagation
                Matrix gradient = Loss.gradient(Matrix.arrayToMatrix(values[y]), output);
//                  back propagation through all layers in reverse
                for (int i = hiddenLayers.size() - 1; i >= 0; i--) {
                    HiddenLayer hiddenLayer = hiddenLayers.get(i);
                    gradient = hiddenLayer.backwardPropagation(gradient, learningRate);
                }

            }
            error /= states.length;

            long endTime = System.nanoTime();
            long duration = (endTime - total) / (long) Math.pow(10, 9);
            System.out.println("Epoch: " + x + "; error: " + error + "; Time: " + duration);
        }
    }
}

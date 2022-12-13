package NeuralNet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Network implements Serializable {
    //    stores all a layers
    List<Layers> layers;

    //    initial constructor
    public Network() {
        layers = new ArrayList<>();
    }

    //    adds layers to network
    public void add(Layers layer) {
        layers.add(layer);
    }

    //    forward propagation to get utility values after training
    public List<Double> predict(int[] X) {
        Matrix output = Matrix.arrayToMatrix(X);
        for (Layers l : layers) {
            output = l.forwardPropagation(output);
        }
        return output.toArray();

    }

    //    fit/train
    public void fit(int[][] X, double[][] Y, int epochs, double learningRate) {
        for (int x = 0; x < epochs; x++) {
            long total = System.nanoTime();
            double error = 0.0;
            for (int y = 0; y < X.length; y++) {
//                initializes output
                Matrix output = Matrix.arrayToMatrix(X[y]);
//                forward propagation through all the layers
                for (Layers layer : layers) {
                    output = layer.forwardPropagation(output);
                }
//                calculates error for display
                error += mse.mse(Y[y], output.toArray());

//                calculates error for back propagation
                Matrix errorPrime = mse.mse_prime(Matrix.arrayToMatrix(Y[y]), output);
//                  back propagation through all layers
                for (int i = layers.size() - 1; i >= 0; i--) {
                    Layers layer = layers.get(i);
                    errorPrime = layer.backwardPropagation(errorPrime, learningRate);
                }

            }
            error /= X.length;

            long endTime = System.nanoTime();
            long duration = (endTime - total) / (long) Math.pow(10, 9);
            System.out.println("Epoch: " + x + "; error: " + error + "; Time: " + duration);
        }
    }
}

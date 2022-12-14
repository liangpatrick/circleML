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
    public void fit(int[][] X, double[][] Y, int epochs, double learningRate) {
        for (int x = 0; x < epochs; x++) {
            long total = System.nanoTime();
            double error = 0.0;
            for (int y = 0; y < X.length; y++) {
//                initializes output
                Matrix output = Matrix.arrayToMatrix(X[y]);
//                forward propagation through all the hidden layers
                for (HiddenLayer hiddenLayer : hiddenLayers) {
                    output = hiddenLayer.forwardPropagation(output);
                }
//                calculates error for display
                error += Loss.mse(Y[y], output.toArray());

//                calculates error for back propagation
                Matrix errorPrime = Loss.gradient(Matrix.arrayToMatrix(Y[y]), output);
//                errorPrime.print();
//                  back propagation through all layers
                for (int i = hiddenLayers.size() - 1; i >= 0; i--) {
                    HiddenLayer hiddenLayer = hiddenLayers.get(i);
                    errorPrime = hiddenLayer.backwardPropagation(errorPrime, learningRate);
                }

            }
            error /= X.length;

            long endTime = System.nanoTime();
            long duration = (endTime - total) / (long) Math.pow(10, 9);
            System.out.println("Epoch: " + x + "; error: " + error + "; Time: " + duration);
        }
    }


    public void fitPartial(double[][] X, double[][] Y, int epochs, double learningRate) {
        for (int x = 0; x < epochs; x++) {
            long total = System.nanoTime();
            double error = 0.0;
            for (int y = 0; y < X.length; y++) {
//                initializes output
                Matrix output = Matrix.arrayToMatrix(X[y]);
//                forward propagation through all the layers
                for (HiddenLayer hiddenLayer : hiddenLayers) {
                    output = hiddenLayer.forwardPropagation(output);
                }
//                calculates error for display
                error += Loss.mse(Y[y], output.toArray());

//                calculates error for back propagation
                Matrix errorPrime = Loss.gradient(Matrix.arrayToMatrix(Y[y]), output);
//                errorPrime.print();
//                  back propagation through all layers
                for (int i = hiddenLayers.size() - 1; i >= 0; i--) {
                    HiddenLayer hiddenLayer = hiddenLayers.get(i);
                    errorPrime = hiddenLayer.backwardPropagation(errorPrime, learningRate);
                }

            }
            error /= X.length;

            long endTime = System.nanoTime();
            long duration = (endTime - total) / (long) Math.pow(10, 9);
            System.out.println("Epoch: " + x + "; error: " + error + "; Time: " + duration);
        }
    }
}

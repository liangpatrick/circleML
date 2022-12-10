package NeuralNet;

import java.util.ArrayList;
import java.util.List;

public class Network {
    List<Layers> layers;
    public Network(){
        layers = new ArrayList<>();
    }
    public void add(Layers layer){
        layers.add(layer);
    }
    public List<Double> predict(int[] X){
        Matrix output = Matrix.fromArray(X);
        for(Layers l: layers){
            output = l.forward_propagation(output);
        }
        return output.toArray();

    }
    public void fit(int[][] X, double[][] Y, int epochs, double learning_rate) {

        for(int x = 0; x < epochs; x++){
            long total = System.nanoTime();

            for(int y = 0; y < X.length; y++) {
                Matrix output = Matrix.fromArray(X[y]);
                for (Layers layer : layers) {
                    output = layer.forward_propagation(output);
                }
                Matrix error = mse.mse_prime(Matrix.fromArray(Y[y]), output);
                for (Layers layer : layers) {
                    error = layer.backward_propagation(error, learning_rate);
                }
            }

            long endTime = System.nanoTime();
            long duration = (endTime - total) / (long) Math.pow(10, 9);
            System.out.println("Epoch: " + x + "; Time: " + duration);
        }
    }
}

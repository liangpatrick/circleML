package NeuralNet;

import java.io.Serializable;
import java.util.*;

public class NeuralNetwork extends HiddenLayers implements Serializable {


    double l_rate = 0.0001;

    List<HiddenLayers> layers = new ArrayList<>();

    public NeuralNetwork(int fcLayers, int naLayers, int i, int h, int o) {
        for (int x = 0; x < fcLayers; x++) {
            layers.add(new HiddenLayers(i, i, i, true));
        }
        for (int x = 0; x < naLayers; x++) {
            layers.add(new HiddenLayers(i, o, o, false));
        }

    }

    public List<Double> predict(int[] X) {

//        Matrix output = Matrix.fromArray(X);
//        for (HiddenLayers hl : layers) {
//            output = Matrix.multiply(output, hl.weights_ho);
//            output.add(hl.bias_h);
//            if (hl.activation)
//                output.tanh();
//        }

        Matrix output = Matrix.fromArray(X);
        int count = 0;
        for (HiddenLayers hl : layers) {
            hl.setInput(output);
            if(count++ == 0)
                output = Matrix.multiply(hl.weights_ih, output);
            else
                output = Matrix.multiply(hl.weights_ho, output);
            output.add(hl.bias_h);
//            System.out.println("fp add");
            if (hl.activation)
                output.tanh();
        }


        return output.toArray();


    }


    public void train(int[] X, double[] Y) {
        Matrix output = Matrix.fromArray(X);
        int count = 0;
        for (HiddenLayers hl : layers) {
//            System.out.println(hl.weights_ih.rows + ", " + hl.weights_ih.cols);
            hl.setInput(output);
            if(count++ == 0)
                output = Matrix.multiply(hl.weights_ih, output);
            else
                output = Matrix.multiply(hl.weights_ho, output);
            output.add(hl.bias_h);
//            System.out.println("fp add");
            if (hl.activation) {
                output.tanh();
            }
        }
        output.print();
        count = 0;

//        Matrix target = Matrix.fromArray(Y);

//        Matrix error = Matrix.subtract(target, output);
        Matrix error = mse.mse_prime(Matrix.fromArray(Y), output);
//        Matrix gradient = output.dtanh();
//
//        gradient.multiply(error);
//        gradient.multiply(l_rate);
        System.out.println(error.rows + ", " +error.cols);
        for (int x = layers.size()-1; x >= 0; x--) {

            HiddenLayers hl = layers.get(x);

            System.out.println(hl.getInput().rows + " , " + hl.getInput().cols);
            System.out.println(error.rows + ",,,, " +error.cols);
            Matrix initError = Matrix.multiply(error, Matrix.transpose(hl.weights_ho));
            Matrix weightsError;
            if(count++ == 0) {
                weightsError = Matrix.multiply(hl.getInput(), error);
            }else{
                weightsError = Matrix.multiply(error, Matrix.transpose(hl.getInput()));
            }

            System.out.println(weightsError.rows + " ,,,," + weightsError.cols);
            System.out.println(hl.weights_ih.rows + " ,,, " + hl.weights_ih.cols);
            weightsError.multiply(l_rate);
            weightsError.multiply(-1);
            hl.weights_ih.add(weightsError);
            System.out.println("bp add 1");
            error.multiply(l_rate);
            error.multiply(-1);
            hl.bias_h.add(error);
            System.out.println("bp add 2");
            if(hl.activation) {
                initError.dtanh().multiply(error);

            }
            error = initError;
        }
        error.print();
//        Matrix hidden_T = Matrix.transpose(hidden);
//        Matrix who_delta =  Matrix.multiply(gradient, hidden_T);
//
//        weights_ho.add(who_delta);
//        bias_o.add(gradient);
//
//        Matrix who_T = Matrix.transpose(weights_ho);
//        Matrix hidden_errors = Matrix.multiply(who_T, error);
//
//        Matrix h_gradient = hidden.dsigmoid();
//        h_gradient.multiply(hidden_errors);
//        h_gradient.multiply(l_rate);
//
//        Matrix i_T = Matrix.transpose(input);
//        Matrix wih_delta = Matrix.multiply(h_gradient, i_T);
//
//        weights_ih.add(wih_delta);
//        bias_h.add(h_gradient);
    }

    public void fit(int[][] X, double[][] Y, int epochs) {
        long total = System.nanoTime();

        int samples = X.length;
        for (int i = 0; i < epochs; i++) {
            for (int j = 0; j < samples; j++) {
//                int sampleN = (int) (Math.random() * X.length);

                this.train(X[j], Y[j]);
            }
            long endTime = System.nanoTime();
            long duration = (endTime - total) / (long) Math.pow(10, 9);
            System.out.println("Epoch: " + i + "; Time: " + duration);
        }

    }


}

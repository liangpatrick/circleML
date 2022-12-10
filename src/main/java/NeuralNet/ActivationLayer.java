package NeuralNet;

import java.util.List;

public class ActivationLayer extends Layers {
    static String activation;
    static String activation_prime;

    public ActivationLayer() {
        this.activation = activation;
        this.activation_prime = activation_prime;
    }

    //    # returns the activated input
    public Matrix forward_propagation(Matrix input_m) {
        input = input_m;
        input.tanh();
        output = input;
        return output;
    }

    //    Returns input_error=dE/dX for a given output_error=dE/dY.
//    learning_rate is not used because there is no "learnable" parameters.
    public Matrix backward_propagation(Matrix output_error, double learning_rate) {
        input.dtanh();
        input.rowMultiply(output_error);
        return input;
    }

}

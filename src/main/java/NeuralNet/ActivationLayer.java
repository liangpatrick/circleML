package NeuralNet;

import java.util.List;

public class ActivationLayer extends Layers {

    //    applies activation function
    public Matrix forwardPropagation(Matrix input_m) {
        input = input_m;
        input.tanh();
        output = input;
        return output;
    }

    //    applies activation function and updates input
    public Matrix backwardPropagation(Matrix output_error, double learningRate) {
        input.dtanh();
        input.rowMultiply(output_error);
        return input;
    }

}

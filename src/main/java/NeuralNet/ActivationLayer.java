package NeuralNet;

import java.io.Serializable;
import java.util.List;

public class ActivationLayer extends Layers implements Serializable {

    //    applies activation function
    public Matrix forwardPropagation(Matrix input_m) {
        this.input = input_m;
        this.output = this.input.tanh();
        return this.output;
    }

    //    applies activation function and updates input
    public Matrix backwardPropagation(Matrix output_error, double learningRate) {
        Matrix temp = this.input.dtanh();
        temp.rowMultiply(output_error);
        return temp;
    }

}

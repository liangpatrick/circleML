package NeuralNet;


import java.util.List;

public class FCLayer extends Layers{

    static Matrix weights;
    static Matrix bias;


//   input_size = number of input neurons
//   output_size = number of output neurons

    public FCLayer (int input_size, int output_size) {
        this.weights = new Matrix(input_size, output_size);
        weights.add(-0.5);
        this.bias = new Matrix(1, output_size);
        bias.add(-0.5);
    }

//   returns output for a given input
    public Matrix forward_propagation(Matrix input_m) {
        input = input_m;
        output = Matrix.multiply(input, weights);
        output.rowAdd(bias);
        return output;
    }

//    # computes dE/dW, dE/dB for a given output_error=dE/dY. Returns input_error=dE/dX.
    public Matrix backward_propagation(Matrix output_error, double learning_rate) {
        Matrix input_error = Matrix.multiply(output_error, Matrix.transpose(weights));
        Matrix weights_error = Matrix.multiply(Matrix.transpose(input), output_error);

//            dBias = output_error

//        update parameters
        weights_error.multiply(learning_rate);
        output_error.multiply(learning_rate);
        weights.rowSubtract(weights_error);
        bias.rowSubtract(output_error);
        return input_error;
    }





}

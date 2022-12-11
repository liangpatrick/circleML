package NeuralNet;


import java.util.List;

public class FCLayer extends Layers {

    Matrix weights;
    Matrix bias;

    //  Constructor to initialize fully connected layer
    public FCLayer(int inputSize, int outputSize) {
//        arbitrary starting weights and arbitrary starting bias
        this.weights = new Matrix(inputSize, outputSize);
        weights.add(-0.5);
        this.bias = new Matrix(1, outputSize);
        bias.add(-0.5);
    }

    //   performs forward pass without activation function
    public Matrix forwardPropagation(Matrix input_m) {
        input = input_m;
        output = Matrix.multiply(input, weights);
        output.rowAdd(bias);
        return output;
    }

    //   performs packwards pass without activation function
    public Matrix backwardPropagation(Matrix error, double learningRate) {

//        calculates dE/dW and dE/dB for a given error
        Matrix inputError = Matrix.multiply(error, Matrix.transpose(weights));
        Matrix weightsError = Matrix.multiply(Matrix.transpose(input), error);
//        update parameters
        weightsError.multiply(learningRate);
        error.multiply(learningRate);
//      updates weights/bias
        weights.subtract(weightsError);
        bias.subtract(error);

        return inputError;
    }


}

package NeuralNet;

import java.io.Serializable;

public class HiddenLayer extends Layers implements Serializable {
    Matrix weights;
    Matrix bias;
    boolean activation;

    //  Constructor to initialize fully connected layer
    public HiddenLayer(int inputSize, int outputSize, boolean activation) {
//        arbitrary starting weights and arbitrary starting bias
        this.weights = new Matrix(inputSize, outputSize);
//        weights.add(-0.5);
        this.bias = new Matrix(1, outputSize);
//        bias.add(-0.5);
        this.activation = activation;
    }

    //   performs forward pass without activation function
    public Matrix forwardPropagation(Matrix input_m) {
        this.input = input_m;
        this.output = Matrix.multiply(this.input, this.weights);
        this.output.rowAdd(this.bias);

        this.input = this.output;
        this.output = this.input.tanh();
        return this.output;
    }

    //   performs packwards pass without activation function
    public Matrix backwardPropagation(Matrix error, double learningRate) {

//        calculates dE/dW and dE/dB for a given error
        Matrix inputError = Matrix.multiply(error, Matrix.transpose(this.weights));
        Matrix weightsError = Matrix.multiply(Matrix.transpose(this.input), error);
//        update parameters
//        weightsError.multiply(learningRate);
//        error.multiply(learningRate);
//      updates weights/bias

        this.weights.subtract(Matrix.multiply(weightsError, learningRate));
        this.bias.subtract(Matrix.multiply(error, learningRate));


        Matrix temp = this.input.dtanh();
        temp.rowMultiply(inputError);
        return temp;


//        return inputError;
    }
}

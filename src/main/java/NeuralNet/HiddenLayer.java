package NeuralNet;

import java.io.Serializable;

public class HiddenLayer extends Matrix /*Layers*/ implements Serializable {
    Matrix weights;
    Matrix bias;
    Matrix input;
    Matrix output;
    boolean activation;

    //  Constructor to initialize fully connected layer
    public HiddenLayer(int inputSize, int outputSize, boolean activation) {
        this.input = null;
        this.output = null;
//        arbitrary starting weights and arbitrary starting bias
        this.weights = new Matrix(inputSize, outputSize);
        this.bias = new Matrix(1, outputSize);
        this.activation = activation;
    }

    //   performs forward pass without activation function
    public Matrix forwardPropagation(Matrix input_m) {
        this.input = input_m;
        this.output = Matrix.multiply(this.input, this.weights);
        this.output.rowAdd(this.bias);
        if (this.activation)
            this.output = this.output.tanh();
        return this.output;
    }

    public Matrix backwardPropagation(Matrix inputError, double learningRate) {

//        calculates dE/dW and dE/dB for a given error
        Matrix error = Matrix.multiply(inputError, Matrix.transpose(this.weights));
        Matrix weightsError = Matrix.multiply(Matrix.transpose(this.input), inputError);

        //      updates weights/bias
        this.weights.subtract(Matrix.multiply(weightsError, learningRate));
        this.bias.subtract(Matrix.multiply(inputError, learningRate));

        if (this.activation) {
            Matrix temp = this.input.dtanh();
            temp.rowMultiply(error);
            return temp;
        } else {
            return error;
        }
    }

}

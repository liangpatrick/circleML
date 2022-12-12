package NeuralNet;


import java.io.Serializable;
import java.util.List;

public class FCLayer extends Layers implements Serializable {

    Matrix weights;
    Matrix bias;

    //  Constructor to initialize fully connected layer
    public FCLayer(int inputSize, int outputSize) {
//        arbitrary starting weights and arbitrary starting bias
        this.weights = new Matrix(inputSize, outputSize);
        this.bias = new Matrix(1, outputSize);
    }

    //   performs forward pass without activation function
    public Matrix forwardPropagation(Matrix input_m) {
        this.input = input_m;
        this.output = Matrix.multiply(this.input, this.weights);
        this.output.rowAdd(this.bias);
        return this.output;
    }

    //   performs packwards pass without activation function
    public Matrix backwardPropagation(Matrix error, double learningRate) {

//        calculates dE/dW and dE/dB for a given error
        Matrix inputError = Matrix.multiply(error, Matrix.transpose(this.weights));
        Matrix weightsError = Matrix.multiply(Matrix.transpose(this.input), error);
//        update parameters
        if(!(weightsError.rows == this.weights.rows && weightsError.cols == this.weights.cols))
            System.out.println("oh no");
//      updates weights/bias

        this.weights.subtract(Matrix.multiply(weightsError, learningRate));
        this.bias.subtract(Matrix.multiply(error, learningRate));

        return inputError;
    }


}

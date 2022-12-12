package NeuralNet;

import org.apache.commons.lang3.NotImplementedException;

import java.io.Serializable;

// Parent class
public class Layers extends Matrix implements Serializable {

    public Matrix input;
    public Matrix output;

    //    initial constructor
    public Layers() {
        this.input = null;
        this.output = null;
    }


    //    parent method that throws an exception if its not implemented
    public Matrix forwardPropagation(Matrix input_m) {
        throw new NotImplementedException("NotImplemented");
    }

    //    parent method that throws an exception if its not implemented
    public Matrix backwardPropagation(Matrix output_error, double learningRate) {
        throw new NotImplementedException("NotImplemented");
    }
}

package NeuralNet;
import org.apache.commons.lang3.NotImplementedException;
import java.util.ArrayList;
import java.util.List;

public class Layers extends Matrix {
    public static Matrix input;
    public static Matrix output;
    public Layers(){
        this.input = null;
        this.output = null;
    }


//    computes the output Y of a layer for a given input X
    public Matrix forward_propagation(Matrix input_m) {
        throw new NotImplementedException("NotImplemented");
    }

//    computes dE/dX for a given dE/dY (and update parameters if any)
    public Matrix backward_propagation(Matrix output_error, double learning_rate) {
        throw new NotImplementedException("NotImplemented");
    }
}

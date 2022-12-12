package NeuralNet;

import java.io.Serializable;
import java.util.List;

public class ActivationLayer extends Layers implements Serializable {

    //    applies activation function
    public Matrix forwardPropagation(Matrix input_m) {
        this.input = input_m;
        this.output = this.input.tanh();
        for(int x = 0; x < this.input.rows; x++){
            if(this.input.m[0][x] == this.output.m[0][x])
                System.out.println("no way");
        }
        return this.output;
    }

    //    applies activation function and updates input
    public Matrix backwardPropagation(Matrix output_error, double learningRate) {
        if(output_error.rows != 1 || output_error.cols != 150)
            System.out.println(output_error.rows + ", " + output_error.cols);
        Matrix temp = this.input.dtanh();
        temp.rowMultiply(output_error);
        return temp;
    }

}

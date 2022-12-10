package NeuralNet;


import java.util.List;

public class mse {


    public static Matrix mse_prime(Matrix y_true, Matrix y_pred) {
//        (Matrix.subtract(y_pred, y_true)).multiply(2);
//        double[] temp = {2*(y_pred.get(0) - y_true[0]) / y_true.length};
        y_pred.rowSubtract(y_true);
        Matrix temp = y_pred;
        temp.multiply(2);
        temp.divide(y_true.rows*y_true.cols);
        return temp;
    }
}

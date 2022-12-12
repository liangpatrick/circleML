package NeuralNet;


import java.io.Serializable;
import java.util.List;

public class mse implements Serializable {

    //    calculates error for visualization
    public static double mse(double[] y_true, List<Double> y_pred) {
        if(y_true.length != y_pred.size()){
            System.out.println("wut");
        }
        int n = y_true.length;
        double sum = 0.0;
        double diff;
        for (int x = 0; x < n; x++) {
            diff = y_true[x] - y_pred.get(x);
            sum += Math.pow(diff, 2);
        }
        double mse = sum / n;
        return mse;
    }

    //   calculates error for back propagation init
    public static Matrix mse_prime(Matrix y_true, Matrix y_pred) {
        if(!(y_true.rows == y_pred.rows && y_pred.cols == y_true.cols)){
            System.out.println("huh");
        }
        y_pred.subtract(y_true);
        Matrix temp = Matrix.multiply(y_pred, 2);
        double size = y_true.rows * y_true.cols;
        temp.divide(size);
        return temp;
    }
}

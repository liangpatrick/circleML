package NeuralNet;


import java.util.List;

public class mse {

    //    calculates error for visualization
    public static double mse(double[] y_true, List<Double> y_pred) {
        int n = y_true.length;
        double sum = 0.0;
        double diff;
        for (int i = 0; i < n; i++) {
            diff = y_true[i] - y_pred.get(i);
            sum = sum + diff * diff;
        }
        double mse = sum / n;
        return mse;
    }

    //   calculates error for back propagation init
    public static Matrix mse_prime(Matrix y_true, Matrix y_pred) {
        y_pred.subtract(y_true);
        Matrix temp = y_pred;
        temp.multiply(2);
        double size = y_true.rows * y_true.cols;
        temp.divide(size);
        return temp;
    }
}

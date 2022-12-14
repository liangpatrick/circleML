package NeuralNet;


import java.io.Serializable;
import java.util.List;

public class Loss implements Serializable {

    //    calculates error for visualization
    public static double mse(double[] original, List<Double> prediction) {
        int n = original.length;
        double sum = 0.0;
        double diff;
        for (int x = 0; x < n; x++) {
            diff = original[x] - prediction.get(x);
            sum += Math.pow(diff, 2);
        }
        double mse = sum / n;
        return mse;
    }

    //   calculates error for back propagation init
    public static Matrix gradient(Matrix original, Matrix prediction) {
//        2/n * (orig - prediction)
        prediction.subtract(original);
        Matrix temp = Matrix.multiply(prediction, 2);
        double size = original.rows * original.cols;
        temp.divide(size);
        return temp;
    }
}

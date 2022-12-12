package NeuralNet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Matrix implements Serializable {
    double[][] m;
    public int rows, cols;

    //    basic constructor
    public Matrix() {
        m = null;
    }

    //    constructor that randomizes matrix
    public Matrix(int rows, int cols) {
        m = new double[rows][cols];
        this.rows = rows;
        this.cols = cols;
        for (int x = 0; x < rows; x++)
            for (int y = 0; y < cols; y++) {
                m[x][y] = Math.random() - 0.5;
            }

    }

    //    used for debugging
    public void print() {
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                System.out.print(this.m[x][y] + " ");
            }
            System.out.println();
        }
    }

    //  used when number of rows doesn't match but columns does
    public void rowAdd(Matrix m) {
        for (int x = 0; x < rows; x++)
            for (int y = 0; y < cols; y++) {
                this.m[x][y] += m.m[0][y];
            }

    }

    //    used to calculate average
    public void divide(double d) {
        for (int x = 0; x < rows; x++)
            for (int y = 0; y < cols; y++) {
                this.m[x][y] /= d;
            }

    }


    //  used when number of rows doesn't match but columns does
    public void rowMultiply(Matrix m) {
        for (int x = 0; x < rows; x++)
            for (int y = 0; y < cols; y++) {
                this.m[x][y] *= m.m[0][y];
            }
    }



    //  add a double
    public void add(double d) {
        for (int x = 0; x < rows; x++)
            for (int y = 0; y < cols; y++) {
                this.m[x][y] += d;
            }
    }




    //    transforms array to matrix
    public static Matrix arrayToMatrix(double[] arr) {
        Matrix temp = new Matrix(1, arr.length);
        for (int x = 0; x < arr.length; x++)
            temp.m[0][x] = arr[x];
        return temp;

    }

    //    transforms array to matrix but for int arrays
    public static Matrix arrayToMatrix(int[] arr) {
        Matrix temp = new Matrix(1, arr.length);
        for (int x = 0; x < arr.length; x++)
            temp.m[0][x] = arr[x];
        return temp;

    }

    //    transforms matrix to list
    public List<Double> toArray() {
        List<Double> temp = new ArrayList<>();

        for (int x = 0; x < rows; x++)
            for (int y = 0; y < cols; y++) {
                temp.add(m[x][y]);
            }

        return temp;
    }

    //    subtract
    public void subtract(Matrix a) {
        for (int x = 0; x < rows; x++)
            for (int y = 0; y < cols; y++) {
                this.m[x][y] -= a.m[x][y];
            }
    }

    //    transpose matrix
    public static Matrix transpose(Matrix m) {
        Matrix temp = new Matrix(m.cols, m.rows);
        for (int x = 0; x < m.rows; x++)
            for (int y = 0; y < m.cols; y++) {
                temp.m[y][x] = m.m[x][y];
            }

        return temp;
    }

    //    multiply two matrices together
    public static Matrix multiply(Matrix m1, Matrix m2) {
        Matrix temp = new Matrix(m1.rows, m2.cols);
        for (int x = 0; x < temp.rows; x++) {
            for (int y = 0; y < temp.cols; y++) {
                double sum = 0;
//                dot product
                for (int z = 0; z < m1.cols; z++) {
                    sum += m1.m[x][z] * m2.m[z][y];
                }
                temp.m[x][y] = sum;
            }
        }
        return temp;
    }


    //    multiply by double
    public static Matrix multiply(Matrix m, double d) {
        Matrix temp = new Matrix(m.rows, m.cols);
        for (int x = 0; x < temp.rows; x++)
            for (int y = 0; y < temp.cols; y++) {
                temp.m[x][y] = m.m[x][y] * d;
            }

        return temp;
    }

    //    activation function
    public Matrix tanh() {
        Matrix temp = new Matrix(rows, cols);

        for (int x = 0; x < rows; x++)
            for (int y = 0; y < cols; y++) {
                temp.m[x][y] = Math.tanh(this.m[x][y]);
            }

        return temp;
    }

    //    derivative of activation function
    public Matrix dtanh() {

        Matrix temp = new Matrix(rows, cols);

        for (int x = 0; x < rows; x++)
            for (int y = 0; y < cols; y++) {
                double tanh = Math.tanh(this.m[x][y]);

                temp.m[x][y] = 1 - (Math.pow(tanh, 2));
            }

        return temp;
    }


}

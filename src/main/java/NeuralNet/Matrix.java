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
//                m[i][j] = Math.random();
                m[x][y] = Math.random() * 2 - 1;
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

//    public void rowSubtract(Matrix m) {
//        for (int i = 0; i < rows; i++) {
//            for (int j = 0; j < cols; j++) {
//                this.m[i][j] -= m.m[0][j];
//            }
//        }
//    }
//
//    public void columnSubtract(Matrix m) {
//        for (int i = 0; i < rows; i++) {
//            for (int j = 0; j < cols; j++) {
//                this.m[i][j] -= m.m[j][0];
//            }
//        }
//    }

    //  used when number of rows doesn't match but columns does
    public void rowMultiply(Matrix m) {
        for (int x = 0; x < rows; x++)
            for (int y = 0; y < m.cols; y++) {
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


//    public void add(Matrix m) {
//        if (cols != m.cols || rows != m.rows) {
//            System.out.println("Shape Mismatch");
//            return;
//        }
//
//        for (int i = 0; i < rows; i++) {
//            for (int j = 0; j < cols; j++) {
//                this.m[i][j] += m.m[i][j];
//            }
//        }
//    }

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

//    public void multiply(Matrix a) {
//        for (int i = 0; i < a.rows; i++) {
//            for (int j = 0; j < a.cols; j++) {
//                this.m[i][j] *= a.m[i][j];
//            }
//        }
//
//    }

    //    multiply by double
    public void multiply(double d) {
        for (int x = 0; x < rows; x++)
            for (int y = 0; y < cols; y++) {
                this.m[x][y] *= d;
            }


    }

    //    activation function
    public void tanh() {
        for (int x = 0; x < rows; x++)
            for (int y = 0; y < cols; y++) {
                this.m[x][y] = Math.tanh(this.m[x][y]);
            }


    }

    //    derivative of activation function
    public void dtanh() {

        for (int x = 0; x < rows; x++)
            for (int y = 0; y < cols; y++) {
                double tanh = Math.tanh(this.m[x][y]);
                this.m[x][y] = 1 - (Math.pow(tanh, 2));
            }


    }


}

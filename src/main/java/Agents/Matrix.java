package Agents;

import java.util.ArrayList;
import java.util.List;

public class Matrix {
    double[][] m;
    int rows, cols;

    public Matrix(int rows, int cols) {
        m = new double[rows][cols];
        this.rows = rows;
        this.cols = cols;
        for (int x = 0; x < rows; x++)
            for (int y = 0; y < cols; y++)
                m[x][y] = Math.random() * 2 - 1;

    }

    public void print() {
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++)
                System.out.print(this.m[x][y] + " ");
            System.out.println();
        }
    }

    public void add(int scaler) {
        for (int x = 0; x < rows; x++)
            for (int y = 0; y < cols; y++)
                this.m[x][y] += scaler;

    }

    public void add(Matrix mat) {
        if (rows != mat.rows || cols != mat.cols) {
            System.out.println("Shape Mismatch");
            return;
        }

        for (int x = 0; x < rows; x++)
            for (int y = 0; y < cols; y++)
                this.m[x][y] += mat.m[x][y];

    }

    public static Matrix fromArray(double[] arr) {
        Matrix temp = new Matrix(arr.length, 1);
        for (int x = 0; x < arr.length; x++)
            temp.m[x][0] = arr[x];
        return temp;

    }

    public List<Double> toArray() {
        List<Double> temp = new ArrayList<Double>();

        for (int x = 0; x < rows; x++)
            for (int y = 0; y < cols; y++)
                temp.add(m[x][y]);

        return temp;
    }

    public static Matrix subtract(Matrix mat1, Matrix mat2) {
        Matrix temp = new Matrix(mat1.rows, mat1.cols);
        for (int x = 0; x < mat1.rows; x++)
            for (int y = 0; y < mat1.cols; y++)
                temp.m[x][y] = mat1.m[x][y] - mat2.m[x][y];

        return temp;
    }

    public static Matrix transpose(Matrix mat) {
        Matrix temp = new Matrix(mat.cols, mat.rows);
        for (int x = 0; x < mat.rows; x++)
            for (int y = 0; y < mat.cols; y++)
                temp.m[y][x] = mat.m[x][y];


        return temp;
    }

    public static Matrix multiply(Matrix mat1, Matrix mat2) {
        Matrix temp = new Matrix(mat1.rows, mat2.cols);
        for (int x = 0; x < temp.rows; x++) {
            for (int y = 0; y < temp.cols; y++) {
                double sum = 0;
                for (int z = 0; z < mat1.cols; z++) {
                    sum += mat1.m[x][z] * mat2.m[z][y];
                }
                temp.m[x][y] = sum;
            }
        }
        return temp;
    }

    public void multiply(Matrix mat) {
        for (int x = 0; x < mat.rows; x++) {
            for (int y = 0; y < mat.cols; y++) {
                this.m[x][y] *= mat.m[x][y];
            }
        }

    }

    public void multiply(double d) {
        for (int x = 0; x < rows; x++)
            for (int y = 0; y < cols; y++)
                this.m[x][y] *= d;


    }

    public void sigmoid() {
        for (int x = 0; x < rows; x++)
            for (int y = 0; y < cols; y++)
                this.m[x][y] = 1 / (1 + Math.exp(-this.m[x][y]));


    }

    public Matrix dsigmoid() {
        Matrix temp = new Matrix(rows, cols);
        for (int x = 0; x < rows; x++)
            for (int y = 0; y < cols; y++)
                temp.m[x][y] = this.m[x][y] * (1 - this.m[x][y]);

        return temp;

    }
}	
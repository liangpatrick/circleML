package NeuralNet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Matrix implements Serializable {
    public double [][]m;
    public int rows,cols;

    public Matrix(){
        m = null;
//        rows = -1;
//        cols = -1;
    }
    public Matrix(int rows,int cols) {
        m= new double[rows][cols];
        this.rows=rows;
        this.cols=cols;
        for(int i=0;i<rows;i++)
        {
            for(int j=0;j<cols;j++)
            {
                m[i][j]=Math.random()*2-1;
            }
        }
    }

    public void print()
    {
        for(int i=0;i<rows;i++)
        {
            for(int j=0;j<cols;j++)
            {
                System.out.print(this.m[i][j]+" ");
            }
            System.out.println();
        }
    }


    public void rowAdd(Matrix m){
        for(int i=0;i<rows;i++) {
            for (int j = 0; j < cols; j++) {
                this.m[i][j]+=m.m[0][j];
            }
        }
    }
    public void divide(double d){
        for(int i=0;i<rows;i++) {
            for (int j = 0; j < cols; j++) {
                this.m[i][j]/=d;
            }
        }
    }
    public void rowSubtract(Matrix m){
        for(int i=0;i<rows;i++) {
            for (int j = 0; j < cols; j++) {
                this.m[i][j]-=m.m[0][j];
            }
        }
    }
    public void rowMultiply(Matrix m){
        for(int i=0;i<rows;i++) {
            for (int j = 0; j < cols; j++) {
                this.m[i][j]*=m.m[0][j];
            }
        }
    }


    public void add(double scaler)
    {
        for(int i=0;i<rows;i++)
        {
            for(int j=0;j<cols;j++)
            {
                this.m[i][j]+=scaler;
            }

        }
    }


    public void add(Matrix m)
    {
        if(cols!=m.cols || rows!=m.rows) {
            System.out.println("Shape Mismatch");
            return;
        }

        for(int i=0;i<rows;i++)
        {
            for(int j=0;j<cols;j++)
            {
                this.m[i][j]+=m.m[i][j];
            }
        }
    }

    public static Matrix fromArray(double[]x)
    {
        Matrix temp = new Matrix(x.length,1);
        for(int i =0;i<x.length;i++)
            temp.m[i][0]=x[i];
        return temp;

    }
    public static Matrix fromArray(int[]x)
    {
        Matrix temp = new Matrix(x.length,1);
        for(int i =0;i<x.length;i++)
            temp.m[i][0]=x[i];
        return temp;

    }
    public List<Double> toArray() {
        List<Double> temp= new ArrayList<Double>()  ;

        for(int i=0;i<rows;i++)
        {
            for(int j=0;j<cols;j++)
            {
                temp.add(m[i][j]);
            }
        }
        return temp;
    }

    public static Matrix subtract(Matrix a, Matrix b) {
        Matrix temp=new Matrix(a.rows,a.cols);
        for(int i=0;i<a.rows;i++)
        {
            for(int j=0;j<a.cols;j++)
            {
                temp.m[i][j]=a.m[i][j]-b.m[i][j];
            }
        }
        return temp;
    }

    public static Matrix transpose(Matrix a) {
        Matrix temp=new Matrix(a.cols,a.rows);
        for(int i=0;i<a.rows;i++)
        {
            for(int j=0;j<a.cols;j++)
            {
                temp.m[j][i]=a.m[i][j];
            }
        }
        return temp;
    }

    public static Matrix multiply(Matrix a, Matrix b) {
        Matrix temp=new Matrix(a.rows,b.cols);
        for(int i=0;i<temp.rows;i++)
        {
            for(int j=0;j<temp.cols;j++)
            {
                double sum=0;
                for(int k=0;k<a.cols;k++)
                {
                    sum+=a.m[i][k]*b.m[k][j];
                }
                temp.m[i][j]=sum;
            }
        }
        return temp;
    }

    public void multiply(Matrix a) {
        for(int i=0;i<a.rows;i++)
        {
            for(int j=0;j<a.cols;j++)
            {
                this.m[i][j]*=a.m[i][j];
            }
        }

    }

    public void multiply(double a) {
        for(int i=0;i<rows;i++)
        {
            for(int j=0;j<cols;j++)
            {
                this.m[i][j]*=a;
            }
        }

    }

    public void tanh() {
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++) {
                this.m[i][j] = Math.tanh(this.m[i][j]);
            }


    }

    public Matrix dtanh() {
        Matrix temp = new Matrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                temp.m[i][j] = 1 - (this.m[i][j] *  this.m[i][j]);
            }

        }
        return temp;

    }


}

package NeuralNet;

public class HiddenLayers extends Layers{
    Matrix weights_ih, weights_ho, bias_h, bias_o;
    public Matrix input;
    boolean activation;
    public HiddenLayers(){
        this.weights_ih = null;
        this.weights_ho = null;
        this.bias_h = null;
        this.bias_o = null;
        this.activation = true;

    }
    public HiddenLayers(int i, int h, int o, boolean activation) {
        weights_ih = new Matrix(i, h);
        weights_ih.add(-0.5);
        weights_ho = new Matrix(h, o);
        weights_ho.add(-0.5);
        bias_h = new Matrix(h, 1);
        bias_h.add(-0.5);
        bias_o = new Matrix(1, o);
        bias_o.add(-0.5);
        this.activation = activation;

    }
    public void setInput(Matrix i){
        input = i;
    }
    public Matrix getInput(){
        return input;
    }



}

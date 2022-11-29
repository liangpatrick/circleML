package Environment;

import java.util.Random;

//Agent class
public class Agent {
    int cell;
//    constructor which randomly generates position
    public Agent(){
        this.cell = new Random().nextInt(50);
    }
//    returns cell of agent
    public int getCell(){
        return cell;
    }
//    updates agent position
    public void setCell(int cell){
        this.cell = cell;
    }
}

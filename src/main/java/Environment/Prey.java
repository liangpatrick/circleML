package Environment;

import java.util.ArrayList;
import java.util.Random;

public class Prey {
    int cell;
//    constructor for prey
    public Prey(Agent agent){
//        doesn't occupy same cell as agent
        int prey = new Random().nextInt(50);
        while(prey == agent.getCell()){
            prey = new Random().nextInt(50);
        }
        this.cell = prey;
    }
//    returns position of prey
    public int getCell(){
        return cell;
    }
//    updates prey position
    public void setCell(int cell){
        this.cell = cell;
    }

//    randomly chooses between itself and its neighbors
    public static int choosesNeighbors(int cell, ArrayList<ArrayList<Graph.Node>> maze){
        int newCell = new Random().nextInt(maze.get(cell).size());
        return maze.get(cell).get(newCell).getCell();
    }
}

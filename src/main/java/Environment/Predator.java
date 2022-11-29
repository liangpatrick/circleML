package Environment;

import java.util.*;

public class Predator {
    int cell;
//    predator constructor
    public Predator(Agent agent){
//        doesn't occupy same cell as agent
        int pred = new Random().nextInt(50);
        while(pred == agent.getCell()){
            pred = new Random().nextInt(50);
        }
        this.cell = pred;
    }
//    return predator position
    public int getCell(){
        return cell;
    }
//    updates cell of predator
    public void setCell(int cell){
        this.cell = cell;
    }
//    finds shortest path
    public static List<Graph.Node> bfs(int start, Agent agent, ArrayList<ArrayList<Graph.Node>> maze){
//      fringe to store cells that need to be visited
        Queue<Graph.Node> fringe = new LinkedList<>();
        HashSet<Integer> visited = new HashSet<Integer>();

//      add beginning cell to fringe and visited
        fringe.add(new Graph.Node(start,null));
        visited.add(start);
        while(!fringe.isEmpty()) {
//          use poll instead of remove so no errors are thrown
            Graph.Node curr = fringe.poll();
            int ind = curr.getCell();
//          if arrived at destination
            if (agent.getCell() == ind) {
                List<Graph.Node> path = new ArrayList<>();
                getPath(curr, path);
                return path;
            }
//          checks all neighbors to see if they are eligible to be added to the fringe
            List<Graph.Node> edges = maze.get(ind).subList(1, maze.get(ind).size());
            for(Graph.Node n: edges)
            {
                if(!visited.contains(n.getCell())) {
                    visited.add(n.getCell());
                    fringe.add(new Graph.Node(n.getCell(), curr));
                }
            }

        }
        return null;
    }
//  returns path from start to goal node
    public static void getPath(Graph.Node node, List<Graph.Node> path){
        if (node != null){
            getPath(node.prev, path);
            path.add(node);

        }
    }


//  randomly chooses neighbor(not itself)
    public static int choosesNeighbors(int cell, ArrayList<ArrayList<Graph.Node>> maze){
        int newCell = new Random().nextInt(maze.get(cell).size()-1);
        return maze.get(cell).get(newCell+1).getCell();
    }
}

package Agents;
import Environment.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Collections;
import java.util.Queue;
import java.util.LinkedList;
import java.util.HashSet;

import static Environment.Predator.getPath;


public class CompleteInformation {
    public static Result agentOne(ArrayList<ArrayList<Graph.Node>> maze){
//        initializes all player positions
        Agent agent = new Agent();
        Prey prey = new Prey(agent);
        Predator predator = new Predator(agent);

        int count = 0;

//        will return only when Agent dies or succeeds
        while(true){
//            hung
            if (count == 5000)
                return new Result(false, false, false,false, count);
//            creates arraylists of neighbors, predator distances, and prey distances
            ArrayList<Graph.Node> neighbors = maze.get(agent.getCell());
            ArrayList<Integer> predatorDistances = new ArrayList<>();
            ArrayList<Integer> preyDistances = new ArrayList<>();
//            adds distances to predator/prey from all neighbors
            for(int x = 0; x < neighbors.size(); x++){
                List<Graph.Node> predatorList = searchPred(neighbors.get(x).getCell(), predator.getCell(), maze);
                List<Graph.Node> preyList = searchPrey(neighbors.get(x).getCell(), prey.getCell(), maze);
                predatorDistances.add(x, predatorList.size());
                preyDistances.add(x, preyList.size());

            }
//            stores distance of agent to predator/prey in a named variable
            int agentToPrey = preyDistances.get(0);
            int agentToPredator = predatorDistances.get(0);
//            used to store arraylist of possible ties
            HashMap<Integer,ArrayList<Integer>> moves = new HashMap<Integer,ArrayList<Integer>>();
//            initializes the arraylists
            for(int x = 0; x < 7; x++)
                moves.put(x, new ArrayList<>());
//            want to start iterating through neighbors and compare to agent position
            for(int x = 1; x < predatorDistances.size(); x++){
                int currPredator = predatorDistances.get(x);
                int currPrey = preyDistances.get(x);
//                logical statements provided in the writeup
                if(currPrey < agentToPrey && currPredator > agentToPredator){
                    moves.get(0).add(neighbors.get(x).getCell());
                } else if (currPrey < agentToPrey && currPredator == agentToPredator) {
                    moves.get(1).add(neighbors.get(x).getCell());
                } else if (currPrey == agentToPrey && currPredator > agentToPredator){
                    moves.get(2).add(neighbors.get(x).getCell());
                } else if (currPrey == agentToPrey && currPredator == agentToPredator){
                    moves.get(3).add(neighbors.get(x).getCell());
                } else if (currPredator > agentToPredator){
                    moves.get(4).add(neighbors.get(x).getCell());
                } else if (currPredator == agentToPredator){
                    moves.get(5).add(neighbors.get(x).getCell());
                } else {
                    moves.get(6).add(agent.getCell());
                }



            }
//          looks for the first non-zero sized list out of all possible moves
            ArrayList<Integer> random = new ArrayList<>();
            for(int x = 0; x < 7; x++){
                if (moves.get(x).size() > 0){
                    random = moves.get(x);
                    break;
                }
            }
//            randomly choose neighbor for ties
             int rand = new Random().nextInt(random.size());
            agent.setCell(random.get(rand));
//            win
            if(agent.getCell() == prey.getCell()){
                return new Result(false, true, false,false, 0);
            }
            else if(agent.getCell() == predator.getCell()){
                return new Result(false, false, true,false, 0);
            }
//          prey move
            prey.setCell(Prey.choosesNeighbors(prey.getCell(), maze));
//            win
            if(agent.getCell() == prey.getCell()){
                return new Result(false, false, false,true, 0);
            }
//            pred move
            List<Graph.Node> predatorNeighbors = maze.get(predator.getCell()).subList(1, maze.get(predator.getCell()).size());
            ArrayList<Integer> distances = new ArrayList<>();

            for(int x = 0; x < predatorNeighbors.size(); x++){
                List<Graph.Node> agentList = Predator.bfs(predatorNeighbors.get(x).getCell(), agent, maze);
                distances.add(agentList.size());
            }
//            randomly choose neighbor for ties
            int min = Collections.min(distances);

            ArrayList<Integer> indices = new ArrayList<>();
            for(int x = 0; x < distances.size(); x++){
                if (distances.get(x) == min){
                    indices.add(x);
                }
            }
            int randInt = new Random().nextInt(indices.size());

//             if prob is <= 6 then it chooses shortest path. Else, chooses randomly
            int prob = new Random().nextInt(10)+1;
            if(prob <= 6) {
                predator.setCell(predatorNeighbors.get(indices.get(randInt)).getCell());
            }
            else {
                predator.setCell(Predator.choosesNeighbors(predator.getCell(), maze));
            }
//            dead
            if(agent.getCell() == predator.getCell()){
                return new Result(true, false, false,false, 0);
            }

            count++;

        }
    }



    public static Result agentTwo(ArrayList<ArrayList<Graph.Node>> maze) {
//        initializes all player positions
        Agent agent = new Agent();
        Prey prey = new Prey(agent);
        Predator predator = new Predator(agent);

        int count = 0;

//        will return only when Agent dies or succeeds
        while(true){
//            hung
            if (count == 5000)
                return new Result(false, false, false,false, count);
            ArrayList<Graph.Node> neighbors = maze.get(agent.getCell());
            ArrayList<Graph.Node> preyNeighbors = maze.get(prey.getCell());

//            calls utility function
            int cell = bestCell(neighbors, predator, preyNeighbors, maze);
            agent.setCell(cell);
//            win
            if(agent.getCell() == prey.getCell()){
                return new Result(false, true, false,false, 0);
            } else if(agent.getCell() == predator.getCell())
                return new Result(false, false, true,false, 0);
//          prey move
            prey.setCell(Prey.choosesNeighbors(prey.getCell(), maze));
//            win
            if(agent.getCell() == prey.getCell()){
                return new Result(false, false, false,true, 0);
            }
//            pred move
            List<Graph.Node> predatorNeighbors = maze.get(predator.getCell()).subList(1, maze.get(predator.getCell()).size());
            ArrayList<Integer> distances = new ArrayList<>();

            for(int x = 0; x < predatorNeighbors.size(); x++){
                List<Graph.Node> agentList = Predator.bfs(predatorNeighbors.get(x).getCell(), agent, maze);
                distances.add(agentList.size());
            }
//            randomly choose neighbor for ties
            int min = Collections.min(distances);

            ArrayList<Integer> indices = new ArrayList<>();
            for(int x = 0; x < distances.size(); x++){
                if (distances.get(x) == min){
                    indices.add(x);
                }
            }
            int randInt = new Random().nextInt(indices.size());

//             if prob is <= 6 then it chooses shortest path. Else, chooses randomly
            int prob = new Random().nextInt(10)+1;
            if(prob <= 6) {
                predator.setCell(predatorNeighbors.get(indices.get(randInt)).getCell());
            }
            else {
                predator.setCell(Predator.choosesNeighbors(predator.getCell(), maze));
            }
//            dead
            if(agent.getCell() == predator.getCell()){
                return new Result(true, false, false,false, 0);
            }

            count++;

        }

    }


//    utility function
    public static int bestCell(ArrayList<Graph.Node> neighbors, Predator predator, ArrayList<Graph.Node> preyNeighbors, ArrayList<ArrayList<Graph.Node>> maze){

//      stores utility of all agent cells
        ArrayList<Double> utilities = new ArrayList<>();
        ArrayList<Double> preyDistances = new ArrayList<>();
        ArrayList<Integer> predatorDistances = new ArrayList<>();
//        collects all preyDistances and predatorDistances
        for(int x = 0; x < neighbors.size(); x++) {
            double aveDistance = 0.0;
//          gets of average distances from each possible agent to cluster of prey neighbors
            for (int y = 0; y < preyNeighbors.size(); y++) {
                int currDistance = searchPrey(neighbors.get(x).getCell(), preyNeighbors.get(y).getCell(), maze).size();
                aveDistance += currDistance;


            }
//            stores average distances
            aveDistance /= preyNeighbors.size();
            preyDistances.add(aveDistance);
            List<Graph.Node> predatorList = searchPred(neighbors.get(x).getCell(), predator.getCell(), maze);
            predatorDistances.add(predatorList.size());
//            stores initial utility values
            utilities.add(predatorList.size() - aveDistance);
        }
//        arbitrary number
        double weightPrey   =    1.0/2;
        double weightPredator = -1.0/2;
//        updates utility of cell depending  on whether current cell has closest distance to predator or closest distance to prey
        for(int x = 0; x < neighbors.size(); x++){
            if(Collections.min(preyDistances) == preyDistances.get(x))
                utilities.set(x, utilities.get(x) + 75*(weightPrey/preyDistances.size()));
            if(Collections.min(predatorDistances) == predatorDistances.get(x))
                utilities.set(x, utilities.get(x) + 100*(weightPredator/predatorDistances.size()));
        }

//        Two options: 1) move towards cell with highest utility when all greatest utility is positive2) move away from predator
        if(Collections.max(utilities) > 0)
            return neighbors.get(utilities.indexOf(Collections.max(utilities))).getCell();
        else
            return neighbors.get(predatorDistances.indexOf(Collections.max(predatorDistances))).getCell();
    }



//  bfs
    public static List<Graph.Node> searchPred(int start, int pred, ArrayList<ArrayList<Graph.Node>> maze){
//      fringe to store cells that need to be visited
        Queue<Graph.Node> fringe = new LinkedList<>();
//        hashset of visited for o(1) lookup
        HashSet<Integer> visited = new HashSet();

//      add beginning cell to fringe and visited
        fringe.add(new Graph.Node(start, null));
        visited.add(start);
        while(!fringe.isEmpty()) {
//          use poll instead of remove so no errors are thrown
            Graph.Node curr = fringe.poll();
            int ind = curr.getCell();
//          if arrived at destination
            if (ind == pred) {
                List<Graph.Node> path = new ArrayList<>();;
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
//        System.out.println("No path");
        return null;
    }
    public static List<Graph.Node> searchPrey(int start, int prey, ArrayList<ArrayList<Graph.Node>> maze){
//      fringe to store cells that need to be visited
        Queue<Graph.Node> fringe = new LinkedList<>();
        HashSet<Integer> visited = new HashSet();
//      add beginning cell to fringe and visited
        fringe.add(new Graph.Node(start, null));
        visited.add(start);
        while(!fringe.isEmpty()) {
//          use poll instead of remove so no errors are thrown
            Graph.Node curr = fringe.poll();
            int ind = curr.getCell();
//          if arrived at destination
            if (ind == prey) {
                List<Graph.Node> path = new ArrayList<>();;
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





}

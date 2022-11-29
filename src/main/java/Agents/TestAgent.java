package Agents;

import Environment.*;

import java.util.*;

import static Agents.CompleteInformation.searchPrey;

//Test Agent that ignores predator and greedily goes towards prey
public class TestAgent {
    public static Result test(ArrayList<ArrayList<Graph.Node>> maze){
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
            ArrayList<Integer> preyDistances = new ArrayList<>();
//            adds distances to predator/prey from all neighbors
            for(int x = 0; x < neighbors.size(); x++){
                List<Graph.Node> preyList = searchPrey(neighbors.get(x).getCell(), prey.getCell(), maze);
                preyDistances.add(x, preyList.size());

            }
//            stores distance of agent to prey in a named variable
            int agentToPrey = preyDistances.get(0);
            double minD = Double.MAX_VALUE;
            for(int x = 0; x < preyDistances.size(); x++){
                if (minD > preyDistances.get(x))
                    minD = preyDistances.get(x);

            }

//          adds shortest distances to d arraylist
            ArrayList<Integer> d = new ArrayList<>();
            for(int x = 0; x < preyDistances.size(); x++){
                if (minD == preyDistances.get(x))
                    d.add(x);

            }
//            randomly break ties
            int random = new Random().nextInt(d.size());
            agent.setCell(neighbors.get(d.get(random)).getCell());

//            win
            if(agent.getCell() == prey.getCell()){
                return new Result(false, true, false,false, 0);
            }
//            dead
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
            ArrayList<Graph.Node> predatorNeighbors = maze.get(predator.getCell());
            ArrayList<Integer> agentDistances = new ArrayList<>();
//          Creates list of distances for predator neighbors
            for(int x = 1; x < predatorNeighbors.size(); x++){
                List<Graph.Node> agentList = Predator.bfs(predatorNeighbors.get(x).getCell(), agent, maze);
                agentDistances.add(agentList.size());
            }
//            randomly choose neighbor for ties
            int min = Collections.min(agentDistances);

            ArrayList<Integer> indices = new ArrayList<>();
            for(int x = 0; x < agentDistances.size(); x++){
                if (agentDistances.get(x) == min){
                    indices.add(x);
                }
            }
            int randInt = new Random().nextInt(indices.size());
            predator.setCell(predatorNeighbors.get(indices.get(randInt)+1).getCell());
//            dead
            if(agent.getCell() == predator.getCell()){
                return new Result(true, false, false,false, 0);
            }

            count++;

        }
    }
}

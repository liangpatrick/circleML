package Agents;
import Environment.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Collections;


public class Ustar {

    static HashMap<State, Double> ustar = new HashMap<>();
    static ArrayList<ArrayList<Graph.Node>> globalMaze = new ArrayList<>();
    public static Result complete(ArrayList<ArrayList<Graph.Node>> maze) {
//        initializes all player positions
        Agent agent = new Agent();
        Prey prey = new Prey(agent);
        Predator predator = new Predator(agent);

        int count = 1;

//        will return only when Agent dies or succeeds
        while(true){
//            hung
            if (count == 5000)
                return new Result(false, false, false,false, count, count);


            List<Graph.Node> neighbors = getNextAgentStates(agent.getCell());
            int cell = neighbors.get(0).getCell();
            double value = Double.NEGATIVE_INFINITY;
            for(Graph.Node n: neighbors){
                if(ustar.get(new State(n.getCell(), prey.getCell(), predator.getCell())) == null || Double.isInfinite(ustar.get(new State(n.getCell(), prey.getCell(), predator.getCell()))) || Double.isNaN(ustar.get(new State(n.getCell(), prey.getCell(), predator.getCell()))))
                    continue;
                double currUtil = ustar.get(new State(n.getCell(), prey.getCell(), predator.getCell()));
                if(value < currUtil){
                    cell = n.getCell();
                    value = currUtil;
                }
            }

            agent.setCell(cell);
//            win
            if(agent.getCell() == prey.getCell()){
                return new Result(false, true, false,false, 0, count);
            } else if(agent.getCell() == predator.getCell())
                return new Result(false, false, true,false, 0, count);

//          prey move
            prey.setCell(Prey.choosesNeighbors(prey.getCell(), maze));
//            win
            if(agent.getCell() == prey.getCell()){
                return new Result(false, false, false,true, 0, count);
            }
//            pred move
            List<Graph.Node> predatorNeighbors = getNextPredatorStates(predator.getCell());
            ArrayList<Integer> distances = new ArrayList<>();

            for(int x = 0; x < predatorNeighbors.size(); x++){
                List<Graph.Node> agentList = Predator.bfs(predatorNeighbors.get(x).getCell(), agent.getCell(), maze);
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
                return new Result(true, false, false,false, 0, count);
            }

            count++;

        }

    }

    static double updateValues(int agent, int prey, int predator){
//        all possible states
        ArrayList<State> states = getNextStates(agent, prey, predator);
//        init value
        double currValue = 0.0;
        double maxValue = Double.NEGATIVE_INFINITY;
//        used to keep track of what action it is on
        int prevAction = -1;

//        predator neighbors
        List<Graph.Node> predatorNeighbors = getNextPredatorStates(predator);

//      finds count of shortest paths from predator to agent
        ArrayList<Integer> distances = new ArrayList<>();
        for(int x = 0; x < predatorNeighbors.size(); x++){
            List<Graph.Node> agentList = Predator.bfs(predatorNeighbors.get(x).getCell(), agent, globalMaze);
            distances.add(agentList.size());
        }
        int min = Collections.min(distances);
        ArrayList<Integer> indices = new ArrayList<>();
        for(int x = 0; x < distances.size(); x++){
            if (distances.get(x) == min){
                indices.add(predatorNeighbors.get(x).getCell());
            }
        }


//      goes through all possible states
        for(State state: states){
//            init prevAction
            if (prevAction == -1) {
                prevAction = state.getAgent();
            }
//            if its a new action, update maxValue
            if (prevAction != state.getAgent()) {
                maxValue = Math.max(maxValue, -1.0+ currValue);
                currValue = 0.0;
            }
//              If its a shortest path for predator, different probability
            if(indices.contains(state.getPredator())) {
                    currValue += (ustar.get(new State(state.getAgent(), state.getPrey(), state.getPredator())) * ((1.0 / getNextPreyStates(prey).size()) * ((0.6 / indices.size()) + (0.4 / getNextPredatorStates(predator).size()))));
            }
            else {
                    currValue += (ustar.get(new State(state.getAgent(), state.getPrey(), state.getPredator())) * ((1.0 / getNextPreyStates(prey).size()) * (0.4 / getNextPredatorStates(predator).size())));
            }
            prevAction = state.getAgent();
        }

//      one last max
        maxValue = Math.max(maxValue, -1.0 + currValue);
        return maxValue;
    }


//    goes through all states and updates values
    static HashMap<State, Double> updateStates(){
        HashMap<State, Double> temp = new HashMap<>();
        for(int agent = 0; agent < 50; agent++){
            for(int prey = 0; prey < 50; prey++){
                for(int predator = 0; predator < 50; predator++){
//                    base cases
                    if(agent == predator)
                        temp.put(new State(agent, prey, predator), Double.NEGATIVE_INFINITY);
                    else if(Predator.bfs(agent, predator, globalMaze).size() == 2)
                        temp.put(new State(agent, prey, predator), Double.NEGATIVE_INFINITY);
                    else if(agent == prey)
                        temp.put(new State(agent, prey, predator), 0.0);
                    else if(Predator.bfs(agent, prey, globalMaze).size() == 2)
                        temp.put(new State(agent, prey, predator), -1.0);
                    else
                        temp.put(new State(agent, prey, predator), updateValues(agent, prey, predator));

//                    System.out.println(temp.get(new State(agent, prey, predator)));

                }
            }
        }
        return temp;
    }

    static void policyIter(){
        ustar = initU(ustar);
//        how close the original ustar and the new policy should be
        double convergence = 1.0 / Math.pow(10, 8);
//        keeps track of time
        long total = System.nanoTime();
//        keeps track of iter
        int count = 1;
        while(true){
//            returns new policy
            HashMap<State, Double> policy = updateStates();
//            greatest difference between new policy and current ustar
            double difference = Collections.max(differences(ustar, policy));
//            updates ustar
            ustar = copyMap(policy);
            long endTime = System.nanoTime();
            long duration = (endTime - total)/(long)Math.pow(10,9);
            System.out.println("Policy Iter: " + count++ + "; Time: " + duration + "; Difference: " + difference);
//            if the largest difference between old ustar and policy qualify, policy is optimal
            if(difference < convergence){
                break;
            }
        }

        long endTime = System.nanoTime();
        long duration = (endTime - total)/(long)Math.pow(10,9);
        System.out.println("Total Time: " + duration);

    }

//    initializes ustar
    static HashMap<State, Double> initU(HashMap<State, Double> arr){
        for(int agent = 0; agent < 50; agent++){
            for(int prey = 0; prey < 50; prey++){
                for(int predator = 0; predator < 50; predator++){
//                    base cases
                    if(agent == predator)
                        arr.put(new State(agent, prey, predator), Double.NEGATIVE_INFINITY);
                    else if(Predator.bfs(agent, predator, globalMaze).size() == 2)
                        arr.put(new State(agent, prey, predator), Double.NEGATIVE_INFINITY);
                    else if(Predator.bfs(agent, prey, globalMaze).size() == 2)
                        arr.put(new State(agent, prey, predator), -1.0);
                    else
                        arr.put(new State(agent, prey, predator), 0.0);
                }
            }
        }
        return arr;
    }

//    return array slist of all possible states for current position
    static ArrayList<State> getNextStates(int agent, int prey, int predator){
        ArrayList<State> states = new ArrayList<State>();
        List<Graph.Node> agentStates = getNextAgentStates(agent);
        ArrayList<Graph.Node> preyStates = getNextPreyStates(prey);
        List<Graph.Node> predatorStates = getNextPredatorStates(predator);
        for(int agentPos = 0; agentPos < agentStates.size(); agentPos++){
            for(int preyPos = 0; preyPos < preyStates.size(); preyPos++){
                for(int predatorPos = 0; predatorPos < predatorStates.size(); predatorPos++){
                    states.add(new State(agentStates.get(agentPos).getCell(), preyStates.get(preyPos).getCell(), predatorStates.get(predatorPos).getCell()));
                }
            }
        }
        return states;
    }
//    return array list of possible agent actions
    static ArrayList<Graph.Node> getNextAgentStates(int agent){
        return globalMaze.get(agent);
    }
//    return array list of prey neighbors
    static ArrayList<Graph.Node> getNextPreyStates(int prey){
        return globalMaze.get(prey);
    }
//    return array list of predator neighbors
    static List<Graph.Node> getNextPredatorStates(int predator){
        return globalMaze.get(predator).subList(1, globalMaze.get(predator).size());
    }
//    deep copy of hashmap
    static HashMap<State, Double> copyMap(HashMap<State, Double> temp){
        HashMap<State, Double> copy = new HashMap<>();
        for(State s: temp.keySet()){
            copy.put(new State(s.getAgent(), s.getPrey(), s.getPredator()), temp.get(s));
        }
        return copy;
    }
//    returns array list of differences between original ustar and new one
    public static ArrayList<Double> differences(HashMap<State, Double> ustar, HashMap<State, Double> policy){
        ArrayList<Double> differences = new ArrayList<>();
        for(int agent = 0; agent < 50; agent++){
            for(int prey = 0; prey < 50; prey++){
                for(int predator = 0; predator < 50; predator++){
//                    ignores infinity and NaN
                    if (!Double.isInfinite(policy.get(new State(agent, prey, predator))) &&  !Double.isInfinite(ustar.get(new State(agent, prey, predator))) && !Double.isNaN(policy.get(new State(agent, prey, predator))) &&  !Double.isNaN(ustar.get(new State(agent, prey, predator)))) {
                        double diff = policy.get(new State(agent, prey, predator)) - ustar.get(new State(agent, prey, predator));
                        differences.add(Math.abs(diff));
                    }
                }
            }
        }
        return differences;
    }

    public static void run(){
        globalMaze = Graph.buildGraph();
        policyIter();
        int[] agentSuccess           = new int[2];
        int[] hung                   = new int[2];
        int[] predatorSuccess        = new int[2];
        int[] agentFail              = new int[2];
        int[] preyDeath              = new int[2];
        int[] steps              = new int[2];
        double[] preySurveyRate      = new double[2];
        double[] predatorSurveyRate  = new double[2];
        for(int x = 0; x < 3000; x++){
            Result resultOne = complete(globalMaze);
//            System.out.println();
//            System.out.println();
//            System.out.println();
            if (resultOne.predatorCatchesAgent) {
//                    predator catches agent
                predatorSuccess[1]++;
            }
            else if (resultOne.agentCatchesPrey) {
//                    agent catches prey
                agentSuccess[1]++;
            }
            else if (resultOne.agentRunsPredator) {
//                    agent runs into agent
                agentFail[1]++;
            }
            else if (resultOne.preyRunsAgent) {
//                    prey runs into agent
                preyDeath[1]++;
            }
            else if (resultOne.hung > 0) {
//                    hung
                hung[1]++;
            }
            steps[1] += resultOne.steps;

        }
        for(int x = 1; x < agentSuccess.length; x++){
            System.out.println("U*: " + ((agentSuccess[x] + preyDeath[x])/3000.0) * 100);
            System.out.println("Predator Catches Agent: " + predatorSuccess[x]);
            System.out.println("Predator Catches Agent out of total Loss: " + predatorSuccess[x]/((double)predatorSuccess[x] + agentFail[x]+hung[x]));
            System.out.println("Agent Catches Prey: " + agentSuccess[x]);
            System.out.println("Prey Runs into Agent: " + preyDeath[x]);
            System.out.println("Agent Runs into Predator: " + agentFail[x]);
            System.out.println("Average number of steps: " + steps[x]/3000.0);
            System.out.println("Average Prey Survey Rate: " + ((preySurveyRate[x] / 3000.0 )* 100));
            System.out.println("Average Predator Survey Rate: " + ((predatorSurveyRate[x] / 3000.0)*100));
            System.out.println("Hung out of Loss: " + (double)hung[x]/((double)predatorSuccess[x] + agentFail[x]+hung[x]));
            System.out.println();
        }
    }

    public static void main (String args[]) {
        run();
    }







}

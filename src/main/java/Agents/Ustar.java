package Agents;
import Environment.*;

import java.io.*;
import java.util.*;


public class Ustar {

    static HashMap<State, Double> ustar = new HashMap<>();
    static ArrayList<ArrayList<Graph.Node>> globalMaze = new ArrayList<>();

    static double[] belief = new double[50];
    static double[][] transMatrix = new double[50][50];
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

            int cell = -1;
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

//            attempts to get out of trap
            if (cell == -1){
                ArrayList<Integer> distances = new ArrayList<>();
                for(int x = 0; x < neighbors.size(); x++){
                    List<Graph.Node> predToAgent = Predator.bfs(neighbors.get(x).getCell(), predator.getCell(), maze);
                    distances.add(predToAgent.size());
                }
//            randomly choose neighbor for ties
                int max = Collections.max(distances);

                ArrayList<Integer> indices = new ArrayList<>();
                for(int x = 0; x < distances.size(); x++){
                    if (distances.get(x) == max){
                        indices.add(neighbors.get(x).getCell());
                    }
                }
                int randInt = new Random().nextInt(indices.size());

                agent.setCell(indices.get(randInt));
            }else {
                agent.setCell(cell);
            }

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
                maxValue = Math.max(maxValue, currValue);
                currValue = 0.0;
            }

//            ArrayList<Integer> distances = new ArrayList<>();
//            for(int x = 0; x < predatorNeighbors.size(); x++){
//                List<Graph.Node> agentList = Predator.bfs(predatorNeighbors.get(x).getCell(), state.getAgent(), globalMaze);
//                distances.add(agentList.size());
//            }
//            int min = Collections.min(distances);
//            ArrayList<Integer> indices = new ArrayList<>();
//            for(int x = 0; x < distances.size(); x++){
//                if (distances.get(x) == min){
//                    indices.add(predatorNeighbors.get(x).getCell());
//                }
//            }

//              If its a shortest path for predator, different probability
            if(indices.contains(state.getPredator())) {
                    currValue += (ustar.get(state) * ((1.0 / getNextPreyStates(prey).size()) * ((0.6 / indices.size()) + (0.4 / getNextPredatorStates(predator).size()))));
            }
            else {
                    currValue += (ustar.get(state) * ((1.0 / getNextPreyStates(prey).size()) * (0.4 / getNextPredatorStates(predator).size())));
            }
            prevAction = state.getAgent();
        }

//      one last max
        maxValue = Math.max(maxValue, currValue);
        return -1.0 + maxValue;
    }

//    goes through all states and updates values
    static HashMap<State, Double> updateStates(){
        HashMap<State, Double> temp = new HashMap<>();
        for(int agent = 0; agent < 50; agent++){
            for(int prey = 0; prey < 50; prey++){
                for(int predator = 0; predator < 50; predator++){
//                    base cases
                    if(agent == predator && prey != agent)
                        temp.put(new State(agent, prey, predator), Double.NEGATIVE_INFINITY);
                    else if(agent == prey)
                        temp.put(new State(agent, prey, predator), 0.0);
                    else if(Predator.bfs(agent, predator, globalMaze).size() == 2)
                        temp.put(new State(agent, prey, predator), Double.NEGATIVE_INFINITY);
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
        double convergence = 1.0 / Math.pow(10, 3);
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

    public static Result partial(ArrayList<ArrayList<Graph.Node>> maze){
//        initializes all player positions
        Agent agent = new Agent();
        Prey prey = new Prey(agent);
        Predator predator = new Predator(agent);
        initTransMatrix(maze);
        initialBelief(agent.getCell());
//        policyIterPartial();
        int count = 0;
        double surveyRate = 0;
//        will return only when Agent dies or succeeds
        while(true){
//            hung
            if (count == 5000)
                return new Result(false, false, false,false, surveyRate/(double)count, count, count);



//            random survey
            int surveyedNode = randomSurvey();
            if(prey.getCell() == surveyedNode){
                bayes(true, prey.getCell());
                surveyRate++;
            } else {
                bayes(false, surveyedNode);
            }
//            update beliefs
            normalize();

            HashMap<State, Double> ustar = updateStatesPartial(agent.getCell(), predator.getCell());

            List<Graph.Node> neighbors = getNextAgentStates(agent.getCell());

            int cell = -1;
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


            if (cell == -1){
                ArrayList<Integer> distances = new ArrayList<>();
                for(int x = 0; x < neighbors.size(); x++){
                    List<Graph.Node> predToAgent = Predator.bfs(neighbors.get(x).getCell(), predator.getCell(), maze);
                    distances.add(predToAgent.size());
                }
//            randomly choose neighbor for ties
                int max = Collections.max(distances);

                ArrayList<Integer> indices = new ArrayList<>();
                for(int x = 0; x < distances.size(); x++){
                    if (distances.get(x) == max){
                        indices.add(neighbors.get(x).getCell());
                    }
                }
                int randInt = new Random().nextInt(indices.size());

                agent.setCell(indices.get(randInt));
            }else {
                agent.setCell(cell);
            }

//            win
            if(agent.getCell() == prey.getCell()){
                surveyRate++;
                return new Result(false, true, false,false, surveyRate/((double)count + 1), 0, count);
            }
            else if(agent.getCell() == predator.getCell())
                return new Result(false, false, true,false, surveyRate/((double)count + 1), 0, count);
            bayes(false,  agent.getCell());
            normalize();
//          prey move
            prey.setCell(Prey.choosesNeighbors(prey.getCell(), maze));
//            win
            if(agent.getCell() == prey.getCell()){
                return new Result(false, false, false,true, surveyRate/((double)count + 1), 0, count);
            }
//            distributes beliefs
            matmul();

//            pred move
            List<Graph.Node> predatorNeighbors = maze.get(predator.getCell()).subList(1, maze.get(predator.getCell()).size());
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
                return new Result(true, false, false,false, surveyRate/((double)count + 1), 0, count);
            }

            count++;


        }

    }

    static double updateValuesPartial(int agent, int predator){
//        all possible states
        ArrayList<State> states = getNextPartialStates(agent, predator);
//        init value
        double currValue = 0.0;
        double maxValue = Double.NEGATIVE_INFINITY;
//        used to keep track of what action it is on
        int prevAction = -1;

//      goes through all possible states
        for(State state: states){
//            init prevAction
            if (prevAction == -1) {
                prevAction = state.getAgent();
            }
//            if its a new action, update maxValue
            if (prevAction != state.getAgent()) {
//                System.out.println(currValue);
                maxValue = Math.max(maxValue, currValue);
                currValue = 0.0;
            }


            if(belief[state.getPrey()] > 0) {
                currValue += belief[state.getPrey()] * ustar.get(state);
            }

            prevAction = state.getAgent();
        }

//      one last max
        maxValue = Math.max(maxValue, currValue);
        return -1.0 + maxValue;
    }


    //    goes through all states and updates values
    static HashMap<State, Double> updateStatesPartial(int agentCell, int predatorCell){
        HashMap<State, Double> temp = new HashMap<>();
        List<Graph.Node> agents = getNextAgentStates(agentCell);
        List<Graph.Node> predators = getNextPredatorStates(predatorCell);
//        List<Integer> preys = getNextPartialPreyStates();
        for(int agent = 0; agent < agents.size(); agent++){

            for(int predator = 0; predator < predators.size(); predator++){
                for(int prey = 0; prey < 50; prey++){
//                    base cases
                    if(agents.get(agent).getCell() == predatorCell && prey != agents.get(agent).getCell())
                        temp.put(new State(agents.get(agent).getCell(), prey, predatorCell), Double.NEGATIVE_INFINITY);
                    else if(agents.get(agent).getCell() == prey)
                        temp.put(new State(agents.get(agent).getCell(), prey, predatorCell), 0.0);
                    else if(Predator.bfs(agents.get(agent).getCell(), predatorCell, globalMaze).size() == 2)
                        temp.put(new State(agents.get(agent).getCell(), prey, predatorCell), Double.NEGATIVE_INFINITY);
                    else if(Predator.bfs(agents.get(agent).getCell(), prey, globalMaze).size() == 2)
                        temp.put(new State(agents.get(agent).getCell(), prey, predatorCell), -1.0);
                    else
                        temp.put(new State(agents.get(agent).getCell(), prey, predatorCell), updateValuesPartial(agentCell, predatorCell));

                }
            }
        }
        return temp;
    }



//    initializes ustar
    static HashMap<State, Double> initU(HashMap<State, Double> arr){
        for(int agent = 0; agent < 50; agent++){
            for(int prey = 0; prey < 50; prey++){
                for(int predator = 0; predator < 50; predator++){
//                    base cases
                    if(agent == predator && prey != agent)
                        arr.put(new State(agent, prey, predator), Double.NEGATIVE_INFINITY);
                    else if (agent == prey)
                        arr.put(new State(agent, prey, predator), 0.0);
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
        ArrayList<State> states = new ArrayList<>();
        List<Graph.Node> agentStates = getNextAgentStates(agent);
        ArrayList<Graph.Node> preyStates = getNextPreyStates(prey);
        List<Graph.Node> predatorStates = getNextPredatorStates(predator);
        for(int agentPos = 0; agentPos < agentStates.size(); agentPos++){
            for(int preyPos = 0; preyPos < preyStates.size(); preyPos++){
                for(int predatorPos = 0; predatorPos < predatorStates.size(); predatorPos++){
                    states.add(new State(agentStates.get(agentPos).getCell(), preyStates.get(preyPos).getCell(), predatorStates.get(predatorPos).getCell()));
//                    states.add(new State(agent, preyStates.get(preyPos).getCell(), predatorStates.get(predatorPos).getCell()));
                }
            }
        }
        return states;
    }

    //    return array slist of all possible states for current position
    static ArrayList<State> getNextPartialStates(int agent, int predator){
        ArrayList<State> states = new ArrayList<State>();
        List<Graph.Node> agentStates = getNextAgentStates(agent);
        List<Graph.Node> predatorStates = getNextPredatorStates(predator);
        for(int agentPos = 0; agentPos < agentStates.size(); agentPos++){
            for(int preyPos = 0; preyPos < 50; preyPos++){
                for(int predatorPos = 0; predatorPos < predatorStates.size(); predatorPos++){
                        states.add(new State(agentStates.get(agentPos).getCell(), preyPos, predatorStates.get(predatorPos).getCell()));
                }
            }
        }
        return states;
    }
//    return array list of possible agent actions
    static List<Graph.Node> getNextAgentStates(int agent){
        return globalMaze.get(agent).subList(1, globalMaze.get(agent).size());
    }
//    return array list of prey neighbors
    static ArrayList<Graph.Node> getNextPreyStates(int prey){
        return globalMaze.get(prey);
    }

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

    //    updates belief when new node is surveyed
    public static void bayes(boolean found, int cell){
//        if node surveyed contains prey
        if (found){
            for (int x = 0; x < belief.length; x++) {
                if (cell == x) {
                    belief[x] = 1.0;
                } else {
                    belief[x] = 0.0;
                }

            }

        } else {
//            update all probabilities based on removal of current probability
            double removedProbability = belief[cell];
            for (int x = 0; x < belief.length; x++) {
                if(x == cell){
                    belief[x] = 0;
                }  else {
                    belief[x] /= (1.0-removedProbability);
                }
            }
        }
    }

    //  returns greatest probability in belief; used in conjunction with maxIndex
    public static double maxBelief(){
        return Arrays.stream(belief).max().getAsDouble();
    }

    //    initializes 1/49 for every non-agent cell
    public static void initialBelief(int agentCell){
        for(int x = 0; x < belief.length; x++){
            if(x != agentCell) {
                belief[x] = 1.0 / (49);
            }
            else {
                belief[x] = 0.0;
            }

        }
    }

    //    sums up belief for normalization and error checking
    public static double beliefSum(double[] array) {
        return Arrays.stream(array).sum();
    }
    //    never changes
    public static void initTransMatrix(ArrayList<ArrayList<Graph.Node>> maze){
        for(int x = 0; x < 50; x++){
            for(int y = 0; y < 50; y++){
                transMatrix[x][y] = 0;
            }
        }
        for(int x = 0; x < maze.size(); x++){
            for(int y = 0; y < maze.get(x).size(); y++){
                transMatrix[maze.get(x).get(0).getCell()][maze.get(x).get(y).getCell()] = 1.0/(maze.get(x).size());
            }
        }
    }

    //    returns random cell that has the highest likelihood of being prey
    public static int randomSurvey(){
        ArrayList<Integer> indices = new ArrayList<>();
        double max = maxBelief();
        for(int x = 0; x < belief.length; x++){
//            stores all indices that are have a probability of having prey
            if(belief[x] == max){
                indices.add(x);
            }
        }
        if(indices.size() == 1)
            return indices.get(0);
//        randomly chooses index from arraylist
        int randInt = new Random().nextInt(indices.size());
        return indices.get(randInt);
    }

    //    updates belief after no new info
    public static void matmul(){
        double[] arr = belief.clone();
        for(int x = 0; x < 50; x++){
            belief[x] = dotProduct(x, arr);
        }


    }

    //    dot product to update belief with transMatrix
    public static double dotProduct(int row, double[] temp) {
        double sum = 0;
        for (int x = 0; x < 50; x++) {
            sum += transMatrix[x][row] * temp[x];
        }
        return sum;

    }

    //    normalizes values
    public static void normalize(){
        double sum = beliefSum(belief);
        for(int x = 0; x < 50; x++){
            belief[x] /= sum;
        }
    }


    public static void oneHotEncoder(){
        ArrayList<ArrayList<Integer>> states = new ArrayList<>();
        ArrayList<Double> values = new ArrayList<>();
        for(int agentP = 0; agentP < 50; agentP++) {
            for (int preyP = 0; preyP < 50; preyP++) {
                for (int predatorP = 0; predatorP < 50; predatorP++) {
                    State state = new State(agentP, preyP, predatorP);
                    ArrayList<Integer> temp = new ArrayList<>();
                    int agent = state.getAgent();
                    int prey = state.getPrey() + 50;
                    int predator = state.getPredator() + 100;
                    for (int y = 0; y < 150; y++) {
                        if (agent == y)
                            temp.add(1);
                        else if (prey == y)
                            temp.add(1);
                        else if (predator == y)
                            temp.add(1);
                        else
                            temp.add(0);
                    }
                    states.add(temp);
                    values.add(ustar.get(state));
                }
            }


        }
        try{
            FileOutputStream fos = new FileOutputStream("states_train");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(states);
            oos.close();
            fos.close();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
        try{
            FileOutputStream fos = new FileOutputStream("values_train");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(values);
            oos.close();
            fos.close();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }

        try{
            FileOutputStream fos = new FileOutputStream("maze");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(globalMaze);
            oos.close();
            fos.close();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }

    }





    public static void run(){
//        keeps track of iter
        globalMaze = Graph.buildGraph();
        long total = System.nanoTime();
        policyIter();
        int[] agentSuccess           = new int[3];
        int[] hung                   = new int[3];
        int[] predatorSuccess        = new int[3];
        int[] agentFail              = new int[3];
        int[] preyDeath              = new int[3];
        int[] steps                  = new int[3];
        double[] preySurveyRate      = new double[3];
        for(int x = 0; x < 3000; x++){
            Result resultOne = complete(globalMaze);
            if (resultOne.predatorCatchesAgent) {
//                    predator catches agent
                predatorSuccess[1]++;
//                System.out.println("DEATH");

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

            Result resultTwo = partial(globalMaze);
            if (resultTwo.predatorCatchesAgent) {
//                    predator catches agent
                predatorSuccess[2]++;
            }
            else if (resultTwo.agentCatchesPrey) {
//                    agent catches prey
                agentSuccess[2]++;
            }
            else if (resultTwo.agentRunsPredator) {
//                    agent runs into agent
                agentFail[2]++;
            }
            else if (resultTwo.preyRunsAgent) {
//                    prey runs into agent
                preyDeath[2]++;
            }
            else if (resultTwo.hung > 0) {
//                    hung
                hung[2]++;
            }
            preySurveyRate[2] += resultTwo.surveyRate;
            steps[2] += resultTwo.steps;
//
            long endTime = System.nanoTime();
            long duration = (endTime - total)/(long)Math.pow(10,9);
            System.out.println("Iter: " + x + "; Time: " +duration );


//            System.out.println();

        }
        long endTime = System.nanoTime();
        long duration = (endTime - total)/(long)Math.pow(10,9);
        System.out.println("Total Time: " + duration);
        for(int x = 1; x < agentSuccess.length; x++){
            System.out.println("U*: " + ((agentSuccess[x] + preyDeath[x])/3000.0) * 100);
            System.out.println("Predator Catches Agent: " + predatorSuccess[x]);
            System.out.println("Predator Catches Agent out of total Loss: " + predatorSuccess[x]/((double)predatorSuccess[x] + agentFail[x]+hung[x]));
            System.out.println("Agent Catches Prey: " + agentSuccess[x]);
            System.out.println("Prey Runs into Agent: " + preyDeath[x]);
            System.out.println("Agent Runs into Predator: " + agentFail[x]);
            System.out.println("Average number of steps: " + steps[x]/3000.0);
            System.out.println("Average Prey Survey Rate: " + ((preySurveyRate[x] / 3000.0 ) * 100));
            System.out.println("Hung out of Loss: " + (double)hung[x]/((double)predatorSuccess[x] + agentFail[x]+hung[x]));
            System.out.println();
        }

        oneHotEncoder();


    }

    public static void main (String args[]) {
        run();
    }







}

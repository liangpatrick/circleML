package Agents;

import Environment.*;
import NeuralNet.*;


import java.io.*;
import java.util.*;


public class Vstar {

    public static ArrayList<ArrayList<Graph.Node>> maze = new ArrayList<>();
    public static Network net;
    public static Network netPartial;
    public static int[][] states = new int[125000][150];
    public static double[][] statesPartial = new double[125000][150];

    public static double[][] values = new double[125000][1];
    static double[] belief = new double[50];
    static double[][] transMatrix = new double[50][50];

    public static void initStates() {
        ArrayList<ArrayList<Integer>> s = new ArrayList<>();
        for (int agentP = 0; agentP < 50; agentP++) {
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
                    s.add(temp);
                }
            }


        }

        for (int x = 0; x < states.length; x++) {
            int[] temp = new int[s.get(x).size()];
            for (int y = 0; y < states[x].length; y++) {
                temp[y] = s.get(x).get(y);
            }
            states[x] = temp;
        }
    }

    public static void initStatesPartial() {
        ArrayList<ArrayList<Double>> s = new ArrayList<>();
        for (int agentP = 0; agentP < 50; agentP++) {
            for (int preyP = 0; preyP < 50; preyP++) {
                for (int predatorP = 0; predatorP < 50; predatorP++) {
                    State state = new State(agentP, preyP, predatorP);
                    ArrayList<Double> temp = new ArrayList<>();
                    int agent = state.getAgent();
                    for (int y = 0; y < 50; y++) {
                        if (agent == y)
                            temp.add(1.0);
                        else
                            temp.add(0.0);
                    }
                    for (int y = 0; y < 50; y++) {
                        if (agent == y)
                            temp.add(0.0);
                        else
                            temp.add(1.0 / 49.0);
                    }

                    int predator = state.getPredator();
                    for (int y = 0; y < 50; y++) {
                        if (predator == y)
                            temp.add(1.0);
                        else
                            temp.add(0.0);
                    }
                    s.add(temp);
                }
            }


        }

        for (int x = 0; x < statesPartial.length; x++) {
            double[] temp = new double[s.get(x).size()];
            for (int y = 0; y < statesPartial[x].length; y++) {
                temp[y] = s.get(x).get(y);
            }
            statesPartial[x] = temp;
        }
    }

    public static void deserialize() {
//        deserializeStates();
        initStates();


        deserializeValues();
        deserializeMaze();
    }

    public static void deserializePartial() {
        initStatesPartial();


        deserializeValues();
        deserializeMaze();
    }


    public static void deserializeValues() {
        ArrayList<Double> v = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream("values_train");
            ObjectInputStream ois = new ObjectInputStream(fis);

            v = (ArrayList) ois.readObject();

            ois.close();
            fis.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return;
        } catch (ClassNotFoundException c) {
            System.out.println("Class not found");
            c.printStackTrace();
            return;
        }

//        transform into []
        double max = -1.0;
        for (int x = 0; x < v.size(); x++) {
            if (!Double.isInfinite(v.get(x)))
                max = Math.max(max, v.get(x));

        }


        for (int x = 0; x < v.size(); x++) {
            if (Double.isInfinite(v.get(x)))
                values[x][0] = max + 10.0;
            else
                values[x][0] = v.get(x);
        }


    }

    public static void deserializeMaze() {
        try {
            FileInputStream fis = new FileInputStream("maze");
            ObjectInputStream ois = new ObjectInputStream(fis);

            maze = (ArrayList) ois.readObject();

            ois.close();
            fis.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return;
        } catch (ClassNotFoundException c) {
            System.out.println("Class not found");
            c.printStackTrace();
            return;
        }

//        //Verify list m
//        for (ArrayList<Graph.Node> employee : maze) {
//            System.out.println(employee);
//        }
    }

    public static Result complete(ArrayList<ArrayList<Graph.Node>> maze) {
//        initializes all player positions
        Agent agent = new Agent();
        Prey prey = new Prey(agent);
        Predator predator = new Predator(agent);

        int count = 1;

//        will return only when Agent dies or succeeds
        while (true) {
//            hung
            if (count == 5000)
                return new Result(false, false, false, false, count, count);
//            System.out.println("Agent:\t\t" + agent.getCell());
//            System.out.println("Prey:\t\t" + prey.getCell());
//            System.out.println("Predator:\t" + predator.getCell());


            List<Graph.Node> neighbors = getNextAgentStates(agent.getCell());

            int cell = -1;
            double value = Double.POSITIVE_INFINITY;
            for (Graph.Node n : neighbors) {


//                if (ustar.get(new State(n.getCell(), prey.getCell(), predator.getCell())) == null || Double.isInfinite(ustar.get(new State(n.getCell(), prey.getCell(), predator.getCell()))) || Double.isNaN(ustar.get(new State(n.getCell(), prey.getCell(), predator.getCell()))))
//                    continue;
                double currUtil;
                if (n.getCell() == predator.getCell() && n.getCell() != prey.getCell()) {
                    currUtil = Double.POSITIVE_INFINITY;
                } else if (Predator.bfs(n.getCell(), predator.getCell(), maze).size() == 2) {
                    currUtil = Double.POSITIVE_INFINITY;
                } else {
                    currUtil = net.predict(stateToHot(new State(n.getCell(), prey.getCell(), predator.getCell()))).get(0);
                }
                if (value > currUtil) {
                    cell = n.getCell();
                    value = currUtil;
                }
            }

//            attempts to get out of trap
            if (cell == -1) {
                ArrayList<Integer> distances = new ArrayList<>();
                for (int x = 0; x < neighbors.size(); x++) {
                    List<Graph.Node> predToAgent = Predator.bfs(neighbors.get(x).getCell(), predator.getCell(), maze);
                    distances.add(predToAgent.size());
                }
//            randomly choose neighbor for ties
                int max = Collections.max(distances);

                ArrayList<Integer> indices = new ArrayList<>();
                for (int x = 0; x < distances.size(); x++) {
                    if (distances.get(x) == max) {
                        indices.add(neighbors.get(x).getCell());
                    }
                }
                int randInt = new Random().nextInt(indices.size());

                agent.setCell(indices.get(randInt));
            } else {
                agent.setCell(cell);
            }


//            win
            if (agent.getCell() == prey.getCell()) {
                return new Result(false, true, false, false, 0, count);
            } else if (agent.getCell() == predator.getCell())
                return new Result(false, false, true, false, 0, count);

//          prey move
            prey.setCell(Prey.choosesNeighbors(prey.getCell(), maze));
//            win
            if (agent.getCell() == prey.getCell()) {
                return new Result(false, false, false, true, 0, count);
            }
//            pred move
            List<Graph.Node> predatorNeighbors = getNextPredatorStates(predator.getCell());
            ArrayList<Integer> distances = new ArrayList<>();

            for (int x = 0; x < predatorNeighbors.size(); x++) {
                List<Graph.Node> agentList = Predator.bfs(predatorNeighbors.get(x).getCell(), agent.getCell(), maze);
                distances.add(agentList.size());
            }
//            randomly choose neighbor for ties
            int min = Collections.min(distances);

            ArrayList<Integer> indices = new ArrayList<>();
            for (int x = 0; x < distances.size(); x++) {
                if (distances.get(x) == min) {
                    indices.add(x);
                }
            }
            int randInt = new Random().nextInt(indices.size());

//             if prob is <= 6 then it chooses shortest path. Else, chooses randomly
            int prob = new Random().nextInt(10) + 1;
            if (prob <= 6) {
                predator.setCell(predatorNeighbors.get(indices.get(randInt)).getCell());
            } else {
                predator.setCell(Predator.choosesNeighbors(predator.getCell(), maze));
            }
//            dead
            if (agent.getCell() == predator.getCell()) {
                return new Result(true, false, false, false, 0, count);
            }

            count++;

        }

    }

    public static Result partial(ArrayList<ArrayList<Graph.Node>> maze) {
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
        while (true) {
//            hung
            if (count == 5000)
                return new Result(false, false, false, false, surveyRate / (double) count, count, count);


//            random survey
            int surveyedNode = randomSurvey();
            if (prey.getCell() == surveyedNode) {
                bayes(true, prey.getCell());
                surveyRate++;
            } else {
                bayes(false, surveyedNode);
            }
//            update beliefs
            normalize();


            List<Graph.Node> neighbors = getNextAgentStates(agent.getCell());

            int cell = -1;
            double value = Double.POSITIVE_INFINITY;
            for (Graph.Node n : neighbors) {
                double currUtil;
                if (n.getCell() == predator.getCell() && n.getCell() != prey.getCell()) {
                    currUtil = Double.POSITIVE_INFINITY;
                } else if (Predator.bfs(n.getCell(), predator.getCell(), maze).size() == 2) {
                    currUtil = Double.POSITIVE_INFINITY;
                } else {
                    currUtil = netPartial.predictPartial(stateToHotPartial(new State(n.getCell(), prey.getCell(), predator.getCell()))).get(0);
                }
                if (value > currUtil) {
                    cell = n.getCell();
                    value = currUtil;
                }
            }


            if (cell == -1) {
                ArrayList<Integer> distances = new ArrayList<>();
                for (int x = 0; x < neighbors.size(); x++) {
                    List<Graph.Node> predToAgent = Predator.bfs(neighbors.get(x).getCell(), predator.getCell(), maze);
                    distances.add(predToAgent.size());
                }
//            randomly choose neighbor for ties
                int max = Collections.max(distances);

                ArrayList<Integer> indices = new ArrayList<>();
                for (int x = 0; x < distances.size(); x++) {
                    if (distances.get(x) == max) {
                        indices.add(neighbors.get(x).getCell());
                    }
                }
                int randInt = new Random().nextInt(indices.size());

                agent.setCell(indices.get(randInt));
            } else {
                agent.setCell(cell);
            }

//            win
            if (agent.getCell() == prey.getCell()) {
                surveyRate++;
                return new Result(false, true, false, false, surveyRate / ((double) count + 1), 0, count);
            } else if (agent.getCell() == predator.getCell())
                return new Result(false, false, true, false, surveyRate / ((double) count + 1), 0, count);
            bayes(false, agent.getCell());
            normalize();
//          prey move
            prey.setCell(Prey.choosesNeighbors(prey.getCell(), maze));
//            win
            if (agent.getCell() == prey.getCell()) {
                return new Result(false, false, false, true, surveyRate / ((double) count + 1), 0, count);
            }
//            distributes beliefs
            matmul();

//            pred move
            List<Graph.Node> predatorNeighbors = maze.get(predator.getCell()).subList(1, maze.get(predator.getCell()).size());
            ArrayList<Integer> distances = new ArrayList<>();

            for (int x = 0; x < predatorNeighbors.size(); x++) {
                List<Graph.Node> agentList = Predator.bfs(predatorNeighbors.get(x).getCell(), agent.getCell(), maze);
                distances.add(agentList.size());
            }
//            randomly choose neighbor for ties
            int min = Collections.min(distances);

            ArrayList<Integer> indices = new ArrayList<>();
            for (int x = 0; x < distances.size(); x++) {
                if (distances.get(x) == min) {
                    indices.add(x);
                }
            }
            int randInt = new Random().nextInt(indices.size());

//             if prob is <= 6 then it chooses shortest path. Else, chooses randomly
            int prob = new Random().nextInt(10) + 1;
            if (prob <= 6) {
                predator.setCell(predatorNeighbors.get(indices.get(randInt)).getCell());
            } else {
                predator.setCell(Predator.choosesNeighbors(predator.getCell(), maze));
            }
//            dead
            if (agent.getCell() == predator.getCell()) {
                return new Result(true, false, false, false, surveyRate / ((double) count + 1), 0, count);
            }

            count++;


        }

    }


    //    return array list of possible agent actions
    static List<Graph.Node> getNextAgentStates(int agent) {
        return maze.get(agent).subList(1, maze.get(agent).size());
    }

    static List<Graph.Node> getNextPredatorStates(int predator) {
        return maze.get(predator).subList(1, maze.get(predator).size());
    }

    //    updates belief when new node is surveyed
    public static void bayes(boolean found, int cell) {
//        if node surveyed contains prey
        if (found) {
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
                if (x == cell) {
                    belief[x] = 0;
                } else {
                    belief[x] /= (1.0 - removedProbability);
                }
            }
        }
    }

    //  returns greatest probability in belief; used in conjunction with maxIndex
    public static double maxBelief() {
        return Arrays.stream(belief).max().getAsDouble();
    }

    //    initializes 1/49 for every non-agent cell
    public static void initialBelief(int agentCell) {
        for (int x = 0; x < belief.length; x++) {
            if (x != agentCell) {
                belief[x] = 1.0 / (49);
            } else {
                belief[x] = 0.0;
            }

        }
    }

    //    sums up belief for normalization and error checking
    public static double beliefSum(double[] array) {
        return Arrays.stream(array).sum();
    }

    //    never changes
    public static void initTransMatrix(ArrayList<ArrayList<Graph.Node>> maze) {
        for (int x = 0; x < 50; x++) {
            for (int y = 0; y < 50; y++) {
                transMatrix[x][y] = 0;
            }
        }
        for (int x = 0; x < maze.size(); x++) {
            for (int y = 0; y < maze.get(x).size(); y++) {
                transMatrix[maze.get(x).get(0).getCell()][maze.get(x).get(y).getCell()] = 1.0 / (maze.get(x).size());
            }
        }
    }

    //    returns random cell that has the highest likelihood of being prey
    public static int randomSurvey() {
        ArrayList<Integer> indices = new ArrayList<>();
        double max = maxBelief();
        for (int x = 0; x < belief.length; x++) {
//            stores all indices that are have a probability of having prey
            if (belief[x] == max) {
                indices.add(x);
            }
        }
        if (indices.size() == 1)
            return indices.get(0);
//        randomly chooses index from arraylist
        int randInt = new Random().nextInt(indices.size());
        return indices.get(randInt);
    }

    //    updates belief after no new info
    public static void matmul() {
        double[] arr = belief.clone();
        for (int x = 0; x < 50; x++) {
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
    public static void normalize() {
        double sum = beliefSum(belief);
        for (int x = 0; x < 50; x++) {
            belief[x] /= sum;
        }
    }


    public static int[] stateToHot(State state) {
        int[] temp = new int[150];
        int agent = state.getAgent();
        int prey = state.getPrey() + 50;
        int predator = state.getPredator() + 100;
        for (int y = 0; y < 150; y++) {
            if (agent == y)
                temp[y] = 1;
            else if (prey == y)
                temp[y] = 1;
            else if (predator == y)
                temp[y] = 1;
            else
                temp[y] = 0;
        }
        return temp;
    }

    public static double[] stateToHotPartial(State state) {

        double[] temp = new double[150];
        int agent = state.getAgent();
        for (int y = 0; y < 50; y++) {
            if (agent == y)
                temp[y] = 1;
            else
                temp[y] = 0;
        }
        for (int y = 50; y < 100; y++) {
            temp[y] = belief[y - 50];
//            if ((agent +50) == y)
//                temp.add(0.0);
//            else
//                temp.add(1.0/49.0);
        }
        int predator = state.getPredator() + 100;
        for (int y = 100; y < 150; y++) {
            if (predator == y)
                temp[y] = 1;
            else
                temp[y] = 0;
        }
//        for(double t: temp){
//            System.out.print(t + " ");
//        }
//        System.out.println();
        return temp;
    }


    public static void serializeV() {
        try {
//            INPUT YOUR OWN FILE NAME
            FileOutputStream fos = new FileOutputStream("v_complete");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(net);
            oos.close();
            fos.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public static void deserializeV() {
        try {
            FileInputStream fis = new FileInputStream("v_complete");
            ObjectInputStream ois = new ObjectInputStream(fis);

            net = (Network) ois.readObject();

            ois.close();
            fis.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return;
        } catch (ClassNotFoundException c) {
            System.out.println("Class not found");
            c.printStackTrace();
            return;
        }

    }

    public static void serializeVpartial() {
        try {
//            INPUT YOUR OWN FILE NAME
            FileOutputStream fos = new FileOutputStream("v_partial");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(netPartial);
            oos.close();
            fos.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public static void deserializeVpartial() {
        try {
            FileInputStream fis = new FileInputStream("v_partial");
            ObjectInputStream ois = new ObjectInputStream(fis);

            netPartial = (Network) ois.readObject();

            ois.close();
            fis.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return;
        } catch (ClassNotFoundException c) {
            System.out.println("Class not found");
            c.printStackTrace();
            return;
        }


    }

    public static void train() {
        long total = System.nanoTime();
        deserialize();
        long endTime = System.nanoTime();
        long duration = (endTime - total) / (long) Math.pow(10, 9);
        System.out.println("Serialization: " + duration);
        System.out.println("Done Serialization. Initiate Training.");

        net = new Network();
        net.add(new HiddenLayer(150, 150, true));
        net.add(new HiddenLayer(150, 150, true));
        net.add(new HiddenLayer(150, 1, false));


        net.fit(states, values, 100, 0.0001);

        serializeV();


    }

    public static void trainPartial() {
        long total = System.nanoTime();
        deserializePartial();
        long endTime = System.nanoTime();
        long duration = (endTime - total) / (long) Math.pow(10, 9);
        System.out.println("Serialization: " + duration);
        System.out.println("Done Serialization. Initiate Training.");

        netPartial = new Network();
        netPartial.add(new HiddenLayer(150, 150, true));
        netPartial.add(new HiddenLayer(150, 150, true));
        netPartial.add(new HiddenLayer(150, 1, false));

        netPartial.fitPartial(statesPartial, values, 100, 0.00005);

        serializeVpartial();


    }


    public static void run() {
//        keeps track of iter
        long total = System.nanoTime();
        initStates();
        deserializeV();
        deserializeVpartial();
        deserializeMaze();

        int[] agentSuccess = new int[3];
        int[] hung = new int[3];
        int[] predatorSuccess = new int[3];
        int[] agentFail = new int[3];
        int[] preyDeath = new int[3];
        int[] steps = new int[3];
        double[] preySurveyRate = new double[3];
        for (int x = 0; x < 3000; x++) {
            Result resultOne = complete(maze);
            if (resultOne.predatorCatchesAgent) {
//                    predator catches agent
                predatorSuccess[1]++;
//                System.out.println("DEATH");

            } else if (resultOne.agentCatchesPrey) {
//                    agent catches prey
                agentSuccess[1]++;
            } else if (resultOne.agentRunsPredator) {
//                    agent runs into agent
                agentFail[1]++;
            } else if (resultOne.preyRunsAgent) {
//                    prey runs into agent
                preyDeath[1]++;
            } else if (resultOne.hung > 0) {
//                    hung
                hung[1]++;
            }
            steps[1] += resultOne.steps;

            Result resultTwo = partial(maze);
            if (resultTwo.predatorCatchesAgent) {
//                    predator catches agent
                predatorSuccess[2]++;
            } else if (resultTwo.agentCatchesPrey) {
//                    agent catches prey
                agentSuccess[2]++;
            } else if (resultTwo.agentRunsPredator) {
//                    agent runs into agent
                agentFail[2]++;
            } else if (resultTwo.preyRunsAgent) {
//                    prey runs into agent
                preyDeath[2]++;
            } else if (resultTwo.hung > 0) {
//                    hung
                hung[2]++;
            }
            preySurveyRate[2] += resultTwo.surveyRate;
            steps[2] += resultTwo.steps;

            long endTime = System.nanoTime();
            long duration = (endTime - total) / (long) Math.pow(10, 9);
            System.out.println("Iter: " + x + "; Time: " + duration);

        }
        long endTime = System.nanoTime();
        long duration = (endTime - total) / (long) Math.pow(10, 9);
        System.out.println("Total Time: " + duration);
        for (int x = 1; x < agentSuccess.length; x++) {
            System.out.println("V*: " + ((agentSuccess[x] + preyDeath[x]) / 3000.0) * 100);
            System.out.println("Predator Catches Agent: " + predatorSuccess[x]);
            System.out.println("Predator Catches Agent out of total Loss: " + predatorSuccess[x] / ((double) predatorSuccess[x] + agentFail[x] + hung[x]));
            System.out.println("Agent Catches Prey: " + agentSuccess[x]);
            System.out.println("Prey Runs into Agent: " + preyDeath[x]);
            System.out.println("Agent Runs into Predator: " + agentFail[x]);
            System.out.println("Average number of steps: " + steps[x] / 3000.0);
            System.out.println("Average Prey Survey Rate: " + ((preySurveyRate[x] / 3000.0) * 100));
            System.out.println("Hung out of Loss: " + (double) hung[x] / ((double) predatorSuccess[x] + agentFail[x] + hung[x]));
            System.out.println();
        }


    }


    public static void main(String[] args) {
        long total = System.nanoTime();
        train();
        trainPartial();
        run();
        long endTime = System.nanoTime();
        long duration = (endTime - total) / (long) Math.pow(10, 9);
        System.out.println("Total Time: " + duration);


    }


}

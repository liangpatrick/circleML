package Agents;

import Environment.*;

import java.util.*;

import static Agents.CompleteInformation.searchPred;
import static Agents.CompleteInformation.searchPrey;

public class PartialPrey {
    static double[] belief = new double[50];
    static double[][] transMatrix = new double[50][50];

    public static Result agentThree(ArrayList<ArrayList<Graph.Node>> maze) {
//        initializes all player positions
        Agent agent = new Agent();
        Prey prey = new Prey(agent);
        Predator predator = new Predator(agent);
//        initialize belief vector and transition matrix
        initTransMatrix(maze);
        initialBelief(agent.getCell());

        int count = 0;
        double surveyRate = 0;
//        will return only when Agent dies or succeeds
        while (true) {
//            hung
            if (count == 5000)
                return new Result(false, false, false, false, surveyRate / (double) count, count, count);
//            creates arraylists of neighbors, predator distances, and prey distances
            ArrayList<Graph.Node> neighbors = maze.get(agent.getCell());
            ArrayList<Integer> predatorDistances = new ArrayList<>();
            ArrayList<Integer> preyDistances = new ArrayList<>();
//            random survey
            int surveyedNode = randomSurvey();
            if (prey.getCell() == surveyedNode) {
                surveyRate++;
                bayes(true, prey.getCell());
            } else {
                bayes(false, surveyedNode);
            }
//            updates beliefs
            normalize();


            int preyCell = randomSurvey();
//            adds distances to predator/prey from all neighbors
            for (int x = 0; x < neighbors.size(); x++) {
                List<Graph.Node> predatorList = searchPred(neighbors.get(x).getCell(), predator.getCell(), maze);
                List<Graph.Node> preyList = searchPrey(neighbors.get(x).getCell(), preyCell, maze);
                predatorDistances.add(x, predatorList.size());
                preyDistances.add(x, preyList.size());

            }
//            stores distance of agent to predator/prey in a named variable
            int agentToPrey = preyDistances.get(0);
            int agentToPredator = predatorDistances.get(0);
//            used to store arraylist of possible ties
            HashMap<Integer, ArrayList<Integer>> moves = new HashMap<Integer, ArrayList<Integer>>();
//            initializes the arraylists
            for (int x = 0; x < 7; x++)
                moves.put(x, new ArrayList<>());
//            want to start iterating through neighbors and compare to agent position
            for (int x = 1; x < predatorDistances.size(); x++) {
                int currPredator = predatorDistances.get(x);
                int currPrey = preyDistances.get(x);
//                logical statements provided in the writeup
                if (currPrey < agentToPrey && currPredator > agentToPredator) {
                    moves.get(0).add(neighbors.get(x).getCell());
                } else if (currPrey < agentToPrey && currPredator == agentToPredator) {
                    moves.get(1).add(neighbors.get(x).getCell());
                } else if (currPrey == agentToPrey && currPredator > agentToPredator) {
                    moves.get(2).add(neighbors.get(x).getCell());
                } else if (currPrey == agentToPrey && currPredator == agentToPredator) {
                    moves.get(3).add(neighbors.get(x).getCell());
                } else if (currPredator > agentToPredator) {
                    moves.get(4).add(neighbors.get(x).getCell());
                } else if (currPredator == agentToPredator) {
                    moves.get(5).add(neighbors.get(x).getCell());
                } else {
                    moves.get(6).add(agent.getCell());
                }


            }
//          looks for the first non-zero sized list out of all possible moves
            ArrayList<Integer> random = new ArrayList<>();
            for (int x = 0; x < 7; x++) {
                if (moves.get(x).size() > 0) {
                    random = moves.get(x);
                    break;
                }
            }
//            randomly choose neighbor for ties
            int rand = new Random().nextInt(random.size());
            agent.setCell(random.get(rand));
//            win
            if (agent.getCell() == prey.getCell()) {
                return new Result(false, true, false, false, surveyRate / ((double) count + 1), 0, count);
            } else if (agent.getCell() == predator.getCell())
                return new Result(false, false, true, false, surveyRate / ((double) count + 1), 0, count);
//            updates beliefs
            bayes(false, agent.getCell());
            normalize();
//          prey move

            prey.setCell(Prey.choosesNeighbors(prey.getCell(), maze));
//            win
            if (agent.getCell() == prey.getCell()) {
                return new Result(false, false, false, true, surveyRate / ((double) count + 1), 0, count);
            }
//            belief distribution
            matmul();
//            System.out.println(beliefSum(belief));
//            normalize();


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

    public static Result agentFour(ArrayList<ArrayList<Graph.Node>> maze) {
//        initializes all player positions
        Agent agent = new Agent();
        Prey prey = new Prey(agent);
        Predator predator = new Predator(agent);
        initTransMatrix(maze);
        initialBelief(agent.getCell());

        int count = 0;
        double surveyRate = 0;
//        will return only when Agent dies or succeeds
        while (true) {
//            hung
            if (count == 5000)
                return new Result(false, false, false, false, surveyRate / (double) count, count, count);
//            creates arraylists of neighbors, predator distances, and prey distances
            ArrayList<Graph.Node> neighbors = maze.get(agent.getCell());

//            random survey
            int surveyedNode = randomSurvey();
            if (prey.getCell() == surveyedNode) {
                bayes(true, prey.getCell());
            } else {
                bayes(false, surveyedNode);
            }
//            update beliefs
            normalize();

            ArrayList<Graph.Node> preyNeighbors = maze.get(maxIndex(maxBelief()));

//            calls utility function
            int cell = bestCell(neighbors, predator, preyNeighbors, maze);
            agent.setCell(cell);

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
//            normalize();


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


    //    utility function
    public static int bestCell(ArrayList<Graph.Node> neighbors, Predator predator, ArrayList<Graph.Node> preyNeighbors, ArrayList<ArrayList<Graph.Node>> maze) {

//      stores utility of all agent cells
        ArrayList<Double> utilities = new ArrayList<>();
        ArrayList<Double> preyDistances = new ArrayList<>();
        ArrayList<Integer> predatorDistances = new ArrayList<>();
//        collects all preyDistances and predatorDistances
        for (int x = 0; x < neighbors.size(); x++) {
            double aveDistance = 0.0;
//          gets of average distances from each possible agent to cluster of prey neighbors
            for (int y = 0; y < preyNeighbors.size(); y++) {
                int currDistance = searchPrey(neighbors.get(x).getCell(), preyNeighbors.get(y).getCell(), maze).size();
                aveDistance += currDistance;


            }
            aveDistance /= preyNeighbors.size();
            preyDistances.add(aveDistance);
            List<Graph.Node> predatorList = searchPred(neighbors.get(x).getCell(), predator.getCell(), maze);
            predatorDistances.add(predatorList.size());
            utilities.add(predatorList.size() - aveDistance);
        }
//        arbitrary number
        double weightPrey = 1.0 / 2;
        double weightPredator = -1.0 / 2;
//        updates utility of cell depending  on whether current cell has closest distance to predator or closest distance to prey
        for (int x = 0; x < neighbors.size(); x++) {
            if (Collections.min(preyDistances) == preyDistances.get(x))
                utilities.set(x, utilities.get(x) + 75 * (weightPrey / preyDistances.size()));
            if (Collections.min(predatorDistances) == predatorDistances.get(x))
                utilities.set(x, utilities.get(x) + 100 * (weightPredator / predatorDistances.size()));
        }

//        Two options: 1) move towards cell with highest utility when all greatest utility is positive2) move away from predator
        if (Collections.max(utilities) > 0)
            return neighbors.get(utilities.indexOf(Collections.max(utilities))).getCell();
        else
            return neighbors.get(predatorDistances.indexOf(Collections.max(predatorDistances))).getCell();
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


    //    returns most probable prey cell; used in conjunction with maxIndex
    public static int maxIndex(double value) {
        for (int x = 0; x < belief.length; x++) {
            if (value == belief[x])
                return x;
        }
        return -1;
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

}
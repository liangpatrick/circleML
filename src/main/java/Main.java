import Agents.*;
import Environment.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    static ArrayList<ArrayList<Graph.Node>> maze = new ArrayList<>();

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


    public static void runAll() {
        int[] agentSuccess = new int[5];
        int[] hung = new int[5];
        int[] predatorSuccess = new int[5];
        int[] agentFail = new int[5];
        int[] preyDeath = new int[5];
        double[] preySurveyRate = new double[5];
        double[] predatorSurveyRate = new double[5];

        long total = System.nanoTime();
        deserializeMaze();

        for (int runs = 1; runs <= 3000; runs++) {
            Result resultOne = CompleteInformation.agentOne(maze);
            if (resultOne.predatorCatchesAgent) {
//                    predator catches agent
                predatorSuccess[1]++;
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

            Result resultTwo = CompleteInformation.agentTwo(maze);
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

            Result resultThree = PartialPrey.agentThree(maze);
            if (resultThree.predatorCatchesAgent) {
//                    predator catches agent
                predatorSuccess[3]++;
            } else if (resultThree.agentCatchesPrey) {
//                    agent catches prey
                agentSuccess[3]++;
            } else if (resultThree.agentRunsPredator) {
//                    agent runs into agent
                agentFail[3]++;
            } else if (resultThree.preyRunsAgent) {
//                    prey runs into agent
                preyDeath[3]++;
            } else if (resultThree.hung > 0) {
//                    hung
                hung[3]++;
            }
            preySurveyRate[3] += resultThree.surveyRate;

            Result resultFour = PartialPrey.agentFour(maze);
            if (resultFour.predatorCatchesAgent) {
//                    predator catches agent
                predatorSuccess[4]++;
            } else if (resultFour.agentCatchesPrey) {
//                    agent catches prey
                agentSuccess[4]++;
            } else if (resultFour.agentRunsPredator) {
//                    agent runs into agent
                agentFail[4]++;
            } else if (resultFour.preyRunsAgent) {
//                    prey runs into agent
                preyDeath[4]++;
            } else if (resultFour.hung > 0) {
//                    hung
                hung[4]++;
            }
            preySurveyRate[4] += resultFour.surveyRate;


            long endTime = System.nanoTime();
            long duration = (endTime - total) / (long) Math.pow(10, 9);
            System.out.println("Iter: " + runs + "; Time:" + duration);

        }
        long endTime = System.nanoTime();
        long duration = (endTime - total) / (long) Math.pow(10, 9);
        System.out.println(duration);
        for (int x = 1; x < agentSuccess.length; x++) {

            System.out.println("A" + x + ": " + ((agentSuccess[x] + preyDeath[x]) / 3000.0) * 100);
            System.out.println("Predator Catches Agent: " + predatorSuccess[x]);
            System.out.println("Predator Catches Agent out of total Loss: " + predatorSuccess[x] / ((double) predatorSuccess[x] + agentFail[x] + hung[x]));
            System.out.println("Agent Catches Prey: " + agentSuccess[x]);
            System.out.println("Prey Runs into Agent: " + preyDeath[x]);
            System.out.println("Agent Runs into Predator: " + agentFail[x]);
            System.out.println("Average Prey Survey Rate: " + ((preySurveyRate[x] / 3000.0) * 100));
            System.out.println("Average Predator Survey Rate: " + ((predatorSurveyRate[x] / 3000.0) * 100));
            System.out.println("Hung out of Loss: " + (double) hung[x] / ((double) predatorSuccess[x] + agentFail[x] + hung[x]));
            System.out.println("Predator Catches Agent: " + predatorSuccess[x]);
            System.out.println();
        }


    }


    public static void main(String args[]) {
        runAll();


    }


}

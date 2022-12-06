import Agents.*;
import Environment.*;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void runAll(){
        int[] agentSuccess           = new int[13];
        int[] hung                   = new int[13];
        int[] predatorSuccess        = new int[13];
        int[] agentFail              = new int[13];
        int[] preyDeath              = new int[13];
        double[] preySurveyRate      = new double[13];
        double[] predatorSurveyRate  = new double[13];

        long total = System.nanoTime();

        for(int graphs = 1; graphs <= 30; graphs++){
            ArrayList<ArrayList<Graph.Node>> maze = Graph.buildGraph();
            for(int runs = 1; runs <= 100; runs++) {
                Result resultOne = CompleteInformation.agentOne(maze);
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

                Result resultTwo = CompleteInformation.agentTwo(maze);
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

                Result resultThree = PartialPrey.agentThree(maze);
                if (resultThree.predatorCatchesAgent) {
//                    predator catches agent
                    predatorSuccess[3]++;
                }
                else if (resultThree.agentCatchesPrey) {
//                    agent catches prey
                    agentSuccess[3]++;
                }
                else if (resultThree.agentRunsPredator) {
//                    agent runs into agent
                    agentFail[3]++;
                }
                else if (resultThree.preyRunsAgent) {
//                    prey runs into agent
                    preyDeath[3]++;
                }
                else if (resultThree.hung > 0) {
//                    hung
                    hung[3]++;
                }
                preySurveyRate[3] += resultThree.surveyRate;

                Result resultFour = PartialPrey.agentFour(maze);
                if (resultFour.predatorCatchesAgent) {
//                    predator catches agent
                    predatorSuccess[4]++;
                }
                else if (resultFour.agentCatchesPrey) {
//                    agent catches prey
                    agentSuccess[4]++;
                }
                else if (resultFour.agentRunsPredator) {
//                    agent runs into agent
                    agentFail[4]++;
                }
                else if (resultFour.preyRunsAgent) {
//                    prey runs into agent
                    preyDeath[4]++;
                }
                else if (resultFour.hung > 0) {
//                    hung
                    hung[4]++;
                }
                preySurveyRate[4] += resultFour.surveyRate;



            }
            long endTime = System.nanoTime();
            long duration = (endTime - total)/(long)Math.pow(10,9);
            System.out.println("Iter: " + graphs + "; Time:" + duration);

        }
        long endTime = System.nanoTime();
        long duration = (endTime - total)/(long)Math.pow(10,9);
        System.out.println(duration);
        for(int x = 1; x < agentSuccess.length; x++){

            if(x < 9) {
                System.out.println("A" + x + ": " + ((agentSuccess[x] + preyDeath[x])/3000.0) * 100);
            }
            else if(x == 9){
                System.out.println("A7 Faulty: " + ((agentSuccess[x] + preyDeath[x])/3000.0) * 100);
            }
            else if(x == 10){
                System.out.println("A8 Faulty: " + ((agentSuccess[x] + preyDeath[x])/3000.0) * 100);
            }
            else if(x == 11){
                System.out.println("A7 Faulty Fixed: " + ((agentSuccess[x] + preyDeath[x])/3000.0) * 100);
            }
            else if(x == 12){
                System.out.println("A8 Faulty Fixed: " + ((agentSuccess[x] + preyDeath[x])/3000.0) * 100);
            }
            System.out.println("Predator Catches Agent: " + predatorSuccess[x]);
            System.out.println("Predator Catches Agent out of total Loss: " + predatorSuccess[x]/((double)predatorSuccess[x] + agentFail[x]+hung[x]));
            System.out.println("Agent Catches Prey: " + agentSuccess[x]);
            System.out.println("Prey Runs into Agent: " + preyDeath[x]);
            System.out.println("Agent Runs into Predaotr: " + agentFail[x]);
            System.out.println("Average Prey Survey Rate: " + ((preySurveyRate[x] / 3000.0 )* 100));
            System.out.println("Average Predator Survey Rate: " + ((predatorSurveyRate[x] / 3000.0)*100));
            System.out.println("Hung out of Loss: " + (double)hung[x]/((double)predatorSuccess[x] + agentFail[x]+hung[x]));
            System.out.println("Predator Catches Agent: " + predatorSuccess[x]);
            System.out.println();
        }



    }



    public static void main (String args[]) {
//        runAll();






//        policy.put(new State(0, 0 , 0), 0.0);
//        for(State s: policy.keySet()){
//            System.out.println(s);
//            System.out.println(policy.get(s));
//        }
//        System.out.println(policy.get(new State(0, 0, 0)));
//        HashMap<State, Double> a = policy.entrySet().stream()
//                .collect(Collectors.toMap(e -> e.getKey(), e -> List.copyOf(e.getValue())));
//
//        System.out.println(a.get(new State(0, 0, 0)));
//        policy.put(new State(0, 0 , 0), 1.0);
//        System.out.println(a.get(new State(0, 0, 0)));
    }


}

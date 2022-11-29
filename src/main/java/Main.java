import Agents.*;
import Environment.*;

import java.util.*;

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

                Result resultFive = PartialPredator.agentFive(maze);
                if (resultFive.predatorCatchesAgent) {
//                    predator catches agent
                    predatorSuccess[5]++;
                }
                else if (resultFive.agentCatchesPrey) {
//                    agent catches prey
                    agentSuccess[5]++;
                }
                else if (resultFive.agentRunsPredator) {
//                    agent runs into agent
                    agentFail[5]++;
                }
                else if (resultFive.preyRunsAgent) {
//                    prey runs into agent
                    preyDeath[5]++;
                }
                else if (resultFive.hung > 0) {
//                    hung
                    hung[5]++;
                }
                predatorSurveyRate[5] += resultFive.surveyRate;

                Result resultSix = PartialPredator.agentSix(maze);
                if (resultSix.predatorCatchesAgent) {
//                    predator catches agent
                    predatorSuccess[6]++;
                }
                else if (resultSix.agentCatchesPrey) {
//                    agent catches prey
                    agentSuccess[6]++;
                }
                else if (resultSix.agentRunsPredator) {
//                    agent runs into agent
                    agentFail[6]++;
                }
                else if (resultSix.preyRunsAgent) {
//                    prey runs into agent
                    preyDeath[6]++;
                }
                else if (resultSix.hung > 0) {
//                    hung
                    hung[6]++;
                }
                predatorSurveyRate[6] += resultSix.surveyRate;

                Result resultSeven = CombinedPartialInformation.agentSeven(maze);
                if (resultSeven.predatorCatchesAgent) {
//                    predator catches agent
                    predatorSuccess[7]++;
                }
                else if (resultSeven.agentCatchesPrey) {
//                    agent catches prey
                    agentSuccess[7]++;
                }
                else if (resultSeven.agentRunsPredator) {
//                    agent runs into agent
                    agentFail[7]++;
                }
                else if (resultSeven.preyRunsAgent) {
//                    prey runs into agent
                    preyDeath[7]++;
                }
                else if (resultSeven.hung > 0) {
//                    hung
                    hung[7]++;
                }
                predatorSurveyRate[7] += resultSeven.predatorSurveyRate;
                preySurveyRate[7] += resultSeven.preySurveyRate;

                Result resultEight = CombinedPartialInformation.agentEight(maze);
                if (resultEight.predatorCatchesAgent) {
//                    predator catches agent
                    predatorSuccess[8]++;
                }
                else if (resultEight.agentCatchesPrey) {
//                    agent catches prey
                    agentSuccess[8]++;
                }
                else if (resultEight.agentRunsPredator) {
//                    agent runs into agent
                    agentFail[8]++;
                }
                else if (resultEight.preyRunsAgent) {
//                    prey runs into agent
                    preyDeath[8]++;
                }
                else if (resultEight.hung > 0) {
//                    hung
                    hung[8]++;
                }
                predatorSurveyRate[8] += resultEight.predatorSurveyRate;
                preySurveyRate[8] += resultEight.preySurveyRate;

                Result resultSevenFaulty = FaultyCombinedPartialInformation.agentSevenFaulty(maze);
                if (resultSevenFaulty.predatorCatchesAgent) {
//                    predator catches agent
                    predatorSuccess[9]++;
                }
                else if (resultSevenFaulty.agentCatchesPrey) {
//                    agent catches prey
                    agentSuccess[9]++;
                }
                else if (resultSevenFaulty.agentRunsPredator) {
//                    agent runs into agent
                    agentFail[9]++;
                }
                else if (resultSevenFaulty.preyRunsAgent) {
//                    prey runs into agent
                    preyDeath[9]++;
                }
                else if (resultSevenFaulty.hung > 0) {
//                    hung
                    hung[9]++;
                }
                predatorSurveyRate[9] += resultSevenFaulty.predatorSurveyRate;
                preySurveyRate[9] += resultSevenFaulty.preySurveyRate;

                Result resultEightFaulty = FaultyCombinedPartialInformation.agentEightFaulty(maze);
                if (resultEightFaulty.predatorCatchesAgent) {
//                    predator catches agent
                    predatorSuccess[10]++;
                }
                else if (resultEightFaulty.agentCatchesPrey) {
//                    agent catches prey
                    agentSuccess[10]++;
                }
                else if (resultEightFaulty.agentRunsPredator) {
//                    agent runs into agent
                    agentFail[10]++;
                }
                else if (resultEightFaulty.preyRunsAgent) {
//                    prey runs into agent
                    preyDeath[10]++;
                }
                else if (resultEightFaulty.hung > 0) {
//                    hung
                    hung[10]++;
                }
                predatorSurveyRate[10] += resultEightFaulty.predatorSurveyRate;
                preySurveyRate[10] += resultEightFaulty.preySurveyRate;

                Result resultSevenFaultyFixed = FixedFaultyCombinedPartialInformation.agentSevenFaultyFixed(maze);
                if (resultSevenFaultyFixed.predatorCatchesAgent) {
//                    predator catches agent
                    predatorSuccess[11]++;
                }
                else if (resultSevenFaultyFixed.agentCatchesPrey) {
//                    agent catches prey
                    agentSuccess[11]++;
                }
                else if (resultSevenFaultyFixed.agentRunsPredator) {
//                    agent runs into agent
                    agentFail[11]++;
                }
                else if (resultSevenFaultyFixed.preyRunsAgent) {
//                    prey runs into agent
                    preyDeath[11]++;
                }
                else if (resultSevenFaultyFixed.hung > 0) {
//                    hung
                    hung[11]++;
                }
                predatorSurveyRate[11] += resultSevenFaultyFixed.predatorSurveyRate;
                preySurveyRate[11] += resultSevenFaultyFixed.preySurveyRate;

                Result resultEightFaultyFixed = FixedFaultyCombinedPartialInformation.agentEightFaultyFixed(maze);
                if (resultEightFaultyFixed.predatorCatchesAgent) {
//                    predator catches agent
                    predatorSuccess[12]++;
                }
                else if (resultEightFaultyFixed.agentCatchesPrey) {
//                    agent catches prey
                    agentSuccess[12]++;
                }
                else if (resultEightFaultyFixed.agentRunsPredator) {
//                    agent runs into agent
                    agentFail[12]++;
                }
                else if (resultEightFaultyFixed.preyRunsAgent) {
//                    prey runs into agent
                    preyDeath[12]++;
                }
                else if (resultEightFaultyFixed.hung > 0) {
//                    hung
                    hung[12]++;
                }
                predatorSurveyRate[12] += resultEightFaultyFixed.predatorSurveyRate;
                preySurveyRate[12] += resultEightFaultyFixed.preySurveyRate;

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
        runAll();


    }


}

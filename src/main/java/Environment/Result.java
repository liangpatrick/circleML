package Environment;

// used to store results of each agent
public class Result {
    public boolean predatorCatchesAgent;
    public boolean agentCatchesPrey;
    public boolean agentRunsPredator;
    public boolean preyRunsAgent;
    public double preySurveyRate;
    public double predatorSurveyRate;
    public int hung;
    public int steps;
//  used for both predator and prey when its not combined partial
    public double surveyRate;
//    combined partial
    public Result(boolean predatorCatchesAgent, boolean agentCatchesPrey, boolean agentRunsPredator, boolean preyRunsAgent, double preySurveyRate, double predatorSurveyRate, int hung, int steps){
        this.predatorCatchesAgent = predatorCatchesAgent;
        this.agentCatchesPrey = agentCatchesPrey;
        this.agentRunsPredator = agentRunsPredator;
        this.preyRunsAgent = preyRunsAgent;
        this.preySurveyRate = preySurveyRate;
        this.predatorSurveyRate = predatorSurveyRate;
        this.hung = hung;
        this.steps = steps;

    }
//    partial
    public Result(boolean predatorCatchesAgent, boolean agentCatchesPrey, boolean agentRunsPredator, boolean preyRunsAgent, double surveyRate, int hung, int steps){
        this.predatorCatchesAgent = predatorCatchesAgent;
        this.agentCatchesPrey = agentCatchesPrey;
        this.agentRunsPredator = agentRunsPredator;
        this.preyRunsAgent = preyRunsAgent;
        this.surveyRate = surveyRate;
        this.hung = hung;
        this.steps = steps;
    }
//    complete information
    public Result(boolean predatorCatchesAgent, boolean agentCatchesPrey, boolean agentRunsPredator, boolean preyRunsAgent, int hung, int steps){
        this.predatorCatchesAgent = predatorCatchesAgent;
        this.agentCatchesPrey = agentCatchesPrey;
        this.agentRunsPredator = agentRunsPredator;
        this.preyRunsAgent = preyRunsAgent;
        this.hung = hung;
        this.steps = steps;
    }


}

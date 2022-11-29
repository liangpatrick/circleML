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
//  used for both predator and prey when its not combined partial
    public double surveyRate;
//    combined partial
    public Result(boolean predatorCatchesAgent, boolean agentCatchesPrey, boolean agentRunsPredator, boolean preyRunsAgent, double preySurveyRate, double predatorSurveyRate, int hung){
        this.predatorCatchesAgent = predatorCatchesAgent;
        this.agentCatchesPrey = agentCatchesPrey;
        this.agentRunsPredator = agentRunsPredator;
        this.preyRunsAgent = preyRunsAgent;
        this.preySurveyRate = preySurveyRate;
        this.predatorSurveyRate = predatorSurveyRate;
        this.hung = hung;

    }
//    partial
    public Result(boolean predatorCatchesAgent, boolean agentCatchesPrey, boolean agentRunsPredator, boolean preyRunsAgent, double surveyRate, int hung){
        this.predatorCatchesAgent = predatorCatchesAgent;
        this.agentCatchesPrey = agentCatchesPrey;
        this.agentRunsPredator = agentRunsPredator;
        this.preyRunsAgent = preyRunsAgent;
        this.surveyRate = surveyRate;
        this.hung = hung;

    }
//    complete information
    public Result(boolean predatorCatchesAgent, boolean agentCatchesPrey, boolean agentRunsPredator, boolean preyRunsAgent, int hung){
        this.predatorCatchesAgent = predatorCatchesAgent;
        this.agentCatchesPrey = agentCatchesPrey;
        this.agentRunsPredator = agentRunsPredator;
        this.preyRunsAgent = preyRunsAgent;
        this.hung = hung;

    }


}

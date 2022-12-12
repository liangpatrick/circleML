package Agents;


import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class State {
    public int agent;
    public int prey;
    public int predator;

    public State(int agent, int prey, int predator) {
        this.agent = agent;
        this.prey = prey;
        this.predator = predator;
    }

    public int getAgent() {
        return agent;
    }

    public int getPrey() {
        return prey;
    }

    public int getPredator() {
        return predator;
    }

//    public void setAgent(int agent){
//        this.agent = agent;
//    }
//    public void setPrey(){
//        return prey;
//    }
//    public void setPredator(){
//        return predator;
//    }


    //        Create my own hashfunction and equals function using Apache Commons
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
//                 if deriving:
//        appendSuper(super.hashCode()).
        append(agent).
                append(prey).
                append(predator).
                toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof State))
            return false;
        if (obj == this)
            return true;

        State rhs = (State) obj;
        return new EqualsBuilder().append(agent, rhs.agent).append(prey, rhs.prey).append(predator, rhs.predator).isEquals();
    }

    @Override
    public String toString() {
        return ("Agent: " + agent + "; Prey: " + prey + "; Predator: " + predator);
    }
}

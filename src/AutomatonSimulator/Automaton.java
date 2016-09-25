package AutomatonSimulator;

import AutomatonParser.Visitor;
import AutomatonParser.lexer.Lexer;
import AutomatonParser.lexer.LexerException;
import AutomatonParser.node.Start;
import AutomatonParser.parser.Parser;
import AutomatonParser.parser.ParserException;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by fabian on 21.04.16.
 */
public class Automaton {

    /**
     * Contains all the states of the automaton.
     */
    private HashSet<State> states;

    /**
     * The initial state.
     */
    private State startState;

    /**
     * Contains all the input string, that are used for the transitions of this automaton.
     */
    private HashSet<String> allInputs;

    /**
     * true, if there are cyclic epsilon-transitions in this automaton.
     */
    private boolean epsilonCycle = false;

    /**
     * The constructor for an automaton with a given set of states and rules.
     *
     * @param states A set of states.
     * @param startState The initial states.
     * @param allInputs A set of input-strings.
     */
    public Automaton(HashSet<State> states, State startState, HashSet<String> allInputs) {
        this.states = states;
        this.startState = startState;
        this.allInputs = allInputs;
        this.removeEpsilonCycles();
        if(epsilonCycle) {
            System.out.println("Epsilon-cycles in this automaton have been removed automatically!");
        }
    }

    /**
     * The constructor for an empty automaton.
     * It just contains an initial state called "z0".
     */
    public Automaton() {
        this.startState = new State("z0", true, false, new HashSet<>());
        this.states = new HashSet<>();
        this.states.add(this.startState);
        this.allInputs = new HashSet<>();
    }

    /**
     * Gets rid of any cyclic epsilon-transitions.
     */
    private void removeEpsilonCycles() {

        //First, check if there any states, that have a rule going to themselves with the empty word as input.
        //If so, these rules can just be removed.
        for(State state : states) {
            //Make a local copy of the rules, so we don't get a "ConcurrentModificationException", while removing rules.
            HashSet<Rule> localRules = new HashSet<>(state.getRules());
            for(Rule rule : localRules) {
                if((rule.getAcceptedInputs().contains("lambda") || rule.getAcceptedInputs().contains("epsilon")) && rule.getGoingTo().equals(state)) {
                    state.getRules().remove(rule);
                }
            }
        }

        //For every state, call a method that checks if this state is part of a epsilon-cycle and if so, gets rid of this cycle.
        HashSet<State> localStates = new HashSet<>(states);
        for(State state : localStates) {
            removeEpsilonCycles(state, new HashSet<>(), false);
        }
    }

    /**
     * Checks, if we can get from a given state into a cycle of epsilon states.
     * If so, the cycle will be removed.
     *
     * @param startState The state, from which we start.
     * @param visitedStates Contains all states, that have already been visited. It is empty initially.
     * @param cycleFound true, if an epsilon-cycle has been found.
     */
    private void removeEpsilonCycles(State startState, HashSet<State> visitedStates, boolean cycleFound) {
        //Get all states, that are reachable with an empty word as input, from startState and startState to visitedStates.
        HashSet<State> nextStates = AutomatonUtil.nextStates(startState, "lambda");
        visitedStates.add(startState);


        for(State state : nextStates) {
            //If visitedStates contains the current state, we obviously found an epsilon-cycle.
            if(visitedStates.contains(state)) {
                HashSet<State> cycleStates = new HashSet<>();
                //If cycleFound is false, we call this method recursively with cycleFound = true and new empty HashSet for visitedStates.
                //That way, visitedStates will be calculated a second time.
                //This is necessary, because right now, there might still be states in it, that are not part of the cycle
                //Example: z0 --epsilon-> z1; z1 --epsilon--> z2; z2 --epsilon--> z1;
                //After that the code for removing the cycle gets executed.
                if(!cycleFound) {
                    removeEpsilonCycles(state, cycleStates, true);
                    epsilonCycle = true;
                }

                //This code removes an epsilon-cycle by merging all states that the cycle contains into one.
                if(cycleStates.size() > 0) {
                    String name = "";
                    boolean isStart = false;
                    boolean isFinal = false;
                    HashSet<Rule> rules = new HashSet<>();
                    //Iterate over all states of the cycle and merge their names into one name.
                    //Also, determine if the merged state is a start state and/or a final state.
                    for (State cycleState : cycleStates) {
                        name += cycleState.getName();
                        if (cycleState.isStart()) {
                            isStart = true;
                        }
                        if (cycleState.isFinal()) {
                            isFinal = true;
                        }
                        for (Rule rule : cycleState.getRules()) {
                            if (!cycleStates.contains(rule.getGoingTo())) {
                                rules.add(rule);
                            }
                        }
                    }

                    State newState = new State(name, isStart, isFinal, rules);

                    //Let all rules that pointed to one of the old states point to the newly created state.
                    for (State state1 : states) {
                        for (Rule rule : state1.getRules()) {
                            if (cycleStates.contains(rule.getGoingTo())) {
                                rule.setGoingTo(newState);
                            }
                        }
                    }

                    //Add the new state to the automaton and remove the old states.
                    states.add(newState);
                    states.removeAll(cycleStates);
                    if (isStart) {
                        this.startState = newState;
                    }
                }
                break;
            }
            removeEpsilonCycles(state, visitedStates, cycleFound);
        }
    }

    /**
     * Getter-method for {@link #states}.
     *
     * @return {@link #states}
     */
    public HashSet<State> getStates() {
        return states;
    }

    /**
     * Getter-method for {@link #startState}.
     *
     * @return {@link #startState}
     */
    public State getStartState() {
        return startState;
    }

    /**
     * Getter-method for {@link #allInputs}.
     *
     * @return {@link #allInputs}
     */
    public HashSet<String> getAllInputs() {
        return allInputs;
    }

    /**
     * Setter-method for {@link #startState}.
     *
     * @param startState The new value for {@link #startState}.
     */
    public void setStartState(State startState) {
        this.startState = startState;
    }
}

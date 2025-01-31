package GrammarSimulator;

import GrammarParser.analysis.DepthFirstAdapter;
import GrammarParser.node.*;
import GrammarSimulator.Grammar;
import GrammarSimulator.Nonterminal;
import GrammarSimulator.Symbol;
import GrammarSimulator.Terminal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * @author fabian
 * @since 06.08.16
 */
class Visitor extends DepthFirstAdapter {

    private Grammar grammar;
    private Nonterminal startSymbol;
    private HashMap<String, Terminal> terminals;
    private HashMap<String, Nonterminal> nonterminals;
    private HashSet<Rule> rules;

    @Override
    public void inASymbols(ASymbols node) {
        terminals = new HashMap<>();
        nonterminals = new HashMap<>();
        rules = new HashSet<>();

        for(TIdentifier nonterminal : node.getNonterminals()) {
            nonterminals.put(nonterminal.getText(), new Nonterminal(nonterminal.getText()));
        }

        for(TSymbol terminal : node.getTerminals()) {
            terminals.put(terminal.getText().replaceAll("'", ""), new Terminal(terminal.getText().replaceAll("'", "")));
        }

        startSymbol = new Nonterminal(node.getStartSymbol().getText());
        nonterminals.put(startSymbol.getName(), startSymbol);
    }

    /**
     * checks, if the occurring symbols are in the Terminal or the nonterminal set
     * An exception is epsilon, which is added to the terminal alphabet by this method.
     *
     * @param node the node
     */
    @Override
    public void inARule(ARule node) {
        if(!nonterminals.containsKey(node.getComingFrom().getText())) {
            System.out.println("Nonterminal " + node.getComingFrom().getText() + " has not been defined!");
            System.exit(1);
        }


        List<Symbol> rightSide = new ArrayList<>();

        for(TSymbol symbol : node.getGoingTo()) {
            if(terminals.containsKey(symbol.getText().replaceAll("'", ""))) {
                rightSide.add(new Terminal(symbol.getText().replaceAll("'", "")));
            }
            else if((nonterminals.containsKey(symbol.getText().replaceAll("'", "")))) {
                rightSide.add(new Nonterminal(symbol.getText().replaceAll("'", "")));
            } else if(symbol.getText().replaceAll("'","").equals("epsilon") || symbol.getText().replaceAll("'","").equals("lambda")  ) {
                rightSide.add(Terminal.NULLSYMBOL);
            } else {
                System.out.println("Symbol " + symbol.getText() + " has not been defined!");
                System.exit(1);
            }
        }
        Rule rule = new Rule(new Nonterminal(node.getComingFrom().getText()),rightSide);
        rules.add(rule);
    }

    @Override
    public void outARoot(ARoot node) {
        grammar = new Grammar(startSymbol,rules,"G",null);

    }

    public Grammar getGrammar(String name) {

        return  new Grammar(grammar.getStartSymbol(),grammar.getRules(),name,null);
    }
}

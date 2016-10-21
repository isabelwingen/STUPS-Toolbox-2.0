package CLIPlugins;

import GrammarSimulator.Grammar;
import GrammarSimulator.GrammarUtil;
import GrammarSimulator.Nonterminal;

import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by fabian on 22.08.16.
 */
public class GrammarNullablePlugin implements CLIPlugin {

    private boolean errorFlag = false;

    @Override
    public String[] getNames() {
        return new String[]{"nullable"};
    }

    @Override
    public boolean checkParameters(String[] parameters) {
        return true;
    }

    @Override
    public String getHelpText() {
        return "Prints the Nullable-set of the loaded grammar. Doesn't take any parameters.";
    }

    @Override
    public Object execute(Object object, String[] parameters) {
        errorFlag = false;
        if(object == null) {
            System.out.println("Please load a grammar before using this command!");
            errorFlag = true;
            return null;
        }
        Grammar grammar = (Grammar) object;
        HashSet<Nonterminal> result = GrammarUtil.calculateNullable(grammar);

        System.out.print("nullable = {");

        Iterator<Nonterminal> it = result.iterator();
        while(it.hasNext()) {
            Nonterminal currentNonterminal = it.next();
            System.out.print(currentNonterminal.getName());

            if(it.hasNext()) {
                System.out.print(", ");
            }
        }

        System.out.println("}");

        return result;
    }

    @Override
    public Class inputType() {
        return Grammar.class;
    }

    @Override
    public Class outputType() {
        return null;
    }

    @Override
    public boolean errorFlag() {
        return errorFlag;
    }
}


package CLIPlugins;

import GrammarSimulator.*;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Isabel on 22.10.2016.
 */
public class GrammarEliminateUnitRules implements CLIPlugin {
    private boolean errorFlag = false;



    @Override
    public String[] getNames() {
        return new String[]{"eur","eliminate-unit-rules"};
    }

    @Override
    public boolean checkParameters(String[] parameters) {
        return true;
    }

    @Override
    public String getHelpText() {
        return "eliminates unit Rules (A -> B) from the loaded grammar";
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
        HashSet<Node> unitRules= GrammarUtil.findUnitRules(grammar);
        GrammarUtil.dfs(unitRules);
        unitRules.stream().forEach(x -> System.out.printf("Node %s: dfs: %d, dfe: %d\n",x.getName(),x.getDfs(),x.getDfe()));
        GrammarUtil.print(grammar);
        return null;
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

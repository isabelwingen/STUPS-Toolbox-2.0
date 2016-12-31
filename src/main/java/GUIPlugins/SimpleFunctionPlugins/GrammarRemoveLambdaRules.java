package GUIPlugins.SimpleFunctionPlugins;

import CLIPlugins.GrammarRemoveLambdaPlugin;
import GrammarSimulator.Grammar;
import GrammarSimulator.GrammarUtil;
import Print.Printable;
import Print.Printer;

import java.util.ArrayList;

/**
 * Created by Isabel on 03.12.2016.
 */
public class GrammarRemoveLambdaRules extends SimpleFunctionPlugin {
    @Override
    public Object execute(Object object) {
        if(object == null) {
            gui.errorDialog("Please load a grammar before using this command!");
            return null;
        }
        Grammar grammar = (Grammar) object;
        if(GrammarUtil.isLambdaFree(grammar)) {
            gui.infoDialog("this grammar is already lambda-free");
            return null;
        }

        return GrammarUtil.removeLambdaRules(grammar);
    }

    @Override
    String getName() {
        return "remove Lambda-Rules";
    }

    @Override
    public Class inputType() {
        return Grammar.class;
    }

    @Override
    Class outputType() {
        return Grammar.class;
    }

    @Override
    public boolean createsOutput() {
        return true;
    }
}

package GUIPlugins.SimpleFunctionPlugins;

import GrammarSimulator.Grammar;
import GrammarSimulator.GrammarUtil;
import Main.Storable;

/**
 * Created by Isabel on 03.12.2016.
 */
public class GrammarChomskyNormalForm extends SimpleFunctionPlugin {
    @Override
    public Storable execute(Object object) {
        Grammar grammar = (Grammar) object;
        if(object == null) {
            gui.errorDialog("Please load a Grammar befor using this command");
            return null;
        }
        if(!GrammarUtil.isLambdaFree(grammar)) {
            gui.errorDialog("Can't do this! The grammar has lambda-rules!");
        } else if(GrammarUtil.hasUnitRules(grammar)) {
            gui.errorDialog("Can't do this! The grammar has unit-rules");
        } else if(GrammarUtil.isInChomskyNormalForm(grammar)) {
            gui.infoDialog("Can't do this! The grammar is already in chomsky normal-form");
        } else {
            return GrammarUtil.chomskyNormalForm(grammar);
        }
        return grammar;
    }

    @Override
    String getName() {
        return "chomsky normal form";
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

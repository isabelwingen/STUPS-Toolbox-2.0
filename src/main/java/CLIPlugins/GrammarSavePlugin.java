package CLIPlugins;

import GrammarSimulator.Grammar;
import GrammarSimulator.GrammarUtil;
import Main.Storable;

import java.io.IOException;

/**
 * saves the current {@link Grammar} into a file.
 * @author fabian
 * @since 12.08.16
 */


public class GrammarSavePlugin extends CLIPlugin {

    private boolean errorFlag = false;

    @Override
    public String[] getNames() {
        return new String[]{"sg", "save-grammar"};
    }

    @Override
    public boolean checkParameters(String[] parameters) {
        if(parameters.length < 1) {
            System.out.println("Please enter a filename as parameter for this command!");
            return false;
        }
        return true;
    }

    @Override
    public String getHelpText() {
        return "Writes the loaded automaton into a text file, which can later be reloaded by this program. Takes a file as parameter.";
    }

    @Override
    public Storable execute(Storable object, String[] parameters) {
        errorFlag = false;
        if(object == null) {
            System.out.println("Please load a grammar before using this command!");
            errorFlag = true;
            return null;
        }
        Grammar grammar = (Grammar) object;
        try {
            GrammarUtil.save(grammar, parameters[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

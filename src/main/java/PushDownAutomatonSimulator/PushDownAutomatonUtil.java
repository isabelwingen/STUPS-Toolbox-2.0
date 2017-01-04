package PushDownAutomatonSimulator;



import PushDownAutomatonParser.lexer.Lexer;
import PushDownAutomatonParser.lexer.LexerException;
import PushDownAutomatonParser.node.Start;
import PushDownAutomatonParser.parser.Parser;
import PushDownAutomatonParser.parser.ParserException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

/**
 * Created by Isabel on 29.10.2016.
 */
public class PushDownAutomatonUtil {


    public static PushDownAutomaton parse(String fileInput, String name) throws ParserException, IOException, LexerException {
        StringReader reader = new StringReader(fileInput);
        PushbackReader r = new PushbackReader(reader, 100);
        Lexer l = new Lexer(r);
        Parser parser = new Parser(l);
        Start start = parser.parse();
        Visitor visitor = new Visitor();
        start.apply(visitor);
        PushDownAutomaton pda = visitor.getPushDownAutomaton(name);
        return pda;
    }

    public static PushDownAutomaton parse(File file) throws IOException, LexerException, ParserException {
        String name = file.getName();
        PushDownAutomaton pda;
        BufferedReader grammarReader = new BufferedReader(new FileReader(file));
        String string = "";
        String line;
        while ((line = grammarReader.readLine()) != null) {
            string = string + line + "\n";
        }
        pda = PushDownAutomatonUtil.parse(string,name);
        grammarReader.close();
        return pda;

    }
    public static void save(PushDownAutomaton pda, String fileName) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));

            writer.write("{'");
            writer.write(pda.getInputAlphabet().stream().map(InputLetter::getName).collect(joining("', '")));
            writer.write("';'");
            writer.write(pda.getStackAlphabet().stream().map(StackLetter::getName).collect(joining("', '")));
            writer.write("';");
            writer.write(pda.getStates().stream().map(State::getName).collect(joining(", ")));
            writer.write("; ");
            writer.write(pda.getStartState().getName());
            writer.write("; '");
            writer.write(pda.getInitalStackLetter().getName());
            writer.write("'}\n\n");

            pda.getRules().forEach(rule -> {
                try {
                    writer.write("'" + rule.getComingFrom().getName() + "', '");
                    writer.write(rule.getReadIn().getName() + "', '");
                    writer.write(rule.getOldToS().getName() + "' --> '");
                    writer.write(rule.getGoingTo().getName() + "', '");
                    writer.write(rule.getNewToS().stream().map(x -> x.getName()).collect(joining("', '")) + "'\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            writer.flush();
            writer.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static RunThroughInfo doRule(PDARule rule, RunThroughInfo run) {
        PushDownAutomaton pda = run.getMyPDA();
        if(run.getStack().isEmpty()) {
            return run;
        }
        if(!run.getCurrentState().equals(rule.getComingFrom())) {//rule cannot be applied, because the states don't match
            return run;
        }
        if(!run.getStack().peek().equals(rule.getOldToS())) { //rule cannto be applied because the ToS doens't match
            return run;
        }
        if(run.getInput().isEmpty()) {
            if (!rule.getReadIn().equals(InputLetter.NULLSYMBOL)) {
                return run;
            } else {
                State currentState = rule.getGoingTo();
                Stack<StackLetter> stack = new Stack<>();
                stack.addAll(run.getStack());
                stack.pop();
                rule.getNewToS().stream().filter(stackLetter -> !stackLetter.equals(StackLetter.NULLSYMBOL)).forEach(stack::add);
                return new RunThroughInfo(stack,run.getInput(),currentState,run,pda);
            }
        }
        if(!run.getInput().get(0).equals(rule.getReadIn())) { // the first letter and the letter of the rule are not the same
            if(!rule.getReadIn().equals(InputLetter.NULLSYMBOL)) { // the rule cannot be applied, because the input is not right
                return run;
            } else {
                State currentState = rule.getGoingTo();
                Stack<StackLetter> stack = new Stack<>();
                stack.addAll(run.getStack());
                stack.pop();
                rule.getNewToS().stream().filter(stackLetter -> !stackLetter.equals(StackLetter.NULLSYMBOL)).forEach(stack::add);
                return new RunThroughInfo(stack,run.getInput(),currentState,run,pda);
            }
        }

        //the rule is not a lambda-rule and can be applied

        State currentState = rule.getGoingTo();
        Stack<StackLetter> stack = new Stack<>();
        stack = (Stack<StackLetter>) run.getStack().clone();
        stack.pop();
        rule.getNewToS().stream().filter(sym -> !sym.equals(StackLetter.NULLSYMBOL)).forEach(stack::add);
        ArrayList<InputLetter> input = new ArrayList<>();
        System.out.println(stack.peek().getName());
        input.addAll(run.getInput().stream().skip(1).collect(Collectors.toList()));
        return new RunThroughInfo(stack,input,currentState,run,pda);
    }

    public static RunThroughInfo startRunThrough(PushDownAutomaton pda, List<String> strings) {
        State currentState = pda.getStartState();
        Stack<StackLetter> stack = new Stack<>();
        stack.add(pda.getInitalStackLetter());
        ArrayList<InputLetter> input = (ArrayList<InputLetter>) strings.stream().map(InputLetter::new).collect(toList());
        return new RunThroughInfo(stack,input,currentState,null,pda);
    }

}

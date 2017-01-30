package Main;

import AutomatonSimulator.Automaton;
import CLIPlugins.CLIPlugin;
import GrammarSimulator.Grammar;
import Print.PrintMode;
import Print.Printable;
import Print.Printer;
import PushDownAutomatonSimulator.PushDownAutomaton;
import javafx.application.Platform;
import org.reflections.Reflections;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

/**
 * One of the two main classes of the application. Is used as a console and executes the commands the user types into
 * the console.
 * @author fabian
 * @since 15.06.16
 */
@SuppressWarnings("ALL")
public class CLI {

    private final GUI gui;
    /**
     * Contains all loaded objects (Automaton, Grammars, etc.).
     * The class-type of the object is mapped to an instance of it.
     */
    public final HashMap<Class, Object> objects = new HashMap<>();

    /**
     * Contains all stored (saved) objects (Automaton, Grammars, etc.).
     * The class-type of the object is mapped to a hashmap.
     * In this map names are mapped to instances of the class
     */
    public final HashMap<Class, HashMap<String, Storable>> store= new HashMap<>();

    /**
     * Contains the different types of storable objects (Automaton, Grammar, etc.).
     * Maps the name of the class to the class.
     * If you want to add new types of storable objects to the application, you need
     * to add an entry to this hashmap.
     */
    public final HashMap<String,Class> lookUpTable =new HashMap<>();

    String types="";

    /**
     * the constructor. Creates a new instance of cli.
     * @param gui the {@link GUI}
     */
    public CLI(GUI gui) {
        this.gui=gui;
    }

    private boolean isStoreFunction(String command) {
        String[] allCommands=new String[]{"str","store","switch","swt","remove","rmv","copy"};
        return Arrays.stream(allCommands).anyMatch(string -> string.equals(command));
    }

    private boolean buildIn(String command, String[] parameters, ArrayList<CLIPlugin> plugins) throws InterruptedException {
        switch (command) {
            case "gui":

                Platform.runLater(gui::show);
                while (!gui.IS_VISIBLE) {
                    Thread.sleep(500);
                }

                //  } else if(isStoreFunction(command) && doStoreCommand(command,parameters[0],parameters[1])) {
                break;
            case "show-all":
                if (parameters.length == 1) {
                    Class clazz = lookUpTable.get(parameters[0].toLowerCase());
                    if (clazz == null) {
                        System.out.println("no such objects stored");
                    } else {
                        HashMap<String, Storable> correctMap = store.get(clazz);
                        if (correctMap == null || correctMap.isEmpty()) {
                            System.out.println("no objects of type " + parameters[0] + " stored!");
                        } else {
                            correctMap.keySet().forEach(key -> Printer.print((Printable) correctMap.get(key)));
                        }
                    }
                } else {
                    System.out.println("Please enter a storable type as a parameter for this command!");
                }
                break;
            case "clear_store":
                store.clear();
                //} else if(command.equals("switch_workspace")) { //TODO

                break;
            case "remove":
            case "rmv":
                if(parameters.length==2) {
                    Class clazz = lookUpTable.get(parameters[0]);
                    if(clazz != null) {
                        HashMap rightMap = store.get(clazz);
                        rightMap.remove(parameters[1]);
                    } else {
                        System.out.println("no such type!");
                    }
                } else {
                    System.out.println("you need two parameters");
                }
                break;
            case "str":
            case "store":
                if(parameters.length==2) {
                    Class clazz = lookUpTable.get(parameters[0]);

                    if(clazz != null) {
                        HashMap rightMap = store.get(clazz);
                        if(rightMap == null) {
                            store.put(clazz,new HashMap<>());
                        }
                        if(objects.get(clazz) != null) {
                            Storable storable = (Storable) objects.get(clazz);
                            store.get(clazz).put(parameters[1], storable.otherName(parameters[1]));
                        } else {
                            System.out.print("no current object of type "+parameters[0]);
                        }
                    } else {
                        System.out.println("no such type!");
                    }
                } else {
                    System.out.println("you need two parameters");
                }
                break;
            case "swt":
            case "switch":
                if(parameters.length==2) {
                    Class clazz = lookUpTable.get(parameters[0]);
                    if(clazz != null) {
                        HashMap rightMap = store.get(clazz);
                        if(rightMap != null) {
                            if(rightMap.keySet().contains(parameters[1])) {
                                Storable storable = (Storable) rightMap.get(parameters[1]);
                                if(storable != null) {
                                    objects.put(clazz,storable);
                                }
                            } else {
                                System.out.println("no object of type "+ parameters[0] + " and name "+ parameters[1]);
                            }
                        } else {
                            System.out.println("no objects stored of this type");
                        }
                    } else {
                        System.out.println("no such type!");
                    }
                } else {
                    System.out.println("you need two parameters");
                }
                break;
            case "h":
            case "help":

                plugins.stream().sorted().forEach(plugin -> {
                    System.out.print("'" + plugin.getNames()[0] + "'");
                    for (int i = 1; i < plugin.getNames().length; i++) {
                        if (i < plugin.getNames().length - 1) {
                            System.out.print(", ");
                        } else {
                            System.out.print(" or ");
                        }
                        System.out.print("'" + plugin.getNames()[i] + "'");
                    }
                    System.out.println("  --  " + plugin.getHelpText());
                });

                System.out.println("'gui' -- Opens a graphical user interface. Doesn't take any parameters");
                System.out.println("'clear_store' -- deletes every stored item");
                System.out.println("'str' or 'store' -- takes "+types+" as first parameter and a name as second. Stores the current object of this type");
                System.out.println("'remove' or 'rmv' -- takes "+types+" as first parameter and a name as second. Removes the stored object at this position");
                System.out.println("'swt' or 'switch' --  takes "+types+" as first parameter and a name as second. Sets the current objects of this type to the object at this position");
                System.out.println("'show-all' -- takes "+types+" as a parameter. Prints all objects of this kind");
                System.out.println("'e' or 'exit' -- Leaves the program. Doesn't take any parameters");
                System.out.println("'a' or 'about' -- Shows the release information");
                System.out.println("'h' or 'help' -- Shows this help message. Doesn't take any parameters");
                break;
            case "e":
            case "exit":
                System.out.println("Goodbye!");
                if (Printer.writerIsNotNull() && Printer.printmode == PrintMode.LATEX) {
                    Printer.printEndOfLatex();
                    Printer.closeWriter();
                }
                save_workspace();
                System.exit(0);
            case "a":
            case "about":
                System.out.println("STUPS-Toolbox Release 1 (22-09-2016)");
                System.out.println("Written and developed by Fabian Ruhland.");
                System.out.println("--------------------------------------------");
                System.out.println("This program uses the JUNG2-library to display automatons.");
                System.out.println("JUNG2 is licensed under the BSD open-source license.");
                System.out.println("See http://jung.sourceforge.net/site/license.html or the file \"lib/JUNG2/JUNG-license.txt\" for more information.");
                break;
            default:
                return false;
        }
        return true;
    }

    private boolean doStoreCommand(String command, String parameter1, String parameter2) {
        if(isStoreFunction(command)) {
                    // Integer i = Integer.parseInt(parameters[1]);
                    //first: detect which object should be stored
                    Class clazz = lookUpTable.get(parameter1.toLowerCase());

                    if (clazz == null) {
                        System.out.println("There are no objects of type " + parameter1);
                    } else {
                        HashMap<String, Storable> correctMap = store.get(clazz);
                        switch (command) {
                            case "store":
                            case "str":
                            case "copy":

                                Object object = objects.get(clazz);
                                if (object == null) {
                                    System.out.println("Please load an object of type " + parameter1 + " before using this command!");
                                } else {

                                    Storable toBeStored;
                                    if (command.equals("copy")) {
                                        toBeStored = ((Storable) object).otherName(parameter2);
                                    } else {
                                        toBeStored = ((Storable) object).otherName(parameter2);
                                    }
                                    if (correctMap == null) {
                                        HashMap<String, Storable> tmp = new HashMap<>();
                                        tmp.put(parameter2, toBeStored);

                                        store.put(clazz, tmp);
                                    } else {
                                        correctMap.put(parameter2, toBeStored);
                                        //    store.get(clazz).put(i, toBeStored);
                                    }
                                }
                                break;
                            case "switch":
                            case "swt":
                                Storable theNewCurrent = correctMap.get(parameter2);
                                if (theNewCurrent == null) {
                                    System.out.println("no Object with this Index"); //TODO
                                } else {
                                    objects.put(clazz, theNewCurrent);
                                }
                                break;
                            case "remove":
                            case "rmv":
                                correctMap.remove(parameter2);
                                break;
                            default:
                                System.out.println("something went wrong");
                                break;
                        }
                    }
        } else {
            return false;
        }
        return true;
    }

    /**
     * checks, if the store contains the {@link Storable} storable, which is an instance of {@link Class} type.
     * @param storable the object, that is checked
     * @param type the class of the object
     * @return true, if the store contains the storable; false otherwise.
     */
    public boolean storeContains(Storable storable, Class type) {
        return store.get(type) != null && !store.get(type).values().isEmpty() && store.get(type).values().contains(storable);
    }


    private void restore_workspace() {
        if(new File("config").exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader("config"));
                String path=reader.readLine();
                reader.close();
                File ret = new File(path);

                store.clear();
                File[] directoryListing = ret.listFiles();
                if (directoryListing != null) {
                    for (File child : directoryListing) {
                        // get the class belonging to that directory
                        Class clazz = lookUpTable.get(child.getName().toLowerCase());
                        try {
                            // a instance of this class to parse the saved storable
                            Storable storable = (Storable) clazz.newInstance();
                            // go through every file in the directory
                            File[] files = child.listFiles();
                            if(files != null) {
                                for (File file : files) {
                                    // the parsed object
                                    Storable restored = storable.restoreFromFile(file);

                                    // store it in the store
                                    HashMap<String, Storable> correctMap = store.get(clazz);
                                    String i = restored.getName();
                                    if (correctMap == null) {
                                        HashMap<String, Storable> tmp = new HashMap<>();
                                        tmp.put(i, restored);
                                        store.put(clazz, tmp);
                                        objects.putIfAbsent(clazz, restored);
                                    } else {
                                        correctMap.put(i, restored);
                                        objects.putIfAbsent(clazz, restored);
                                        //    store.get(clazz).put(i, toBeStored);
                                    }
                                }
                            }
                        } catch (InstantiationException | IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            System.err.println("error while restoring the workspace. A " +child.getName()+ " is corrupt");
                            e.printStackTrace();

                            File ptw = new File("path_to_workspace");
                            ptw.delete();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * This method starts the Main.CLI and enters an endless loop, listening for user input.
     */
    public void start() {
        lookUpTable.put("grammar",Grammar.class);
        lookUpTable.put("automaton",Automaton.class);
        lookUpTable.put("pda",PushDownAutomaton.class);
        lookUpTable.put("pushdownautomaton",PushDownAutomaton.class);
        List<String> list = lookUpTable.keySet().stream().filter(x -> !x.equals("pda"))
                .map(s -> "'"+s+"'")
                .sorted()
                .collect(Collectors.toList());
        types=list.get(0);
        for(int i=1;i<list.size()-1;i++) {
            types+=", ";
            types+=list.get(i);
        }
        types+=" or "+list.get(list.size()-1);
        //Print a welcome message and initialize some variables.
        System.out.println("Welcome to the STUPS-Toolbox!\nPlease enter a command!\nFor a list of commands enter 'h' or 'help'...");
        String input, command, parameters[];
        ArrayList<CLIPlugin> plugins = new ArrayList<>();
        objects.put(null, null);
        restore_workspace();

        Reflections reflections = new Reflections("CLIPlugins");
        Set<Class<? extends CLIPlugin>> s = reflections.getSubTypesOf(CLIPlugin.class);
        s.forEach(r -> {
            try {
                CLIPlugin plugin = r.newInstance();
                plugins.add(plugin);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });

        //Enter an endless loop and listen for user input.
        while(true) {

            //Sleep, while the Main.GUI is visible.
            while(gui.IS_VISIBLE) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            command = "";
            System.out.print(">");
            try {
                //Read user input from stdin and tokenize it into a command and parameters.
                BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
                input = inputReader.readLine();
                StringTokenizer inputTokenizer = new StringTokenizer(input, " ");
                if(inputTokenizer.hasMoreElements()) {
                    command = inputTokenizer.nextToken();
                }
                parameters = new String[inputTokenizer.countTokens()];
                for (int i = 0; inputTokenizer.hasMoreElements(); i++) {
                    parameters[i] = inputTokenizer.nextToken();
                }

                //Execute the entered command. "gui" and "help" are hardcoded, all the other commands come from plugins.
                //If a plugin returns an object, it is put into the objects-Hashmap. If an object of this type already exists, it will be overwritten.
                Storable ret;
                boolean validCommand = false;
                if(buildIn(command,parameters,plugins)) {
                    validCommand=true;
                } else {
                    for (CLIPlugin plugin : plugins) {
                        if (Arrays.asList(plugin.getNames()).contains(command) && plugin.checkParameters(parameters)) {
                            validCommand = true;
                            ret = plugin.execute(objects.get(plugin.inputType()), parameters);
                            if (!plugin.errorFlag()) {
                                objects.put(plugin.outputType(), ret);
                                if(store.get(plugin.outputType()) != null && store.get(plugin.outputType()).keySet().contains(ret.getName())) {
                                    store.get(plugin.outputType()).put(ret.getName(),ret);
                                }
                            }
                            break;
                        }
                    }
                }

                if(!validCommand && !command.isEmpty()) {
                    System.out.println("Wrong input!");
                }
            } catch (Exception e) {
                System.out.println("Fatal error! Exiting program...\n");
                e.printStackTrace();
                System.exit(1);
            }
        }
    }

    /**
     * saves the current workspace
     */

    private void save_workspace() {
        if(new File("config").exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader("config"));
                String ground = reader.readLine();
                String path = ground+"/";
               // String path_tmp =  ground+ "_tmp/";
                deleteDirectory(new File(path));
                reader.close();
                File workspace= new File(path);
                workspace.mkdir();
                store.keySet().forEach(key -> {
                    if (!store.get(key).isEmpty()) {
                        File subDir = new File(path + key.getSimpleName());
                        if (!subDir.exists()) {
                            subDir.mkdir();
                        }
                        store.get(key).values().forEach(storable -> {
                            String name = storable.getName();
                            storable.printToSave(path + key.getSimpleName() + "/" + name);
                        });
                    }
                });


            } catch (IOException io) {
                io.printStackTrace();
            }



        }
    }

    /**
     * deletes a directory and all files in it
     * @param file the directory, that should be deleted
     */
    private boolean deleteDirectory(File file) {
        boolean check = true;
        if(file.exists() && file.isDirectory()) {
            File[] list = file.listFiles();
            if(list != null) {
                for (File child : list) {
                    check &= deleteDirectory(child);
                }
            }
        }
        return check & file.delete();
    }



}

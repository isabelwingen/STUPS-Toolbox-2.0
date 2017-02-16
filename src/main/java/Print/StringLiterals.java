package Print;

public class StringLiterals {
    //Literals for remove Lambda Rules
   public static final String[] RLR_POINT_DESCRIPTIONS_SHORT=new String[]{"Before","Step 1","Step 2","Step 3"};
    public static final String[] RLR_TEXTS_SHORT=new String[]{"",
            "calculate the nullable set",
            "for every rule that contains a nullable nonterminal, add that rule without this nonterminal",
            "remove lambda-rules"};
    public static final String[]  RLR_POINT_DESCRIPTIONS_LONG=new String[]{"Before","Special Rule for Empty Word",
            "Step 1","Step 2","Step 3"};
    public static final String[] RLR_TEXTS_LONG=new String[]{"",
            "add a nonterminal according to the special rule for empty word",
            "calculate the nullable set",
            "for every rule that contains a nullable nonterminal, add that rule without this nonterminal",
            "remove lambda-rules"};

    public static final String RLR_TITLE = "Remove Lambda-Rules";

    //Literals for eliminate unit rules
    public static final String[] EUR_TEXTS=new String[]{"",
            "remove circles",
            "number the nonterminals",
            "remove unit rules beginning by the highest number."};
    public static final String[]  EUR_POINT_DESCRIPTIONS=new String[]{"Before","Step 1","Step 2","Step 3"};
    public static final String EUR_TITLE = "Eliminate Unit Rules";

    //Literals for chomsky normal form
    public static final String[] CNF_TEXTS=new String[]{"","rules in form A -> a are " +
            "already in chomsky normal form and we keep them",
            "in all other rules replace every appearance of a terminal a through a new nonterminal",
            "in all rules that contain more than two nonterminals, add a new nonterminal that points to the " +
                    "end of the rule."};
    public static final String[] CNF_POINT_DESCRIPTIONS=new String[]{"Before","Step 1", "Step 2", "Step 3"};
    public static final String CNF_TITLE = "Chomsky - Normal - Form";

    //Literals for Simplify
    public static final String[] SIMPLIFY_POINT_DESCRIPTIONS = new String[]{"Before", "After"};
    public static final String[] SIMPLIFY_TEXTS = new String[]{"","Remove redundant and unreachable nonterminals"};
    public static final String SIMPLIFY_TITLE = "Simplify";

    //Literals for Rename Nonterminals
    public static final String[] RENAME_POINT_DESCRIPTIONS = new String[]{"Before", "After"};
    public static final String[] RENAME_TEXTS = new String[]{"","rename nonterminals to short, easy names"};
    public static final String RENAME_TITLE = "Rename Nonterminals";

    //toPDA
    public static final String[] TOPDA_POINT_DESCRIPTIONS = new String[]{"Grammar", "Initial","Step 1","Step 2"};
    public static final String[] TOPDA_TEXTS = new String[]{"","","",""};
    public static final String TOPDA_TITLE = "Grammar to PDA";
}

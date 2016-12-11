package Print;



import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by Isabel on 18.11.2016.
 */
public class Printer {
    /**
     * the print mode
     */
    public static PrintMode printmode=PrintMode.CONSOLE;
    /**
     * the current file that should be used
     */
    public static String currentFile;
    //BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));

    private static int deepnes=0;
    private static BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(System.out));

    public static void print(Printable printable) {
        switch (printmode) {
            case NO:
                break;
            case LATEX:
                printable.printLatex(writer,getSpace(deepnes));
                break;
            case CONSOLE:
                printable.printConsole(writer);
                break;
        }
    }
    public static void print(String string) {
        print(string,Printer.writer);
    }
    public static void print(String string, BufferedWriter writer) {
        try {
            writer.write(string);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void println(String string, BufferedWriter writer) {
        try {
            writer.write(string+"\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void print(int i, BufferedWriter writer) {
        try {
            writer.write(i);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void println(int i, BufferedWriter writer) {
        try {
            writer.write(i+"\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printEnumeration(ArrayList<Printable> printables, String[] point_descriptions, String[] texts, String titel) {
        switch(printmode) {
            case NO:
                break;
            case CONSOLE:
                printEnumerationConsole(printables,point_descriptions,texts,titel);
                break;
            case LATEX:
                printEnumerationLatex(printables,point_descriptions,texts,titel);
                break;
        }
    }

    public static void printWithTitel(String titel, Printable printable) {
        switch(printmode) {
            case NO:
                break;
            case CONSOLE:
                printWithTitelLatex(titel,printable);
                break;
            case LATEX:
                printWithTitelConsole(titel, printable);
                break;
        }

    }

    public static void printWithTitelLatex(String titel, Printable printable) {
        Printer.print("\\section{"+titel+"}\n\n",writer);
        printable.printLatex(writer,getSpace(deepnes));

    }

    public static void printWithTitelConsole(String titel, Printable printable) {
        Printer.print(titel+"\n\n",writer);
        printable.printConsole(writer);
    }


    public static void setWriter(BufferedWriter writer) {
        Printer.writer = writer;
    }

    public static void closeWriter() {
        try {
            Printer.writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static boolean writerIsNull() {
        return writer==null;
    }

    /** LATEX **/
    private static void printEnumerationLatex(ArrayList<Printable> printables, String[] point_descriptions, String[] texts, String titel) {
        if(printables.size()!=texts.length) {
            return;
        }
        try {
            Printer.print("\\section{"+titel+"}\n\n",writer);
            Printer.print("\\begin{description}\n",writer);
            Printer.deepnes++;
            for(int i=0;i<printables.size();i++) {
                writeItem(point_descriptions[i],texts[i]);
                printables.get(i).printLatex(writer,getSpace(deepnes));

            }
            Printer.deepnes--;
            Printer.print("\\end{description}\n\n",writer);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static void printStartOfLatex() {
        Printer.println("%this document was generated by the STUPS Toolbox 2.0",writer);
        Printer.print("\\documentclass{article}\n\\" +
                "usepackage{amssymb}\n\\" +
                "usepackage{amsmath,amsthm}\n\\" +
                "usepackage[ngerman,english]{babel}\n\\" +
                "usepackage{tikz}\n\\" +
                "usetikzlibrary{automata,positioning}\n\n\\" +
                "begin{document}\n\n",writer);
    }
    public static void printEndOfLatex() {
        if(printmode==PrintMode.LATEX) {
            print("\\end{document}",writer);
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static String makeToGreek(String string) {
        if(string.equals("epsilon")||string.equals("lambda")) {
            return "\\"+string;
        } else {
            return string;
        }
    }


    private static void writeItem(String titel, String subtitel) throws IOException {
        String s="";
        for(int i=0;i<Printer.deepnes;i++) {
            s+="\t";
        }
        if(subtitel.isEmpty()) {
            Printer.print(s+"\\item["+titel+"] \\hfill \\\\ \n"+s+subtitel,writer);
        } else {
            Printer.print(s+"\\item["+titel+"] \\hfill \\\\ \n"+s+subtitel+"\\\\ \n",writer);
        }

    }


    /** CONSOLE **/


    private static void printEnumerationConsole(ArrayList<Printable> printables, String[] point_description, String[] texts, String titel) {
        if(printables.size()!=texts.length) {
            return;
        }

        for(int i=0;i<printables.size();i++) {
            Printer.print(point_description[i]+": "+texts[i]+"\n",writer);

            printables.get(i).printConsole(writer);

        }

    }


    /** PRIVATE **/
    private static String getSpace(int x) {
        String res="";
        for(int i=0;i<x;i++) {
            res+="\t";
        }
        return res;
    }

    public static String checkIfLatexSpecial(String string) {
        String[] special = new String[]{"#","epsilon","lambda","alpha","beta","$","%","{","}","&","_",""};
        List<String> list = Arrays.asList(special);
        if(Printer.printmode==PrintMode.LATEX && list.contains(string)) {
            return "\\"+string;
        } else {
            return string;
        }
    }




}

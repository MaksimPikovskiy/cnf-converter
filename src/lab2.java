import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class lab2 {

    public static void usage() {
        System.err.println("Program should accept 1 argument, cnf-file." +
                "\n Example: $ java lab2.java testcases/functions/f1.cnf");
    }

    public static void main(String[] args) {
        if(args.length != 1) {
            System.err.println("Error: Incorrect number of arguments");
            usage();
            System.exit(0);
        }

        try (Scanner reader = new Scanner(new File(args[0]), StandardCharsets.UTF_8)) {
            String predicates = reader.nextLine();
            String variables = reader.nextLine();
            String constants = reader.nextLine();
            String functions = reader.nextLine();
            List<String> clauses = new LinkedList<>();
            while(reader.hasNextLine()) {
                clauses.add(reader.nextLine());
            }

            List<String> predicatesList = new LinkedList<>(Arrays.asList(predicates.split(" ")));
            predicatesList.remove(0);
            List<String> constantsList = new LinkedList<>(Arrays.asList(constants.split(" ")));
            constantsList.remove(0);
            List<String> functionsList = new LinkedList<>(Arrays.asList(functions.split(" ")));
            functionsList.remove(0);
            List<String> variablesList = new LinkedList<>(Arrays.asList(variables.split(" ")));
            variablesList.remove(0);
            clauses.remove(0);

            KnowledgeBase kb = new KnowledgeBase();
            kb.addPredicates(predicatesList);
            kb.addVariables(variablesList);
            kb.addConstants(constantsList);
            kb.addFunctions(functionsList);
            kb.addClauses(clauses);

            System.out.println(kb);

            System.out.println(clauses);


        } catch (FileNotFoundException e) {
            System.err.println("File not Found!\n" + e.getMessage());
        } catch (IOException e) {
            System.err.println("IO error encountered!\n" + e.getMessage());
        }
    }
}

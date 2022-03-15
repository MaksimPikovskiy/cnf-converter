import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * This is the main class for initiating Knowledge Base and Resolution.
 *
 * It checks whether correct number of arguments was passed and if the files
 * exist and are accessible.
 *
 * Using the provided argument, it reads from the provided file of CNF
 * representation of Knowledge Base. It reads line by line, putting the
 * data into {@linkplain List lists}. After putting the data, it creates
 * {@linkplain KnowledgeBase} with the data provided.
 *
 * After creating {@linkplain KnowledgeBase}, it creates a {@linkplain Resolution}
 * and passes created {@linkplain KnowledgeBase}. It then initiates {@linkplain Resolution}
 * and checks whether created {@linkplain KnowledgeBase} is satisfiable.
 *
 * This class has a debug mode, enabled/disabled with boolean enableDebug in
 * main method. The debug mode prints the provided data as it was given and
 * then prints created {@linkplain KnowledgeBase}.
 *
 * @author <a href='mailto:mp8671@rit.edu'>Maksim Pikovskiy</a>
 */
public class lab2 {

    /**
     * Prints the string values provided for debugging.
     *
     * @param str the string to be printed.
     */
    private static void debug(String str) {
        System.out.println(str + "\n\n");
    }

    /**
     * Prints the message on how to use the program.
     * Only prints when incorrect number of arguments are given.
     */
    private static void usage() {
        System.err.println("Program should accept 1 argument, cnf-file." +
                "\n Example: $ java lab2.java testcases/functions/f1.cnf");
    }

    /**
     * Main body of the program.
     *
     * Retrieves all the required information needed to create a {@linkplain KnowledgeBase}.
     * Then initiates {@linkplain Resolution} with created {@linkplain KnowledgeBase}.
     *
     * @param args required 1 argument for the {@link KnowledgeBase}.
     */
    public static void main(String[] args) {

        // allows printing how the data from cnf file and then how KB took that data.
        boolean enableDebug = false;

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

            if(enableDebug) {
                String debugStr = predicates + "\n" + variables + "\n" + constants + "\n" + functions + "\n" + clauses;
                debug(debugStr);
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

            if(enableDebug) {
                debug(kb.toString());
            }

            Resolution res = new Resolution(kb);
            System.out.println(res.isSatisfiable());


        } catch (FileNotFoundException e) {
            System.err.println("File not Found!\n" + e.getMessage());
        } catch (IOException e) {
            System.err.println("IO error encountered!\n" + e.getMessage());
        }
    }
}

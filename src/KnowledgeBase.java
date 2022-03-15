import java.util.ArrayList;
import java.util.List;

/**
 * Knowledge Base is a data structure representing the CNF representation of
 * Knowledge Base provided in the file.
 *
 * Knowledge Base contains:
 *      -predicates representations (used for comparison)
 *      -constants representations (used for comparison)
 *      -functions representations (used for comparison)
 *      -variables representations (used for comparison)
 *      -clauses (actual clauses to be tested)
 *
 * @author <a href='mailto:mp8671@rit.edu'>Maksim Pikovskiy</a>
 */
public class KnowledgeBase {
    private List<String> predicates;
    private List<String> constants;
    private List<String> functions;
    private List<String> variables;
    private List<Clause> clauses;

    /**
     * Initiates the {@linkplain KnowledgeBase}.
     */
    public KnowledgeBase() {
        predicates = new ArrayList<>();
        constants = new ArrayList<>();
        functions = new ArrayList<>();
        variables = new ArrayList<>();
        clauses = new ArrayList<>();
    }

    /**
     * Adds String representations of predicates.
     *
     * @param newPred {@link List} of predicates in KB
     */
    public void addPredicates(List<String> newPred) {
        predicates.addAll(newPred);
    }

    /**
     * Adds String representations of constants.
     *
     * @param newConst {@link List} of constants in KB
     */
    public void addConstants(List<String> newConst) {
        constants.addAll(newConst);
    }

    /**
     * Adds String representations of functions.
     *
     * @param newFunc {@link List} of functions in KB
     */
    public void addFunctions(List<String> newFunc) {
        functions.addAll(newFunc);
    }

    /**
     * Adds String representations of variables.
     *
     * @param newVar {@link List} of variables in KB
     */
    public void addVariables(List<String> newVar) {
        variables.addAll(newVar);
    }

    /**
     * Creates Clauses with provided data and adds them to
     * the {@linkplain KnowledgeBase}.
     *
     * @param newClauses {@link List} of clauses to be put in KB
     */
    public void addClauses(List<String> newClauses) {
        for (String temp : newClauses) {
            Clause clause = new Clause(temp, this);
            clauses.add(clause);
        }
    }

    /**
     * Checks whether the predicate is part of {@linkplain KnowledgeBase}.
     *
     * @param predicate predicate to be checked
     * @return true if predicate is part of {@linkplain KnowledgeBase}
     *         false, otherwise
     */
    public boolean isPredicateOfKB(String predicate) {
        return predicates.contains(predicate);
    }

    /**
     * Checks whether the variable is part of {@linkplain KnowledgeBase}.
     *
     * @param variable variable to be checked
     * @return true if variable is part of {@linkplain KnowledgeBase}
     *         false, otherwise
     */
    public boolean isVariableOfKB(String variable) {
        return variables.contains(variable);
    }

    /**
     * Checks whether the constant is part of {@linkplain KnowledgeBase}.
     *
     * @param constant constant to be checked
     * @return true if constant is part of {@linkplain KnowledgeBase}
     *         false, otherwise
     */
    public boolean isConstantOfKB(String constant) {
        return constants.contains(constant);
    }

    /**
     * Checks whether the function is part of {@linkplain KnowledgeBase}.
     *
     * @param function function to be checked
     * @return true if function is part of {@linkplain KnowledgeBase}
     *         false, otherwise
     */
    public boolean isFunctionOfKB(String function) {
        return functions.contains(function);
    }

    /**
     * Retrieves {@linkplain List} of {@linkplain Clause Clauses} in
     * {@linkplain KnowledgeBase}.
     *
     * @return clauses of {@linkplain KnowledgeBase}
     */
    public List<Clause> getClauses() {
        return clauses;
    }

    /**
     * Retrieves {@linkplain List} of Constants in {@linkplain KnowledgeBase}.
     *
     * @return constants of {@linkplain KnowledgeBase}
     */
    public List<String> getConstants() {
        return constants;
    }

    /**
     * Retrieves {@linkplain List} of Functions in {@linkplain KnowledgeBase}.
     *
     * @return functions of {@linkplain KnowledgeBase}
     */
    public List<String> getFunctions() {
        return functions;
    }

    /**
     * Retrieves {@linkplain List} of Predicates in {@linkplain KnowledgeBase}.
     *
     * @return predicates of {@linkplain KnowledgeBase}
     */
    public List<String> getPredicates() {
        return predicates;
    }

    /**
     * Retrieves {@linkplain List} of Variables in {@linkplain KnowledgeBase}.
     *
     * @return variables of {@linkplain KnowledgeBase}
     */
    public List<String> getVariables() {
        return variables;
    }

    /**
     * Prints out a toString representation of this {@linkplain KnowledgeBase}.
     *
     * @return String representation of this {@link KnowledgeBase}.
     */
    @Override
    public String toString() {
        return "Knowledge Base:" +
                "\nPredicates: " + predicates +
                "\nVariables: " + variables +
                "\nConstants: " + constants +
                "\nFunctions: " + functions +
                "\nClauses: " + clauses;
    }

}

import java.util.ArrayList;
import java.util.List;

public class KnowledgeBase {
    private List<String> predicates;
    private List<String> constants;
    private List<String> functions;
    private List<String> variables;
    private List<Clause> clauses;

    public KnowledgeBase() {
        predicates = new ArrayList<>();
        constants = new ArrayList<>();
        functions = new ArrayList<>();
        variables = new ArrayList<>();
        clauses = new ArrayList<>();
    }

    public void addPredicates(List<String> newPred) {
        predicates.addAll(newPred);
    }

    public void addConstants(List<String> newConst) {
        constants.addAll(newConst);
    }

    public void addFunctions(List<String> newFunc) {
        functions.addAll(newFunc);
    }

    public void addVariables(List<String> newVar) {
        variables.addAll(newVar);
    }

    public void addClauses(List<String> newClauses) {
        for (String temp : newClauses) {
            Clause clause = new Clause(temp);
            clauses.add(clause);
        }
    }

    public boolean isPredicateOfKB(String predicate) {
        return predicates.contains(predicate);
    }

    public boolean isVariableOfKB(String variable) {
        return variables.contains(variable);
    }

    public boolean isConstantOfKB(String constant) {
        return constants.contains(constant);
    }

    public boolean isFunctionOfKB(String function) {
        return functions.contains(function);
    }

    public List<Clause> getClauses() {
        return clauses;
    }

    public String toString() {
        return "Knowledge Base:" +
                "\nPredicates: " + predicates +
                "\nVariables: " + variables +
                "\nConstants: " + constants +
                "\nFunctions: " + functions +
                "\nClauses: " + clauses;
    }

}

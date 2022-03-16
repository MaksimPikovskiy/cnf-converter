import java.util.*;

/**
 * Resolution is class which uses Resolution algorithm to find whether a {@linkplain KnowledgeBase} is
 * satisfiable.
 *
 * @author <a href='mailto:mp8671@rit.edu'>Maksim Pikovskiy</a>
 */
public class Resolution {
    KnowledgeBase kb;

    /**
     * Constructs a {@linkplain Resolution} with provided {@linkplain KnowledgeBase}.
     *
     * @param newKB {@link KnowledgeBase}
     */
    public Resolution(KnowledgeBase newKB) {
        kb = newKB;
    }

    /**
     * Checks whether the provided {@linkplain KnowledgeBase} is satisfiable.
     * It uses {@linkplain Set Sets} to make sure there are no duplicate
     * {@linkplain Clause Clauses}.
     *
     * @return "no" if it isn't satisfiable (contains {@link Clause Empty Clause}
     *         "yes" if it is satisfiable
     */
    public String isSatisfiable() {
        Set<Clause> clauses = new LinkedHashSet<>(kb.getClauses());
        Set<Clause> newClauses = new LinkedHashSet<>();

        // Remove Tautologies if they are present in KB
        discardTautologies(clauses);

        while(true) {
            List<Clause> clausesList = new LinkedList<>(clauses);

            for(int i = 0; i < clausesList.size() - 1; i++) {
                Clause clause_i = clausesList.get(i);
                for(int j = i + 1; j < clauses.size(); j++) {
                    Clause clause_j = clausesList.get(j);
                    Set<Clause> resolvents = new LinkedHashSet<>();

                    resolvents.addAll(resolve(clause_i, clause_j));
                    resolvents.addAll(resolve(clause_j, clause_i));

                    // If any resolvent Clause is empty, KB is not satisfiable
                    for(Clause resolvent : resolvents) {
                        if(resolvent.equals(Clause.EMPTY)) {
                            return "no";
                        }
                    }

                    newClauses.addAll(resolvents);
                }
            }

            // If all the new clauses are contained in clauses, KB is satisfiable
            if (clauses.containsAll(newClauses)) {
                return "yes";
            }

            clauses.addAll(newClauses);
        }
    }

    /**
     * Checks whether two {@linkplain Clause Clauses} have same {@linkplain Predicate Predicates}.
     * It iterates over same {@linkplain Predicate Predicates} and builds new {@linkplain Clause Resolvent Clauses}
     * to be added to Resolution ongoing clauses {@linkplain List}.
     *
     * @param clause_i first {@link Clause} to be checked against another
     * @param clause_j second {@link Clause} to be checked against another
     * @return {@link Set} with unique {@link Clause Clauses} (no duplicates)
     */
    private Set<Clause> resolve(Clause clause_i, Clause clause_j) {
        Set<Clause> resolvents = new LinkedHashSet<>();

        // Get the Set of Predicates that are the same, excluding their negation (!friend(Kim) and friend(Kim))
        Set<Predicate> complementarySet = new LinkedHashSet<>(clause_i.getPositivePredicates());
        Set<Predicate> tempSet = new LinkedHashSet<>(clause_j.getNegativePredicates());
        if(complementarySet != tempSet) {
            complementarySet.retainAll(tempSet);
        }
        // Get the Mapping of replacement (e.g. x1 => Kim, x3 => George, etc.)
        Map<Term, Term> replacements = new HashMap<>();
        if(!complementarySet.isEmpty()) {
            replacements.putAll(getReplacements(clause_i.getPositivePredicates(), clause_j.getNegativePredicates()));
            replacements.putAll(getReplacements(clause_j.getNegativePredicates(), clause_i.getPositivePredicates()));
        }

        // Go through predicates, adding predicates that are not in the complementary set
        for(Predicate complement : complementarySet) {
            List<Predicate> resolventPredicates = new LinkedList<>();

            // If the clause is negated or is not equal to complement, add it to resolventPredicates
            // replace the terms inside Predicate if it is required (x1 => Kim / Variable => Constant/Function)
            for(Predicate ciPred : clause_i.getPredicates()) {
                if(ciPred.isNegated() || !ciPred.equals(complement)) {
                    replace(replacements, resolventPredicates, ciPred);
                }
            }

            // If the clause is negated or is not equal to complement, add it to resolventPredicates
            // replace the terms inside Predicate if it is required (x1 => Kim / Variable => Constant/Function)
            for(Predicate cjPred : clause_j.getPredicates()) {
                if(!cjPred.isNegated() || !cjPred.equals(complement)) {
                    replace(replacements, resolventPredicates, cjPred);
                }
            }

            // Create resolvent Clause and add it to resolvents if it is not a Tautology.
            Clause resolvent = new Clause(resolventPredicates, kb);
            if(!resolvent.isTautology()) {
                resolvents.add(resolvent);
            }
        }

        return resolvents;
    }

    /**
     * Replaces the {@linkplain Term Terms} of the {@linkplain Predicate}
     * Variable to Constant/Function replacements of {@linkplain Term Terms}.
     *
     * @param replacements the {@link Map} of replacements (Variable=Constant)
     * @param resolventPredicates the {@link List} of {@link Predicate Predicates} for new
     *                            {@link Clause} to contain
     * @param pred {@link Predicate} to have its {@link Term Terms} replaced
     */
    private void replace(Map<Term, Term> replacements, List<Predicate> resolventPredicates, Predicate pred) {
        // if there are replacements, Predicate contains terms in replacement map,
        // replace that Variable Term with the Constant/Function Term
        // otherwise, add the Predicate with no modification
        if(!replacements.isEmpty() &&
                containsAny(pred.getTerms(), new LinkedList<>(replacements.keySet()))) {
            List<Term> replaceTerms = pred.getTerms();
            int index;
            for(Term t1 : replacements.keySet()) {
                index = replaceTerms.indexOf(t1);
                if (index != -1) {
                    replaceTerms.set(index, replacements.get(t1));
                }
            }
            resolventPredicates.add(new Predicate(pred.getPredicate(), replaceTerms, pred.isNegated(), kb));
        } else {
            resolventPredicates.add(pred);
        }
    }

    /**
     * Retrives a {@linkplain Map} of {@linkplain Term} to {@linkplain Term} replacement.
     * It will be used to replace Variables with Constants or Functions.
     *
     * This method replaces the role of unify in this {@linkplain Resolution} algorithm.
     * It concerns variable to constant/function replacement, as two different variables can be
     * freely replaced with each other.
     * This logic is based on the website representing unification:
     *         https://www-users.cse.umn.edu/~gini/4511/unification.html
     *
     * @param list1 first {@link List} of {@link Predicate Predicates} to get Constants/Functions
     * @param list2 second {@link List} of {@link Predicate Predicates} to get Variables
     * @return {@linkplain Map} of {@linkplain Term Variable Term} to {@linkplain Term Constant/Function Term} replacement
     */
    private Map<Term, Term> getReplacements(List<Predicate> list1, List<Predicate> list2) {
        Map<Term, Term> replacements = new HashMap<>(); // (toReplace, replacement)

        // Go through two lists, finding equal clauses (complements),
        // create a mapping where first term is variable to be replaced and
        // second term is a constant or function to be put into the variable place.
        for(Predicate pred1 : list1) {
            for(Predicate pred2 : list2) {
                if (pred1.equals(pred2)) {
                    List<Term> terms = pred1.getTerms();
                    for (int i = 0; i < terms.size(); i++) {
                        Term term1 = terms.get(i);
                        Term term2 = pred2.getTerms().get(i);
                        if ((term1.isConstant(kb) || term1.isFunction(kb)) && term2.isVariable(kb)) {
                            replacements.put(term2, term1);
                        }
                    }
                }
            }
        }

        return replacements;

    }

    /**
     * Checks whether the one {@linkplain List} contains any {@linkplain Term} of another {@linkplain List}
     * @param list1 first {@link List} of {@link Term Terms}
     * @param list2 second {@link List} of {@link Term Terms}
     * @return true if first {@link List} contains any {@link Term} of second {@link List}
     *         false, otherwise
     */
    private boolean containsAny(List<Term> list1, List<Term> list2) {
        for(Term term1 : list1)
            for(Term term2 : list2)
                if(term1.equals(term2))
                    return true;
        return false;
    }

    /**
     * Removes {@linkplain Clause Clauses} that are tautologies.
     *
     * @param clauses {@link Set} of unique {@link Clause Clauses} to remove tautologies from
     */
    private void discardTautologies(Set<Clause> clauses) {
        clauses.removeIf(Clause::isTautology);
    }
}

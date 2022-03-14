import java.util.*;

public class Resolution {
    KnowledgeBase kb;

    public Resolution(KnowledgeBase newKB) {
        kb = newKB;
    }

    public String isSatisfiable() {
        Set<Clause> clauses = new LinkedHashSet<>(kb.getClauses());
        Set<Clause> newClauses = new LinkedHashSet<>();

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

                    for(Clause resolvent : resolvents) {
                        if(resolvent.equals(Clause.EMPTY)) {
                            return "no";
                        }
                    }

                    newClauses.addAll(resolvents);
                }
            }

            if (clauses.containsAll(newClauses)) {
                return "yes";
            }

            clauses.addAll(newClauses);
        }
    }

    private Set<Clause> resolve(Clause clause_i, Clause clause_j) {
        Set<Clause> resolvents = new LinkedHashSet<>();

        Set<Predicate> complementarySet = new LinkedHashSet<>(clause_i.getPositivePredicates());
        Set<Predicate> tempSet = new LinkedHashSet<>(clause_j.getNegativePredicates());
        if(complementarySet != tempSet) {
            complementarySet.retainAll(tempSet);
        }

        Map<Term, Term> replacements = new HashMap<>();
        if(!complementarySet.isEmpty()) {
            replacements.putAll(getReplacements(clause_i.getPositivePredicates(), clause_j.getNegativePredicates()));
            replacements.putAll(getReplacements(clause_j.getNegativePredicates(), clause_i.getPositivePredicates()));
        }

        for(Predicate complement : complementarySet) {
            List<Predicate> resolventLiterals = new LinkedList<>();

            for(Predicate ciPred : clause_i.getPredicates()) {
                if(ciPred.isNegated() || !ciPred.equals(complement)) {
                    replace(replacements, resolventLiterals, ciPred);
                }
            }

            for(Predicate cjPred : clause_j.getPredicates()) {
                if(!cjPred.isNegated() || !cjPred.equals(complement)) {
                    replace(replacements, resolventLiterals, cjPred);
                }
            }

            Clause resolvent = new Clause(resolventLiterals, kb);
            if(!resolvent.isTautology()) {
                resolvents.add(resolvent);
            }
        }

        return resolvents;
    }

    private void replace(Map<Term, Term> replacements, List<Predicate> resolventLiterals, Predicate pred) {
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
            resolventLiterals.add(new Predicate(pred.getPredicate(), replaceTerms, pred.isNegated(), kb));
        } else {
            resolventLiterals.add(pred);
        }
    }

    private Map<Term, Term> getReplacements(List<Predicate> list1, List<Predicate> list2) {
        Map<Term, Term> replacements = new HashMap<>(); // (toReplace, replacement)


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

    private boolean containsAny(List<Term> list1, List<Term> list2) {
        for(Term term1 : list1)
            for(Term term2 : list2)
                if(term1.equals(term2))
                    return true;
        return false;
    }

    private void discardTautologies(Set<Clause> clauses) {
        clauses.removeIf(Clause::isTautology);
    }
}

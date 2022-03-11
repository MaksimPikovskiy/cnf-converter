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

        for(Predicate complement : complementarySet) {
            List<Predicate> resolventLiterals = new LinkedList<>();

            for(Predicate ciPred : clause_i.getPredicates()) {
                if(ciPred.isNegated() || !ciPred.equals(complement)) {
                    resolventLiterals.add(ciPred);
                }
            }

            for(Predicate cjPred : clause_j.getPredicates()) {
                if(!cjPred.isNegated() || !cjPred.equals(complement)) {
                    resolventLiterals.add(cjPred);
                }
            }

            Clause resolvent = new Clause(resolventLiterals, kb);
            if(!resolvent.isTautology()) {
                resolvents.add(resolvent);
            }
        }

        return resolvents;
    }

    private void discardTautologies(Set<Clause> clauses) {
        clauses.removeIf(Clause::isTautology);
    }
}

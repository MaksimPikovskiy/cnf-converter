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
            List<Clause> clausesList = new ArrayList<>(clauses);

            for(int i = 0; i < clausesList.size() - 1; i++) {
                Clause clause_i = clausesList.get(i);
                for(int j = i + 1; j < clauses.size(); j++) {
                    Clause clause_j = clausesList.get(j);
                    Set<Clause> resolvents = new LinkedHashSet<>();
                    resolvents.addAll(resolve(clause_i, clause_j));
                    resolvents.addAll(resolve(clause_j, clause_i));

                    System.out.println(resolvents);
                    //System.out.println("\n1 " + Clause.EMPTY.isEmpty());
                    for(Clause resolvent : resolvents) {
                        //System.out.println("2 " + resolvent.isEmpty());
                        //System.out.println("3 " + resolvent.equals(Clause.EMPTY, kb));
                        if(resolvent.equals(Clause.EMPTY, kb)) {
                            return "no";
                        }
                    }
                    System.out.println("LOOP");
                    newClauses.addAll(resolvents);
                }
            }

            System.out.println("->" + clauses);
            System.out.println("->" + newClauses);

            for(Clause clause : clauses) {
                for(Clause newClause : newClauses) {
                    if(clause.equals(newClause, kb)) {
                        return "yes";
                    }
                }
            }

            clauses.addAll(newClauses);
        }
    }

    private Set<Clause> resolve(Clause clause_i, Clause clause_j) {
        Set<Clause> resolvents = new LinkedHashSet<>();

        Set<Predicate> complementarySet = new LinkedHashSet<>(clause_i.getPositivePredicates());
        complementarySet.addAll(clause_j.getNegativePredicates());
        for(Predicate complement : complementarySet) {
            List<Predicate> resolventLiterals = new LinkedList<>();


            for(Predicate ciPred : clause_i.getNegativePredicates()) {
                if(!ciPred.equals(complement, kb)) {
                    resolventLiterals.add(ciPred);
                }
            }

            for(Predicate cjPred : clause_j.getPositivePredicates()) {
                if(!cjPred.equals(complement, kb)) {
                    resolventLiterals.add(cjPred);
                }
            }

            Clause resolvent = new Clause(resolventLiterals);
            if(!resolvent.isTautology(kb)) {
                resolvents.add(resolvent);
            }
        }

        return resolvents;
    }

    private void discardTautologies(Set<Clause> clauses) {
        clauses.removeIf(n -> (n.isTautology(kb)));
    }
}

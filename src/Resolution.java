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
            //System.out.println("\nAll Clauses: " + clauses);
            List<Clause> clausesList = new LinkedList<>(clauses);

            for(int i = 0; i < clausesList.size() - 1; i++) {
                Clause clause_i = clausesList.get(i);
                for(int j = i + 1; j < clauses.size(); j++) {
                    Clause clause_j = clausesList.get(j);
                    Set<Clause> resolvents = new LinkedHashSet<>();

                    System.out.println("\n\t" + clause_i + " AND " + clause_j);
                    System.out.println("\n\tNew Clauses: " + newClauses);

                    System.out.println("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
                    resolvents.addAll(resolve(clause_i, clause_j));
                    resolvents.addAll(resolve(clause_j, clause_i));
                    System.out.println("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-\n");

                    //System.out.println("\t Resolvents: " + resolvents);
                    for(Clause resolvent : resolvents) {
                        if(resolvent.equals(Clause.EMPTY)) {
                            return "no";
                        }
                    }

                    newClauses.addAll(resolvents);
                }
            }
            System.out.println("\tClauses: " + clauses);
            System.out.println("\tNew Clauses: " + newClauses);
            System.out.println("\tClauses has New Clauses: " + clauses.containsAll(newClauses));
            List<Clause> list1 = new ArrayList<>(clauses);
            List<Clause> list2 = new ArrayList<>(newClauses);
            boolean flag = false;
            for(int i = 0; i < list1.size(); i++) {
                for(int j = 0; j < list2.size(); j++) {
                    System.out.println("\t\t" + list1.get(i) + " == " + list2.get(j));
                    System.out.println("\t\t" + list1.get(i).equals(list2.get(j)));
                    if(list1.get(i).equals(list2.get(j))) {
                        flag = true;
                    }
                }
            }
            System.out.println("\tClauses has New Clauses: " + flag);
            if (clauses.containsAll(newClauses)) {
                return "yes";
            }

            clauses.addAll(newClauses);
        }
    }

    private Set<Clause> resolve(Clause clause_i, Clause clause_j) {
        Set<Clause> resolvents = new LinkedHashSet<>();

        Set<Predicate> set1 = new LinkedHashSet<>(clause_i.getPositivePredicates());
        Set<Predicate> set2 = new LinkedHashSet<>(clause_j.getNegativePredicates());
        System.out.println("positive preds: " + set1);
        System.out.println("negativae preds: " + set2);
        Set<Predicate> complementarySet = new LinkedHashSet<>(set1);
        if(set1 != set2) {
            complementarySet.retainAll(set2);
        }

        System.out.println("Complementary Set: " + complementarySet);

        for(Predicate complement : complementarySet) {
            System.out.println("complement: " + complement);
            List<Predicate> resolventLiterals = new LinkedList<>();


            for(Predicate ciPred : clause_i.getPredicates()) {
                System.out.println("ciPred: " + ciPred);
                if(ciPred.isNegated() || !ciPred.equals(complement)) {
                    resolventLiterals.add(ciPred);
                }
            }

            for(Predicate cjPred : clause_j.getPredicates()) {
                System.out.println("cjPred: " + cjPred);
                if(!cjPred.isNegated() || !cjPred.equals(complement)) {
                    resolventLiterals.add(cjPred);
                }
            }

            Clause resolvent = new Clause(resolventLiterals, kb);
            System.out.println("Add Resolvent: " + resolvent);
            System.out.println("Add Resolvents: " + resolvents);
            if(!resolvent.isTautology()) {
                resolvents.add(resolvent); //calls clause.equals
            }
        }

//        try {
//            Thread.sleep(2500);
//        } catch (InterruptedException e) {
//
//        }

        if(resolvents.isEmpty()) {
            System.out.println("Resolvents: none");
        }
        else {
            System.out.println("Resolvents: " + resolvents);
            List<Clause> temp = new ArrayList<>(resolvents);
            if(temp.size() == 2) {
                System.out.println(temp.get(0) + "is equal to " + temp.get(1));
                System.out.println(temp.get(0).equals(temp.get(1)));
            }
        }

        return resolvents;
    }

    private void discardTautologies(Set<Clause> clauses) {
        clauses.removeIf(Clause::isTautology);
    }
}

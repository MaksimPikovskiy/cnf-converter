import java.util.*;
import java.util.stream.StreamSupport;

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
            List<Clause> clausesList = new ArrayList<>(clauses);

            for(int i = 0; i < clausesList.size() - 1; i++) {
                Clause clause_i = clausesList.get(i);
                for(int j = i + 1; j < clauses.size(); j++) {
                    Clause clause_j = clausesList.get(j);
                    Set<Clause> resolvents = new LinkedHashSet<>();

                    //System.out.println("\n\t" + clause_i + " and " + clause_j);
                    //System.out.println("\n\tNew Clauses: " + newClauses);

                    //System.out.println("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
                    resolvents.addAll(resolve(clause_i, clause_j));
                    //resolvents.addAll(resolve(clause_j, clause_i));
                    //System.out.println("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-\n");

                    //System.out.println("\t Resolvents: " + resolvents);
                    for(Clause resolvent : resolvents) {
                        if(resolvent.equals(Clause.EMPTY)) {
                            return "yes";
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
                return "no";
            }

            clauses.addAll(newClauses);
        }
    }

    private Set<Clause> resolve(Clause clause1, Clause clause2) {
        Set<Clause> resolvents = new LinkedHashSet<>();

        List<Predicate> clausePredicates1 = new LinkedList<>(clause1.getPositivePredicates());
        List<Predicate> clausePredicates2 = new LinkedList<>(clause2.getNegativePredicates());

        System.out.println("before 1: " + clausePredicates1);
        System.out.println("before 1: " + clausePredicates2);

        if(clausePredicates1.isEmpty() && !clausePredicates2.isEmpty()) {
            Clause resolvent = new Clause(clausePredicates2, kb);
            resolvents.add(resolvent);
        }
        else if(clausePredicates2.isEmpty() && !clausePredicates1.isEmpty()) {
            Clause resolvent = new Clause(clausePredicates1, kb);
            resolvents.add(resolvent);
        }

        outerloop:
        for(int i = clausePredicates1.size() - 1; i >= 0; i--) {
            Predicate pred1 = clausePredicates1.get(i);
            for(int j = clausePredicates2.size() - 1; j >= 0; j--) {
                Predicate pred2 = clausePredicates2.get(j);
                System.out.println("\t" + pred1 + " =/= " + pred2 + " : " + !pred1.equals(pred2));
                System.out.println("\t" + pred1 + " =/= " + pred2 + " (tautology): " + pred1.BothCreateTautology(pred2));
                if(!pred1.equals(pred2) && pred1.BothCreateTautology(pred2)) {
                    clausePredicates1.remove(i);
                    clausePredicates2.remove(j);
                    if(clausePredicates1.isEmpty() || clausePredicates2.isEmpty()) {
                        break outerloop;
                    }
                }
            }
        }

        System.out.println("after 1: " + clausePredicates1);
        System.out.println("after 1: " + clausePredicates2);

        List<Predicate> leftOverPreds = new LinkedList<>();
        if(!clausePredicates1.isEmpty()) leftOverPreds.addAll(clausePredicates1);
        if(!clausePredicates2.isEmpty()) leftOverPreds.addAll(clausePredicates2);

        clausePredicates1 = new LinkedList<>(clause1.getNegativePredicates());
        clausePredicates2 = new LinkedList<>(clause2.getPositivePredicates());

        System.out.println("before 2: " + clausePredicates1);
        System.out.println("before 2: " + clausePredicates2);

        if(clausePredicates1.isEmpty() && !clausePredicates2.isEmpty()) {
            Clause resolvent = new Clause(clausePredicates2, kb);
            resolvents.add(resolvent);
        }
        else if(clausePredicates2.isEmpty() && !clausePredicates1.isEmpty()) {
            Clause resolvent = new Clause(clausePredicates1, kb);
            resolvents.add(resolvent);
        }

        outerloop:
        for(int i = clausePredicates1.size() - 1; i >= 0; i--) {
            Predicate pred1 = clausePredicates1.get(i);
            for(int j = clausePredicates2.size() - 1; j >= 0; j--) {
                Predicate pred2 = clausePredicates2.get(j);
                System.out.println("\t" + pred1 + " =/= " + pred2 + " : " + !pred1.equals(pred2));
                System.out.println("\t" + pred1 + " =/= " + pred2 + " (tautology): " + pred1.BothCreateTautology(pred2));
                if(!pred1.equals(pred2) && pred1.BothCreateTautology(pred2)) {
                    clausePredicates1.remove(i);
                    clausePredicates2.remove(j);
                    if(clausePredicates1.isEmpty() || clausePredicates2.isEmpty()) {
                        break outerloop;
                    }
                }
            }
        }

        System.out.println("after 2: " + clausePredicates1);
        System.out.println("after 2: " + clausePredicates2);

        if(!clausePredicates1.isEmpty()) leftOverPreds.addAll(clausePredicates1);
        if(!clausePredicates2.isEmpty()) leftOverPreds.addAll(clausePredicates2);

        Clause resolvent = new Clause(leftOverPreds, kb);
        resolvents.add(resolvent);
        System.out.println(resolvents);
        return resolvents;
    }

//    private Set<Clause> resolve(Clause clause_i, Clause clause_j) {
//        Set<Clause> resolvents = new LinkedHashSet<>();
//
//        Set<Predicate> complementarySet = new LinkedHashSet<>(clause_i.getPositivePredicates());
//        complementarySet.addAll(clause_j.getNegativePredicates());
//
//        System.out.println("Complementary Set: " + complementarySet);
//
//        for(Predicate complement : complementarySet) {
//            System.out.println("complement: " + complement);
//            List<Predicate> resolventLiterals = new LinkedList<>();
//
//
//            resolventLiterals.addAll(clause_i.getNegativePredicates());
////            for(Predicate ciPred : clause_i.getNegativePredicates()) {
////                System.out.println("ciPred: " + ciPred);
////                    resolventLiterals.add(ciPred);
////            }
//            resolventLiterals.addAll(clause_i.getPositivePredicates());
////            for(Predicate cjPred : clause_j.getPositivePredicates()) {
////                System.out.println("cjPred: " + cjPred);
////                    resolventLiterals.add(cjPred);
////            }
//
//            Clause resolvent = new Clause(resolventLiterals, kb);
//            System.out.println("Add Resolvent: " + resolvent);
//            System.out.println("Add Resolvents: " + resolvents);
//            if(!resolvent.isTautology()) {
//                resolvents.add(resolvent); //calls clause.equals
//            }
//        }
//
//        try {
//            Thread.sleep(2500);
//        } catch (InterruptedException e) {
//
//        }
//
//        if(resolvents.isEmpty()) {
//            System.out.println("Resolvents: none");
//        }
//        else {
//            System.out.println("Resolvents: " + resolvents);
//            List<Clause> temp = new ArrayList<>(resolvents);
//            if(temp.size() == 2) {
//                System.out.println(temp.get(0) + "is equal to " + temp.get(1));
//                System.out.println(temp.get(0).equals(temp.get(1)));
//            }
//        }
//
//        return resolvents;
//    }

    private void discardTautologies(Set<Clause> clauses) {
        clauses.removeIf(Clause::isTautology);
    }
}

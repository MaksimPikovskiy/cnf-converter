import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class Clause {
    public static final Clause EMPTY = new Clause();

    private KnowledgeBase kb;

    private List<Predicate> predicates;
    private List<Predicate> positivePredicates;
    private List<Predicate> negativePredicates;

    public Clause(String clause, KnowledgeBase ownKB) {
        kb = ownKB;

        predicates = new LinkedList<>();
        positivePredicates = new LinkedList<>();
        negativePredicates = new LinkedList<>();

        String[] given_predicates = clause.split(" ");
        for(String temp : given_predicates) {
            Predicate predicate = new Predicate(temp, kb);
            predicates.add(predicate);
            if(predicate.isNegated()) {
                negativePredicates.add(predicate);
            }
            else {
                positivePredicates.add(predicate);
            }
        }
    }

    public Clause(List<Predicate> new_pred, KnowledgeBase ownKB) {
        kb = ownKB;

        predicates = new LinkedList<>(new_pred);
        positivePredicates = new LinkedList<>();
        negativePredicates = new LinkedList<>();

        for(Predicate pred : new_pred) {
            if(pred.isNegated()) {
                negativePredicates.add(pred);
            }
            else {
                positivePredicates.add(pred);
            }
        }
    }

    public Clause() {
        this(new LinkedList<Predicate>(), new KnowledgeBase());
    }

    public boolean isTautology() {
        if(predicates.size() == 2) {
            for(int i = 0; i < predicates.size() - 1; i++) {
                Predicate pred_i = predicates.get(i);
                for(int j = i + 1; j < predicates.size(); j++) {
                    Predicate pred_j = predicates.get(j);
                    if(pred_i.BothCreateTautology(pred_j)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public List<Predicate> getPredicates() {
        return predicates;
    }

    public List<Predicate> getPositivePredicates() {
        return positivePredicates;
    }

    public List<Predicate> getNegativePredicates() {
        return negativePredicates;
    }

    @Override
    public int hashCode() {
        return Objects.hash(predicates, positivePredicates, negativePredicates);
    }

    @Override
    public boolean equals(Object that) {
        if(this == that) return true;
        if(that == null) return false;
        if (this.getClass() != that.getClass()) return false;

        Clause thatClause = (Clause) that;
        if((!this.isEmpty() && thatClause.isEmpty()) || (this.isEmpty() && !thatClause.isEmpty())) {
            return false;
        }

        return predicates.size() == thatClause.getPredicates().size() && predicates.containsAll(thatClause.getPredicates());
    }

    public boolean isEmpty() {
        return predicates.isEmpty();
    }

    public String toString() {
        StringJoiner str = new StringJoiner(" ");
        for(Predicate predicate : positivePredicates) {
            str.add(predicate.toString());
        }
        for(Predicate predicate : negativePredicates) {
            str.add(predicate.toString());
        }
        return str.toString();
    }
}

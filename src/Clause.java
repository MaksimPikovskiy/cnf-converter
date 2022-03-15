import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Clause is a data structure class that represents a clause in the {@linkplain KnowledgeBase}.
 *
 * It has:
 *      -{@linkplain KnowledgeBase} it is in
 *      -all of its predicates
 *      -its positive predicates
 *      -its negative predicates
 *      -Empty Clause representation
 *
 * @author <a href='mailto:mp8671@rit.edu'>Maksim Pikovskiy</a>
 */
public class Clause {
    public static final Clause EMPTY = new Clause();

    private KnowledgeBase kb;

    private List<Predicate> predicates;
    private List<Predicate> positivePredicates;
    private List<Predicate> negativePredicates;

    /**
     * Constructs a {@linkplain Clause} with given values.
     *
     * @param clause the String clause to be converted to {@link Clause}
     * @param ownKB {@link KnowledgeBase} of the clause that is getting created
     */
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

    /**
     * Constucts a {@linkplain Clause} with given {@linkplain List} of
     * {@linkplain Predicate Predicates} and its {@linkplain KnowledgeBase}.
     *
     * @param new_pred {@link Predicate Predicates} of this {@link Clause}
     * @param ownKB {@link KnowledgeBase} of the clause that is getting created
     */
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

    /**
     * Constructs an {@linkplain Clause Empty Clause}.
     */
    public Clause() {
        this(new LinkedList<Predicate>(), new KnowledgeBase());
    }

    /**
     * Checks whether {@linkplain Clause} is a tautology.
     *
     * @return true if {@linkplain Clause} is a tautology
     *         false, otherwise
     */
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

    /**
     * Retrieves the {@linkplain Predicate Predicates} of this {@linkplain Clause}.
     *
     * @return {@link List} of {@link Predicate Predicates}.
     */
    public List<Predicate> getPredicates() {
        return predicates;
    }

    /**
     * Retrieves the {@linkplain Predicate positive Predicates} of this {@linkplain Clause}.
     *
     * @return {@link List} of {@link Predicate positive Predicates}.
     */
    public List<Predicate> getPositivePredicates() {
        return positivePredicates;
    }

    /**
     * Retrieves the {@linkplain Predicate negative Predicates} of this {@linkplain Clause}.
     *
     * @return {@link List} of {@link Predicate negative Predicates}.
     */
    public List<Predicate> getNegativePredicates() {
        return negativePredicates;
    }

    /**
     * Retrieves the hash code of this {@linkplain Clause}.
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(predicates, positivePredicates, negativePredicates);
    }

    /**
     * Checks whether two {@linkplain Clause Clauses} are equal.
     *
     * @param that a {@link Clause} to compare this {@link Clause} to
     * @return true if two clauses are the same.
     *         false, otherwise
     */
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

    /**
     * Checks whether the clause has no {@linkplain Predicate predicates} (is Empty Clause).
     *
     * @return true if {@link Clause} is empty
     *         false, otherwise
     */
    public boolean isEmpty() {
        return predicates.isEmpty();
    }

    /**
     * Prints out a toString representation of this {@linkplain Clause}.
     *
     * @return String representation of this {@link Clause}.
     */
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

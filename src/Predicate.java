import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Predicates is a data structure class that represents a predicate that a {@linkplain Clause} of
 * {@linkplain KnowledgeBase} can contain.
 *
 * It has:
 *      -its {@linkplain KnowledgeBase}
 *      -predicate String representation
 *      -its terms
 *      -check whether it is negated.
 *
 * @author <a href='mailto:mp8671@rit.edu'>Maksim Pikovskiy</a>
 */
public class Predicate {
    private KnowledgeBase kb;

    private boolean isNegated;
    private String predicate;
    private List<Term> terms;

    /**
     * Constucts a {@linkplain Predicate} with given values.
     *
     * @param pred predicate String representation
     * @param ownKB {@link KnowledgeBase} of the predicate that is getting created
     */
    public Predicate(String pred, KnowledgeBase ownKB) {
        kb = ownKB;

        isNegated = (pred.charAt(0) == '!');

        if(isNegated)
            pred = pred.substring(1);

        terms = new LinkedList<>();

        int termStart = pred.indexOf("(");
        if(termStart != -1) {
            predicate = pred.substring(0, termStart);

            String[] tempTerms = (pred.substring(termStart + 1, pred.length() - 1)).split(",");
            for (String temp : tempTerms) {
                Term tempTerm = new Term(temp);
                terms.add(tempTerm);
            }
        }
        else {
            predicate = pred;
        }
    }

    /**
     * Creates a {@linkplain Predicate} with given values
     * (copies over values from another {@linkplain Predicate}).
     *
     * @param predicate predicate String representation
     * @param terms terms the {@link Predicate}
     * @param isNegated boolean value whether {@link Predicate}
     * @param kb {@link KnowledgeBase} of the predicate that is getting created
     */
    public Predicate(String predicate, List<Term> terms, boolean isNegated, KnowledgeBase kb) {
        this.predicate = predicate;
        this.terms = terms;
        this.isNegated = isNegated;
        this.kb = kb;
    }

    /**
     * Retrieves the String representation of {@linkplain Predicate}.
     *
     * @return predicate String representation
     */
    public String getPredicate() {
        return predicate;
    }

    /**
     * Retrieves the {@linkplain List} of {@linkplain Term Terms} of {@linkplain Predicate}.
     *
     * @return the {@linkplain List} of {@linkplain Term Terms}
     */
    public List<Term> getTerms() {
        return terms;
    }

    /**
     * Retrieves whether this {@linkplain Predicate} is negated.
     *
     * @return true if {@linkplain Predicate} is negated
     *         false, otherwise
     */
    public boolean isNegated() {
        return isNegated;
    }

    /**
     * Retrieves the hash code of this {@linkplain Predicate}.
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(predicate, terms.size());
    }

    /**
     * Checks whether two {@linkplain Predicate Predicates} are equal.
     *
     * @param that a {@link Predicate} to compare this {@link Predicate} to
     * @return true if two predicates are the same.
     *         false, otherwise
     */
    @Override
    public boolean equals(Object that) {
        if(this == that) return true;
        if(that == null) return false;
        if (this.getClass() != that.getClass()) return false;

        Predicate thatPred = (Predicate) that;
        boolean flag = predicate.equals(thatPred.getPredicate())
                && terms.size() == thatPred.getTerms().size();

        if(!flag)
            return false;

        for(int i = 0; i < terms.size(); i++) {
            Term term1 = terms.get(i);
            Term term2 = thatPred.getTerms().get(i);

            if((term1.isConstant(kb) && term2.isConstant(kb) && !term1.equals(term2)) ||
                    (term1.isConstant(kb) && term2.isFunction(kb)) ||
                    (term1.isFunction(kb) && term2.isConstant(kb)) ||
                    (term1.isVariable(kb) && term2.isFunction(kb) && term2.getFuncTerms().contains(term1)) ||
                    (term1.isFunction(kb) && term2.isFunction(kb) && !term1.equalsFunction(term2))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks whether this {@linkplain Predicate} and another {@linkplain Predicate} create a tautology.
     *
     * @param that presumably {@linkplain Predicate}
     * @return true if this {@linkplain Predicate} and another {@linkplain Predicate} create tautology
     *         false, otherwise
     */
    public boolean BothCreateTautology(Predicate that) {
        boolean flag = predicate.equals(that.getPredicate())
                && terms.size() == that.getTerms().size()
                && ((this.isNegated() && !that.isNegated()) || (!this.isNegated() && that.isNegated()));

        if(!flag)
            return false;

        for(int i = 0; i < terms.size(); i++) {
            Term term1 = terms.get(i);
            Term term2 = that.getTerms().get(i);

            if((term1.isConstant(kb) && term2.isConstant(kb) && !term1.equals(term2)) ||
                    (term1.isConstant(kb) && term2.isFunction(kb)) ||
                    (term1.isFunction(kb) && term2.isConstant(kb)) ||
                    (term1.isVariable(kb) && term2.isFunction(kb) && term2.getFuncTerms().contains(term1)) ||
                    (term1.isFunction(kb) && term2.isFunction(kb) && !term1.equalsFunction(term2))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Prints out a toString representation of this {@linkplain Predicate}.
     *
     * @return String representation of this {@link Predicate}.
     */
    @Override
    public String toString() {
        StringJoiner str = new StringJoiner(", ");
        for(Term temp : terms) {
            str.add(temp.toString());
        }
        return ((isNegated) ? "!" : "") + predicate + "(" + str + ")";
    }
}

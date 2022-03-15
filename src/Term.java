import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Term is a data structure class that represents a {@linkplain Term} of {@linkplain Predicate}.
 *
 * It has:
 *      -term String representation
 *      -check whether this term is a function
 *      -list of terms if it is a function
 *
 * @author <a href='mailto:mp8671@rit.edu'>Maksim Pikovskiy</a>
 */
public class Term {
    private String term;
    private List<Term> funcTerms;
    private boolean isFunction;

    /**
     * Constructs a {@linkplain Term} with given value.
     * If the term is a function, it calls on buildFunction method.
     *
     * @param newTerm the new Term
     */
    public Term(String newTerm) {
        if(newTerm.contains("(") && newTerm.contains(")")) {
            buildFunction(newTerm);
        }
        else {
            term = newTerm;
            isFunction = false;
        }
    }

    /**
     * Builds this {@linkplain Term} as a function.
     *
     * @param newTerm the term to be built as function
     */
    private void buildFunction(String newTerm) {
        isFunction = true;

        funcTerms = new LinkedList<>();

        int termStart = newTerm.indexOf("(");
        term = newTerm.substring(0, termStart);

        String[] tempTerms = (newTerm.substring(termStart + 1, newTerm.length() - 1)).split(",");
        for(String temp: tempTerms) {
            Term tempTerm = new Term(temp);
            funcTerms.add(tempTerm);
        }
    }

    /**
     * Checks if this {@linkplain Term} is a function
     *
     * @param kb {@link KnowledgeBase} to be checked against.
     * @return true if this {@link Term} is a function
     *         false, otherwise
     */
    public boolean isFunction(KnowledgeBase kb) {
        return isFunction && kb.isFunctionOfKB(term);
    }

    /**
     * Checks if this {@linkplain Term} is a constant.
     *
     * @param kb {@link KnowledgeBase} to be checked against.
     * @return true if this {@link Term} is a constant
     *         false, otherwise
     */
    public boolean isConstant(KnowledgeBase kb) {
        return kb.isConstantOfKB(term);
    }

    /**
     * Checks if this {@linkplain Term} is a variable.
     *
     * @param kb {@link KnowledgeBase} to be checked against.
     * @return true if this {@link Term} is a variable
     *         false, otherwise
     */
    public boolean isVariable(KnowledgeBase kb) {
        return kb.isVariableOfKB(term);
    }

    /**
     * Retrieves String representation of this {@linkplain Term}.
     *
     * @return term String representation
     */
    public String getTerm() {
        return term;
    }

    /**
     * Retrieves {@linkplain List} of {@linkplain Term Terms} for this
     * function term.
     *
     * @return {@linkplain List} of {@linkplain Term Terms}
     */
    public List<Term> getFuncTerms() {
        return funcTerms;
    }

    /**
     * Retrieves the hash code of this {@linkplain Term}.
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(term, funcTerms, isFunction);
    }

    /**
     * Checks whether two {@linkplain Term Terms} are equal.
     *
     * @param that a {@link Term} to compare this {@link Term} to
     * @return true if two terms are the same.
     *         false, otherwise
     */
    @Override
    public boolean equals(Object that) {
        if(this == that) return true;
        if(that == null) return false;
        if (this.getClass() != that.getClass()) return false;

        Term thatTerm = (Term) that;
        return term.equals(thatTerm.getTerm());
    }

    /**
     * Checks whether two {@linkplain Term Function Terms} are equal.
     *
     * @param that a {@link Term} to compare this {@link Term} to
     * @return true if two function terms are the same.
     *         false, otherwise
     */
    public boolean equalsFunction(Object that) {
        boolean flag = this.equals(that);

        //System.out.println("this that equal " + flag);

        if(flag && funcTerms != null) {
            Term thatTerm = (Term) that;

            flag = funcTerms.equals(thatTerm.getFuncTerms());
        }
        return flag;
    }

    /**
     * Prints out a toString representation of this {@linkplain Term}.
     *
     * @return String representation of this {@link Term}.
     */
    @Override
    public String toString() {
        if(isFunction) {
            StringJoiner str = new StringJoiner(", ");
            for(Term temp : funcTerms) {
                str.add(temp.toString());
            }
            return term + "(" + str + ")";
        }
        return term;
    }
}

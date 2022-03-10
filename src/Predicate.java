import java.util.LinkedList;
import java.util.List;
import java.util.StringJoiner;

public class Predicate {
    private boolean isNegated;
    private String predicate;
    private List<Term> terms;

    public Predicate(String pred) {
        isNegated = (pred.charAt(0) == '!');

        if(isNegated)
            pred = pred.substring(1);

        terms = new LinkedList<>();

        int termStart = pred.indexOf("(");
        predicate = pred.substring(0, termStart);

        String[] tempTerms = (pred.substring(termStart + 1, pred.length() - 1)).split(",");
        for(String temp: tempTerms) {
            Term tempTerm = new Term(temp);
            terms.add(tempTerm);
        }
    }

    public String getPredicate() {
        return predicate;
    }

    public List<Term> getTerms() {
        return terms;
    }

    public boolean isNegated() {
        return isNegated;
    }

    public boolean equals(Object that, KnowledgeBase kb) {
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
                    (!term1.equalsFunction(term2))) {
                return false;
            }
        }
        return true;
    }

    public String toString() {
        StringJoiner str = new StringJoiner(", ");
        for(Term temp : terms) {
            str.add(temp.toString());
        }
        return ((isNegated) ? "!" : "") + predicate + "(" + str + ")";
    }
}

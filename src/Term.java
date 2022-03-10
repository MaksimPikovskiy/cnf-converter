import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class Term {
    private String term;
    private List<Term> funcTerms;
    private boolean isFunction;

    public Term(String newTerm) {
        if(newTerm.contains("(") && newTerm.contains(")")) {
            buildFunction(newTerm);
        }
        else {
            term = newTerm;
            isFunction = false;
        }
    }

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

    public boolean isFunction(KnowledgeBase kb) {
        return isFunction && kb.isFunctionOfKB(term);
    }

    public boolean isConstant(KnowledgeBase kb) {
        return kb.isConstantOfKB(term);
    }

    public boolean isVariable(KnowledgeBase kb) {
        return kb.isVariableOfKB(term);
    }

    public String getTerm() {
        return term;
    }

    public List<Term> getFuncTerms() {
        return funcTerms;
    }

    @Override
    public int hashCode() {
        return Objects.hash(term, funcTerms, isFunction);
    }

    @Override
    public boolean equals(Object that) {
        if(this == that) return true;
        if(that == null) return false;
        if (this.getClass() != that.getClass()) return false;

        Term thatTerm = (Term) that;
        return term.equals(thatTerm.getTerm());
    }

    public boolean equalsFunction(Object that) {
        boolean flag = equals(that);

        if(flag && funcTerms != null) {
            Term thatTerm = (Term) that;

            flag = funcTerms.equals(thatTerm.getFuncTerms());
        }
        return flag;
    }

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

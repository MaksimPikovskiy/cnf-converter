import java.util.Arrays;
import java.util.List;

public class Predicate {
    boolean isNegated;
    String predicate;
    List<String> terms;

    public Predicate(String pred) {
        isNegated = (pred.charAt(0) == '!');

        if(isNegated)
            pred = pred.substring(1);

        int termStart = pred.indexOf("(");

        predicate = pred.substring(0, termStart);
        terms = Arrays.asList((pred.substring(termStart + 1, pred.length() - 1)).split(", "));
    }

    public String toString() {
        return ((isNegated) ? "!" : "") + predicate + "(" + String.join(", ", terms) + ")";
    }
}

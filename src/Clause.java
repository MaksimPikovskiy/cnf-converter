import java.util.LinkedList;
import java.util.List;
import java.util.StringJoiner;

public class Clause {
    List<Predicate> positivePredicates;
    List<Predicate> negativePredicates;

    public Clause(String clause) {
        positivePredicates = new LinkedList<>();
        negativePredicates = new LinkedList<>();

        String[] predicates = clause.split(" ");
        for(String temp : predicates) {
            Predicate predicate = new Predicate(temp);
            if(predicate.isNegated) {
                negativePredicates.add(predicate);
            }
            else {
                positivePredicates.add(predicate);
            }
        }
        System.out.println(positivePredicates.size());
        System.out.println(negativePredicates.size());
    }

    public String toString() {
        StringJoiner str = new StringJoiner(", ");
        for(Predicate predicate : positivePredicates) {
            str.add(predicate.toString());
        }
        for(Predicate predicate : negativePredicates) {
            str.add(predicate.toString());
        }
        return str.toString();
    }
}

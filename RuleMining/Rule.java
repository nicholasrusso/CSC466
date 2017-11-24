package RuleMining;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by cgels on 11/3/17.
 */
public class Rule implements Serializable {
    public ItemSet left;
    public ItemSet right;

    public Rule() {

    }

    public Rule(ItemSet left, ItemSet right) {
        this.left = left;
        this.right = right;
    }

    public ItemSet getUnion() {
        ArrayList<Integer> union = left.getItems();
        union.addAll(right.getItems());

        Collections.sort(union);

        return new ItemSet(union);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rule rule = (Rule) o;

        if (!left.equals(rule.left)) return false;
        return right.equals(rule.right);
    }

    @Override
    public int hashCode() {
        int result = left.hashCode();
        result = 31 * result + right.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return left.toString() + "--> " + right.toString();
    }
}

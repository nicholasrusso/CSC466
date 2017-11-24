package RuleMining;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by cgels on 11/24/17.
 */
public class RuleTest {
    Rule rule;
    ItemSet original;
    ItemSet left;
    ItemSet right;

    @Before
    public void setUp() throws Exception {
        original = new ItemSet();
        original.add(1);
        original.add(2);
        original.add(3);
        original.add(4);
    }

    @Test
    public void getUnion() throws Exception {
        left = new ItemSet();
        right = new ItemSet();

        left.add(original.getItem(0));
        left.add(original.getItem(2));
        right.add(original.getItem(1));
        right.add(original.getItem(3));

        rule = new Rule(left, right);
        assertEquals(original, rule.getUnion());


    }

}
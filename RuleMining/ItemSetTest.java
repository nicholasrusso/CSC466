package RuleMining;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by cgels on 11/3/17.
 */
public class ItemSetTest {
    ItemSet test;

    @Before
    public void setup() {
        test = new ItemSet();
    }

    @Test
    public void contains() throws Exception {
        test.add(1);
        assertTrue(test.contains(1));
        assertFalse(test.contains(2));
    }

    @Test
    public void containsSubset() throws Exception {
        test.add(1);
        test.add(2);
        test.add(3);

        ItemSet subset = new ItemSet();
        subset.add(1);
        subset.add(2);

        assertTrue(test.containsSubset(subset));

        ItemSet notSubset = new ItemSet();
        notSubset.add(1);
        notSubset.add(5);

        assertFalse(test.containsSubset(notSubset));
    }

    @Test
    public void equals() throws Exception {
        test.add(1);
        ItemSet eq = new ItemSet();
        eq.add(1);

        assertTrue(test.equals(eq));

        ItemSet neq = new ItemSet();
        neq.add(2);
        assertFalse(test.equals(neq));
    }

}
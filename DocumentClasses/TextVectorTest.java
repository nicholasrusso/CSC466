package DocumentClasses;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by cgels on 9/14/17.
 */
public class TextVectorTest {
    public DocumentClasses.TextVector vector;

    @Before
    public void setup() {
        vector = new TextVector();
    }

    @Test
    public void add() throws Exception {
        vector.add("test1");
        assertEquals(1, vector.getDistinctWordCount());
        assertEquals(1,vector.getTotalWordCount());

        vector.add("test1");
        assertEquals(1, vector.getDistinctWordCount());
        assertEquals(2, vector.getTotalWordCount());
        vector.add("test2");
        assertEquals(2, vector.getDistinctWordCount());
    }

    @Test
    public void getRawRequency() throws Exception {
        vector.add("test1");
        assertEquals(1, vector.getRawRequency("test1"));
        vector.add("test1");
        assertEquals(2, vector.getRawRequency("test1"));
    }

    @Test
    public void contains() throws Exception {
        vector.add("test1");
        assertTrue(vector.contains("test1"));
        assertFalse(vector.contains("test2"));
    }

    @Test
    public void getTotalWordCount() throws Exception {
        vector.add("test1");
        vector.add("test1");
        vector.add("test2");
        assertEquals(3,vector.getTotalWordCount());
    }

    @Test
    public void getDistinctWordCount() throws Exception {
        vector.add("test1");
        vector.add("test1");
        vector.add("test2");
        assertEquals(2, vector.getDistinctWordCount());
    }

    @Test
    public void getHighestRawFrequency() throws Exception {
        vector.add("test1");
        vector.add("test1");
        vector.add("test1");
        vector.add("test2");
        assertEquals(3, vector.getHighestRawFrequency());
    }

    @Test
    public void getMostFrequentWord() throws Exception {
        vector.add("test1");
        vector.add("test1");
        vector.add("test1");
        vector.add("test2");

        assertEquals("test1", vector.getMostFrequentWord());

    }

}
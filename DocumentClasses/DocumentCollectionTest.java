package DocumentClasses;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by cgels on 9/14/17.
 */
public class DocumentCollectionTest {
    private DocumentCollection docs;
    @Before
    public void setup() {
        docs = new DocumentCollection();
        docs.makeTextVector(new String[]{"test1", "test1", "test2", "test1"});
        docs.makeTextVector(new String[]{"test1", "test3", "test2", "test3"});
        docs.makeTextVector(new String[]{"test1", "test4", "test1", "test2"});
        docs.makeTextVector(new String[]{"test7", "test8"});

    }

    @Test
    public void getDocumentById() throws Exception {
        String[] testVec = new String[]{"test1", "test4", "test1", "test2"};
        TextVector actual = new TextVector();
        for (String ele : testVec) {
            actual.add(ele);
        }

        TextVector result = docs.getDocumentById(3);
        int matchCount = result.getRawVectorEntrySet().stream().mapToInt(ele -> actual.contains(ele.getKey()) ? 1: 0).sum();
        assertEquals(3, matchCount);
    }

    @Test
    public void getAverageDocumentLength() throws Exception {
        assertEquals(3.5, docs.getAverageDocumentLength(), .1);
    }

    @Test
    public void getSize() throws Exception {
        assertEquals(4, docs.getSize());
    }

    @Test
    public void getDocumentFrequency() throws Exception {
        assertEquals(3, docs.getDocumentFrequency("test1"));
        assertEquals(1, docs.getDocumentFrequency("test8"));
    }

}
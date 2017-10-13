package tests;

import DocumentClasses.DocumentCollection;
import DocumentClasses.DocumentVector;
import DocumentClasses.TextVector;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by cgels on 9/14/17.
 */
public class DocumentCollectionTest {
    private DocumentCollection docs;

    @Before
    public void setup() {
        docs = new DocumentCollection(null,"document");
        docs.makeTextVector(new String[]{"test", "test", "rest", "test"});
        docs.makeTextVector(new String[]{"test", "zest", "quest", "zest"});
        docs.makeTextVector(new String[]{"test", "quest", "test", "rest"});
        docs.makeTextVector(new String[]{"vest", "nest"});


    }

    @Test
    public void getDocumentById() throws Exception {
        String[] testVec = new String[]{"test", "quest", "test", "rest"};
        TextVector actual = new DocumentVector();
        for (String ele : testVec) {
            actual.add(ele);
        }

        TextVector result = docs.getDocumentById(3);
        int matchCount = result.getRawVectorEntrySet().stream().mapToInt(ele -> actual.contains(ele.getKey()) ? 1 : 0).sum();
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
        assertEquals(3, docs.getDocumentFrequency("test"));
        assertEquals(1, docs.getDocumentFrequency("nest"));
    }

    @Test
    public void getMaxDocFrequency() throws Exception {
        assertEquals("test", docs.getHighestDocumentFrequencyTerm().getKey());
    }

}
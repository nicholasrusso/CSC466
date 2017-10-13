package DocumentClasses;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by cgels on 9/14/17.
 */
public class DocumentVectorTest {
    public DocumentClasses.TextVector vector;
    public DocumentClasses.TextVector vector2;
    public DocumentClasses.TextVector vector3;
    public DocumentClasses.DocumentCollection documents;

    @Before
    public void setup() {
        vector = new DocumentVector();
        vector2 = new DocumentVector();
        vector3 = new DocumentVector();

        // <"test", "vest", "best"> --> < 3 , 1, 1 >
        vector.add("test");
        vector.add("test");
        vector.add("vest");
        vector.add("best");
        vector.add("test");

        // <"best", "zest", "pest"> --> < 1 , 1, 1 >=
        vector2.add("best");
        vector2.add("zest");
        vector2.add("pest");

        vector3.add("test");
        vector3.add("best");
        vector3.add("pest");


        documents = new DocumentCollection("document");
        documents.addTextVector(vector);
        documents.addTextVector(vector2);
        documents.addTextVector(vector3);
    }

    @Test
    public void add() throws Exception {

        assertEquals(5, vector.getTotalWordCount());
        assertEquals(3, vector.getDistinctWordCount());
    }

    @Test
    public void getRawRequency() throws Exception {
        assertEquals(3, vector.getRawFrequency("test"));
    }

    @Test
    public void getL2Norm() {
        documents.normalize(documents);

        double l2Norm1 = 0.73;
        double l2Norm2 = 0.98;
        double actual1 = vector.getL2Norm();
        double actual2 = vector2.getL2Norm();


        assertEquals(l2Norm1, actual1, .1);
        assertEquals(l2Norm2, actual2, .1);
    }

    @Test
    public void contains() throws Exception {
        assertTrue(vector.contains("test"));
        assertFalse(vector.contains("zest"));
    }

    @Test
    public void getTotalWordCount() throws Exception {
        assertEquals(5, vector.getTotalWordCount());
    }

    @Test
    public void getDistinctWordCount() throws Exception {
        assertEquals(3, vector.getDistinctWordCount());
    }

    @Test
    public void getHighestRawFrequency() throws Exception {
        assertEquals(3, vector.getHighestRawFrequency());
    }

    @Test
    public void getMostFrequentWord() throws Exception {
        assertEquals("test", vector.getMostFrequentWord());
    }


    @Test
    public void testDistance() throws Exception {
        documents.normalize(documents);
        DocumentDistance func = new CosineDistance();
        assertEquals(0.5248, func.findDistance(vector, vector3, documents), .1);
    }

    @Test
    public void testRank() throws Exception {
        documents.normalize(documents);

        TextVector query = documents.getDocumentById(1);

        ArrayList<Integer> result = query.findClosestDocuments(documents, new CosineDistance());

        System.out.print(result);
    }

}
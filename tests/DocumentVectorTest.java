package tests;

import DocumentClasses.*;
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
    double epsilon = .05;

    @Before
    public void setup() {
        vector = new DocumentVector();
        vector2 = new DocumentVector();
        vector3 = new DocumentVector();

        // <"test", "vest", "best"> --> < 3 , 1, 1 > && normalizedVector = <.585, .527, 0>
        vector.add("test");
        vector.add("test");
        vector.add("vest");
        vector.add("best");
        vector.add("test");

        // <"best", "zest", "pest"> --> < 1 , 1, 1 > && normalizedVector = <0, 1.585, .585>
        vector2.add("best");
        vector2.add("zest");
        vector2.add("pest");

        // <"test", "best", "pest"> --> < 1 , 1, 1 > && normalizedVector = <.585, 0, .585>
        vector3.add("test");
        vector3.add("best");
        vector3.add("pest");


        documents = new DocumentCollection(null, "document");
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

        double l2Norm1 = 0.79;
        double l2Norm2 = 1.69;
        double l2Norm3 = 0.83;

        assertEquals(l2Norm1, vector.getL2Norm(), epsilon);
        assertEquals(l2Norm2, vector2.getL2Norm(), epsilon);
        assertEquals(l2Norm3, vector3.getL2Norm(), epsilon);
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
        assertEquals(0.5248, func.findDistance(vector, vector3, documents), epsilon);
    }

    @Test
    public void testRank() throws Exception {
        documents.normalize(documents);

        TextVector query = documents.getDocumentById(1);

        ArrayList<Integer> result = query.findClosestDocuments(documents, new CosineDistance());

        System.out.print(result);
    }

}
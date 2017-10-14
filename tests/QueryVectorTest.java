package tests;

import DocumentClasses.DocumentCollection;
import DocumentClasses.QueryVector;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by cgels on 9/28/17.
 */
public class QueryVectorTest {
    public DocumentClasses.TextVector vector;
    public DocumentClasses.TextVector vector2;
    public DocumentClasses.TextVector vector3;
    public DocumentClasses.DocumentCollection documents;

    double epsilon = .05;

    @Before
    public void setup() {
        vector = new QueryVector();
        vector2 = new QueryVector();
        vector3 = new QueryVector();

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

        documents = new DocumentCollection("", "query");
        documents.addTextVector(vector);
        documents.addTextVector(vector2);
        documents.addTextVector(vector3);
    }

    @Test
    public void getL2Norm() {
        documents.normalize(documents);

        double l2Norm1 = 1.21;
        double l2Norm2 = 1.69;
        double l2Norm3 = .82;

        assertEquals(l2Norm1, vector.getL2Norm(), epsilon);
        assertEquals(l2Norm2, vector2.getL2Norm(), epsilon);
        assertEquals(l2Norm3, vector3.getL2Norm(), epsilon);
    }

}
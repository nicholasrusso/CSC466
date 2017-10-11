package DocumentClasses;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by cgels on 9/28/17.
 */
public class QueryVectorTest {
    public DocumentClasses.TextVector vector;
    public DocumentClasses.TextVector vector2;
    public DocumentClasses.DocumentCollection documents;

    @Before
    public void setup() {
        vector = new QueryVector();
        vector2 = new QueryVector();

        // <"test1", "test2", "test3"> --> < 3 , 1, 1 >
        vector.add("test1");
        vector.add("test1");
        vector.add("test2");
        vector.add("test3");
        vector.add("test1");

        // <"test3", "test4", "test5"> --> < 1 , 1, 1 >=
        vector2.add("test3");
        vector2.add("test4");
        vector2.add("test5");


        documents = new DocumentCollection("query");
        documents.addTextVector(vector);
        documents.addTextVector(vector2);
    }

    @Test
    public void getL2Norm() {
        documents.normalize(documents);

        double l2Norm1 = 0.866;
        double l2Norm2 = 0.96;
        double actual1 = vector.getL2Norm();
        double actual2 = vector2.getL2Norm();


        assertEquals(l2Norm1, actual1, .1);
        assertEquals(l2Norm2, actual2, .1);
    }

}
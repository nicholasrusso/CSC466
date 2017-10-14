package tests;

import labs.Lab3;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * Created by cgels on 10/14/17.
 */
public class Lab3MetricsTest {
    public double epsilon = .01;
    ArrayList<Integer> returned;
    ArrayList<Integer> returned2;
    ArrayList<Integer> returned3;
    ArrayList<Integer> relevant;
    ArrayList<Integer> relevant2;
    ArrayList<Integer> relevant3;

    HashMap<Integer, ArrayList<Integer>> queryResults;
    HashMap<Integer, ArrayList<Integer>> expectedResults;

    @Before
    public void setup() {
        returned = new ArrayList<>();
        returned.add(1);
        returned.add(3);
        returned.add(5);
        returned.add(7);
        returned.add(4);
        returned.add(2);

        relevant = new ArrayList<>();
        relevant.add(1);
        relevant.add(2);
        relevant.add(4);
        relevant.add(5);

        returned2 = new ArrayList<>();
        returned2.add(1);
        returned2.add(2);
        returned2.add(5);

        relevant2 = new ArrayList<>();
        relevant2.add(1);
        relevant2.add(2);
        relevant2.add(4);
        relevant2.add(6);

        returned3 = new ArrayList<>();
        returned3.add(1);
        returned3.add(2);
        returned3.add(3);

        relevant3 = new ArrayList<>();
        relevant3.add(4);
        relevant3.add(5);
        relevant3.add(7);
        relevant3.add(6);

        queryResults = new HashMap<>();
        expectedResults = new HashMap<>();
        queryResults.put(1, returned);
        queryResults.put(2, returned2);
        queryResults.put(3, returned3);

        expectedResults.put(1, relevant);
        expectedResults.put(2, relevant2);
        expectedResults.put(3, relevant3);
    }

    @Test
    public void computeMAP() throws Exception {

        double result = Lab3.computeMAP(expectedResults, queryResults);
        assertEquals(.411, result, epsilon);
    }

    @Test
    public void computeAveragePrecision() throws Exception {

        double result = Lab3.computeAveragePrecision(relevant, returned);
        assertEquals(0.733, result, epsilon);

        double result2 = Lab3.computeAveragePrecision(relevant2, returned2);
        assertEquals(0.5, result2, epsilon);

        double result3 = Lab3.computeAveragePrecision(relevant3, returned3);
        assertEquals(0.0, result3, epsilon);
    }

}
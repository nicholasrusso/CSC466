package tests;

import DecisionTree.Matrix;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by cgels on 11/28/17.
 */
public class MatrixTest {
    Matrix m;

    @Before
    public void setUp() throws Exception {
        int[][] data = new int[5][5];
        // row 1
        data[0][0] = 1; // age
        data[0][1] = 0; // has_job
        data[0][2] = 0; // owns_hous
        data[0][3] = 1; // credit_rating
        data[0][4] = 2;// fake class label for now
        //row 2
        data[1][0] = 1;
        data[1][1] = 0;
        data[1][2] = 0;
        data[1][3] = 2;
        data[1][4] = 2;// fake class label for now
        //row 3
        data[2][0] = 1;
        data[2][1] = 1;
        data[2][2] = 0;
        data[2][3] = 2;
        data[2][4] = 3;// fake class label for now
        //row 4
        data[3][0] = 1;
        data[3][1] = 1;
        data[3][2] = 1;
        data[3][3] = 1;
        data[3][4] = 3;// fake class label for now
        //row 5 -- tiebreaker
        //row 4
        data[4][0] = 1;
        data[4][1] = 1;
        data[4][2] = 1;
        data[4][3] = 2;
        data[4][4] = 3;// fake class label for now


        m = new Matrix(data);
    }

    @Test
    public void computeIGR() throws Exception {
    }

    @Test
    public void findMostCommonValue() throws Exception {
        ArrayList<Integer> rows = new ArrayList<Integer>();
        rows.add(0);
        rows.add(1);
        rows.add(2);
        rows.add(3);

        assertEquals(3, m.findMostCommonCategory(rows));

    }

    @Test
    public void split() throws Exception {
        int attribute_index = 3; // column index

        ArrayList<Integer> rows = new ArrayList<Integer>();
        rows.add(0);
        rows.add(1);
        rows.add(2);
        rows.add(3);
        rows.add(4);
        HashMap<Integer, ArrayList<Integer>> actual = m.split(attribute_index, rows);

        Set<Integer> expectedKeys = new HashSet<Integer>();
        expectedKeys.add(1);
        expectedKeys.add(2);

        assertEquals(expectedKeys, actual.keySet());

        ArrayList<Integer> expectedRows1 = new ArrayList<>();
        expectedRows1.add(0);
        expectedRows1.add(3);
        ArrayList<Integer> expectedRows2 = new ArrayList<>();
        expectedRows2.add(1);
        expectedRows2.add(2);
        expectedRows2.add(4);

        assertEquals(expectedRows1, actual.get(1));
        assertEquals(expectedRows2, actual.get(2));

    }

}
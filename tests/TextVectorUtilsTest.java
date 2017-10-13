package tests;

import DocumentClasses.TextVectorUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by cgels on 10/11/17.
 */
public class TextVectorUtilsTest {

    @Test
    public void testSortByValueAscending() {
        // src: https://stackoverflow.com/questions/109383/sort-a-mapkey-value-by-values-java

        Random random = new Random(System.currentTimeMillis());
        Map<String, Integer> testMap = new HashMap<>(1000);
        for (int i = 0; i < 1000; ++i) {
            testMap.put("SomeString" + random.nextInt(), random.nextInt());
        }

        testMap = TextVectorUtils.sortByValueAscending(testMap);
        Assert.assertEquals(1000, testMap.size());

        Integer previous = null;
        for (Map.Entry<String, Integer> entry : testMap.entrySet()) {
            Assert.assertNotNull(entry.getValue());
            if (previous != null) {
                Assert.assertTrue(entry.getValue() >= previous);
            }
            previous = entry.getValue();
        }
    }

    @Test
    public void testSortByValueDescending() {
        // src: https://stackoverflow.com/questions/109383/sort-a-mapkey-value-by-values-java

        Random random = new Random(System.currentTimeMillis());
        Map<String, Integer> testMap = new HashMap<>(1000);
        for (int i = 0; i < 1000; ++i) {
            testMap.put("SomeString" + random.nextInt(), random.nextInt());
        }

        testMap = TextVectorUtils.sortByValueDescending(testMap);
        Assert.assertEquals(1000, testMap.size());

        Integer previous = null;
        for (Map.Entry<String, Integer> entry : testMap.entrySet()) {
            Assert.assertNotNull(entry.getValue());
            if (previous != null) {
                Assert.assertTrue(entry.getValue() <= previous);
            }
            previous = entry.getValue();
        }
    }

}
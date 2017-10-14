package tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Run Vector tests pertaining to Docme package.
 * Created by cgels on 10/13/17.
 */
@Suite.SuiteClasses({
        DocumentVectorTest.class,
        QueryVectorTest.class,
        TextVectorUtilsTest.class
})
@RunWith(Suite.class)
public class DocumentClassesTestSuite {
}

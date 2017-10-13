package tests;

import DocumentClasses.DocumentCollection;
import DocumentClasses.DocumentVector;
import org.junit.runners.Suite;
import org.junit.runner.RunWith;

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

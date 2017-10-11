package DocumentClasses;

/**
 * Created by cgels on 9/19/17.
 */
public interface DocumentDistance {

    double findDistance(TextVector query, TextVector document, DocumentCollection documents);
}

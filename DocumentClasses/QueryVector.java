package DocumentClasses;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by cgels on 9/19/17.
 */
public class QueryVector extends TextVector {
    private HashMap<String, Double> normalizedVector = new HashMap<>();

    @Override
    public double getNormalizedFrequency(String word) {
        return .5 +  .5 * rawVector.get(word) / getHighestRawFrequency();
    }

    @Override
    public Set<Map.Entry<String, Double>> getNormalizedVectorEntrySet() {
        return normalizedVector.entrySet();
    }

    @Override
    public void normalize(DocumentCollection dc) {
        getRawVectorEntrySet().stream().forEach(termEntry -> {
            double df = dc.getDocumentFrequency(termEntry.getKey());
            double tfidf = getNormalizedFrequency(termEntry.getKey()) * Math.log(dc.getSize() / df);
            normalizedVector.put(termEntry.getKey(), tfidf);
        });
    }
}

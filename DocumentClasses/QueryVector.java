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
    public Set<Map.Entry<String, Double>> getNormalizedVectorEntrySet() {
        return normalizedVector.entrySet();
    }

    @Override
    public double getNormalizedFrequency(String word) {
        return normalizedVector.getOrDefault(word, 0.0);
    }

    @Override
    public void normalize(DocumentCollection dc) {
        getRawVectorEntrySet().stream()
                .forEach(termEntry -> normalizedVector.put(termEntry.getKey(), TfIdf(termEntry, dc)));
    }

    private double TfIdf(Map.Entry<String, Integer> term, DocumentCollection dc) {
        double df = dc.getDocumentFrequency(term.getKey());
        double x = df > 0 ? (1.0 * dc.getSize()) / df : 0;

        return computeTermFrequency(term.getKey()) * log2(x);
    }

    private double computeTermFrequency(String word) {
        int highestRawFrequency = getHighestRawFrequency();

        if (highestRawFrequency <= 0) {
            return 0.0;
        }

        return 0.5 + 0.5 * rawVector.getOrDefault(word, 0) / highestRawFrequency;

    }

    private double log2(double x) {
        if (x > 0) {
            return Math.log(x) / Math.log(2);
        }

        return 0;
    }
}

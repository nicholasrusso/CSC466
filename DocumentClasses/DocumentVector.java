package DocumentClasses;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by cgels on 9/19/17.
 */
public class DocumentVector extends TextVector {
    private HashMap<String, Double> normalizedVector;

    public DocumentVector() {
        normalizedVector = new HashMap<>();
    }

    @Override
    public double getNormalizedFrequency(String word) {
        return ((double) rawVector.get(word)) / getHighestRawFrequency();
    }

    @Override
    public Set<Map.Entry<String, Double>> getNormalizedVectorEntrySet() {
        return normalizedVector.entrySet();
    }

    @Override
    public void normalize(DocumentCollection dc) {
        getRawVectorEntrySet().stream().forEach(termEntry -> {
            normalizedVector.put(termEntry.getKey(), TfIdf(termEntry, dc));
        });
    }


    private double TfIdf(Map.Entry<String, Integer> term, DocumentCollection dc) {
        double df = dc.getDocumentFrequency(term.getKey());
        double x = df > 0 ? dc.getSize() / df : 0;
        return getNormalizedFrequency(term.getKey()) * log2(x);
    }


    private double log2(double x) {
        if (x > 0) {return Math.log(x) / Math.log(2);}

        return 0;
    }
}

package DocumentClasses;

import java.util.Map;

/**
 * Created by cgels on 9/19/17.
 */
public class CosineDistance implements DocumentDistance {

    @Override
    public double findDistance(TextVector query, TextVector document, DocumentCollection documents) {
        double qDotD = 0.0;

        for (Map.Entry<String, Double> qEntry : query.getNormalizedVectorEntrySet()) {
            if (document.contains(qEntry.getKey())) {
                qDotD += qEntry.getValue() * document.getNormalizedFrequency(qEntry.getKey());
            }
        }

        return qDotD / (query.getL2Norm() * document.getL2Norm());
    }
}

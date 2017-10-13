package DocumentClasses;

import java.util.Map;

/**
 * Created by cgels on 9/19/17.
 */
public class CosineDistance implements DocumentDistance {

    @Override
    public double findDistance(TextVector query, TextVector document, DocumentCollection documents) {
        double QdotD = 0.0;

        for (Map.Entry<String, Double> qEntry : query.getNormalizedVectorEntrySet()) {

            for (Map.Entry<String, Double> dEntry : document.getNormalizedVectorEntrySet()) {

                if (dEntry.getKey().equals(qEntry.getKey())) {
                    QdotD += dEntry.getValue() * qEntry.getValue();
                }
            }

        }

        return QdotD / (query.getL2Norm() * document.getL2Norm());
    }
}

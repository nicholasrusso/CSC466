package DocumentClasses;

import java.util.Map;

/**
 * Created by cgels on 10/13/17.
 */
public class OkapiDistance implements DocumentDistance {
    // parameters given by lab specification
    private double k1 = 1.2;
    private double b = 0.75;
    private double k2 = 100;

    @Override
    public double findDistance(TextVector query, TextVector document, DocumentCollection documents) {
        double dist = 0.0;
        int N = documents.getSize();
        double avgDocLength = documents.getAverageDocumentLength();

        for (Map.Entry<String, Integer> qEntry : query.getRawVectorEntrySet()) {

            if (document.contains(qEntry.getKey())) {
                int df_i = documents.getDocumentFrequency(qEntry.getKey());
                int f_ij = document.getRawFrequency(qEntry.getKey());
                int f_iq = qEntry.getValue();
                int docLen_j = document.getTotalWordCount();

                double x1 = Math.log((N - df_i + 0.5) / (df_i + .5));
                double x2 = ((k1 + 1.0) * f_ij) / (k1 * (1.0 - b + b * (docLen_j / avgDocLength)) + f_ij);
                double x3 = ((k2 + 1.0) * f_iq) / (k2 + f_iq);

                dist += x1 * x2 * x3;
            }
        }
        return dist;
    }
}

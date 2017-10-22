package DocumentClasses;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by cgels on 9/14/17.
 */
public abstract class TextVector implements Serializable {
    HashMap<String, Integer> rawVector;
    int distinctCount = 0;
    int totalCount = 0;

    public TextVector() {
        rawVector = new HashMap<>();
    }

    public Set<Map.Entry<String, Integer>> getRawVectorEntrySet() {
        return rawVector.entrySet();
    }

    public void add(String term) {
        if (!term.matches(".*\\d+.*")) {
            int val = rawVector.getOrDefault(term, 0);
            if (val == 0) {
                distinctCount++;
            }
            totalCount++;

            rawVector.put(term, val + 1);
        }
    }


    public int getRawFrequency(String term) {
        return rawVector.getOrDefault(term, 0);
    }

    public boolean contains(String term) {
        return rawVector.containsKey(term);
    }

    public int getTotalWordCount() {
        return totalCount;
    }

    public int getDistinctWordCount() {
        return distinctCount;
    }

    public int getHighestRawFrequency() {
        if (rawVector.isEmpty()) {
            return 0;
        }

        return rawVector.values()
                        .stream()
                        .max((a, b) -> a.longValue() > b.longValue() ? 1 : -1)
                        .get();
    }

    public String getMostFrequentWord() {
        return rawVector.entrySet()
                        .stream()
                        .max((a, b) -> a.getValue() > b.getValue() ? 1 : -1)
                        .get()
                        .getKey();
    }


    public double getL2Norm() {
        double ssDist = getNormalizedVectorEntrySet().stream()
                                                     .mapToDouble(term -> Math.pow(term.getValue(), 2))
                                                     .sum();
        return Math.sqrt(ssDist);
    }

    public abstract Set<Map.Entry<String, Double>> getNormalizedVectorEntrySet();

    public abstract double getNormalizedFrequency(String word);

    public abstract void normalize(DocumentCollection dc);

    public ArrayList<Integer> findClosestDocuments(DocumentCollection documents, DocumentDistance distanceAlg) {
        Map<Integer, Double> docDistances = new HashMap<>(documents.getSize());
        ArrayList<Integer> top20 = new ArrayList<>(20);

        for (Map.Entry<Integer, TextVector> doc : documents.getEntrySet()) {
            double dist = 0.0;

            if (!doc.getValue()
                    .getRawVectorEntrySet()
                    .isEmpty()) {
                dist = distanceAlg.findDistance(this, doc.getValue(), documents);
            }

            docDistances.put(doc.getKey(), dist);
        }

        Map<Integer, Double> ranked = MapUtilities.sortByValueDescending(docDistances);

        ranked.entrySet()
              .stream()
              .forEach(entry -> {
                  if (top20.size() < 20) {
                      top20.add(entry.getKey());
                  }
              });

        return top20;
    }


    @Override
    public int hashCode() {
        int result = rawVector != null ? rawVector.hashCode() : 0;
        result = 31 * result + distinctCount;
        result = 31 * result + totalCount;
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TextVector vector = (TextVector) o;

        if (distinctCount != vector.distinctCount) return false;
        if (totalCount != vector.totalCount) return false;
        return rawVector != null ? rawVector.equals(vector.rawVector) : vector.rawVector == null;
    }
}

package DocumentClasses;

import java.io.Serializable;
import java.util.*;

/**
 * Created by cgels on 9/14/17.
 */
public abstract class TextVector implements Serializable {
    HashMap<String, Integer> rawVector;
    private int distinctCount = 0;
    private int totalCount = 0;

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

        return rawVector.values().stream().max((a, b) -> a.longValue() > b.longValue() ? 1 : -1).get();
    }

    public String getMostFrequentWord() {
        return rawVector.entrySet().stream()
                .max((a, b) -> a.getValue() > b.getValue() ? 1 : -1).get().getKey();
    }

    public double getL2Norm() {
        double ssDist = getNormalizedVectorEntrySet().stream()
                .mapToDouble(term -> Math.pow(term.getValue(), 2)).sum();
        return Math.sqrt(ssDist);
    }

    public abstract Set<Map.Entry<String, Double>> getNormalizedVectorEntrySet();

    public ArrayList<Integer> findClosestDocuments(DocumentCollection documents, DocumentDistance distanceAlg) {
        PriorityQueue<Map.Entry<Integer, Double>> top20 = new PriorityQueue<>(20, (o2, o1) -> o2.getValue() >
                o1.getValue() ? 1 : -1);

        documents.getEntrySet().stream().forEach(term -> {
            double dist = distanceAlg.findDistance(this, term.getValue(), documents);

            if (top20.size() >= 20) {
                top20.poll();
            }
            top20.offer(new AbstractMap.SimpleEntry<Integer, Double>(term.getKey(), dist));
        });

        ArrayList<Integer> topIDs = new ArrayList<>();

        while (top20.iterator().hasNext()) {
            Map.Entry<Integer, Double> v = top20.poll();
            topIDs.add(v.getKey());
            System.out.println(v.getValue());
        }

        topIDs.sort(Collections.reverseOrder());
        return topIDs;
    }

    public abstract double getNormalizedFrequency(String word);

    public abstract void normalize(DocumentCollection dc);

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

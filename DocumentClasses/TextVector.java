package DocumentClasses;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by cgels on 9/14/17.
 */
public class TextVector implements Serializable{
    private HashMap<String, Integer> rawVector;
    private String mostFrequent = null;
    private int maxRawFrequency = 0;
    private int distinctCount = 0;
    private int totalCount = 0;

    public TextVector() {
        rawVector = new HashMap<>();
    }

    public Set<Map.Entry<String, Integer>> getRawVectorEntrySet() {
        return rawVector.entrySet();
    }

    public void add(String term) {
        int val = rawVector.getOrDefault(term, 0);
        totalCount++;
        if (val == 0) {
            distinctCount++;
        }
        rawVector.put(term, val + 1);
    }


    public int getRawRequency(String term) {
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
        return rawVector.values().stream().max((a,b) -> a.longValue() > b.longValue() ? 1 : -1).get();
    }

    public String getMostFrequentWord() {
        return rawVector.entrySet().stream().max((a, b) -> a.getValue() > b.getValue() ? 1 : -1).get().getKey();
    }

}

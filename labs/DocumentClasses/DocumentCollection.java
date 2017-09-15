package DocumentClasses;

import javax.print.Doc;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by cgels on 9/14/17.
 */
public class DocumentCollection implements Serializable {
    private HashMap<Integer, TextVector> documents;
    private int currentIndex = 1;

    public static  String noiseWordArray[] = {"a", "about", "above", "all", "along",
            "also", "although", "am", "an", "and", "any", "are", "aren't", "as", "at",
            "be", "because", "been", "but", "by", "can", "cannot", "could", "couldn't",
            "did", "didn't", "do", "does", "doesn't", "e.g.", "either", "etc", "etc.",
            "even", "ever", "enough", "for", "from", "further", "get", "gets", "got", "had", "have",
            "hardly", "has", "hasn't", "having", "he", "hence", "her", "here",
            "hereby", "herein", "hereof", "hereon", "hereto", "herewith", "him",
            "his", "how", "however", "i", "i.e.", "if", "in", "into", "it", "it's", "its",
            "me", "more", "most", "mr", "my", "near", "nor", "now", "no", "not", "or", "on", "of", "onto",
            "other", "our", "out", "over", "really", "said", "same", "she",
            "should", "shouldn't", "since", "so", "some", "such",
            "than", "that", "the", "their", "them", "then", "there", "thereby",
            "therefore", "therefrom", "therein", "thereof", "thereon", "thereto",
            "therewith", "these", "they", "this", "those", "through", "thus", "to",
            "too", "under", "until", "unto", "upon", "us", "very", "was", "wasn't",
            "we", "were", "what", "when", "where", "whereby", "wherein", "whether",
            "which", "while", "who", "whom", "whose", "why", "with", "without",
            "would", "you", "your", "yours", "yes"};


    public DocumentCollection() {
        documents = new HashMap<>();


    }

    public void makeTextVector(String[] elements) {
        TextVector vector = new TextVector();
        for (String term : elements) {
            if (!isNoiseWord(term)) {
                vector.add(term);
            }
        }
        documents.put(currentIndex++, vector);
    }

    private boolean isNoiseWord(String word) {
        return Arrays.stream(noiseWordArray).anyMatch(noise -> noise.equals(word));
    }

    public TextVector getDocumentById(int id) {
        return documents.getOrDefault(id, new TextVector());

    }

    public double getAverageDocumentLength() {
        return documents.values().stream().mapToInt(vec -> vec.getTotalWordCount()).average().getAsDouble();
    }

    public int getSize() {
        return documents.size();
    }

    public Collection<TextVector> getDocuments() {
        return documents.values();
    }

    public Set<Map.Entry<Integer, TextVector>> getEntrySet() {
        return documents.entrySet();
    }

    public int getDocumentFrequency(String term) {
        return documents.values().stream().mapToInt(textVector -> textVector.contains(term) ? 1 : 0).sum();
    }

}


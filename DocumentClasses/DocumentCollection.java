package DocumentClasses;

import javax.print.Doc;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by cgels on 9/14/17.
 */
public class DocumentCollection implements Serializable {
    private HashMap<Integer, TextVector> documents;
    private String dataPath;
    private int currentIndex = 1;
    private int maxDocFreq = 0;
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

    public DocumentCollection(String dataFilePath) {
        documents = new HashMap<>();
        digestFile(dataFilePath);
    }

    public DocumentCollection() {
        documents = new HashMap<>();
    }

    private void digestFile(String dataFilePath) {
        if (!dataFilePath.equals(dataPath)) {
            dataPath = dataFilePath;
            processFile();
        }
    }

    private void processFile() {
        try {
            int index = 0;
            boolean body = false;

            Iterator<String> tokenLists = Files.lines(Paths.get(dataPath)).collect(Collectors.toList()).iterator();


            while (tokenLists.hasNext()) {
                String[] curTokens = tokenLists.next().split(" ");
                if (".I".equals(curTokens[0])) {
                    index = Integer.parseInt(curTokens[1]);
                }

                if (".W".equals(curTokens[0])) {
                    body = true;
                }

                while (body && tokenLists.hasNext()) {
                    String line = tokenLists.next();
                    String[] flagTokens = line.split(" ");
                    String[] bodyTokens = line.split("[^a-zA-Z]+");
                    if (".I".equals(flagTokens[0])) {
                        body = false;
                        index = Integer.parseInt(flagTokens[1]);
                    } else {
                        TextVector newVector = new TextVector();
                        Arrays.stream(bodyTokens).map(term -> term.toLowerCase()).filter(term -> !isNoiseWord(term)).forEach(term -> newVector.add(term));
                        documents.put(index, newVector);
                    }
                }
            }
        }
        catch (IOException ex) {
            ex.printStackTrace(System.err);
        }

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

    private void getMaxDocumentFrequency() {
        maxDocFreq = getDocuments().stream().flatMap(textVector -> textVector.getRawVectorEntrySet().stream()).map(entry -> entry.getKey()).mapToInt(term -> getDocumentFrequency(term)).max().getAsInt();
    }

    public String getMostFrequentTerm() {
        getMaxDocumentFrequency();
        String mostFrequent = getDocuments().stream().flatMap(textVector -> textVector.getRawVectorEntrySet().stream()).map(entry -> entry.getKey()).filter(key -> getDocumentFrequency(key) == maxDocFreq).findFirst().get();
        return mostFrequent;
    }

    public int getTotalDistinctWordCount() {
        return getDocuments().stream().mapToInt(textVector -> textVector.getDistinctWordCount()).sum();
    }

    public int getTotalWordCount() {
        return getDocuments().stream().mapToInt(textVector -> textVector.getTotalWordCount()).sum();
    }
}


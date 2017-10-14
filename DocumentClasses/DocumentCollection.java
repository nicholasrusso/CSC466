package DocumentClasses;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by cgels on 9/14/17.
 */
public class DocumentCollection implements Serializable {
    private HashMap<Integer, TextVector> documents;
    private HashMap<String, Integer> documentFrequencies;
    private String dataPath;
    private boolean useDocVec = true;
    int currentIndex;

    public static final String standardNoiseWordArray[] = {"", "a", "about", "above", "all", "along",
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


    public DocumentCollection(String dataFilePath, String docType) {
        this.useDocVec = docType.equals("document");
        this.currentIndex = 1;

        this.documents = new HashMap<>();
        this.documentFrequencies = new HashMap<>();

        if (dataFilePath != null && !dataFilePath.equals("")) {
            digestFile(dataFilePath);
        }

    }

    private void digestFile(String dataFilePath) {
        if (!dataFilePath.equals(dataPath)) {
            dataPath = dataFilePath;
            processFile();
        }
    }

    private void processFile() {
        // do not want to restructure tokenizer logic... set index to 0 instead of 1 at beginning :|
        currentIndex = 0;
        try {
            boolean parsingTextBody = false;

            Iterator<String> tokenLists = Files.lines(Paths.get(dataPath))
                                               .collect(Collectors.toList())
                                               .iterator();

            while (tokenLists.hasNext()) {
                String[] curTokens = tokenLists.next().split(" ");

                if (".I".equals(curTokens[0])) currentIndex++;
                if (".W".equals(curTokens[0])) parsingTextBody = true;

                TextVector newVector = useDocVec ? new DocumentVector() : new QueryVector();

                while (parsingTextBody && tokenLists.hasNext()) {
                    String line = tokenLists.next();
                    String[] flagTokens = line.split(" ");
                    String[] bodyTokens = line.split("[^a-zA-Z]+");

                    if (".I".equals(flagTokens[0])) {
                        parsingTextBody = false;
                        currentIndex++;
                    }
                    else {
                        Arrays.stream(bodyTokens)
                              .map(term -> term.toLowerCase().trim())
                              .filter(term -> !isNoiseWord(term) && term.length() > 1)
                              .forEach(term -> newVector.add(term));
                    }
                    documents.put(currentIndex, newVector);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }
    }

    private boolean isNoiseWord(String word) {
        return Arrays.stream(standardNoiseWordArray)
                     .anyMatch(noise -> noise.equals(word));
    }

    public int getDocumentFrequency(String term) {
        return getEntrySet().stream()
                            .mapToInt(vec -> vec.getValue()
                                                .contains(term) ? 1 : 0)
                            .sum();
    }

    public Set<Map.Entry<Integer, TextVector>> getEntrySet() {
        return documents.entrySet();
    }

    /**
     * Injection point for testing which relies on conditional logic within DocumentCollection.
     * */
    public void makeTextVector(String[] elements) {
        TextVector vector = useDocVec ? new DocumentVector() : new QueryVector();
        for (String term : elements) {
            if (!isNoiseWord(term)) {
                vector.add(term);
            }
        }
        documents.put(currentIndex++, vector);

    }


    public void addTextVector(TextVector vec) {
        documents.put(currentIndex++, vec);
    }

    public TextVector getDocumentById(int id) {
        return documents.getOrDefault(id, useDocVec ? new DocumentVector() : new QueryVector());
    }

    public double getAverageDocumentLength() {
        return getDocuments().stream()
                             .mapToInt(vec -> vec.getTotalWordCount())
                             .average()
                             .getAsDouble();
    }

    public Collection<TextVector> getDocuments() {
        return documents.values();
    }

    public int getSize() {
        return documents.size();
    }

    public Map.Entry<String, Integer> getHighestDocumentFrequencyTerm() {
        if (documentFrequencies.isEmpty()) {
            for (TextVector doc : getDocuments()) {
                for (Map.Entry<String, Integer> term : doc.getRawVectorEntrySet()) {
                    int val = documentFrequencies.getOrDefault(term.getKey(), 0);
                    documentFrequencies.put(term.getKey(), val + 1);
                }
            }
        }

        System.out.println(Collections.max(documentFrequencies.entrySet(), Comparator.comparingInt(Map.Entry::getValue))
                                      .toString());
        return Collections.max(documentFrequencies.entrySet(), Comparator.comparingInt(Map.Entry::getValue));
    }

    public AbstractMap.SimpleEntry<String, Integer> getSingleHighestDocumentFrequency() {
        AbstractMap.SimpleEntry<String, Integer> highest = new AbstractMap.SimpleEntry<>(null, -1);

        for (TextVector vec : getDocuments()) {
            if (vec.getHighestRawFrequency() > highest.getValue()) {
                highest = new AbstractMap.SimpleEntry<>(
                        vec.getMostFrequentWord(), vec.getHighestRawFrequency());
            }
        }

        return highest;
    }


    public int getTotalDistinctWordCount() {
        return getEntrySet().stream()
                            .mapToInt(vec -> vec.getValue()
                                                .getDistinctWordCount())
                            .sum();
    }

    public int getTotalWordCount() {
        return getEntrySet().stream()
                            .mapToInt(vec -> vec.getValue()
                                                .getTotalWordCount())
                            .sum();
    }

    public void normalize(DocumentCollection dc) {
        getEntrySet().stream()
                     .forEach(vec -> vec.getValue()
                                        .normalize(dc));
    }
}


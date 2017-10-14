package labs;

import DocumentClasses.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Comparator.reverseOrder;

/**
 * CSC466 Lab3 Specification : https://docs.google.com/document/d/10rHQDLd2MM8aOgp7zSiMbOn0YFHtosB8tHEgVxX0t9I/edit?usp=sharing
 *
 * Created by cgels on 10/13/17.
 */
public class Lab3 {
    static DocumentCollection documents;
    static DocumentCollection queries;
    static Map<Integer, ArrayList<Integer>> humanRanking;
    static Map<Integer, ArrayList<Integer>> cosineResults;
    static Map<Integer, ArrayList<Integer>> okapiResults;

    public static void main(String args[]) {
        documents = deserializeDocumentCollection("./files/docvector");
        queries = new DocumentCollection("labs/queries.txt", "queries");
        humanRanking = loadHumanRankings();

        try {
            cosineResults = deserializePrecomputedQueryResults("./files/queryResultsCosineSimilarity");
        } catch (Exception e) {
            cosineResults = computeSimilarities(new CosineDistance());
        }

        if (okapiResults == null) okapiResults = computeSimilarities(new OkapiDistance());

//        System.out.println("Okapi Distances for Query 1");
//        getDistancesForQueryID(1, new OkapiDistance()).stream().forEach(dist -> System.out.println(dist));

        System.out.println(String.format("Cosine MAP = %f", computeMAP(humanRanking, cosineResults)));
        System.out.println(String.format("Okapi MAP = %f", computeMAP(humanRanking, okapiResults)));
    }

    /**
     * Measures the mean average precision by comparing the system rankings with human graded relevant results.
     * !! ONLY USES FIRST 20 QUERIES !!
     */
    public static double computeMAP(Map<Integer, ArrayList<Integer>> groundTruth, Map<Integer, ArrayList<Integer>> predictions) {
        int numQueries = 20;
        int index = 0;
        // store each queries average precision
        ArrayList<Double> averagePrecisions = new ArrayList<>(numQueries);
        Set<Map.Entry<Integer, ArrayList<Integer>>> first20Queries = new TreeMap<>(predictions).headMap(numQueries, true)
                                                                                               .entrySet();

        for (Map.Entry<Integer, ArrayList<Integer>> searchResult : first20Queries) {
            double P = computeAveragePrecision(groundTruth.get(searchResult.getKey()),
                                               predictions.get(searchResult.getKey()));
            averagePrecisions.add(index++, P);
        }

        double sumAvgPrecision = averagePrecisions.stream()
                                     .mapToDouble(p -> p)
                                     .sum();

        return sumAvgPrecision / numQueries;
    }

    /**
     * Compute the average precision for a single query result.
     * @param relevant - list human judged relevant document IDs for single query.
     * @param returned - top 20 most relevant document IDs as computed by this system for single query.
     * */
    public static double computeAveragePrecision(ArrayList<Integer> relevant, ArrayList<Integer> returned) {
        int numCorrect = 0;

        ArrayList<Double> precisions = new ArrayList<>(returned.size());
        for (int doc_index = 0; doc_index < returned.size(); doc_index++) {
            double p = 0.0;
            int current_doc = returned.get(doc_index);

            // is the returned document considered relevant by human grader?
            if (relevant.contains(current_doc)) {
                numCorrect++;
                int rank = doc_index + 1;
                p = (1.0 * numCorrect) / rank;
            }
            // add precision up to current document
            precisions.add(doc_index, p);
        }

        double sumPrecision = precisions.stream()
                                        .mapToDouble(p -> p)
                                        .sum();

        return sumPrecision / relevant.size();
    }

    // HELPER METHODS BELOW ...

    /**
     * Compute specified distances for some Query ID using all Document Vectors.
     * */
    public static ArrayList<Double> getDistancesForQueryID(int queryID, DocumentDistance distance) {
        final DocumentDistance docDist = distance;
        final QueryVector queryVector = (QueryVector) queries.getDocumentById(queryID);
        //ensure normalized vectors if computing cosine dist
        if (docDist instanceof CosineDistance) documents.normalize(documents);

        return (ArrayList<Double>) documents.getEntrySet().stream()
                                      .map(entry -> docDist.findDistance(queryVector, entry.getValue(), documents))
                                      .sorted(reverseOrder()).collect(Collectors.toList());
    }

    /**
     * Try to load existing computations for similarity scores from a file path.
     * */
    public static Map<Integer, ArrayList<Integer>> deserializePrecomputedQueryResults(String filePath) throws Exception {
        try (ObjectInputStream is = new ObjectInputStream(new FileInputStream(new File(filePath)))) {
            return (HashMap<Integer, ArrayList<Integer>>) is.readObject();
        }
        catch (IOException | ClassNotFoundException e) {
            throw e;
        }
    }

    /**
     * Load DocumentCollection from a filePath.
     * */
    public static DocumentCollection deserializeDocumentCollection(String filePath) {
        try (ObjectInputStream is = new ObjectInputStream(new FileInputStream(new File(filePath)))) {
            return (DocumentCollection) is.readObject();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    /**
     * Get 20 most relevant documents per query vector using the given distance function.
     */
    public static Map<Integer, ArrayList<Integer>> computeSimilarities(DocumentDistance distanceFunction) {
        HashMap<Integer, ArrayList<Integer>> queryResults = new HashMap<>();

        // make sure documents normalized before trying to compute cosine distances.
        if (distanceFunction instanceof CosineDistance) {
            documents.normalize(documents);
            queries.normalize(documents);
        }
        queries.getEntrySet()
               .stream()
               .forEach(query -> queryResults.put(query.getKey(), query.getValue()
                                                                      .findClosestDocuments(documents, distanceFunction)));

        return queryResults;
    }

    /**
     * Reads human relevance ranking for each query into HashMap.
     */
    public static HashMap<Integer, ArrayList<Integer>> loadHumanRankings() {
        HashMap<Integer, ArrayList<Integer>> human = new HashMap<>();

        try {
            Iterator<String> judgments = Files.lines(Paths.get("labs/human_judgement.txt"))
                                              .collect(Collectors.toList())
                                              .iterator();

            while (judgments.hasNext()) {
                String[] data = judgments.next()
                                         .split(" ");
                int query = Integer.parseInt(data[0]);
                int document = Integer.parseInt(data[1]);
                int relevance = Integer.parseInt(data[2]);

                ArrayList<Integer> relevantDocs = human.getOrDefault(query, new ArrayList<>());
                if (relevance > 0 && relevance < 4) {
                    relevantDocs.add(document);
                }
                human.put(query, relevantDocs);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return human;
    }


}

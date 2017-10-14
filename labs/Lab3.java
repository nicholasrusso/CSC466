package labs;

import DocumentClasses.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * CSC466 Lab3 Specification : https://docs.google.com/document/d/10rHQDLd2MM8aOgp7zSiMbOn0YFHtosB8tHEgVxX0t9I/edit?usp=sharing
 * <p>
 * Created by cgels on 10/13/17.
 */
public class Lab3 {
    public static DocumentCollection documents;
    public static DocumentCollection queries;

    public static void main(String args[]) {
        try (ObjectInputStream is = new ObjectInputStream(new FileInputStream(new File("./files/docvector")))) {
            documents = (DocumentCollection) is.readObject();
        } catch (Exception e) {
            System.out.println(e);
        }

        queries = new DocumentCollection("labs/queries.txt", "queries");

        OkapiDistance okapi = new OkapiDistance();

        final QueryVector q1 = (QueryVector) queries.getDocumentById(1);

        List<Double> distances = new ArrayList<>();
        for (Map.Entry<Integer, TextVector> item : documents.getEntrySet()) {
            double dist = okapi.findDistance(q1, item.getValue(), documents);
            distances.add(dist);
        }

        distances.sort(Comparator.reverseOrder());

        for (int i = 0; i < 20; i++) {
            System.out.println(distances.get(i));
        }


        Map<Integer, ArrayList<Integer>> cosineSimilarities = computeSimilarities(new CosineDistance());
        Map<Integer, ArrayList<Integer>> okapiSimilarities = computeSimilarities(new OkapiDistance());

        System.out.println(okapiSimilarities.get(1)
                                            .toString());

        Map<Integer, ArrayList<Integer>> humanRanking = loadHumanRankings();

        System.out.println(String.format("Cosine MAP = %f", computeMAP(humanRanking, cosineSimilarities)));
        System.out.println(String.format("Okapi MAP = %f", computeMAP(humanRanking, okapiSimilarities)));
    }

    /**
     * Get 20 most relevant documents per query vector using the given distance function.
     */
    public static HashMap<Integer, ArrayList<Integer>> computeSimilarities(DocumentDistance distanceFunction) {
        HashMap<Integer, ArrayList<Integer>> queryResults = new HashMap<>();

        // make sure documents normalized before trying to compute cosine distances.
        if (distanceFunction instanceof CosineDistance) {
            documents.normalize(documents);
            queries.normalize(documents);
        }

        queries.getEntrySet()
               .stream()
               .forEach(
                       query -> queryResults.put(query.getKey(), query.getValue()
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

                if (relevance > 0) {
                    ArrayList<Integer> relevantDocs = human.getOrDefault(query, new ArrayList<>());
                    relevantDocs.add(document);
                    human.put(query, relevantDocs);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return human;
    }

    /**
     * Measures the mean average precision by comparing the system rankings with 'ground truth' (Human judged relevance).
     * Value returned is the MAP for first 20 queries.
     */
    public static double computeMAP(Map<Integer, ArrayList<Integer>> groundTruth, Map<Integer, ArrayList<Integer>> predictions) {
        int numQueries = 20;
        int index = 0;
        ArrayList<Double> averagePrecisions = new ArrayList<>(numQueries);

        Set<Map.Entry<Integer, ArrayList<Integer>>> first20 = new TreeMap<>(predictions).entrySet()
                                                                                        .stream()
                                                                                        .limit(20)
                                                                                        .collect(Collectors.toSet());

        for (Map.Entry<Integer, ArrayList<Integer>> searchResult : first20) {
            ArrayList<Integer> relevant = groundTruth.get(searchResult.getKey());
            ArrayList<Integer> returned = predictions.get(searchResult.getKey());

            averagePrecisions.add(index++, computePrecision(relevant, returned));
        }

        return averagePrecisions.stream()
                                .mapToDouble(p -> p)
                                .average()
                                .getAsDouble();
    }

    public static double computePrecision(ArrayList<Integer> relevant, ArrayList<Integer> returned) {
        int numCorrect = 0;

        ArrayList<Double> precisions = new ArrayList<>(returned.size());
        for (int k = 0; k < returned.size(); k++) {
            double p = 0.0;

            if (relevant.contains(returned.get(k))) {
                int rank = returned.indexOf(relevant.get(k)) + 1;
                numCorrect++;
                p = numCorrect / rank;
            }

            precisions.add(k, p);
        }

        return precisions.stream()
                         .mapToDouble(x -> x)
                         .sum() / relevant.size();
    }
}

import DocumentClasses.CosineDistance;
import DocumentClasses.DocumentCollection;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cgels on 9/19/17.
 */
public class Lab2 {
    public static DocumentCollection documents;
    public static DocumentCollection queries;

    public static void main(String args[]) {
        // read in binary file containing DocumentCollection from Lab1
        try (ObjectInputStream is = new ObjectInputStream(new FileInputStream(new File("./files/docvector")))) {
            documents = (DocumentCollection) is.readObject();
        } catch (Exception e) {
            System.out.println(e);
        }

        queries = new DocumentCollection("labs/queries.txt", "queries");


        documents.normalize(documents);
        queries.normalize(documents);


        HashMap<Integer, ArrayList<Integer>> queryResults = new HashMap<>();
        queries.getEntrySet()
               .stream()
               .forEach(query -> queryResults.put(query.getKey(),
                                                 query.getValue().findClosestDocuments(documents, new CosineDistance())));


        for (Map.Entry<Integer, ArrayList<Integer>> searchResult : queryResults.entrySet()) {
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("Query %d: ", searchResult.getKey()));
            sb.append(searchResult.getValue().toString());
            System.out.println(sb.toString());
        }

        try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(new File("./files/queryResultsCosineSimilarity")))) {
            os.writeObject(queryResults);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }
}

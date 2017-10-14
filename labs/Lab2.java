import DocumentClasses.CosineDistance;
import DocumentClasses.DocumentCollection;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

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


        ArrayList<ArrayList<Integer>> queryResults = new ArrayList<>();
        queries.getEntrySet()
               .stream()
               .forEach(query ->
                                queryResults.add(query.getValue()
                                                      .findClosestDocuments(documents, new CosineDistance())));


        int qID = 1;
        for (ArrayList<Integer> top20 : queryResults) {
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("Query %d: ", qID));
            sb.append(top20.toString());
            System.out.println(sb.toString());
            qID++;
        }
    }
}

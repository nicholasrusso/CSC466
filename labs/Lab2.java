import DocumentClasses.CosineDistance;
import DocumentClasses.DocumentCollection;
import DocumentClasses.QueryVector;
import DocumentClasses.TextVector;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by cgels on 9/19/17.
 */
public class Lab2 {
    public static DocumentCollection documents;
    public static DocumentCollection queries;

    public static void main(String args[]) {
//        documents = new DocumentCollection("./labs/documents.txt");
        try (ObjectInputStream is = new ObjectInputStream(
                new FileInputStream(
                        new File("./files/docvector")))) {
            documents = (DocumentCollection) is.readObject();
        } catch (Exception e) {
            System.out.println(e);
        }


        queries = new DocumentCollection("./labs/queries.txt", "queries");

        documents.normalize(documents);
        queries.normalize(queries);


        ArrayList<ArrayList<Integer>> queryResults = new ArrayList<>();
        queries.getEntrySet().stream()
                .forEach(query ->
                    queryResults.add(query.getValue().findClosestDocuments(documents, new CosineDistance())));

        StringBuilder sb = new StringBuilder();
        queryResults.get(0).stream().forEach(id -> sb.append(id +  ","));
        System.out.println(sb.toString());
    }
}

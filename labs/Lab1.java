import DocumentClasses.DocumentCollection;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

/**
 * Created by cgels on 9/15/17.
 */
public class Lab1 {

    public static void main(String[] args) {
        DocumentCollection dc = new DocumentCollection("labs/documents.txt", "document");
        StringBuilder stringBuilder = new StringBuilder();

        Map.Entry<String, Integer> singleMaxDocFreq = dc.getSingleHighestDocumentFrequency();

        stringBuilder.append("Word = " + singleMaxDocFreq.getKey() + "\n");
        stringBuilder.append("Frequency = " + singleMaxDocFreq.getValue() + "\n");
        stringBuilder.append("Distinct Number of Words = " + dc.getTotalDistinctWordCount() + "\n");
        stringBuilder.append("Total Number of Words = " + dc.getTotalWordCount() + "\n");

        System.out.println(stringBuilder.toString());

        try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(new File("./files/docvector")))) {
            os.writeObject(dc);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

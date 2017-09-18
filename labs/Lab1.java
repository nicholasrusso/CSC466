import DocumentClasses.DocumentCollection;
import DocumentClasses.TextVector;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by cgels on 9/15/17.
 */
public class Lab1 {

    public static void main(String[] args) {
        DocumentCollection dc = new DocumentCollection("./labs/documents.txt");
        StringBuilder stringBuilder = new StringBuilder();


        stringBuilder.append("Word = " + dc.getMostFrequentTerm() + "\n");
        stringBuilder.append("Frequency = " + dc.getDocumentFrequency(dc.getMostFrequentTerm()) + "\n");
        stringBuilder.append("Distinct Number of Words = " + dc.getTotalDistinctWordCount() + "\n");
        stringBuilder.append("Total Number of Words = " + dc.getTotalWordCount() + "\n");

        System.out.println(stringBuilder.toString());

        try(ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(new File("./files/docvector")))){
            os.writeObject(dc);
        } catch(Exception e){
            e.printStackTrace();
        }

    }
}

package labs;

import RuleMining.ItemSet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by cgels on 10/21/17.
 */
public class Lab5 {
    static ArrayList<ItemSet> transcations = new ArrayList<>();
    static HashSet<Integer> items = new HashSet(); // set of single item
    static HashMap<ItemSet, Integer> supportCounts = new HashMap<>();// maps ItemSet to occurrences
    static HashMap<Integer, ArrayList<ItemSet>> frequentItemLists = new HashMap<>(); // K-th FrequentItemSet -> Frequent ItemSets
    static double minSupport = .01;


    public static void main(String[] args) {
        process("labs/shopping_data.txt.csv");
        System.out.println(transcations.size());
        System.out.println(items.size());
//        for (Map.Entry<ItemSet, Integer> set : supportCounts.entrySet()) {
//            if (set.getValue() > 1) System.out.println(set.toString());
//        }

        findFrequentSingleItemSets();

        for ( ItemSet frequent : frequentItemLists.get(1)) {
            System.out.println(frequent);
            System.out.println(supportCounts.get(frequent));

        }

    }

    public static void process(String fileName) {
        try {
            Files.lines(Paths.get(fileName))
                 .collect(Collectors.toList())
                 .stream()
                 .forEach(line -> {
                     processItem(line);
                 });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void processItem(String line) {
        ItemSet t = new ItemSet();
        String[] strings = line.split(",");
        for (String item : Arrays.copyOfRange(strings, 1, strings.length)) {
            Integer i = Integer.parseInt(item.trim());
            items.add(i);
            t.add(i);
//            supportCounts.put(new ItemSet(t), supportCounts.getOrDefault(t, 0) + 1);
        }
        transcations.add(t);
    }


    public static boolean isFrequent(ItemSet itemSet) {

        return (((double) supportCounts.get(itemSet)) / transcations.size()) >= minSupport;
    }

    private static boolean isFrequentK(ItemSet itemSet, int k) {
        return isFrequent(itemSet) && itemSet.size() == k;
    }

    public static void findFrequentSingleItemSets() {
        ArrayList<ItemSet> frequentOneSets = new ArrayList<>();
        for (Map.Entry<ItemSet, Integer> set : supportCounts.entrySet()) {
            if (isFrequentK(set.getKey(), 1) && !frequentOneSets.contains(set.getKey())) {
                frequentOneSets.add(set.getKey());
            }
        }
        frequentItemLists.put(1, frequentOneSets);
    }
}
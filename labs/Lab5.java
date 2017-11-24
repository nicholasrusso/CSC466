package labs;

import RuleMining.AprioriAlgoHelpers;
import RuleMining.ItemSet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by cgels on 10/21/17.
 */
public class Lab5 {
    static ArrayList<ItemSet> transactions = new ArrayList<>();
    static HashSet<Integer> items = new HashSet(); // set of single item
    static HashMap<ItemSet, Integer> supportCounts = new HashMap<>();// maps ItemSet to occurrences
    static HashMap<Integer, ArrayList<ItemSet>> frequentItemLists = new HashMap<>(); // K-th FrequentItemSet -> Frequent ItemSets
    static double minSupport = .01;


    public static void main(String[] args) {
        process("labs/shopping_data.txt.csv");
        System.out.println(transactions.size());
        System.out.println(items.size());
        findFrequentSingleItemSets();

        int k = 1;
        do {
            k++;
            findFrequentItemSets(k);

        } while (!frequentItemLists.getOrDefault(k, new ArrayList<>())
                                   .isEmpty());

        try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(new File("./files/transactionList")))) {
            os.writeObject(transactions);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }

        try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(new File("./files/frequentItemSetLists")))) {
            os.writeObject(frequentItemLists);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }

        try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(new File("./files/supportCounts")))) {
            os.writeObject(supportCounts);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }

        System.out.println(frequentItemLists.toString());



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
        ArrayList<Integer> itemList = new ArrayList<>();
        String[] strings = line.split(",");

        // build temp ItemSet for i-th transaction
        for (String item : Arrays.copyOfRange(strings, 1, strings.length)) {
            Integer i = Integer.parseInt(item.trim());
            items.add(i);
            itemList.add(i);
        }

        for (List<Integer> itemSet : AprioriAlgoHelpers.powerset(itemList)) {
            ItemSet i = new ItemSet(itemSet);
            supportCounts.put(i, supportCounts.getOrDefault(i, 0) + 1);
            if (i.size() == itemList.size()) {
                transactions.add(i); // add full itemset to transcation list
            }
        }
    }


    private static boolean isFrequentK(ItemSet itemSet, int k) {

        return AprioriAlgoHelpers.isFrequent(itemSet, supportCounts, minSupport, transactions.size()) && itemSet
                .size() == k;
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

    public static void findFrequentItemSets(int k) {
        if (k > 1) {
            List<ItemSet> freqItemsetsPrev = frequentItemLists.get(k - 1);
            ArrayList<ItemSet> frequentItemSetsK = new ArrayList<>();

            List<ItemSet> candidates = AprioriAlgoHelpers.generateCandidates(k, freqItemsetsPrev);

            for (ItemSet c : candidates) {
                if (AprioriAlgoHelpers.isFrequent(c, supportCounts, minSupport, transactions.size())) {
                    frequentItemSetsK.add(c);
                }
            }

            if (!frequentItemSetsK.isEmpty()) {
                frequentItemLists.put(k, frequentItemSetsK);
            }
        }
    }




}
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
        findFrequentSingleItemSets();

        int k = 1;
        do {
            k++;
            findFrequentItemSets(k);

        } while (!frequentItemLists.getOrDefault(k, new ArrayList<>())
                                   .isEmpty());


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

        for (List<Integer> itemSet : powerset(itemList)) {
            ItemSet i = new ItemSet(itemSet);
            supportCounts.put(i, supportCounts.getOrDefault(i, 0) + 1);
            if (i.size() == itemList.size()) {
                transcations.add(i); // add full itemset to transcation list
            }
        }
    }


    public static List<List<Integer>> powerset(List<Integer> itemList) {
        // source: http://rosettacode.org/wiki/Power_Set#Java
        List<List<Integer>> ps = new ArrayList<List<Integer>>();
        ps.add(new ArrayList<Integer>());   // add the empty set

        // for every item in the original list
        for (Integer item : itemList) {
            List<List<Integer>> newPs = new ArrayList<List<Integer>>();

            for (List<Integer> subset : ps) {
                // copy all of the current powerset's subsets
                newPs.add(subset);
                // plus the subsets appended with the current item
                List<Integer> newSubset = new ArrayList<Integer>(subset);
                newSubset.add(item);
                newPs.add(newSubset);
            }
            // powerset is now powerset of list.subList(0, list.indexOf(item)+1)
            ps = newPs;
        }
        ps = ps.subList(1, ps.size());
        Collections.sort(ps, (l1, l2) -> l1.get(0)
                                           .compareTo(l2.get(0)));
        return ps;
    }


    public static boolean isFrequent(ItemSet itemSet) {

        return (((double) supportCounts.getOrDefault(itemSet, 0)) / transcations.size()) >= minSupport;
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

    public static void findFrequentItemSets(int k) {
        if (k > 1) {
            List<ItemSet> freqItemsetsPrev = frequentItemLists.get(k - 1);
            List<ItemSet> candidates = new ArrayList<>();
            ArrayList<ItemSet> frequentItemSetsK = new ArrayList<>();

            for (int i = 0; i < freqItemsetsPrev.size(); i++) {
                for (int j = i + 1; j < freqItemsetsPrev.size(); j++) {
                    ItemSet f1 = freqItemsetsPrev.get(i);
                    ItemSet f2 = freqItemsetsPrev.get(j);

                    if (differsByLastOnly(f1, f2)) {
                        ItemSet candidate = new ItemSet(f1);
                        candidate.add(f2.getItem(f1.size() - 1));

                        List<List<Integer>> subsets = powerset(candidate.getItems());
                        boolean containsAll = true;
                        for (List<Integer> subItems : subsets) {
                            if (subItems.size() == k - 1) {
                                ItemSet subset = new ItemSet(subItems);
                                containsAll = containsAll && freqItemsetsPrev.contains(subset);
                            }
                        }

                        if (containsAll) {
                            candidates.add(candidate);
                        }
                    }
                }

            }

            for (ItemSet c : candidates) {
                if (isFrequent(c)) {
                    frequentItemSetsK.add(c);
                }
            }

            if (!frequentItemSetsK.isEmpty()) {
                frequentItemLists.put(k, frequentItemSetsK);
            }
        }
    }

    private static boolean differsByLastOnly(ItemSet a, ItemSet b) {
        boolean good = true;
        for (int i = 0; i < a.size() - 1; i++) {
            if (a.getItem(i) != b.getItem(i)) {
                good = false;
            }
        }


        return good && (a.getItem(a.size() - 1) < b.getItem(a.size() - 1));
    }
}
package RuleMining;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by cgels on 11/24/17.
 */
public class AprioriAlgoHelpers {

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

    public static List<ItemSet> generateCandidates(int k, List<ItemSet> freqItemsetsPrev) {
        List<ItemSet> candidates = new ArrayList<>();
        for (int i = 0; i < freqItemsetsPrev.size(); i++) {
            for (int j = i + 1; j < freqItemsetsPrev.size(); j++) {
                ItemSet f1 = freqItemsetsPrev.get(i);
                ItemSet f2 = freqItemsetsPrev.get(j);

                if (differsByLastOnly(f1, f2)) {
                    ItemSet candidate = new ItemSet(f1);
                    candidate.add(f2.getItem(f1.size() - 1));

                    List<List<Integer>> subsets = AprioriAlgoHelpers.powerset(candidate.getItems());
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
        return candidates;
    }

    public static boolean differsByLastOnly(ItemSet a, ItemSet b) {
        boolean good = true;
        for (int i = 0; i < a.size() - 1; i++) {
            if (a.getItem(i) != b.getItem(i)) {
                good = false;
            }
        }


        return good && (a.getItem(a.size() - 1) < b.getItem(a.size() - 1));
    }

    public static boolean isFrequent(ItemSet itemSet, HashMap<ItemSet, Integer> supportCounts, double minSupport, int N) {

        return (((double) supportCounts.getOrDefault(itemSet, 0)) / N) >= minSupport;
    }

    public static boolean isMinConfidenceMet(Rule r, HashMap<ItemSet, Integer> supportCounts, double minConf) {
        return minConf <= (supportCounts.getOrDefault(r.getUnion(), 0) / supportCounts.get(r.left));
    }

    public static Rule buildRule(ArrayList<Integer> X, ArrayList<Integer> Y){
        ArrayList<Integer> antecedent = new ArrayList<>(Y);
        ItemSet consequent = new ItemSet();

        for (Integer alpha : X) {
            antecedent.remove(alpha);
            consequent.add(alpha);
        }
        return new Rule(new ItemSet(antecedent), consequent);
    }
}

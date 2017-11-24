package labs;

import RuleMining.AprioriAlgoHelpers;
import RuleMining.ItemSet;
import RuleMining.Rule;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by cgels on 11/23/17.
 */
public class Lab6 {
    static ArrayList<ItemSet> transactions = new ArrayList<>();
    static HashMap<ItemSet, Integer> supportCounts = new HashMap<>();// maps ItemSet to occurrences
    static HashMap<Integer, ArrayList<ItemSet>> frequentItemLists = new HashMap<>(); // K-th FrequentItemSet -> Frequent ItemSets

    static ArrayList<Rule> rules = new ArrayList<>();


    static double minSupport = 0.01;
    static double minConf = 0.99;


    public static void main(String[] args) {

        try (ObjectInputStream is = new ObjectInputStream(new FileInputStream(new File("./files/supportCounts")))) {
            supportCounts = (HashMap<ItemSet, Integer>) is.readObject();
        } catch (Exception e) {
            System.out.println(e);
        }

        try (ObjectInputStream is = new ObjectInputStream(new FileInputStream(new File("./files/frequentItemSetLists")))) {
            frequentItemLists = (HashMap<Integer, ArrayList<ItemSet>>) is.readObject();
        } catch (Exception e) {
            System.out.println(e);
        }

        try (ObjectInputStream is = new ObjectInputStream(new FileInputStream(new File("./files/transactionList")))) {
            transactions = (ArrayList<ItemSet>) is.readObject();
        } catch (Exception e) {
            System.out.println(e);
        }

        generateRules();

        try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(new File("./files/rules")))) {
            os.writeObject(rules);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }


        System.out.println(rules.size());
        System.out.println(rules);


    }

    public static void generateRules() {
       // generate rules from all frequent item sets
        for (int k = 2; frequentItemLists.getOrDefault(k, new ArrayList<>()).size() > 0; k++) {
            for (ItemSet f_k : frequentItemLists.get(k)) {
                ArrayList<Rule> oneConsequentRules = generateCandidateOneConsequentRulesFromItemSet(f_k);
                int m = 1;
                ArrayList<ItemSet> H_1 = new ArrayList<>();
                for (Rule r : oneConsequentRules) {
                    H_1.add(r.right);
                }
                rules.addAll(oneConsequentRules);
                aprioriGenerateRules(f_k, H_1, m);
            }
        }
    }

    public static void aprioriGenerateRules(ItemSet f_k, ArrayList<ItemSet> H_m, int m) {
        int k = f_k.size();
        int g = m + 1;
        ArrayList<Integer> Y = f_k.getItems();

        if (k > g && !H_m.isEmpty()) {
            // list of m+1 consequents to be formed into rules
            List<ItemSet> H_g = AprioriAlgoHelpers.generateCandidates(g, H_m);
            for (ItemSet X : H_g) {
                Rule r = AprioriAlgoHelpers.buildRule(X.getItems(), Y);
                if (isValidRule(r, f_k)) {
                    rules.add(r);
                }
            }
        }

    }

    private static ArrayList<Rule> generateCandidateOneConsequentRulesFromItemSet(ItemSet f_k) {
        ArrayList<Rule> validOneConsequents = new ArrayList<>();

        for (Integer alpha : f_k.getItems()) {
            ArrayList<Integer> X = new ArrayList<>();
            X.add(alpha);
            Rule candidate = AprioriAlgoHelpers.buildRule(X, f_k.getItems());
            if (isValidRule(candidate, f_k)) {
                validOneConsequents.add(candidate);
            }
        }
        return validOneConsequents;
    }


    private static boolean isValidRule(Rule candidate, ItemSet freq_kSet) {
        return AprioriAlgoHelpers.isMinConfidenceMet(candidate, supportCounts, minConf) &&
                AprioriAlgoHelpers.isFrequent(freq_kSet, supportCounts, minSupport, transactions.size());
    }



}

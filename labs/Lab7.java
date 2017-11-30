package labs;

import DecisionTree.Matrix;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by cgels on 11/28/17.
 */
public class Lab7 {

    public static void main(String args[]) {
        int[][] data = process("labs/data.txt");

        if (data == null) {
            System.exit(0);
        }

        ArrayList<Integer> attributes = new ArrayList<>();
        attributes.add(0);
        attributes.add(1);
        attributes.add(2);
        attributes.add(3);
        System.out.println(data.length + "," + data[0].length);

        ArrayList<Integer> intialRows = new ArrayList<>();
        for (int i = 0; i < data.length; i++) {
            intialRows.add(i);
        }


        printDecisionTree(data, attributes, intialRows,0, 100);
    }

    public static int[][] process(String filename) {
        ArrayList<ArrayList<Integer>> rowValues = new ArrayList<>();

        try {
            Iterator<String> lines = Files.lines(Paths.get(filename))
                                               .collect(Collectors.toList())
                                               .iterator();

            lines.forEachRemaining(l -> {
                ArrayList<Integer> rVals = new ArrayList<>();
                String[] data = l.split(",");
                for (String d : data) {
                    double val = Double.parseDouble(d);
                    rVals.add( (int) Math.floor(val));
                }
                rowValues.add(rVals);
            });

            int numCols = rowValues.get(0).size();
            int[][] data = new int[rowValues.size()][numCols];

            for (int r = 0; r < rowValues.size(); r++) {
                for (int c = 0; c < numCols; c++) {
                    data[r][c] = rowValues.get(r).get(c);
                }
            }

            return data;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void printDecisionTree(int[][] data, ArrayList<Integer> attributes, ArrayList<Integer> rows, int level, double currentIGR) {
        Matrix m = new Matrix(data);
        int bestAttr = -1;
        double bestIGR = -1;

        String indent = "";
        int l = 0;
        while (l < level) {
            indent += "\t";
            l++;
        }

        //base case if no attributes then finished
        if (attributes.size() > 0) {

            TreeMap<Integer, Double> gainRatios = new TreeMap<>();
            for (Integer a : attributes){
                gainRatios.put(a, m.computeIGR(a, rows));
            }

            Map.Entry<Integer, Double> best = gainRatios.lastEntry();
            bestAttr = best.getKey();

            // must be better gain than current best
            if (best.getValue() > bestIGR) {
                bestIGR = best.getValue();
            }

            if (attributes.size() == 1) {
                bestIGR = 0.0;
            }

        }

        if (bestIGR < .01) {
            System.out.println(String.format(indent + "value is %d", m.findMostCommonCategory(rows)).toString());
        }
        else {

            HashMap<Integer, ArrayList<Integer>> split = m.split(bestAttr, rows);
            attributes.remove(bestAttr);
            for (Map.Entry<Integer, ArrayList<Integer>> branch : split.entrySet()) {

                System.out.println(indent + String.format("When attribute %d has value %d",
                                                           bestAttr + 1,
                                                           branch.getKey()));
                printDecisionTree(data, attributes, branch.getValue(), level + 1, bestIGR);
            }
        }

    }
}

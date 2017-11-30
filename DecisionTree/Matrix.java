package DecisionTree;

import java.util.*;

/**
 * Created by cgels on 11/28/17.
 */
public class Matrix {
    private int[][] data;
    public int numRows = 0;
    public int numCols = 0;
    private ArrayList<Integer> rowIndices;

    public Matrix(int[][] matrix) {
        data = matrix;

        numRows = data.length;
        numCols = data[0].length;

        rowIndices = new ArrayList<>();
        for (int i = 0; i < numRows; i++) {
            rowIndices.add(i);
        }
    }

    /***
     * Examines only the specified rows of the array. It returns the number of rows in which the element at position attribute (a number between 0 and 4) is equal to value.
     */
    private int findFrequency(int attribute, int value, ArrayList<Integer> rows) {
        int count = 0;

        for (Integer r : rows) {
            if (data[r][attribute] == value) count++;
        }

        return count;
    }

    /**
     * Examines only the specified rows of the array. It returns a HashSet of the different values for the specified attribute.
     */
    private HashSet<Integer> findDifferentValues(int attribute, ArrayList<Integer> rows) {
        HashSet<Integer> unique = new HashSet<>();
        for (Integer r : rows) {
            unique.add(data[r][attribute]);
        }
       return unique;
    }

    /**
     * returns log2 of the input
     */
    private double log2(double number) {
        return Math.log(number) / Math.log(2);
    }

    /**
     * finds the entropy of the dataset that consists of the specified rows
     * */
    private double findEntropy(ArrayList<Integer> rows) {
        HashSet<Integer> categories = findDifferentValues(4, rows);

        double entropy = 0;
        for (Integer c : categories) {
            double pr_c = (1.0 * findFrequency(numCols - 1, c, rows)) / rows.size();
            entropy -= pr_c * log2(pr_c);
        }

        return entropy;
    }

    private double findEntropyA(int attribute, ArrayList<Integer> rows) {
        double entropy = 0;

        for (Integer value : findDifferentValues(attribute, rows)) {
            ArrayList<Integer> D_j = getRows(attribute, value, rows);
            entropy += ((D_j.size() * 1.0) / rows.size()) * findEntropy(D_j);
        }


        return entropy;
    }

    /**
     * finds the information gain of partitioning on the attribute. Considers only the specified rows.
     */
    private double findGain(int attribute, ArrayList<Integer> rows) {
        return findEntropy(rows) - findEntropyA(attribute, rows);
    }

    /**
     *  returns the Information Gain Ratio, where we only look at the data defined by the set of rows and we consider splitting on attribute.
    */
    public double computeIGR(int attribute, ArrayList<Integer> rows) {
        HashMap<Integer, ArrayList<Integer>> partition = split(attribute, rows);

        double total = 0;
        for (Map.Entry<Integer, ArrayList<Integer>> p : partition.entrySet()) {
            double proportion = (p.getValue()
                                  .size() * 1.0) / rows.size();
            total -= proportion * log2(proportion);
        }

        return findGain(attribute, rows) / total;
    }


    /**
     * returns the most common category for the dataset that is the defined by the specified rows.
     * */
    public int findMostCommonCategory(ArrayList<Integer> rows) {
        TreeMap<Integer, Integer> categoryCount = new TreeMap<>();
        for (Integer r : rows) {
            int category = data[r][numCols - 1];
            categoryCount.put(category, categoryCount.getOrDefault(category, 0) + 1);
        }

        return categoryCount.lastKey();
    }

    /**
     * Splits the dataset that is defined by rows on the attribute.
     * Each element of the HashMap that is returned contains the value for the attribute and an ArrayList of rows that have this value.
     * */
    public HashMap<Integer, ArrayList<Integer>> split(int attribute, ArrayList<Integer> rows) {
        HashMap<Integer, ArrayList<Integer>> attrValueMap = new HashMap<>();

        for (Integer r : rows) {
            int attrVal = data[r][attribute];

            ArrayList<Integer> currentRows = attrValueMap.getOrDefault(attrVal, new ArrayList<>());
            currentRows.add(r);
            attrValueMap.put(attrVal, currentRows);
        }

        return attrValueMap;
    }

    private ArrayList<Integer> getRows(int attribute, int value, ArrayList<Integer> rows) {
        ArrayList<Integer> rtn = new ArrayList<>();
        for (int r : rows) {
            if (data[r][attribute] == value) rtn.add(r);
        }
        return rtn;
    }

    /**
     * returns all the indices of all rows, e.g., 0,1,... up to the total number of rows -1
     * */
    public ArrayList<Integer> findAllRows() {
        return (ArrayList<Integer>) rowIndices.clone();
    }

    /**
     * returns the index of the category attribute
     * */
    public int getCategoryAttribute() {
        return numCols - 1;
    }


    /**
     * takes as input the values for a single row, e.g., 5,3,1,2 and the category, e.g. 2.
     * Returns the probability that the row belongs to the category using the Naïve Bayesian model.
     * */
    public double findProb(int[] row, int category) {
        double lambda = 1.0 / numRows;
        int n_j = findFrequency(getCategoryAttribute(), category, findAllRows());
        double prob = 1.0;

        for (int attrIndex = 0; attrIndex < getCategoryAttribute(); attrIndex++) {
            int n_ij = 0;
            int a_i = row[attrIndex];
            int m_i = findDifferentValues(attrIndex, findAllRows()).size();
            for (Map.Entry<Integer, ArrayList<Integer>> a_j : split(attrIndex, findAllRows()).entrySet()) {
                // if they have the same attribute value
                if (a_j.getKey() == a_i) {
                    // count the number of rows which also have the same class
                    for (Integer idx : a_j.getValue()) {
                        if (data[idx][getCategoryAttribute()] == category) {
                            n_ij++;
                        }
                    }



                }
            }
            prob *= (n_ij + lambda) / (n_j + lambda * m_i);
        }


        double p_ck =  0;
        for(Map.Entry<Integer, ArrayList<Integer>> c: split(getCategoryAttribute(), findAllRows()).entrySet()) {
            if (c.getKey() == category) {
                p_ck = (1.0 * c.getValue()
                               .size()) / numRows;
            }
        }



        return p_ck * prob;
    }


    /**
     * takes as input the values for a single row, e.g., 5,3,1,2.
     * Returns the most probable category of the row using the Naïve Bayesian Model.
     * */
    public int findCategory(int[] row) {
        HashMap<Integer, Double> catMap = new HashMap<>();
        for (Integer category : findDifferentValues(getCategoryAttribute(), findAllRows())) {
            double p = findProb(row, category);
            catMap.put(category, p);
        }

        return Collections.max(catMap.entrySet(), Map.Entry.comparingByValue()).getKey();
    }




}

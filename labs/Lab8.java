package labs;

import DecisionTree.Matrix;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Created by cgels on 11/29/17.
 */
public class Lab8 {
    static Scanner sc;

    public static void main(String args[]) {
        Matrix m = new Matrix(readInput("labs/data.txt"));

        boolean again = true;
        sc = new Scanner(System.in);
        do{
            int c = m.findCategory(getCustomerInput());
            System.out.println(String.format("Expected category %d", c));
            System.out.print("Again? [y/n]");
            if (sc.hasNext()) {
                again = sc.nextLine()
                          .equalsIgnoreCase("y");
            }

        }while (again);
    }

    public static int[][] readInput(String filename) {
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

    public static int[] getCustomerInput() {
        int[] newRow = new int[4];
        System.out.println("Enter discrete value for Attribute 1");
        if (sc.hasNext()) {
            newRow[0] = Integer.parseInt(sc.nextLine());
        }

        System.out.println("Enter discrete value for Attribute 2");
        if (sc.hasNext()) {
            newRow[1] = Integer.parseInt(sc.nextLine());
        }

        System.out.println("Enter discrete value for Attribute 3");
        if (sc.hasNext()) {
            newRow[2] = Integer.parseInt(sc.nextLine());
        }

        System.out.println("Enter discrete value for Attribute 4");
        if (sc.hasNext()) {
            newRow[3] = Integer.parseInt(sc.nextLine());
        }

        return newRow;
    }
}

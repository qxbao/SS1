package org.orca.stable_matching;

import org.moeaframework.algorithm.NSGAIII;
import org.moeaframework.core.Algorithm;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.Permutation;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        // n = 30
        int[][] prefs = new int[][]{
                { 18, 34, 32, 15, 20, 26, 33, 21, 19, 17, 25, 31, 30, 27, 16, 22, 29, 23, 28, 24 },
                { 30, 24, 32, 25, 22, 28, 15, 26, 16, 31, 33, 21, 23, 29, 19, 34, 17, 18, 27, 20 },
                { 25, 31, 15, 16, 17, 19, 22, 28, 30, 24, 20, 21, 23, 34, 33, 29, 26, 18, 27, 32 },
                { 22, 17, 23, 16, 25, 15, 31, 26, 29, 34, 27, 30, 19, 21, 24, 32, 33, 18, 20, 28 },
                { 34, 16, 25, 32, 18, 19, 26, 23, 27, 21, 17, 29, 28, 30, 20, 33, 24, 31, 15, 22 },
                { 27, 28, 29, 31, 30, 23, 19, 18, 20, 24, 17, 22, 16, 21, 33, 25, 34, 32, 26, 15 },
                { 21, 24, 31, 23, 30, 20, 27, 29, 33, 25, 15, 34, 28, 17, 16, 18, 32, 22, 19, 26 },
                { 21, 34, 31, 17, 22, 23, 15, 24, 25, 20, 19, 33, 27, 16, 29, 26, 18, 32, 28, 30 },
                { 17, 30, 32, 25, 15, 18, 16, 28, 21, 26, 23, 29, 31, 33, 19, 34, 24, 22, 27, 20 },
                { 28, 33, 24, 16, 34, 25, 22, 31, 20, 27, 26, 21, 19, 32, 17, 15, 29, 18, 30, 23 },
                { 23, 19, 30, 32, 25, 18, 24, 28, 17, 29, 21, 34, 16, 31, 22, 15, 27, 20, 26, 33 },
                { 15, 28, 33, 16, 34, 27, 22, 19, 30, 23, 20, 32, 25, 21, 17, 29, 24, 18, 26, 31 },
                { 19, 22, 24, 32, 20, 29, 28, 27, 33, 16, 34, 23, 15, 18, 31, 25, 30, 26, 17, 21 },
                { 17, 19, 32, 28, 33, 30, 20, 24, 27, 18, 22, 15, 23, 29, 21, 25, 34, 31, 16, 26 },
                { 16, 29, 32, 25, 31, 17, 28, 22, 20, 27, 24, 21, 15, 19, 34, 30, 33, 18, 26, 23 },
                { 11, 6, 7, 12, 13, 4, 0, 3, 1, 9, 8, 5, 10, 2, 14 },
                { 13, 11, 4, 9, 0, 8, 14, 7, 6, 2, 5, 12, 1, 10, 3 },
                { 7, 9, 11, 12, 2, 3, 1, 14, 8, 5, 10, 4, 6, 0, 13 },
                { 14, 11, 12, 7, 0, 4, 10, 1, 13, 9, 3, 2, 6, 5, 8 },
                { 9, 12, 1, 7, 10, 6, 0, 8, 13, 5, 4, 3, 2, 14, 11 },
                { 6, 2, 3, 12, 9, 4, 1, 14, 5, 13, 10, 7, 0, 11, 8 },
                { 0, 1, 5, 6, 9, 2, 12, 11, 14, 4, 7, 13, 8, 3, 10 },
                { 2, 3, 8, 13, 12, 0, 6, 14, 11, 9, 4, 1, 5, 10, 7 },
                { 3, 2, 12, 5, 10, 4, 6, 9, 13, 14, 0, 7, 11, 8, 1 },
                { 12, 5, 2, 14, 11, 3, 6, 7, 13, 1, 4, 0, 8, 9, 10 },
                { 2, 5, 6, 0, 8, 4, 3, 14, 13, 7, 11, 10, 9, 12, 1 },
                { 5, 7, 0, 11, 6, 12, 2, 10, 3, 9, 13, 14, 4, 1, 8 },
                { 4, 2, 3, 9, 1, 5, 11, 8, 12, 0, 14, 10, 13, 7, 6 },
                { 5, 13, 11, 7, 9, 4, 3, 2, 12, 0, 14, 10, 1, 6, 8 },
                { 1, 4, 3, 12, 8, 13, 5, 6, 10, 14, 7, 9, 2, 11, 0 },
                { 2, 9, 10, 1, 6, 0, 8, 11, 12, 13, 3, 4, 7, 5, 14 },
                { 0, 11, 2, 3, 7, 9, 14, 13, 1, 5, 4, 12, 10, 6, 8 },
                { 7, 2, 11, 3, 4, 14, 8, 5, 13, 1, 9, 0, 12, 10, 6 },
                { 11, 13, 4, 12, 6, 9, 0, 7, 14, 2, 1, 5, 10, 3, 8 },
                { 0, 6, 1, 4, 10, 5, 12, 8, 14, 13, 2, 3, 11, 9, 7 },
        };
        StableMatchingProblem problem = new StableMatchingProblem(prefs);
        Algorithm algorithm = new NSGAIII(problem);
        algorithm.run(30000);
        NondominatedPopulation solutions = algorithm.getResult();
        for (Solution solution: solutions) {
            System.out.println("SOLUTION");
            int[] var0 = ((Permutation) solution.getVariable(0)).toArray();
            Matches matches = problem.StableMatchingExtra(var0);
            System.out.println("- Order: " + Arrays.toString(var0));
            System.out.println("- Pairs:");
            List<Integer> pairs = matches.getList();
            Set<Integer> nodes = new HashSet<>();
            for (int i = 0; i < pairs.size(); i++) {
                if (nodes.contains(i) || nodes.contains(pairs.get(i))) continue;
                if (pairs.get(i) == -1) continue;
                System.out.printf("  + [%2d - %1d]%n", i, pairs.get(i));
                nodes.add(i);
                nodes.add(pairs.get(i));
            }
            System.out.println("Leftover: " + matches.getLeftOver());
            System.out.println("Satisfaction: " + (- solution.getObjective(0)));
        }
    }
}

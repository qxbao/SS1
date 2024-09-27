package org.orca.stable_matching;

import org.moeaframework.algorithm.NSGAII;
import org.moeaframework.core.Algorithm;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.Permutation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        int n = 30;
        int[][] pref = new int[][]{
                {17, 20, 29, 28, 21, 19, 26, 24, 16, 22, 27, 15, 18, 25, 23},
                {28, 19, 17, 26, 16, 25, 20, 23, 22, 18, 24, 15, 27, 21, 29},
                {24, 17, 26, 23, 18, 20, 19, 16, 27, 21, 22, 25, 29, 15, 28},
                {17, 18, 20, 24, 15, 16, 28, 27, 25, 21, 22, 23, 26, 19, 29},
                {29, 27, 26, 23, 20, 16, 28, 22, 19, 21, 25, 18, 17, 15, 24},
                {21, 16, 29, 28, 23, 15, 17, 22, 18, 20, 25, 24, 19, 27, 26},
                {19, 17, 15, 26, 27, 24, 16, 25, 29, 23, 22, 21, 20, 28, 18},
                {25, 27, 15, 18, 24, 29, 20, 28, 26, 22, 16, 21, 23, 17, 19},
                {20, 23, 22, 26, 18, 29, 24, 27, 21, 16, 15, 17, 28, 25, 19},
                {25, 17, 21, 23, 16, 26, 24, 22, 20, 28, 29, 18, 15, 27, 19},
                {27, 28, 20, 17, 18, 16, 26, 24, 25, 23, 29, 21, 19, 22, 15},
                {24, 25, 23, 15, 19, 21, 29, 18, 28, 16, 26, 22, 20, 27, 17},
                {24, 22, 27, 16, 17, 28, 20, 23, 19, 15, 21, 26, 25, 29, 18},
                {21, 29, 26, 19, 24, 17, 27, 15, 23, 25, 28, 22, 18, 20, 16},
                {22, 29, 19, 20, 27, 24, 16, 28, 26, 17, 18, 15, 23, 25, 21},
                {11, 5, 1, 2, 6, 10, 0, 13, 4, 7, 12, 9, 8, 14, 3},
                {14, 3, 2, 0, 10, 12, 5, 8, 13, 1, 11, 9, 4, 7, 6},
                {12, 13, 9, 5, 11, 8, 7, 14, 2, 3, 0, 10, 6, 1, 4},
                {4, 9, 2, 14, 5, 11, 12, 6, 3, 13, 8, 7, 1, 10, 0},
                {11, 8, 13, 6, 5, 7, 4, 3, 14, 9, 12, 1, 10, 0, 2},
                {4, 1, 10, 5, 8, 7, 0, 11, 6, 9, 12, 13, 2, 14, 3},
                {5, 3, 6, 0, 1, 8, 7, 9, 10, 4, 11, 14, 12, 2, 13},
                {10, 0, 6, 4, 12, 11, 14, 13, 2, 5, 3, 1, 8, 7, 9},
                {12, 1, 8, 5, 3, 9, 13, 6, 4, 0, 11, 2, 14, 10, 7},
                {14, 1, 9, 12, 8, 0, 6, 3, 4, 2, 7, 11, 10, 5, 13},
                {2, 13, 14, 1, 12, 11, 4, 0, 9, 5, 3, 8, 7, 6, 10},
                {13, 7, 12, 14, 2, 4, 5, 11, 1, 10, 0, 3, 9, 8, 6},
                {0, 13, 6, 5, 3, 8, 7, 4, 9, 14, 12, 11, 10, 2, 1},
                {7, 9, 8, 5, 14, 2, 0, 13, 3, 6, 1, 12, 4, 10, 11},
                {1, 6, 0, 14, 2, 8, 9, 10, 7, 11, 5, 3, 13, 12, 4},
        };
        StableMatchingProblem3 problem = new StableMatchingProblem3(n, pref);
        Algorithm algorithm = new NSGAII(problem);
        algorithm.run(100);
        NondominatedPopulation solutions = algorithm.getResult();
        for (Solution solution: solutions) {
            System.out.println("SOLUTION");
            System.out.println("   Pairs");
            List<Integer> pairs = problem.GaleShapley(((Permutation) solution.getVariable(0)).toArray());
            Set<Integer> nodes = new HashSet<>();
            for (int i = 0; i < pairs.size(); i++) {
                if (nodes.contains(i) || nodes.contains(pairs.get(i))) continue;
                System.out.printf("[ %s - %s ]%n", i, pairs.get(i));
                nodes.add(i);
                nodes.add(pairs.get(i));
            }
            System.out.println("Satisfaction: " + (- solution.getObjective(0)));
        }
    }
}

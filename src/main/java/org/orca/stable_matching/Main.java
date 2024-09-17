package org.orca.stable_matching;

import org.moeaframework.core.Algorithm;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Problem;
import org.moeaframework.core.Solution;
import org.moeaframework.core.spi.AlgorithmFactory;
import org.moeaframework.core.variable.Permutation;

public class Main {
    public static void main(String[] args) {
        int n = 4;
        int[][] menPref = new int[][]{
                {3, 1, 2, 0},
                {1, 0, 2, 3},
                {0, 1, 2, 3},
                {0, 1, 2, 3}
        };
        int [][] womenPref = new int[][]{
                {0, 1, 2, 3},
                {0, 1, 2, 3},
                {0, 1, 2, 3},
                {0, 1, 2, 3}
        };
        StableMatchingProblem problem = new StableMatchingProblem(n, menPref, womenPref);
        Algorithm algorithm = AlgorithmFactory.getInstance().getAlgorithm("NSGAII", problem);
        algorithm.run(100);
        NondominatedPopulation solution = algorithm.getResult();
        int i = 1;
        for (Solution sol : solution) {
            System.out.println("Solution " + i + ":");
            int[] women = ((Permutation) sol.getVariable(0)).toArray();
            for (int j = 0; j < n; j++) {
                System.out.printf("(Man %s - Woman %s)%n", j, women[j]);
            }
            int s = 0;
            for (int j = 0; j < n; j++) {
                s += problem.getSatisfactory(j, women[j]);
            }
            System.out.printf("Instability: %s", sol.getObjective(0));
            i++;
        }
    }
}

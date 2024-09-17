package org.orca.stable_matching;

import org.moeaframework.core.Algorithm;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Solution;
import org.moeaframework.core.spi.AlgorithmFactory;
import org.moeaframework.core.variable.Permutation;

import java.util.Arrays;

public class Main2 {
    public static void main(String[] args) {
        int n = 4;
        int[][] menPref = new int[][]{
                {1, 0, 2, 3},
                {3, 0, 1, 2},
                {0, 2, 1, 3},
                {1, 2, 0, 3}
        };
        int [][] womenPref = new int[][]{
                {0, 2, 1, 3},
                {2, 3, 0, 1},
                {3, 1, 2, 0},
                {2, 1, 0, 3}
        };
        StableMatchingProblem2 problem = new StableMatchingProblem2(n, menPref, womenPref);
        Algorithm algorithm = AlgorithmFactory.getInstance().getAlgorithm("NSGAII", problem);
        algorithm.run(100);
        NondominatedPopulation solution = algorithm.getResult();
        int i = 1;
        for (Solution sol : solution) {
            System.out.println("Solution " + i + ":");
            int[] womenOrder = ((Permutation) sol.getVariable(0)).toArray();
            System.out.println("Women order: " + Arrays.toString(womenOrder));
            int[] menPartner = problem.GaleShapley(womenOrder);
            for (int j = 0; j < n; j++) {
                System.out.printf("(Man %s - Woman %s)%n", j, menPartner[j]);
            }
            System.out.printf("Satisfaction: %s", -sol.getObjective(0));
            i++;
        }
    }
}

package org.orca.knapsack;

import org.moeaframework.algorithm.NSGAII;
import org.moeaframework.core.Algorithm;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Problem;
import org.moeaframework.core.Solution;

public class Main {
    public static void main(String[] args) {
        Problem problem = new KnapsackProblem();
        Algorithm algorithm = new NSGAII(problem);
        algorithm.run(1000);
        NondominatedPopulation res = algorithm.getResult();
        for (Solution solution : res) {
            String[] items = KnapsackProblem.names;
            char[] var0 = solution.getVariable(0).toString().toCharArray();
            double obj0 = -solution.getObjective(0);
            int weight = 0;
            StringBuilder picked = new StringBuilder();
            for (int i = 0; i < var0.length; i++) {
                if (var0[i] == '1') {
                    picked.append(items[i]);
                    weight += KnapsackProblem.weights[i];
                    if (i != var0.length - 1) picked.append(", ");
                }
            }
            System.out.printf("Items: %s%nPoints: %s%nWeight: %skg(s)%n", picked, obj0, weight);

        }
    }
}
package org.orca.traffic;

import org.moeaframework.algorithm.NSGAII;
import org.moeaframework.core.Algorithm;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Problem;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.RealVariable;

public class Main {
    public static void main(String[] args) {
        Problem problem = new YagiAidProblem();
        Algorithm algorithm = new NSGAII(problem);
        algorithm.run(10000);
        NondominatedPopulation res = algorithm.getResult();
        int no = 1;
        for (Solution sol: res) {
            System.out.println("Solution " + no + ":");
            for (int i = 0; i < YagiAidProblem.ncity; i++) {
                System.out.printf("+ %s: %.2f billion%n", YagiAidProblem.city[i], ((RealVariable) sol.getVariable(i)).getValue());
            }
            System.out.printf("Over budget: %.2f%n", sol.getConstraint(0));
            System.out.printf("Satisfaction: %.2f%n", -sol.getObjective(0));
            System.out.printf("Dissatisfaction: %.2f%n", sol.getObjective(1));
            System.out.println();
            no++;
        }
    }
}

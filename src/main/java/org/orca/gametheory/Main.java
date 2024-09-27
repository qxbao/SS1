package org.orca.gametheory;

import org.moeaframework.algorithm.NSGAII;
import org.moeaframework.core.Algorithm;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Problem;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.RealVariable;

public class Main {
    public static void main(String[] args) {
        Problem problem = new MarketCompetitionProblem();
        Algorithm algorithm = new NSGAII(problem);
        algorithm.run(30000);
        NondominatedPopulation solutions = algorithm.getResult();
        int solindex = 1;
        for (Solution sol: solutions) {
            System.out.printf("Solution %s:%n", solindex++);
            double price = 100000;
            for (int i = 0; i < 30; i++) {
                double saleOff = ((RealVariable) sol.getVariable(i)).getValue();
                price -= price * saleOff;
                System.out.printf("+ Day %s: price = %s %n", i + 1, price);
            }
            System.out.printf("===> Total profit: %.3f%n", -sol.getObjective(0));
        }
    }
}

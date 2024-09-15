package knapsack;

import org.moeaframework.algorithm.NSGAII;
import org.moeaframework.core.Algorithm;
import org.moeaframework.core.Problem;

public class Main {
    public static void main(String[] args) {
        Problem problem = new KnapsackProblem();
        Algorithm algorithm = new NSGAII(problem);
        algorithm.run(10000);
        algorithm.getResult().display();
    }
}
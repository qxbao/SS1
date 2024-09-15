package org.orca.shortestpath;

import org.moeaframework.algorithm.NSGAII;
import org.moeaframework.core.Algorithm;
import org.moeaframework.core.Problem;

public class Main {
    public static void main(String[] args) {
        Problem problem = new ShortestPathProblem();
        Algorithm algorithm = new NSGAII(problem);
        algorithm.run(1000);
        algorithm.getResult().display();
    }
}

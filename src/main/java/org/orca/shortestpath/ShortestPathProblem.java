package org.orca.shortestpath;

import org.moeaframework.core.Problem;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.Permutation;

public class ShortestPathProblem implements Problem {
    protected int max = 10000;
    protected int loc = 5;
    protected int[][] path = {
            { max, 5, 6, 9, max},
            { 5, max, 10, 2, 7},
            { 6, 10, max, max, 15},
            { 9, 2, max, max, 1},
            { max, 7, 15, 1, max}
    };
    public ShortestPathProblem() {
        super();
    }
    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public int getNumberOfVariables() {
        return 1;
    }

    @Override
    public int getNumberOfObjectives() {
        return 1;
    }

    @Override
    public int getNumberOfConstraints() {
        return 1;
    }

    @Override
    public void evaluate(Solution solution) {
        int[] cleanSolution = ((Permutation) solution.getVariable(0)).toArray();
        int totalPath = 0;
        for (int i = 1; i < cleanSolution.length; i++) {
            int from = cleanSolution[i - 1], to = cleanSolution[i];
            totalPath += path[from][to];
        }
        int penalty = 0;
        if (cleanSolution[0] != 0)
            penalty = -1000;
        solution.setObjective(0, totalPath);
        solution.setConstraint(0, penalty);
    }

    @Override
    public Solution newSolution() {
        Solution solution = new Solution(this.getNumberOfVariables(), this.getNumberOfObjectives(), this.getNumberOfConstraints());
        solution.setVariable(0, new Permutation(loc));
        return solution;
    }

    @Override
    public void close() {

    }
}

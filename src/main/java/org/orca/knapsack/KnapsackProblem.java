package org.orca.knapsack;

import org.moeaframework.core.Problem;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;

public class KnapsackProblem implements Problem {
    protected final int nitems = 10;
    protected final int maxWeight = 50;
    protected static final String[] names = new String[]{"Sleeping bag", "Rope", "Knife", "Torch", "Bottle", "Noodles", "Sugar", "Tent", "Swimming Suit", "Umbrella"};
    protected static final int[] weights = new int[]{15, 3, 2, 5, 9, 20, 4, 15, 3, 8};
    protected final int[] points = new int[]{15, 7, 10, 5, 8, 17, 7, 19, 1, 10};

    public KnapsackProblem() {
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
        boolean[] chromosome = EncodingUtils.getBinary(solution.getVariable(0));
        int totalPoint = 0;
        int totalWeight = 0;

        for (int i = 0; i < nitems; i++) {
            if (chromosome[i]) {
                totalPoint += points[i];
                totalWeight += weights[i];
            }
        }

        if (totalWeight <= maxWeight)
            totalWeight = 0;
        else
            totalWeight = totalWeight - maxWeight;

        solution.setObjective(0, -totalPoint);
        solution.setConstraint(0, totalWeight);
    }

    @Override
    public Solution newSolution() {
        Solution solution = new Solution(this.getNumberOfVariables(), this.getNumberOfObjectives(), this.getNumberOfConstraints());
        solution.setVariable(0, EncodingUtils.newBinary(nitems));
        return solution;
    }

    @Override
    public void close() {

    }
}

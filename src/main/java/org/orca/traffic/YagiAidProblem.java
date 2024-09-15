package org.orca.traffic;

import org.moeaframework.core.Problem;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;
import org.moeaframework.core.variable.Permutation;
import org.moeaframework.core.variable.RealVariable;

public class TrafficProblem implements Problem {
    protected final double budget = 500;
    protected final int ncity = 6;
    protected final String[] city = new String[]{
            "Hanoi",  "Tuyen Quang", "Yen Bai", "Quang Ninh", "Hai  Phong", "Bac Ninh"
    };
    protected final int[] damage = new int[]{40, 180, 120, 84, 250, 170};
    protected final int[] citybudget = new int[]{200, 50, 80, 150, 150, 120};
    public TrafficProblem(){super();}

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public int getNumberOfVariables() {
        return ncity;
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
        double[] cleanSolution = new double[ncity];
        for (int i = 0; i < ncity; i++) {
            cleanSolution[i] = ((RealVariable) solution.getVariable(i)).getValue();
        }
        double satisfaction = 0;
        double spent = 0;
        double alpha;
        for (int i = 0; i < cleanSolution.length; i++) {
            spent += cleanSolution[i];
            alpha = (double) damage[i] /  citybudget[i];
            satisfaction += (damage[i] * (cleanSolution[i] / damage[i]) + Math.pow(damage[i], 1.2)) * alpha;
        }
        double overbudget = spent > budget ? Math.abs(budget - spent) : 0;
        solution.setObjective(0, -satisfaction);
        solution.setConstraint(0, overbudget);
    }

    @Override
    public Solution newSolution() {
        Solution solution = new Solution(this.getNumberOfVariables(), this.getNumberOfObjectives(), this.getNumberOfConstraints());
        for (int i = 0; i < ncity; i++) {
            solution.setVariable(i, new RealVariable(0, damage[i]));
        }
        return solution;
    }

    @Override
    public void close() {

    }
}
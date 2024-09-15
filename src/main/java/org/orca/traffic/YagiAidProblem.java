package org.orca.traffic;

import org.moeaframework.core.Problem;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.RealVariable;

public class YagiAidProblem implements Problem {
    protected final double fund = 500;
    protected static final int ncity = 6;
    protected static final String[] city = new String[]{
            "Hanoi",  "Tuyen Quang", "Yen Bai", "Quang Ninh", "Hai Phong", "Bac Ninh"
    };
    protected final int[] damage = new int[]{40, 180, 120, 84, 250, 170};
    protected final int[] citybudget = new int[]{200, 50, 80, 150, 150, 120};
    public YagiAidProblem(){super();}
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
        return 2;
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
        double dissatisfaction = 0;
        double spent = 0;
        for (int i = 0; i < cleanSolution.length; i++) {
            spent += cleanSolution[i];
            satisfaction += (cleanSolution[i] + damage[i]);
            if (citybudget[i] * 0.3 + cleanSolution[i] < damage[i]) {
                dissatisfaction += damage[i] - (citybudget[i] * 0.3 + cleanSolution[i]);
            }
        }
        double overbudget = spent > fund ? Math.abs(fund - spent) : 0;
        solution.setObjective(0, -satisfaction);
        solution.setObjective(1, dissatisfaction);
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
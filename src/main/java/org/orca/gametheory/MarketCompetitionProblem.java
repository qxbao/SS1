package org.orca.gametheory;

import org.moeaframework.core.Problem;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.RealVariable;

public class MarketCompetitionProblem implements Problem {
    private int nCustomer = 100;
    private int nday = 30;
    public MarketCompetitionProblem() {
        super();
    }
    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public int getNumberOfVariables() {
        return nday;
    }

    @Override
    public int getNumberOfObjectives() {
        return 1;
    }

    @Override
    public int getNumberOfConstraints() {
        return 0;
    }

    @Override
    public void evaluate(Solution solution) {
        // 3.000 guest in total, produced 2000 products
        double manuCost = 50000;
        double totalEarn = 0;
        double price = 100000, competitorPrice;
        for (int i = 0; i < nday; i++) {
            double saleOff = ((RealVariable) solution.getVariable(i)).getValue();
            competitorPrice = (price - 1000 < manuCost) ? manuCost + 1000 : price - 1000;
            price -= price * saleOff;
            int nSold;
            if (price < competitorPrice) {
                nSold = 75;
            } else if (price > competitorPrice) {
                nSold = 25;
            } else {
                nSold = 50;
            }
            totalEarn += nSold * (price - manuCost);
        }
        solution.setObjective(0, -totalEarn);
    }

    @Override
    public Solution newSolution() {
        Solution solution = new Solution(this.getNumberOfVariables(), this.getNumberOfObjectives());
        for (int i = 0; i < nday; i++) {
            solution.setVariable(i, new RealVariable(0, 1));
        }
        return solution;
    }

    @Override
    public void close() {

    }
}

package org.orca.gametheory;

import org.moeaframework.core.Problem;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.BinaryVariable;
import org.moeaframework.core.variable.EncodingUtils;

public class PrisonerDilemmaProblem implements Problem {
    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public int getNumberOfVariables() {
        return 2;
    }

    @Override
    public int getNumberOfObjectives() {
        return 2;
    }

    @Override
    public int getNumberOfConstraints() {
        return 0;
    }

    @Override
    public void evaluate(Solution solution) {
        // true = admitting to the crime, false = not
        boolean aDecision = EncodingUtils.getBinary(solution.getVariable(0))[0];
        boolean bDecision = EncodingUtils.getBinary(solution.getVariable(1))[0];
        int aSentence = 0, bSentence = 0;
        if (aDecision && bDecision) {
            aSentence = 24;
            bSentence = 24;
        } else if (aDecision && !bDecision) {
            bSentence = 120;
        } else if (!aDecision && bDecision) {
            aSentence = 120;
        }
        solution.setObjective(0, aSentence);
        solution.setObjective(1, bSentence);
    }

    @Override
    public Solution newSolution() {
        Solution solution = new Solution(this.getNumberOfVariables(), this.getNumberOfObjectives());
        solution.setVariable(0, new BinaryVariable(1));
        solution.setVariable(1, new BinaryVariable(1));
        return solution;
    }

    @Override
    public void close() {

    }
}

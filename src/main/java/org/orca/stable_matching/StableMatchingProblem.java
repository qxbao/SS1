package org.orca.stable_matching;

import org.moeaframework.core.Problem;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.Permutation;

public class StableMatchingProblem implements Problem {
    private final int n;
    private final int[][] menPreference, womenPreference;

    public int getN() {
        return n;
    }
    public int[][] getMenPreference() {
        return menPreference;
    }
    public int[][] getWomenPreference() {
        return womenPreference;
    }
    public StableMatchingProblem(int n, int[][] menPref, int[][] womenPref) {
        super();
        this.n = n;
        menPreference = menPref;
        womenPreference = womenPref;
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
        return 0;
    }

    @Override
    public void evaluate(Solution solution) {
        int[] cleanSolution = ((Permutation) solution.getVariable(0)).toArray();
        int unstability = calculateUnstability(cleanSolution);
        solution.setObjective(0, unstability);
    }

    private int calculateUnstability(int[] women) {
        int unstability = 0;
        for (int i = 0; i < n; i++) {
            int man1 = i, woman1 = women[i];
            for (int j = 0; j < n; j++) {
                if (j == i) continue;
                int man2 = j, woman2 = women[j];
                if (isUnfaithful(man1, woman1, man2, woman2)) {
                    unstability++;
                    break;
                }
            }
        }
        return unstability;
    }
    private boolean isUnfaithful(int m1, int w1, int m2, int w2) {
        boolean is1stPairUnfaithful = rankOf(w2, menPreference[m1]) < rankOf(w1, menPreference[m1])
                && rankOf(m1, womenPreference[w2]) < rankOf(m2, womenPreference[w2]);
        boolean is2ndPairUnfaithful = rankOf(w1, menPreference[m2]) < rankOf(w2, menPreference[m2])
                && rankOf(m2, womenPreference[w1]) < rankOf(m1, womenPreference[w1]);
        return is1stPairUnfaithful || is2ndPairUnfaithful;
    }
    private int rankOf(int entity, int[] pref) {
        for (int i = 0; i < pref.length; i++) {
            if (pref[i] == entity) return i;
        }
        throw new RuntimeException("Inputs (pref) are not valid.");
    }
    @Override
    public Solution newSolution() {
        Solution solution = new Solution(this.getNumberOfVariables(), this.getNumberOfObjectives(), this.getNumberOfConstraints());
        solution.setVariable(0, new Permutation(n));
        return solution;
    }

    @Override
    public void close() {

    }
}

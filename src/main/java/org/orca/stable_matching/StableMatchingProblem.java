package org.orca.stable_matching;

import org.moeaframework.core.Problem;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.Permutation;

public class StableMatchingProblem implements Problem {
    // Number of pairs
    private final int n;
    // Men & women preferences
    private final int[][] menPreference, womenPreference;

    // Getters
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
        /* 1 variable - an array with size n, stand for a list of woman who match with man-i (i: their index)
        *  Example: [1, 2, 0, 3]
        *  Means: (Man 0 - Woman 1), (Man 1 - Woman 2), (Man 2 - Woman 0), (Man 3, Woman 3)
        * */
        return 1;
    }

    @Override
    public int getNumberOfObjectives() {
        // 1 objective is to minimize the number of unstable pair to zero.
        return 1;
    }

    @Override
    public int getNumberOfConstraints() {
        return 0;
    }

    @Override
    public void evaluate(Solution solution) {
        // Format the solution
        int[] cleanSolution = ((Permutation) solution.getVariable(0)).toArray();
        // Calculate instability
        int instability = calculateInstability(cleanSolution);
        // Set objective (to minimize instability)
        solution.setObjective(0, instability);
    }

    private int calculateInstability(int[] women) {
        int instability = 0;
        // For each pair, compare with other pairs to see if any of them would cheat
        for (int i = 0; i < n; i++) {
            int man1 = i, woman1 = women[i];
            for (int j = 0; j < n; j++) {
                if (j == i) continue;
                int man2 = j, woman2 = women[j];
                if (isUnfaithful(man1, woman1, man2, woman2)) {
                    instability++;
                    break;
                }
            }
        }
        return instability;
    }
    private boolean isUnfaithful(int m1, int w1, int m2, int w2) {
        // Would m1 have an affair with w2?
        boolean is1stPairUnfaithful = rankOf(w2, menPreference[m1]) < rankOf(w1, menPreference[m1])
                && rankOf(m1, womenPreference[w2]) < rankOf(m2, womenPreference[w2]);
        // Would w1 have an affair with m2?
        boolean is2ndPairUnfaithful = rankOf(w1, menPreference[m2]) < rankOf(w2, menPreference[m2])
                && rankOf(m2, womenPreference[w1]) < rankOf(m1, womenPreference[w1]);
        return is1stPairUnfaithful || is2ndPairUnfaithful;
    }
    // Just a linear-search
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

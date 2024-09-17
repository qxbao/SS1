package org.orca.stable_matching;

import org.moeaframework.core.Problem;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.Permutation;

import java.util.Arrays;

public class StableMatchingProblem2 implements Problem {
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

    public StableMatchingProblem2(int n, int[][] menPref, int[][] womenPref) {
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
        // 1 objective is to maximize the satisfactory.
        return 1;
    }

    @Override
    public int getNumberOfConstraints() {
        return 0;
    }

    @Override
    public void evaluate(Solution solution) {
        // Format the solution
        int[] men = ((Permutation) solution.getVariable(0)).toArray();
        // Get the partner of all men using Gale Shapley algorithm
        int[] menPartner = GaleShapley(men);
        // Calculate the satisfactory
        int satisfactory = 0;
        for (int i = 0; i < n; i++) {
            satisfactory += getSatisfactory(i, menPartner[i]);
        }
        solution.setObjective(0, -satisfactory);
    }
    public int[] GaleShapley(int[] men) {
        int[] menPartner = new int[n];
        // All women and men are free at first
        Arrays.fill(menPartner, -1);
        boolean[] womanAvailable = new boolean[n];
        // Quantity of free man
        int nFreeWoman = n;
        // While there are free woman
        while (nFreeWoman > 0) {
            // Pick the first available woman
            int woman;
            for (woman = 0; woman < n; woman++) {
                if (!womanAvailable[woman]) {
                    break;
                }
            }
            // Let her take a look at all men, following her preference
            for (int i = 0; i < n && womanAvailable[woman] == false; i++) {
                int man = womenPreference[woman][i];
                // If the man she saw is single, they will date.
                if (menPartner[man] == -1) {
                    menPartner[man] = woman;
                    womanAvailable[woman] = true;
                    nFreeWoman--;
                }
                // If the man she saw has a partner
                else {
                    // She will find her
                    int woman1 = menPartner[man];
                    // And try to steal her boyfriend
                    if (mLikeWmore(man, woman, woman1)) {
                        menPartner[man] = woman;
                        womanAvailable[woman] = true;
                        womanAvailable[woman1] = false;
                    }
                }
            }
        }
        return menPartner;
    }

    // Find out if man m likes woman w more than w1, or not
    public boolean mLikeWmore(int m, int w, int w1) {
        return rankOf(w, menPreference[m]) < rankOf(w1, menPreference[m]);
    }
    // Get the satisfactory point for the couple of woman w and man m
    public int getSatisfactory(int m, int w) {
        return (n - rankOf(w, menPreference[m])) + (n - rankOf(m, womenPreference[w]));
    }
    // Just a linear-search
    private int rankOf(int entity, int[] pref) {
        for (int i = 0; i < n; i++) {
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
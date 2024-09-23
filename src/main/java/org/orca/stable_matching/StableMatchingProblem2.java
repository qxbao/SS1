package org.orca.stable_matching;

import org.moeaframework.core.Problem;
import org.moeaframework.core.Solution;
import org.moeaframework.core.Variable;
import org.moeaframework.core.variable.Permutation;
import org.moeaframework.core.variable.Subset;

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
        // 1 objective is to maximize the satisfaction.
        return 1;
    }

    @Override
    public int getNumberOfConstraints() {
        return 0;
    }

    @Override
    public void evaluate(Solution solution) {
        // Format the solution
        int[] order = ((Permutation) solution.getVariable(0)).toArray();
        int[] order2 = new int[n];
        for (int i = 0; i < n; i++) {
            order2[i] = order[i] + n;
        }
        // Get the partner of all men using Gale Shapley algorithm
        int[] partners = GaleShapley(order);
        int[] partners2 = GaleShapley(order2);
        // Calculate the overall satisfaction
        int s1 = getTotalSatisfaction(order, partners);
        int s2 = getTotalSatisfaction(order2, partners2);
        // Try to minimize it
        solution.setObjective(0, -(Math.max(s1, s2)));
    }
    public int[] GaleShapley(int[] input) {
        int[] partners = new int[n];
        boolean isWoman = input[0] >= n;
        int[][] apref, bpref;
        apref = isWoman ? womenPreference : menPreference;
        bpref = isWoman ? menPreference : womenPreference;
        int subtract = isWoman ? n : 0;
        int subtract1 = isWoman ? 0 : n;
        // All women and men are free at first
        Arrays.fill(partners, -1);
        boolean[] dated = new boolean[n];
        // Quantity of free man
        int freeleft = n;
        // While there are free woman
        while (freeleft > 0) {
            // Pick the first available woman
            int a = input[0];
            for (int i = 0; i < n; i++) {
                a = input[i];
                if (!dated[a - subtract]) {
                    break;
                }
            }
            // Let her take a look at all men, following her preference
            for (int i = 0; i < n && !dated[a - subtract]; i++) {
                int b = apref[a - subtract][i];
                // If the man she saw is single, they will date.
                if (partners[b - subtract1] == -1) {
                    partners[b - subtract1] = a;
                    dated[a - subtract] = true;
                    freeleft--;
                }
                // If the man she saw has a partner
                else {
                    // She will find her
                    int enemy = partners[b - subtract1];
                    // And try to steal her boyfriend
                    if (xLikeYMore(b, a, enemy)) {
                        partners[b - subtract1] = a;
                        dated[a - subtract] = true;
                        dated[enemy - subtract] = false;
                    }
                }
            }
        }
        return partners;
    }

    // Find out if man m likes woman w more than w1, or not
    public boolean xLikeYMore(int a, int b, int b1) {
        if (a < n) {
            for (int i = 0; i < n; i++) {
                if (menPreference[a][i] == b) return true;
                if (menPreference[a][i] == b1) return false;
            }
        }
        else {
            for (int i = 0; i < n; i++) {
                if (womenPreference[a - n][i] == b) return true;
                if (womenPreference[a - n][i] == b1) return false;
            }
        }
        throw new RuntimeException("Invalid preference");
    }
    // Get the satisfaction point of the couple of woman w and man m
    public int getSatisfaction(int a, int b) {
        return (n - rankOf(a, b)) + (n - rankOf(b, a));
    }
    public int getTotalSatisfaction(int[] order, int[] partners) {
        int satisfaction = 0;
        for (int i = 0; i < n; i++) {
            satisfaction += getSatisfaction(order[0] < n ? i + n : i, partners[i]);
        }
        return satisfaction;
    }
    // Just a linear-search
    private int rankOf(int a, int b) {
        if (a < n) {
            for (int i = 0; i < n; i++) {
                if (menPreference[a][i] == b) return i;
            }
        } else {
            for (int i = 0; i < n; i++) {
                if (womenPreference[a - n][i] == b) return i;
            }
        }
        throw new RuntimeException("Inputs (pref) are not valid." + a + b);
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
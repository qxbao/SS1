package org.orca.stable_matching;

import org.moeaframework.core.Problem;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.Permutation;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class StableMatching3 implements Problem {
    private int n;
    private int[][] pref;
    public StableMatching3(int n, int[][] pref) {
        this.n = n;
        this.pref = pref;
    }
    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public int getNumberOfVariables() {
        return n;
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
        int[] order = ((Permutation) solution.getVariable(0)).toArray();
        int[] partners = GaleShapley(order);
    }

    public int[] GaleShapley(int[] order) {
        int[] partners = new int[n];
        Arrays.fill(partners, -1);
        int[] bId = new int[n];
        HashSet inputSet = new HashSet<>(Arrays.asList(order));
        int idPointer = 0;
        for (int i = 0; i < n * 2; i++) {
            if (!inputSet.contains(i)) {
                bId[idPointer] = i;
                idPointer++;
            }
        }
        boolean[] dated = new boolean[n];
        int freeLeft = n;
        while (freeLeft > 0) {
            int subject = order[0];
            for (int i = 0; i < n; i++) {
                subject = order[i];
                if (dated[i] == false) break;
            }
            boolean iAmWoman = isWoman(subject);
            for (int i = 0; i < n && !dated[subject]; i++) {
                if (isWoman(bId[i]) == iAmWoman) continue;
                int object = bId[i];
                if (partners[object] == -1) {
                    partners[object] = subject;
                    dated[indexOf(order, i)] = true;
                    freeLeft--;
                } else {
                    int enemy;
                }
            }

        }
        return partners;
    }
    public int indexOf(int[] array, int tar) {
        for (int i = 0; i < array.length; i++) {
            if (tar == array[i]) return i;
        }
        throw new RuntimeException("Invalid input");
    }
    public boolean isWoman(int id) {
        return id >= n;
    }

    @Override
    public Solution newSolution() {
        Solution solution = new Solution(getNumberOfVariables(), getNumberOfObjectives());
        int[] input = new int[n * 2];
        for (int i = 0; i < n * 2; input[i++] = i);
        input = Arrays.copyOfRange(input, 0, n);
        Permutation permutation = new Permutation(n);
        permutation.fromArray(input);
        solution.setVariable(0, permutation);
        return solution;
    }

    @Override
    public void close() {

    }
}

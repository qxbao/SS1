package org.orca.stable_matching;

import org.moeaframework.core.Problem;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.Permutation;

import java.util.*;

public class StableMatchingProblem3 implements Problem {
    private final int n;
    private final int[][] preferences;
    public StableMatchingProblem3(int[][] prefs) {
        this.preferences = prefs;
        this.n = prefs.length;
        if (this.n % 2 == 1) {
            throw new RuntimeException("Input cannot be an odd number");
        }
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
        int[] order = ((Permutation) solution.getVariable(0)).toArray();
        List<Integer> partners = StableMatchingExtra(order);
        int satisfactionSum = calculateSatisfaction(partners);
        solution.setObjective(0, -satisfactionSum);
    }

    public List<Integer> StableMatchingExtra(int[] order) {
        Queue<Integer> singleQueue = new LinkedList<>();
        for(int node : order) singleQueue.add(node);
        List<Integer> partners = new ArrayList<>();
        for (int i = 0; i < n; i++) partners.add(-1);
        Set<Integer> matched = new HashSet<>();
        while(!singleQueue.isEmpty()){
            int a = singleQueue.poll();
            if (matched.contains(a)) continue;
            int[] aPreference = preferences[a];
            for (int b : aPreference) {
                if (partners.get(a) == b && partners.get(b) == a) break;
                if (partners.get(b) == -1) {
                    partners.set(a, b);
                    partners.set(b, a);
                    matched.add(b);
                    break;
                } else {
                    int bPartner = partners.get(b);
                    if (bLikeAMore(a, b, bPartner)) {
                        partners.set(b, -1);
                        partners.set(bPartner, -1);
                        matched.remove(bPartner);
                        singleQueue.add(bPartner);
                        partners.set(a, b);
                        partners.set(b, a);
                        matched.add(a);
                        break;
                    }
                }
            }
        }
        return partners;
    }
    public int calculateSatisfaction(List<Integer> list) {
        int totalSatisfaction = 0;
        Set<Integer> nodes = new HashSet<>();
        for (int a = 0; a < n; a++) {
            int b = list.get(a);
            if (nodes.contains(a) || nodes.contains(b)) continue;
            nodes.add(a); nodes.add(b);
            int rankA = findRank(a, b);
            int rankB = findRank(b, a);
            totalSatisfaction += preferences[b].length - rankA;
            totalSatisfaction += preferences[a].length - rankB;
        }
        return totalSatisfaction;
    }
    public boolean bLikeAMore(int a, int b, int c) {
        for (int individual : preferences[b]) {
            if (individual == a) return true;
            if (individual == c) return false;
        }
        throw new RuntimeException("The input (preference) have some problem: " + Arrays.toString(preferences[b]));
    }
    public int findRank(int target, int from) {
        for (int i = 0; i < preferences[from].length; i++) {
            if (preferences[from][i] == target) return i;
        }
        throw new RuntimeException("This should not happen");
    }

    @Override
    public Solution newSolution() {
        Solution solution = new Solution(this.getNumberOfVariables(), this.getNumberOfObjectives());
        solution.setVariable(0, new Permutation(n));
        return solution;
    }

    @Override
    public void close() {

    }
}

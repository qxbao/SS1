package org.orca.stable_matching;

import org.moeaframework.core.Problem;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.Permutation;

import java.util.*;

public class StableMatchingProblem implements Problem {
    private final int n;
    private final int[][] preferences;
    public StableMatchingProblem(int[][] prefs) {
        super();
        this.preferences = prefs;
        this.n = prefs.length;
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
        Matches matches = StableMatchingExtra(order);
        int satisfactionSum = calculateSatisfaction(matches);
        solution.setObjective(0, -satisfactionSum);
    }
    public Matches StableMatchingExtra(int[] order) {
        Queue<Integer> singleQueue = new LinkedList<>();
        for(int node : order) singleQueue.add(node);
        Matches matches = new Matches(n);
        int loop = 0;
        while(!singleQueue.isEmpty()){
            int a = singleQueue.poll();
            if (matches.isLinked(a)) continue;
            // Prevent infinite loop
            if (loop > 2 * n) return new Matches(0);
            int[] aPreference = preferences[a];
            int prefLen = aPreference.length;
            for (int i = 0; i < prefLen; i++) {
                int b = aPreference[i];
                if (matches.isLinkedWith(a, b)) break;
                if (!matches.isLinked(b)) {
                    matches.link(a, b);
                    break;
                } else {
                    int bPartner = matches.getPartner(b);
                    if (bLikeAMore(a, b, bPartner)) {
                        singleQueue.add(bPartner);
                        matches.relinkWith(b, bPartner, a);
                        break;
                    } else if (i == prefLen - 1) {
                        matches.addLeftover(a);
                    }
                }
            }
            loop++;
        }
        return matches;
    }
    public int calculateSatisfaction(Matches matches) {
        if (matches.isEmpty()) return -1;
        List<Integer> list = matches.getList();
        int totalSatisfaction = 0;
        Set<Integer> nodes = new HashSet<>();
        for (int a = 0; a < n; a++) {
            int b = list.get(a);
            if (b == -1 ) continue;
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
    public void close(){}
}

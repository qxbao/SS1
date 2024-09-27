package org.orca.stable_matching;

import org.moeaframework.core.Problem;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.Permutation;

import java.util.*;

public class StableMatchingProblem3 implements Problem {
    private final int n;
    private final int[][] prefs;
    public StableMatchingProblem3(int n, int[][] prefs) {
        this.n = n;
        this.prefs = prefs;
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
        List<Integer> matches = GaleShapley(order);
        int satisfaction = calculateSatisfaction(matches);
        solution.setObjective(0, -satisfaction);
    }

    public List<Integer> GaleShapley(int[] order) {
        Queue<Integer> free = new LinkedList<>();
        for(int node : order) free.add(node);
        List<Integer> matches = new ArrayList<>();
        for (int i = 0; i < n; i++) matches.add(-1);
        Set<Integer> matched = new HashSet<>();
        while(!free.isEmpty()){
            int a = free.poll();
            if (matched.contains(a)) continue;
            int[] apref = prefs[a];
            for (int i = 0; i < apref.length; i++) {
                int b = apref[i];
                if (matches.get(a) == b && matches.get(b) == a) break;
                if (matches.get(b) == -1) {
                    matches.set(a,b);
                    matches.set(b, a);
                    matched.add(b);
                    break;
                } else {
                    int bPartner = matches.get(b);
                    if (bLikeAMore(a, b, bPartner)) {
                        matches.set(b, -1);
                        matches.set(bPartner, -1);
                        matched.remove(bPartner);
                        free.add(bPartner);
                        matches.set(a,b);
                        matches.set(b, a);
                        matched.add(a);
                        break;
                    } else {
                        if (i == apref.length - 1) {
                            break;
                        }
                    }
                }
            }
        }
        return matches;
    }
    public int calculateSatisfaction(List<Integer> list) {
        int total = 0;
        Set<Integer> nodes = new HashSet<>();
        for (int a = 0; a < n; a++) {
            int b = list.get(a);
            if (nodes.contains(a) || nodes.contains(b)) continue;
            nodes.add(a);
            nodes.add(b);
            int rankA = findRank(a, b);
            int rankB = findRank(b, a);
            total += prefs[b].length - rankA;
            total += prefs[a].length - rankB;
        }
        return total;
    }
    public boolean bLikeAMore(int a, int b, int c) {
        for (int individual : prefs[b]) {
            if (individual == a) return true;
            if (individual == c) return false;
        }
        throw new RuntimeException("The input (preference) have some problem: " + Arrays.toString(prefs[b]));
    }
    public int findRank(int target, int from) {
        for (int i = 0; i < prefs[from].length; i++) {
            if (prefs[from][i] == target) return i;
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

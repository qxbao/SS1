package org.orca.stable_matching;

import java.util.*;

public class Matches {
    private final List<Integer> matches;
    private final Set<Integer> matched;
    private final Set<Integer> leftOver;
    public Matches(int n) {
        matches = new ArrayList<>();
        while (matches.size() < n) matches.add(-1);
        matched = new HashSet<>();
        leftOver = new HashSet<>();
    }
    public Set<Integer> getLeftOver() {
        return leftOver;
    }
    public void addLeftover(int target) {
        leftOver.add(target);
    }
    public List<Integer> getList() {
        return matches;
    }
    public boolean isEmpty() {return matches.isEmpty();}

    public void link(int a, int b) {
        matched.add(a);
        matched.add(b);
        matches.set(a, b);
        matches.set(b, a);
    }
    public void relinkWith(int a, int b, int interrupter) {
        matches.set(a, interrupter);
        matches.set(b, -1);
        matches.set(interrupter, a);
        matched.remove(b);
        matched.add(interrupter);
    }
    public boolean isLinkedWith(int a, int b) {
        if (!matched.contains(a) || !matched.contains(b)) return false;
        return matches.get(a) == b && matches.get(b) == a;
    }
    public boolean isLinked(int target) {
        return matched.contains(target);
    }
    public int getPartner(int target) {
        return matches.get(target);
    }

}
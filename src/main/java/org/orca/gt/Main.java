package org.orca.gt;

import org.moeaframework.core.Problem;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.RealVariable;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.Executor;

public class Main {
    public static void main(String[] args) {
        // Khởi tạo và chạy thuật toán NSGA-II
        NondominatedPopulation result = new Executor()
                .withAlgorithm("NSGAII")
                .withProblem(new GameTheoryProblem())
                .withMaxEvaluations(100)  // Số lần đánh giá tối đa
                .run();

        // Hiển thị kết quả
        System.out.println("Các giải pháp không bị chi phối (Pareto Optimal):");
        for (Solution solution : result) {
            System.out.println("Người chơi 1: " + ((RealVariable) solution.getVariable(0)).getValue());
            System.out.println("Người chơi 2: " + ((RealVariable) solution.getVariable(1)).getValue());
            System.out.println("Lợi ích người chơi 1: " + (-solution.getObjective(0)));
            System.out.println("Lợi ích người chơi 2: " + (-solution.getObjective(1)));
            System.out.println("--------------------------------------------");
        }
    }
}

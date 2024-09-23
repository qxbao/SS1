package org.orca.gt;

import org.moeaframework.core.Problem;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.RealVariable;

public class GameTheoryProblem implements Problem {

    @Override
    public String getName() {
        return "GameTheoryProblem";
    }

    @Override
    public int getNumberOfVariables() {
        return 2;  // Hai người chơi, mỗi người có một chiến lược
    }

    @Override
    public int getNumberOfObjectives() {
        return 2;  // Hai mục tiêu: tối đa hóa lợi ích cho người chơi 1 và người chơi 2
    }

    @Override
    public int getNumberOfConstraints() {
        return 0;  // Không có ràng buộc nào
    }

    @Override
    public void evaluate(Solution solution) {
        double x = ((RealVariable) solution.getVariable(0)).getValue();  // Chiến lược của người chơi 1
        double y = ((RealVariable) solution.getVariable(1)).getValue();  // Chiến lược của người chơi 2

        // Hàm lợi ích của người chơi 1
        double f1 = 3 * x + 2 * y - x * x - y * y;
        // Hàm lợi ích của người chơi 2
        double f2 = 2 * x + 3 * y - x * x - y * y;

        solution.setObjective(0, -f1);  // Tối đa hóa f1 (chuyển thành -f1 để thành vấn đề tối thiểu hóa)
        solution.setObjective(1, -f2);  // Tối đa hóa f2 (chuyển thành -f2 để thành vấn đề tối thiểu hóa)
    }

    @Override
    public Solution newSolution() {
        Solution solution = new Solution(2, 2);  // 2 biến quyết định (x, y), 2 mục tiêu (f1, f2)
        solution.setVariable(0, new RealVariable(-5.0, 5.0));  // Giới hạn chiến lược của người chơi 1 từ -5 đến 5
        solution.setVariable(1, new RealVariable(-5.0, 5.0));  // Giới hạn chiến lược của người chơi 2 từ -5 đến 5
        return solution;
    }

    @Override
    public void close() {
        // Không cần thực hiện gì nếu không có tài nguyên cần giải phóng
    }
}
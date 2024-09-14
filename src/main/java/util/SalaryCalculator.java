package util;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class SalaryCalculator {
    public static double calculateSalary(double baseSalary, double bonusPercent, double performanceFactor) {
        return baseSalary + (baseSalary * bonusPercent / 100) * performanceFactor;
    }

    public static void updatePerformanceFactor(int employeeId, double newPerformanceFactor) {
        try (Connection connection = DatabaseConnector.getConnection()) {
            String sql = "UPDATE employees SET performance_factor = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setDouble(1, newPerformanceFactor);
            preparedStatement.setInt(2, employeeId);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import model.Employee;

import java.io.IOException;
import java.sql.*;

public class MainController {
    @FXML
    private TableView<Employee> employeeTable;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/EmployeeManagement";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Humanbeing11!";
    @FXML
    private StackPane mainContainer;

    private EmployeeListController employeeListController;

    private void loadView(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            VBox view = loader.load();
            mainContainer.getChildren().clear();
            mainContainer.getChildren().add(view);

            // Get the controller to access its methods
            Object controller = loader.getController();
            if (controller instanceof EmployeeListController) {
                employeeListController = (EmployeeListController) controller;
                employeeListController.loadEmployeeData();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openEmployeeListView(ActionEvent actionEvent) {
        loadView("/fxml/EmployeeListView.fxml");
    }

    @FXML
    private void openEmployeeLeaveView(ActionEvent actionEvent) {
        loadView("/fxml/EmployeeLeaveView.fxml");
    }

    @FXML
    private void openEmployeeTaskView(ActionEvent actionEvent) {
        loadView("/fxml/EmployeeTaskView.fxml");
    }

    @FXML
    private void openManagerTaskView(ActionEvent actionEvent) {
        loadView("/fxml/ManagerTaskView.fxml");
    }

    @FXML
    private void openPerformanceManagementView(ActionEvent actionEvent) {
        loadView("/fxml/PerformanceManagementView.fxml");
    }

    @FXML
    private void openManagerLeaveFunctionsView(ActionEvent actionEvent) {
        loadView("/fxml/ManagerLeaveFunctionsView.fxml");
    }

    @FXML
    public void openEmployeeProfileView(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EmployeeProfileView.fxml"));
            VBox profileView = loader.load();

            EmployeeProfileController profileController = loader.getController();
            Employee selectedEmployee = employeeListController.getSelectedEmployee();

            if (selectedEmployee != null) {
                profileController.setEmployee(selectedEmployee);
                mainContainer.getChildren().clear();
                mainContainer.getChildren().add(profileView);
            } else {
                // Show alert if no employee is selected
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("No Selection");
                alert.setHeaderText("No Employee Selected");
                alert.setContentText("Please select an employee in the table.");
                alert.showAndWait();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Other existing methods...
    @FXML
    private void generateEmployeePerformanceSummaryReport(ActionEvent actionEvent) {
        String sql = "SELECT id, name, position, department, performance_factor " +
                "FROM employees";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            StringBuilder report = new StringBuilder();
            report.append("Employee Performance Summary Report\n");
            report.append("------------------------------------------------------------\n");
            while (resultSet.next()) {
                report.append("ID: ").append(resultSet.getInt("id")).append("\n");
                report.append("Name: ").append(resultSet.getString("name")).append("\n");
                report.append("Position: ").append(resultSet.getString("position")).append("\n");
                report.append("Department: ").append(resultSet.getString("department")).append("\n");
                report.append("Performance Factor: ").append(getPerformanceDescription(resultSet.getDouble("performance_factor"))).append("\n");
                report.append("------------------------------------------------------------\n");
            }

            showAlert("Employee Performance Summary Report", "Report Generated", report.toString());
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Employee Performance Summary Report", "Error", "Failed to generate report.");
        }
    }

    private String getPerformanceDescription(double performanceFactor) {
        if (performanceFactor >= 1.8) {
            return "Very Good";
        } else if (performanceFactor >= 1.4) {
            return "Good";
        } else if (performanceFactor >= 1.0) {
            return "Average";
        } else if (performanceFactor >= 0.6) {
            return "Below Average";
        } else {
            return "Poor";
        }
    }

    @FXML
    private void generateLeaveManagementAnalysisReport(ActionEvent actionEvent) {
        String sql = "SELECT lr.id, lr.employee_id, e.name, e.department, lr.start_date, lr.end_date, lr.status " +
                "FROM leave_requests lr " +
                "JOIN employees e ON lr.employee_id = e.id";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            StringBuilder report = new StringBuilder();
            report.append("Leave Management Analysis Report\n");
            report.append("------------------------------------------------------------\n");
            while (resultSet.next()) {
                report.append("Leave ID: ").append(resultSet.getInt("lr.id")).append("\n");
                report.append("Employee ID: ").append(resultSet.getInt("lr.employee_id")).append("\n");
                report.append("Employee Name: ").append(resultSet.getString("e.name")).append("\n");
                report.append("Department: ").append(resultSet.getString("e.department")).append("\n");
                report.append("Start Date: ").append(resultSet.getDate("lr.start_date")).append("\n");
                report.append("End Date: ").append(resultSet.getDate("lr.end_date")).append("\n");
                report.append("Status: ").append(resultSet.getString("lr.status")).append("\n");
                report.append("------------------------------------------------------------\n");
            }

            showAlert("Leave Management Analysis Report", "Report Generated", report.toString());
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Leave Management Analysis Report", "Error", "Failed to generate report.");
        }
    }
    @FXML
    private void generateMonthlyPayrollReport(ActionEvent actionEvent) {
        String query = "SELECT e.id, e.name, e.position, e.department, " +
                "e.base_salary, e.performance_factor, " +
                "(SELECT COUNT(*) FROM tasks t WHERE t.employee_id = e.id AND t.completion_status = 'Completed') AS tasks_completed, " +
                "(SELECT COUNT(*) FROM leave_requests lr WHERE lr.employee_id = e.id AND lr.status = 'Approved' AND lr.end_date >= DATE_SUB(CURDATE(), INTERVAL 1 MONTH)) AS leaves_approved " +
                "FROM employees e";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            StringBuilder report = new StringBuilder();
            report.append("Monthly Payroll Report\n\n");

            while (resultSet.next()) {
                int employeeId = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String position = resultSet.getString("position");
                String department = resultSet.getString("department");
                double baseSalary = resultSet.getDouble("base_salary");
                double performanceFactor = resultSet.getDouble("performance_factor");
                int tasksCompleted = resultSet.getInt("tasks_completed");
                int leavesApproved = resultSet.getInt("leaves_approved");

                double performanceBonus = baseSalary * (performanceFactor - 1.0); // assuming performance factor ranges from 1.0 to 2.0
                double taskBonus = tasksCompleted * 100; // assuming a fixed bonus per task
                double leaveDeduction = (leavesApproved > 5) ? (leavesApproved - 5) * 50 : 0; // deducting for leaves beyond 5

                double totalPay = baseSalary + performanceBonus + taskBonus - leaveDeduction;

                report.append(String.format("Employee ID: %d\nName: %s\nPosition: %s\nDepartment: %s\n", employeeId, name, position, department));
                report.append(String.format("Base Salary: %.2f\nPerformance Bonus: %.2f\nTask Bonus: %.2f\nLeave Deduction: %.2f\n", baseSalary, performanceBonus, taskBonus, leaveDeduction));
                report.append(String.format("Total Pay: %.2f\n\n", totalPay));
            }

            // Display report
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Monthly Payroll Report");
            alert.setHeaderText("Report Generated Successfully");
            alert.setContentText(report.toString());
            alert.showAndWait();

        } catch (SQLException e) {
            e.printStackTrace();
            showErrorDialog("Monthly Payroll Report", "Failed to generate report.");
        }
    }

    @FXML
    private void generateTaskCompletionEfficiencyReport(ActionEvent actionEvent) {
        String sql = "SELECT e.id, e.name, e.position, e.department, COUNT(t.id) as task_count, " +
                "SUM(CASE WHEN t.completion_status = 'Completed' THEN 1 ELSE 0 END) as completed_tasks, " +
                "SUM(CASE WHEN t.completion_status = 'Not Completed' THEN 1 ELSE 0 END) as pending_tasks " +
                "FROM employees e " +
                "LEFT JOIN tasks t ON e.id = t.employee_id " +
                "GROUP BY e.id, e.name, e.position, e.department";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            StringBuilder report = new StringBuilder();
            report.append("Task Completion Efficiency Report\n");
            report.append("------------------------------------------------------------\n");
            while (resultSet.next()) {
                report.append("ID: ").append(resultSet.getInt("id")).append("\n");
                report.append("Name: ").append(resultSet.getString("name")).append("\n");
                report.append("Position: ").append(resultSet.getString("position")).append("\n");
                report.append("Department: ").append(resultSet.getString("department")).append("\n");
                report.append("Total Tasks: ").append(resultSet.getInt("task_count")).append("\n");
                report.append("Completed Tasks: ").append(resultSet.getInt("completed_tasks")).append("\n");
                report.append("Pending Tasks: ").append(resultSet.getInt("pending_tasks")).append("\n");
                report.append("------------------------------------------------------------\n");
            }

            showAlert("Task Completion Efficiency Report", "Report Generated", report.toString());
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Task Completion Efficiency Report", "Error", "Failed to generate report.");
        }
    }
    @FXML
    private void generateDepartmentalProductivityReport(ActionEvent actionEvent) {
        String sql = "SELECT e.department, COUNT(e.id) as employee_count, AVG(e.performance_factor) as avg_performance, " +
                "COUNT(t.id) as total_tasks, SUM(CASE WHEN t.completion_status = 'Completed' THEN 1 ELSE 0 END) as completed_tasks " +
                "FROM employees e " +
                "LEFT JOIN tasks t ON e.id = t.employee_id " +
                "GROUP BY e.department";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            StringBuilder report = new StringBuilder();
            report.append("Departmental Productivity Report\n");
            report.append("------------------------------------------------------------\n");
            while (resultSet.next()) {
                report.append("Department: ").append(resultSet.getString("department")).append("\n");
                report.append("Number of Employees: ").append(resultSet.getInt("employee_count")).append("\n");
                report.append("Average Performance Factor: ").append(resultSet.getDouble("avg_performance")).append("\n");
                report.append("Total Tasks Assigned: ").append(resultSet.getInt("total_tasks")).append("\n");
                report.append("Tasks Completed: ").append(resultSet.getInt("completed_tasks")).append("\n");
                report.append("------------------------------------------------------------\n");
            }

            showAlert("Departmental Productivity Report", "Report Generated", report.toString());
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Departmental Productivity Report", "Error", "Failed to generate report.");
        }
    }

    @FXML
    private void viewEmployeePayrollDetails(ActionEvent actionEvent) {
        if (employeeListController == null) {
            showErrorDialog("View Employee Payroll Details", "No employee selected.");
            return;
        }
        Employee selectedEmployee = employeeListController.getSelectedEmployee();
        if (selectedEmployee == null) {
            showErrorDialog("View Employee Payroll Details", "No employee selected.");
            return;
        }

        String query = "SELECT e.id, e.name, e.position, e.department, " +
                "e.base_salary, e.performance_factor, " +
                "(SELECT COUNT(*) FROM tasks t WHERE t.employee_id = e.id AND t.completion_status = 'Completed') AS tasks_completed, " +
                "(SELECT COUNT(*) FROM leave_requests lr WHERE lr.employee_id = e.id AND lr.status = 'Approved' AND lr.end_date >= DATE_SUB(CURDATE(), INTERVAL 1 MONTH)) AS leaves_approved " +
                "FROM employees e WHERE e.id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, selectedEmployee.getId());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int employeeId = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String position = resultSet.getString("position");
                String department = resultSet.getString("department");
                double baseSalary = resultSet.getDouble("base_salary");
                double performanceFactor = resultSet.getDouble("performance_factor");
                int tasksCompleted = resultSet.getInt("tasks_completed");
                int leavesApproved = resultSet.getInt("leaves_approved");

                double performanceBonus = baseSalary * (performanceFactor - 1.0); // assuming performance factor ranges from 1.0 to 2.0
                double taskBonus = tasksCompleted * 100; // assuming a fixed bonus per task
                double leaveDeduction = (leavesApproved > 5) ? (leavesApproved - 5) * 50 : 0; // deducting for leaves beyond 5

                double totalPay = baseSalary + performanceBonus + taskBonus - leaveDeduction;

                StringBuilder payrollDetails = new StringBuilder();
                payrollDetails.append(String.format("Employee ID: %d\nName: %s\nPosition: %s\nDepartment: %s\n", employeeId, name, position, department));
                payrollDetails.append(String.format("Base Salary: %.2f\nPerformance Bonus: %.2f\nTask Bonus: %.2f\nLeave Deduction: %.2f\n", baseSalary, performanceBonus, taskBonus, leaveDeduction));
                payrollDetails.append(String.format("Total Pay: %.2f\n", totalPay));

                // Display details
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Employee Payroll Details");
                alert.setHeaderText("Payroll Details for " + name);
                alert.setContentText(payrollDetails.toString());
                alert.showAndWait();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showErrorDialog("View Employee Payroll Details", "Failed to retrieve payroll details.");
        }
    }

    private void showAlert(String title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    private void showErrorDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText("Error");
        alert.setContentText(content);
        alert.showAndWait();
    }
}

package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import model.Employee;

public class EmployeeProfileController {

    @FXML
    private Label idLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private Label positionLabel;

    @FXML
    private Label departmentLabel;

    @FXML
    private Label performanceFactorLabel;

    // Other labels for tasks and leave requests as needed


    public void setEmployee(Employee employee) {
        idLabel.setText(String.valueOf(employee.getId()));
        nameLabel.setText(employee.getName());
        positionLabel.setText(employee.getPosition());
        departmentLabel.setText(employee.getDepartment());
        performanceFactorLabel.setText(getPerformanceDescription(employee.getPerformanceFactor()));

        // Load additional information such as tasks and leave requests
        loadEmployeeTasks(employee.getId());
        loadEmployeeLeaveRequests(employee.getId());
    }

    private String getPerformanceDescription(double performanceFactor) {
        if (performanceFactor >= 1.5) {
            return "Very Good";
        } else if (performanceFactor >= 1.0) {
            return "Good";
        } else if (performanceFactor >= 0.5) {
            return "Average";
        } else if (performanceFactor > 0.0) {
            return "Bad";
        } else {
            return "Very Bad";
        }
    }

    private void loadEmployeeTasks(int employeeId) {
        // Implement method to load tasks from the database
    }

    private void loadEmployeeLeaveRequests(int employeeId) {
        // Implement method to load leave requests from the database
    }
}

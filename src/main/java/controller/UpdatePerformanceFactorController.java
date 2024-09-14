package controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import model.Employee;

public class UpdatePerformanceFactorController {

    @FXML
    private TextField performanceFactorField;

    private Employee employee;
    private HRManagerController hrManagerController;

    public void setHRManagerController(HRManagerController hrManagerController) {
        this.hrManagerController = hrManagerController;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
        performanceFactorField.setText(String.valueOf(employee.getPerformanceFactor()));
    }

    @FXML
    private void handleUpdatePerformanceFactor() {
        double performanceFactor = Double.parseDouble(performanceFactorField.getText());
        employee.setPerformanceFactor(performanceFactor);
        hrManagerController.updatePerformanceFactor(employee);
    }
}

package controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Employee;

public class UpdateEmployeeController {

    @FXML
    private TextField nameField;
    @FXML
    private TextField positionField;
    @FXML
    private TextField departmentField;
    @FXML
    private TextField baseSalaryField;
    @FXML
    private TextField bonusPercentField;
    @FXML
    private TextField performanceFactorField;

    private Employee employee;

    public void setEmployee(Employee employee) {
        this.employee = employee;
        nameField.setText(employee.getName());
        positionField.setText(employee.getPosition());
        departmentField.setText(employee.getDepartment());
        baseSalaryField.setText(String.valueOf(employee.getBaseSalary()));
        bonusPercentField.setText(String.valueOf(employee.getBonusPercent()));
        performanceFactorField.setText(String.valueOf(employee.getPerformanceFactor()));
    }

    @FXML
    private void handleUpdateEmployee() {
        if (employee != null) {
            employee.setName(nameField.getText());
            employee.setPosition(positionField.getText());
            employee.setDepartment(departmentField.getText());
            employee.setBaseSalary(Double.parseDouble(baseSalaryField.getText()));
            employee.setBonusPercent(Double.parseDouble(bonusPercentField.getText()));
            employee.setPerformanceFactor(Double.parseDouble(performanceFactorField.getText()));
            ((Stage) nameField.getScene().getWindow()).close();
        }
    }
}

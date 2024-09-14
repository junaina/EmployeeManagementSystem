package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Employee;

public class AddEmployeeDialogController {

    @FXML
    private TextField nameField;
    @FXML
    private TextField positionField;
    @FXML
    private TextField departmentField;
    @FXML
    private TextField performanceFactorField;

    private Stage dialogStage;
    private boolean okClicked = false;
    private Employee employee;

    @FXML
    private void initialize() {
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    public Employee getEmployee() {
        return employee;
    }

    @FXML
    private void handleOk() {
        if (isInputValid()) {
            employee = new Employee();
            employee.setName(nameField.getText());
            employee.setPosition(positionField.getText());
            employee.setDepartment(departmentField.getText());
            employee.setPerformanceFactor(Double.parseDouble(performanceFactorField.getText()));

            okClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (nameField.getText() == null || nameField.getText().isEmpty()) {
            errorMessage += "No valid name!\n";
        }
        if (positionField.getText() == null || positionField.getText().isEmpty()) {
            errorMessage += "No valid position!\n";
        }
        if (departmentField.getText() == null || departmentField.getText().isEmpty()) {
            errorMessage += "No valid department!\n";
        }
        if (performanceFactorField.getText() == null || performanceFactorField.getText().isEmpty()) {
            errorMessage += "No valid performance factor!\n";
        } else {
            try {
                Double.parseDouble(performanceFactorField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "No valid performance factor (must be a number)!\n";
            }
        }

        if (errorMessage.isEmpty()) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }
    }
}

package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Employee;

public class AddEmployeeController {
    @FXML
    private TextField nameField;
    @FXML
    private TextField positionField;
    @FXML
    private TextField departmentField;

    @FXML
    private void handleSave() {
        String name = nameField.getText();
        String position = positionField.getText();
        String department = departmentField.getText();

        if (name.isEmpty() || position.isEmpty() || department.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter all fields");
            return;
        }

        Employee newEmployee = new Employee(name, position, department, 1.0); // Ensure this matches one of the constructors

        // Here you would save the new employee to the database and refresh the employee list
        // ...

        ((Stage) nameField.getScene().getWindow()).close();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}

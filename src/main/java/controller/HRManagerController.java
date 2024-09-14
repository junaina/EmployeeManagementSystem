package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import model.Employee;
import util.DatabaseConnector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HRManagerController {

    @FXML
    private TableView<Employee> employeeTable;

    @FXML
    private TableColumn<Employee, Integer> idColumn;
    @FXML
    private TableColumn<Employee, String> nameColumn;
    @FXML
    private TableColumn<Employee, String> positionColumn;
    @FXML
    private TableColumn<Employee, String> departmentColumn;
    @FXML
    private TableColumn<Employee, Double> baseSalaryColumn;
    @FXML
    private TableColumn<Employee, Double> bonusPercentColumn;
    @FXML
    private TableColumn<Employee, Double> performanceFactorColumn;

    @FXML
    private Button removeEmployeeButton;
    @FXML
    private Button updateEmployeeButton;
    @FXML
    private Button updatePerformanceFactorButton;

    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        positionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));
        departmentColumn.setCellValueFactory(new PropertyValueFactory<>("department"));
        baseSalaryColumn.setCellValueFactory(new PropertyValueFactory<>("baseSalary"));
        bonusPercentColumn.setCellValueFactory(new PropertyValueFactory<>("bonusPercent"));
        performanceFactorColumn.setCellValueFactory(new PropertyValueFactory<>("performanceFactor"));

        loadEmployees();
    }

    public void loadEmployees() {
        employeeTable.getItems().clear();
        try (Connection connection = DatabaseConnector.getConnection()) {
            String sql = "SELECT * FROM employees";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Employee employee = new Employee(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("position"),
                        resultSet.getString("department"),
                        resultSet.getDouble("base_salary"),
                        resultSet.getDouble("bonus_percent"),
                        resultSet.getDouble("performance_factor")
                );
                employeeTable.getItems().add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleAddEmployee() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AddEmployeeView.fxml"));
            Parent root = loader.load();
            AddEmployeeController controller = loader.getController();
            Stage stage = new Stage();
            stage.setTitle("Add Employee");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleRemoveEmployee() {
        Employee selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null) {
            try (Connection connection = DatabaseConnector.getConnection()) {
                String sql = "DELETE FROM employees WHERE id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, selectedEmployee.getId());
                preparedStatement.executeUpdate();
                loadEmployees(); // Refresh the table
                showAlert(Alert.AlertType.INFORMATION, "Success", "Employee removed successfully!");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Could not remove employee from the database");
                e.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an employee to remove.");
        }
    }

    public void handleUpdateEmployee() {
        Employee selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/UpdateEmployeeView.fxml"));
                Parent root = loader.load();
                UpdateEmployeeController controller = loader.getController();
                controller.setEmployee(selectedEmployee); // Pass the selected employee
                Stage stage = new Stage();
                stage.setTitle("Update Employee");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an employee to update.");
        }
    }

    public void handleUpdatePerformanceFactor() {
        Employee selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/UpdatePerformanceFactorView.fxml"));
                Parent root = loader.load();
                UpdatePerformanceFactorController controller = loader.getController();
                controller.setHRManagerController(this); // Set reference
                controller.setEmployee(selectedEmployee); // Pass the selected employee
                Stage stage = new Stage();
                stage.setTitle("Update Performance Factor");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an employee to update the performance factor.");
        }
    }

    public void updateEmployee(Employee employee) {
        try (Connection connection = DatabaseConnector.getConnection()) {
            String sql = "UPDATE employees SET name = ?, position = ?, department = ?, base_salary = ?, bonus_percent = ?, performance_factor = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, employee.getName());
            preparedStatement.setString(2, employee.getPosition());
            preparedStatement.setString(3, employee.getDepartment());
            preparedStatement.setDouble(4, employee.getBaseSalary());
            preparedStatement.setDouble(5, employee.getBonusPercent());
            preparedStatement.setDouble(6, employee.getPerformanceFactor());
            preparedStatement.setInt(7, employee.getId());
            preparedStatement.executeUpdate();
            loadEmployees(); // Refresh the table
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePerformanceFactor(Employee employee) {
        try (Connection connection = DatabaseConnector.getConnection()) {
            String sql = "UPDATE employees SET performance_factor = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setDouble(1, employee.getPerformanceFactor());
            preparedStatement.setInt(2, employee.getId());
            preparedStatement.executeUpdate();
            loadEmployees(); // Refresh the table
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import model.Employee;
import util.DatabaseConnector;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class PerformanceManagementController implements Initializable {
    @FXML
    private ComboBox<String> performanceFactorComboBox;
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
    private TableColumn<Employee, Double> performanceFactorColumn;
    @FXML
    private ComboBox<Employee> employeeComboBox;

    private ObservableList<Employee> employeeData = FXCollections.observableArrayList();
    private Map<String, Double> performanceFactorMap = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize the performance factor map
        performanceFactorMap.put("Very Good", 2.0);
        performanceFactorMap.put("Good", 1.5);
        performanceFactorMap.put("Neutral", 1.0);
        performanceFactorMap.put("Bad", 0.5);
        performanceFactorMap.put("Very Bad", 0.0);

        // Set up the performance factor ComboBox
        performanceFactorComboBox.setItems(FXCollections.observableArrayList(
                "Very Good", "Good", "Neutral", "Bad", "Very Bad"
        ));

        // Set up the table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        positionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));
        departmentColumn.setCellValueFactory(new PropertyValueFactory<>("department"));
        performanceFactorColumn.setCellValueFactory(new PropertyValueFactory<>("performanceFactor"));

        loadEmployees();

        employeeComboBox.setItems(employeeData);
        employeeComboBox.setCellFactory(new Callback<ListView<Employee>, ListCell<Employee>>() {
            @Override
            public ListCell<Employee> call(ListView<Employee> p) {
                return new ListCell<Employee>() {
                    @Override
                    protected void updateItem(Employee item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null && !empty) {
                            setText(item.getId() + " - " + item.getName());
                        } else {
                            setText(null);
                        }
                    }
                };
            }
        });

        employeeComboBox.setButtonCell(new ListCell<Employee>() {
            @Override
            protected void updateItem(Employee item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null && !empty) {
                    setText(item.getId() + " - " + item.getName());
                } else {
                    setText(null);
                }
            }
        });
    }

    private void loadEmployees() {
        String sql = "SELECT id, name, position, department, performance_factor FROM employees";

        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            employeeData.clear();
            while (resultSet.next()) {
                Employee employee = new Employee(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("position"),
                        resultSet.getString("department"),
                        resultSet.getDouble("performance_factor")
                );
                employeeData.add(employee);
            }
            employeeTable.setItems(employeeData);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load employees.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleUpdatePerformance() {
        Employee selectedEmployee = employeeComboBox.getSelectionModel().getSelectedItem();
        if (selectedEmployee == null) {
            showAlert(Alert.AlertType.WARNING, "Update Performance", "No employee selected.");
            return;
        }

        String performanceFactorStr = performanceFactorComboBox.getValue();
        if (performanceFactorStr == null) {
            showAlert(Alert.AlertType.WARNING, "Invalid Input", "Please select a performance factor.");
            return;
        }

        double performanceFactor = performanceFactorMap.get(performanceFactorStr);

        String sql = "UPDATE employees SET performance_factor = ? WHERE id = ?";

        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setDouble(1, performanceFactor);
            statement.setInt(2, selectedEmployee.getId());

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Performance factor updated successfully.");
                loadEmployees(); // Refresh the table view
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to update performance factor.");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update performance factor.");
        }
    }
}

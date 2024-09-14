package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Task;

import java.sql.*;

public class EmployeeTaskController {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/EmployeeManagement";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Humanbeing11!";

    @FXML
    private TableView<Task> taskTable;

    @FXML
    private TableColumn<Task, Integer> taskIdColumn;

    @FXML
    private TableColumn<Task, Integer> employeeIdColumn;

    @FXML
    private TableColumn<Task, String> taskDescriptionColumn;

    @FXML
    private TableColumn<Task, String> completionStatusColumn;

    @FXML
    private ComboBox<String> employeeFilterComboBox;

    @FXML
    private ComboBox<String> statusComboBox;

    private ObservableList<Task> taskData;

    @FXML
    private void initialize() {
        taskIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        employeeIdColumn.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        taskDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("taskDescription"));
        completionStatusColumn.setCellValueFactory(new PropertyValueFactory<>("completionStatus"));

        taskData = FXCollections.observableArrayList();
        taskTable.setItems(taskData);

        loadEmployees();
        statusComboBox.setItems(FXCollections.observableArrayList("Not Started", "In Progress", "Completed"));
    }

    private void loadEmployees() {
        String sql = "SELECT id, name FROM employees";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            ObservableList<String> employees = FXCollections.observableArrayList();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                employees.add(id + " - " + name); // Adding id and name to distinguish between employees with the same name
            }
            employeeFilterComboBox.setItems(employees);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Load Employees", "Failed to load employees.");
        }
    }

    @FXML
    private void filterTasksByEmployee() {
        String selectedEmployee = employeeFilterComboBox.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null) {
            int employeeId = Integer.parseInt(selectedEmployee.split(" - ")[0]); // Extracting id from the selected item
            loadTasks(employeeId);
        }
    }

    private void loadTasks(int employeeId) {
        String sql = "SELECT * FROM tasks WHERE employee_id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, employeeId);
            ResultSet resultSet = preparedStatement.executeQuery();

            taskData.clear();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String taskDescription = resultSet.getString("task_description");
                String completionStatus = resultSet.getString("completion_status");

                Task task = new Task(id, employeeId, taskDescription, completionStatus);
                taskData.add(task);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Load Tasks", "Failed to load tasks.");
        }
    }

    @FXML
    private void handleUpdateStatus() {
        Task selectedTask = taskTable.getSelectionModel().getSelectedItem();
        if (selectedTask == null) {
            showAlert(Alert.AlertType.WARNING, "Update Status", "No task selected.");
            return;
        }

        String newStatus = statusComboBox.getSelectionModel().getSelectedItem();
        if (newStatus == null) {
            showAlert(Alert.AlertType.WARNING, "Update Status", "No status selected.");
            return;
        }

        String sql = "UPDATE tasks SET completion_status = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, newStatus);
            preparedStatement.setInt(2, selectedTask.getId());
            preparedStatement.executeUpdate();

            showAlert(Alert.AlertType.INFORMATION, "Update Status", "Task status updated successfully.");
            filterTasksByEmployee(); // Refresh the table after updating a task
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Update Status", "Failed to update task status.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

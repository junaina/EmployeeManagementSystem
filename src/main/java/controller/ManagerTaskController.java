package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Task;

import java.sql.*;

public class ManagerTaskController {

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
    private TextField taskDescriptionField;

    @FXML
    private TextField employeeIdField;

    private ObservableList<Task> taskData;

    @FXML
    private void initialize() {
        taskIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        employeeIdColumn.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        taskDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("taskDescription"));
        completionStatusColumn.setCellValueFactory(new PropertyValueFactory<>("completionStatus"));

        taskData = FXCollections.observableArrayList();
        taskTable.setItems(taskData);

        loadTasks();
    }

    private void loadTasks() {
        String sql = "SELECT * FROM tasks";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            taskData.clear();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int employeeId = resultSet.getInt("employee_id");
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
    private void handleAssignTask() {
        String taskDescription = taskDescriptionField.getText();
        int employeeId = Integer.parseInt(employeeIdField.getText());

        String sql = "INSERT INTO tasks (employee_id, task_description) VALUES (?, ?)";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, employeeId);
            preparedStatement.setString(2, taskDescription);
            preparedStatement.executeUpdate();

            showAlert(Alert.AlertType.INFORMATION, "Assign Task", "Task assigned successfully.");
            loadTasks();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Assign Task", "Failed to assign task.");
        }
    }

    @FXML
    private void handleUpdateTask() {
        Task selectedTask = taskTable.getSelectionModel().getSelectedItem();
        if (selectedTask == null) {
            showAlert(Alert.AlertType.WARNING, "Update Task", "No task selected.");
            return;
        }

        String newTaskDescription = taskDescriptionField.getText();

        String sql = "UPDATE tasks SET task_description = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, newTaskDescription);
            preparedStatement.setInt(2, selectedTask.getId());
            preparedStatement.executeUpdate();

            showAlert(Alert.AlertType.INFORMATION, "Update Task", "Task updated successfully.");
            loadTasks();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Update Task", "Failed to update task.");
        }
    }

    @FXML
    private void handleDeleteTask() {
        Task selectedTask = taskTable.getSelectionModel().getSelectedItem();
        if (selectedTask == null) {
            showAlert(Alert.AlertType.WARNING, "Delete Task", "No task selected.");
            return;
        }

        String sql = "DELETE FROM tasks WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, selectedTask.getId());
            preparedStatement.executeUpdate();

            showAlert(Alert.AlertType.INFORMATION, "Delete Task", "Task deleted successfully.");
            loadTasks();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Delete Task", "Failed to delete task.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

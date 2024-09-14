package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import model.Employee;
import model.LeaveRequest;
import util.DatabaseConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class EmployeeLeaveController {

    @FXML
    private ComboBox<Employee> employeeComboBox;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private TableView<LeaveRequest> leaveRequestTable;

    @FXML
    private TableColumn<LeaveRequest, Integer> idColumn;

    @FXML
    private TableColumn<LeaveRequest, Integer> employeeIdColumn;

    @FXML
    private TableColumn<LeaveRequest, String> employeeNameColumn;

    @FXML
    private TableColumn<LeaveRequest, LocalDate> startDateColumn;

    @FXML
    private TableColumn<LeaveRequest, LocalDate> endDateColumn;

    @FXML
    private TableColumn<LeaveRequest, String> statusColumn;

    @FXML
    private void initialize() {
        loadEmployees();
        configureTable();

        // Set custom cell factory to display employee names
        employeeComboBox.setCellFactory(comboBox -> new ListCell<>() {
            @Override
            protected void updateItem(Employee item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });

        // Set converter to display selected employee's name
        employeeComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Employee employee) {
                return employee != null ? employee.getName() : "";
            }

            @Override
            public Employee fromString(String string) {
                return employeeComboBox.getItems().stream()
                        .filter(emp -> emp.getName().equals(string))
                        .findFirst().orElse(null);
            }
        });

        // Add listener to load leave requests when an employee is selected
        employeeComboBox.setOnAction(event -> loadLeaveRequests());
    }

    @FXML
    private void loadEmployees() {
        try (Connection connection = DatabaseConnector.getConnection()) {
            String sql = "SELECT id, name FROM employees";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            ObservableList<Employee> employees = FXCollections.observableArrayList();
            while (resultSet.next()) {
                employees.add(new Employee(resultSet.getInt("id"), resultSet.getString("name")));
            }
            employeeComboBox.setItems(employees);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSubmitLeaveRequest() {
        Employee selectedEmployee = employeeComboBox.getValue();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        if (selectedEmployee == null || startDate == null || endDate == null) {
            showErrorDialog("Leave Request", "Please fill all fields.");
            return;
        }

        try (Connection connection = DatabaseConnector.getConnection()) {
            String sql = "INSERT INTO leave_requests (employee_id, start_date, end_date, status) VALUES (?, ?, ?, 'Pending')";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, selectedEmployee.getId());
            statement.setDate(2, java.sql.Date.valueOf(startDate));
            statement.setDate(3, java.sql.Date.valueOf(endDate));
            statement.executeUpdate();

            showInfoDialog("Leave Request", "Leave request submitted successfully.");
            loadLeaveRequests();
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorDialog("Leave Request", "Failed to submit leave request.");
        }
    }

    @FXML
    private void loadLeaveRequests() {
        Employee selectedEmployee = employeeComboBox.getValue();
        if (selectedEmployee == null) {
            leaveRequestTable.setItems(FXCollections.observableArrayList());
            return;
        }

        try (Connection connection = DatabaseConnector.getConnection()) {
            String sql = "SELECT lr.id, lr.employee_id, e.name, lr.start_date, lr.end_date, lr.status " +
                    "FROM leave_requests lr " +
                    "JOIN employees e ON lr.employee_id = e.id " +
                    "WHERE lr.employee_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, selectedEmployee.getId());
            ResultSet resultSet = statement.executeQuery();
            ObservableList<LeaveRequest> leaveRequests = FXCollections.observableArrayList();
            while (resultSet.next()) {
                leaveRequests.add(new LeaveRequest(
                        resultSet.getInt("lr.id"),
                        resultSet.getInt("lr.employee_id"),
                        resultSet.getDate("lr.start_date").toLocalDate(),
                        resultSet.getDate("lr.end_date").toLocalDate(),
                        resultSet.getString("lr.status"),
                        resultSet.getString("e.name")
                ));
            }
            leaveRequestTable.setItems(leaveRequests);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void configureTable() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        employeeIdColumn.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        employeeNameColumn.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void showErrorDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText("Error");
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showInfoDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText("Information");
        alert.setContentText(content);
        alert.showAndWait();
    }
}

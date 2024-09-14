package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.LeaveRequest;
import util.DatabaseConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class LeaveManagementController {

    @FXML
    private TableView<LeaveRequest> leaveRequestTable;
    @FXML
    private TableColumn<LeaveRequest, Number> idColumn;
    @FXML
    private TableColumn<LeaveRequest, Number> employeeIdColumn;
    @FXML
    private TableColumn<LeaveRequest, String> employeeNameColumn;
    @FXML
    private TableColumn<LeaveRequest, LocalDate> startDateColumn;
    @FXML
    private TableColumn<LeaveRequest, LocalDate> endDateColumn;
    @FXML
    private TableColumn<LeaveRequest, String> statusColumn;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        employeeIdColumn.setCellValueFactory(cellData -> cellData.getValue().employeeIdProperty());
        employeeNameColumn.setCellValueFactory(cellData -> cellData.getValue().employeeNameProperty());
        startDateColumn.setCellValueFactory(cellData -> cellData.getValue().startDateProperty());
        endDateColumn.setCellValueFactory(cellData -> cellData.getValue().endDateProperty());
        statusColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());

        loadLeaveRequests();
    }

    private void loadLeaveRequests() {
        try (Connection connection = DatabaseConnector.getConnection()) {
            String sql = "SELECT lr.id, lr.employee_id, e.name as employee_name, lr.start_date, lr.end_date, lr.status " +
                    "FROM leave_requests lr " +
                    "JOIN employees e ON lr.employee_id = e.id";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            ObservableList<LeaveRequest> leaveRequests = FXCollections.observableArrayList();
            while (resultSet.next()) {
                LeaveRequest leaveRequest = new LeaveRequest(
                        resultSet.getInt("id"),
                        resultSet.getInt("employee_id"),
                        resultSet.getDate("start_date").toLocalDate(),
                        resultSet.getDate("end_date").toLocalDate(),
                        resultSet.getString("status"),
                        resultSet.getString("employee_name")
                );
                leaveRequests.add(leaveRequest);
                System.out.println("Loaded leave request: " + leaveRequest);
            }
            leaveRequestTable.setItems(leaveRequests);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Could not load leave requests from the database.");
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

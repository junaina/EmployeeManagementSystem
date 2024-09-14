package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.LeaveRequest;
import util.DatabaseConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class ManagerLeaveFunctionsController {

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
        configureTable();
        loadLeaveRequests();
    }

    private void configureTable() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        employeeIdColumn.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        employeeNameColumn.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    @FXML
    private void loadLeaveRequests() {
        try (Connection connection = DatabaseConnector.getConnection()) {
            String sql = "SELECT lr.id, lr.employee_id, e.name, lr.start_date, lr.end_date, lr.status " +
                    "FROM leave_requests lr " +
                    "JOIN employees e ON lr.employee_id = e.id";
            PreparedStatement statement = connection.prepareStatement(sql);
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

    @FXML
    private void handleApproveLeave() {
        LeaveRequest selectedRequest = leaveRequestTable.getSelectionModel().getSelectedItem();
        if (selectedRequest != null) {
            updateLeaveStatus(selectedRequest.getId(), "Approved");
        } else {
            showAlert("No Selection", "No Leave Request Selected", "Please select a leave request in the table.");
        }
    }

    @FXML
    private void handleRejectLeave() {
        LeaveRequest selectedRequest = leaveRequestTable.getSelectionModel().getSelectedItem();
        if (selectedRequest != null) {
            updateLeaveStatus(selectedRequest.getId(), "Rejected");
        } else {
            showAlert("No Selection", "No Leave Request Selected", "Please select a leave request in the table.");
        }
    }

    private void updateLeaveStatus(int requestId, String status) {
        try (Connection connection = DatabaseConnector.getConnection()) {
            String sql = "UPDATE leave_requests SET status = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, status);
            statement.setInt(2, requestId);
            statement.executeUpdate();
            loadLeaveRequests();
            showAlert("Leave Status Update", "Success", "Leave request status updated to " + status + ".");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Leave Status Update", "Error", "Failed to update leave request status.");
        }
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

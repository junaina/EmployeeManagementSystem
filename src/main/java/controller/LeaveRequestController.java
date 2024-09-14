package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import model.LeaveRequest;
import util.DatabaseConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LeaveRequestController {

    @FXML
    private TableView<LeaveRequest> leaveRequestTable;

    @FXML
    private void handleApproveLeave() {
        LeaveRequest selectedRequest = leaveRequestTable.getSelectionModel().getSelectedItem();
        if (selectedRequest != null) {
            updateLeaveStatus(selectedRequest, "Approved");
        } else {
            showAlert(Alert.AlertType.ERROR, "No Selection", "No Leave Request Selected");
        }
    }

    @FXML
    private void handleRejectLeave() {
        LeaveRequest selectedRequest = leaveRequestTable.getSelectionModel().getSelectedItem();
        if (selectedRequest != null) {
            updateLeaveStatus(selectedRequest, "Rejected");
        } else {
            showAlert(Alert.AlertType.ERROR, "No Selection", "No Leave Request Selected");
        }
    }

    private void updateLeaveStatus(LeaveRequest leaveRequest, String status) {
        try (Connection connection = DatabaseConnector.getConnection()) {
            String sql = "UPDATE leave_requests SET status = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, status);
            statement.setInt(2, leaveRequest.getId());
            statement.executeUpdate();

            leaveRequest.setStatus(status);
            leaveRequestTable.refresh();

            showAlert(Alert.AlertType.INFORMATION, "Success", "Leave status updated successfully.");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Could not update leave status.");
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

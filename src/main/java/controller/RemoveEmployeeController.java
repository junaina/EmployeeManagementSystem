package controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import util.DatabaseConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class RemoveEmployeeController {
    @FXML
    private TextField idField;

    @FXML
    private void handleRemoveEmployee() {
        int id = Integer.parseInt(idField.getText());

        try (Connection connection = DatabaseConnector.getConnection()) {
            String sql = "DELETE FROM employees WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

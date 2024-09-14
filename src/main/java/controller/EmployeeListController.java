package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Employee;

import java.sql.*;
import java.util.Optional;

public class EmployeeListController {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/EmployeeManagement";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Humanbeing11!";
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

    private ObservableList<Employee> employeeData;

    public EmployeeListController() {
        this.employeeData = FXCollections.observableArrayList();
    }

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        positionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));
        departmentColumn.setCellValueFactory(new PropertyValueFactory<>("department"));
        performanceFactorColumn.setCellValueFactory(new PropertyValueFactory<>("performanceFactor"));

        loadEmployeeData();
    }

    @FXML
    public void loadEmployeeData() {
        // This is where you would load the data from the database
        employeeData.clear();

        String sql = "SELECT * FROM employees";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        // For now, we'll just add some sample data
        //employeeData.clear();
//        employeeData.addAll(
//                new Employee(1, "John Doe", "Developer", "IT", 0.0, 0.0, 1.0),
//                new Employee(2, "Jane Smith", "Manager", "HR", 0.0, 0.0, 1.2)
//        );
       employeeTable.setItems(employeeData);
    }

    @FXML
    private void handleAddEmployee() {
        // Show Add Employee dialog
        TextInputDialog nameDialog = new TextInputDialog("New Employee Name");
        TextInputDialog positionDialog = new TextInputDialog("New Position");
        TextInputDialog departmentDialog = new TextInputDialog("New Department");
        TextInputDialog salaryDialog = new TextInputDialog("Base Salary"); // New dialog for base salary

        // Set the dialog titles
        nameDialog.setTitle("Add Employee");
        nameDialog.setHeaderText("Enter Employee Name");
        positionDialog.setTitle("Add Employee");
        positionDialog.setHeaderText("Enter Employee Position");
        departmentDialog.setTitle("Add Employee");
        departmentDialog.setHeaderText("Enter Employee Department");
        salaryDialog.setTitle("Add Employee"); // Set title for base salary dialog
        salaryDialog.setHeaderText("Enter Base Salary"); // Set header for base salary dialog

        // Show the dialogs and get the input
        Optional<String> nameResult = nameDialog.showAndWait();
        Optional<String> positionResult = positionDialog.showAndWait();
        Optional<String> departmentResult = departmentDialog.showAndWait();
        Optional<String> salaryResult = salaryDialog.showAndWait(); // Get base salary input

        // If all fields are filled, add the new employee
        if (nameResult.isPresent() && positionResult.isPresent() && departmentResult.isPresent() && salaryResult.isPresent()) {
            String sql = "INSERT INTO employees (name, position, department, performance_factor, base_salary, bonus_percent) VALUES (?, ?, ?, ?, ?, ?)";

            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

                preparedStatement.setString(1, nameResult.get());
                preparedStatement.setString(2, positionResult.get());
                preparedStatement.setString(3, departmentResult.get());
                preparedStatement.setDouble(4, 0.0); // performance factor
                preparedStatement.setDouble(5, Double.parseDouble(salaryResult.get())); // base salary
                preparedStatement.setDouble(6, 0.0); // bonus_percent

                preparedStatement.executeUpdate();

                showAlert("Add Employee", "Success", "Employee added successfully.");
                loadEmployeeData(); // Refresh the table after adding an employee
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Add Employee", "Error", "Failed to add employee.");
            }
        } else {
            showAlert("Add Employee", "Warning", "Missing Information. Please fill in all fields.");
        }
    }
    @FXML
    private void handleUpdateEmployee() {
        Employee selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null) {
            // Show Update Employee dialog
            TextInputDialog nameDialog = new TextInputDialog(selectedEmployee.getName());
            TextInputDialog positionDialog = new TextInputDialog(selectedEmployee.getPosition());

            // Set the dialog titles
            nameDialog.setTitle("Update Employee");
            nameDialog.setHeaderText("Update Employee Name");
            positionDialog.setTitle("Update Employee");
            positionDialog.setHeaderText("Update Employee Position");

            // Show the dialogs and get the input
            Optional<String> nameResult = nameDialog.showAndWait();
            Optional<String> positionResult = positionDialog.showAndWait();

            // If all fields are filled, update the employee
            if (nameResult.isPresent() && positionResult.isPresent()) {
                String sql = "UPDATE employees SET name = ?, position = ? WHERE id = ?";

                try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                     PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

                    preparedStatement.setString(1, nameResult.get());
                    preparedStatement.setString(2, positionResult.get());
                    preparedStatement.setInt(3, selectedEmployee.getId());

                    preparedStatement.executeUpdate();

                    showAlert("Update Employee", "Success", "Employee updated successfully.");
                    loadEmployeeData(); // Refresh the table after updating an employee
                } catch (SQLException e) {
                    e.printStackTrace();
                    showAlert("Update Employee", "Error", "Failed to update employee.");
                }
            } else {
                showAlert("Update Employee", "Warning", "Missing Information. Please fill in all fields.");
            }
        } else {
            showAlert("No Selection", "No Employee Selected", "Please select an employee in the table.");
        }
    }

    @FXML
    private void handleDeleteEmployee() {
        Employee selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Confirmation");
            alert.setHeaderText("Delete Employee");
            alert.setContentText("Are you sure you want to delete this employee?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                String sql = "DELETE FROM employees WHERE id = ?";

                try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                     PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

                    preparedStatement.setInt(1, selectedEmployee.getId());
                    preparedStatement.executeUpdate();

                    showAlert("Delete Employee", "Success", "Employee deleted successfully.");
                    loadEmployeeData(); // Refresh the table after deleting an employee
                } catch (SQLException e) {
                    e.printStackTrace();
                    showAlert("Delete Employee", "Error", "Failed to delete employee.");
                }
            }
        } else {
            showAlert("No Selection", "No Employee Selected", "Please select an employee in the table.");
        }
    }public Employee getSelectedEmployee() {
        return employeeTable.getSelectionModel().getSelectedItem();
    }


    private void showAlert(String title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
}

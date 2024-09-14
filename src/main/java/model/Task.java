package model;

public class Task {
    private int id;
    private int employeeId;
    private String taskDescription;
    private String completionStatus; // Changed to String

    public Task(int id, int employeeId, String taskDescription, String completionStatus) {
        this.id = id;
        this.employeeId = employeeId;
        this.taskDescription = taskDescription;
        this.completionStatus = completionStatus;
    }

    public int getId() {
        return id;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public String getCompletionStatus() {
        return completionStatus;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public void setCompletionStatus(String completionStatus) {
        this.completionStatus = completionStatus;
    }
}

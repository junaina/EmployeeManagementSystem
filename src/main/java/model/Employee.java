package model;

import javafx.beans.property.*;

public class Employee {
    private final IntegerProperty id;
    private final StringProperty name;
    private final StringProperty position;
    private final StringProperty department;
    private final DoubleProperty baseSalary;
    private final DoubleProperty bonusPercent;
    private final DoubleProperty performanceFactor;

    // Constructor with all parameters
    public Employee(int id, String name, String position, String department, double baseSalary, double bonusPercent, double performanceFactor) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.position = new SimpleStringProperty(position);
        this.department = new SimpleStringProperty(department);
        this.baseSalary = new SimpleDoubleProperty(baseSalary);
        this.bonusPercent = new SimpleDoubleProperty(bonusPercent);
        this.performanceFactor = new SimpleDoubleProperty(performanceFactor);
    }
    // Constructor with name, position, department, and performance factor
    public Employee(String name, String position, String department, double performanceFactor) {
        this.id = new SimpleIntegerProperty(0); // Default id
        this.name = new SimpleStringProperty(name);
        this.position = new SimpleStringProperty(position);
        this.department = new SimpleStringProperty(department);
        this.performanceFactor = new SimpleDoubleProperty(performanceFactor);
        this.baseSalary = new SimpleDoubleProperty(0);
        this.bonusPercent = new SimpleDoubleProperty(0);
    }

    // Constructor with only id, name, position, department, and performanceFactor
    public Employee(int id, String name, String position, String department, double performanceFactor) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.position = new SimpleStringProperty(position);
        this.department = new SimpleStringProperty(department);
        this.baseSalary = new SimpleDoubleProperty(0);
        this.bonusPercent = new SimpleDoubleProperty(0);
        this.performanceFactor = new SimpleDoubleProperty(performanceFactor);
    }

    // Constructor with id and name
    public Employee(int id, String name) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.position = new SimpleStringProperty("");
        this.department = new SimpleStringProperty("");
        this.baseSalary = new SimpleDoubleProperty(0);
        this.bonusPercent = new SimpleDoubleProperty(0);
        this.performanceFactor = new SimpleDoubleProperty(0);
    }

    // Constructor with id, name, position, and department
    public Employee(int id, String name, String position, String department) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.position = new SimpleStringProperty(position);
        this.department = new SimpleStringProperty(department);
        this.baseSalary = new SimpleDoubleProperty(0);
        this.bonusPercent = new SimpleDoubleProperty(0);
        this.performanceFactor = new SimpleDoubleProperty(0);
    }

    // No-argument constructor
    public Employee() {
        this.id = new SimpleIntegerProperty(0);
        this.name = new SimpleStringProperty("");
        this.position = new SimpleStringProperty("");
        this.department = new SimpleStringProperty("");
        this.baseSalary = new SimpleDoubleProperty(0);
        this.bonusPercent = new SimpleDoubleProperty(0);
        this.performanceFactor = new SimpleDoubleProperty(0);
    }

    // Getters and setters

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getPosition() {
        return position.get();
    }

    public StringProperty positionProperty() {
        return position;
    }

    public void setPosition(String position) {
        this.position.set(position);
    }

    public String getDepartment() {
        return department.get();
    }

    public StringProperty departmentProperty() {
        return department;
    }

    public void setDepartment(String department) {
        this.department.set(department);
    }

    public double getBaseSalary() {
        return baseSalary.get();
    }

    public DoubleProperty baseSalaryProperty() {
        return baseSalary;
    }

    public void setBaseSalary(double baseSalary) {
        this.baseSalary.set(baseSalary);
    }

    public double getBonusPercent() {
        return bonusPercent.get();
    }

    public DoubleProperty bonusPercentProperty() {
        return bonusPercent;
    }

    public void setBonusPercent(double bonusPercent) {
        this.bonusPercent.set(bonusPercent);
    }

    public double getPerformanceFactor() {
        return performanceFactor.get();
    }

    public DoubleProperty performanceFactorProperty() {
        return performanceFactor;
    }

    public void setPerformanceFactor(double performanceFactor) {
        this.performanceFactor.set(performanceFactor);
    }
}

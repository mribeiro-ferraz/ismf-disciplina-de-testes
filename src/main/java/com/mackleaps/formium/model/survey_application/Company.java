package com.mackleaps.formium.model.survey_application;

import org.apache.commons.lang3.Validate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String cnpj;

    @ManyToMany(mappedBy = "associationsAsManager")
    private List<Employee> managers;

    @ManyToMany(mappedBy = "associationsAsEmployee")
    private List<Employee> employees;

    public Company () { }

    public Company (String name, String cnpj) {

        Validate.notNull(name);
        Validate.notNull(cnpj);

        employees = new ArrayList<>();
        managers = new ArrayList<>();

        this.name = name;
        this.cnpj = cnpj;
    }

    public Company (String name, String cnpj, Employee initialManager) {

        Validate.notNull(name);
        Validate.notNull(cnpj);

        if(!Company.isValidCnpj(cnpj))
            throw new IllegalArgumentException("Invalid cnpj");

        employees = new ArrayList<>();
        managers = new ArrayList<>();
        managers.add(initialManager);

        this.name = name;
        this.cnpj = cnpj;
    }

    public static boolean isValidCnpj(String cnpj) {
        //TODO this
        return true;
    }

    /**
     * Associated employee to current company, adding it to the list of employees
     * @throws IllegalArgumentException if employee already associated to company
     * */
    public void associateEmployeeToCompany(Employee employee) {

        Validate.notNull(employee);

        if(!isAssociatedToCompany(employee)) {
            employees.add(employee);
            employee.getAssociatedToCompany(this);
            return;
        }

        throw new IllegalArgumentException("Already an employee");
    }

    /**
     * Associate to company and then promote to manager
     * @see Company#associateEmployeeToCompany(Employee)
     * @see Company#promoteEmployeeToManager(Employee)
     * */
    public void associatedEmployeeToCompanyAsManager(Employee employee) {
        Validate.notNull(employee);
        associateEmployeeToCompany(employee);
        promoteEmployeeToManager(employee);
    }

    /**
     * Promote a certain employee to manager of the company inside the system
     * @param employee to be promoted
     * @throws IllegalArgumentException if employee not associated to company
     * */
    public void promoteEmployeeToManager(Employee employee){

        Validate.notNull(employee);

        for(int i = 0; i < employees.size(); i++) {
            if(areBothTheSameEmployee(employee,employees.get(i))) {
                employees.remove(i);
                managers.add(employee);
                employee.getPromotedToManagerOnCompany(this);
                return;
            }
        }

        throw new IllegalArgumentException("Employee not associated to company");
    }

    /**
     * @return true if associated to company either as employee or as manager
     * @return false otherwise
     * */
    public boolean isAssociatedToCompany(Employee employee) {

        return isManager(employee) || (isEmployee(employee));

    }

    /**
     * @return true if employee is manager of the current company
     * @return false otherwise
     * */
    public boolean isManager(Employee employee) {

        for(Employee current : managers)
            if(areBothTheSameEmployee(current, employee))
                return true;

        return false;
    }

    /**
     * @return true if employee passed as parameter is associated as employee of the current company
     * @return false otherwise
     * */
    public boolean isEmployee(Employee employee) {

        for(Employee current : employees)
            if(areBothTheSameEmployee(current, employee))
                return true;

        return false;
    }

    /**
     * Currently using the id to compare if both employees passed as parameter are the same.
     * @param first to be compared
     * @param second to be compared
     * @return whether both employees passed as parameter as equal to one another
     * */
    private boolean areBothTheSameEmployee(Employee first, Employee second) {
        return first.getId().equals(second.getId());
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getCnpj() {
        return this.cnpj;
    }

    public List<Employee> getManagers() {
        return this.managers;
    }

    public List<Employee> getEmployees() {
        return this.employees;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public void setManagers(List<Employee> managers) {
        this.managers = managers;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }
}

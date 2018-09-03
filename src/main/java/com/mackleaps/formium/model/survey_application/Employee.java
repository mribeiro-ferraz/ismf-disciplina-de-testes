package com.mackleaps.formium.model.survey_application;

import com.mackleaps.formium.model.auth.Person;
import com.mackleaps.formium.model.auth.User;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.Validate;

import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Employee extends Person {

    @ManyToMany
    @JoinTable
    private List<Company> associationsAsEmployee;

    @ManyToMany
    @JoinTable
    private List<Company> associationsAsManager;

    public Employee(String firstName, String lastName, String cpf, User user) {
        super(firstName, lastName, cpf, user);
        associationsAsManager = new ArrayList<>();
        associationsAsEmployee = new ArrayList<>();
    }

    public Employee() {
        associationsAsManager = new ArrayList<>();
        associationsAsEmployee = new ArrayList<>();
    }

    public void getAssociatedToCompany(Company company) throws IllegalArgumentException {

        Validate.notNull(company);
        Validate.notNull(company.getId());

        if(isAssociatedAsEmployee(company) || isAssociatedAsManager(company))
            throw new IllegalArgumentException("Already associated to company");

        associationsAsEmployee.add(company);
    }

    public void getPromotedToManagerOnCompany(Company company) {

        Validate.notNull(company);
        Validate.notNull(company.getId());

        if(isAssociatedAsManager(company))
            throw new IllegalArgumentException("User is already a manager of the company passed as parameter");

        for(int i = 0; i < associationsAsEmployee.size(); i++) {

            if(areBothTheSameCompany(company, associationsAsEmployee.get(i))) {
                associationsAsEmployee.remove(i);
                associationsAsManager.add(company);
                return;
            }
        }

        throw new IllegalArgumentException("To get promoted the user need to be associated to company first.");
    }

    public boolean isAssociatedAsEmployee(Company company) {
        return isCompanyInList(company,associationsAsEmployee);
    }

    public boolean isAssociatedAsManager(Company company) {
        return isCompanyInList(company,associationsAsManager);
    }

    public boolean isUniqueManagerOfAnyCompany() {

        for(Company company : this.getAssociationsAsManager())
            if(company.getManagers().size() > 1)
                return false;

        return true;
    }

    /**
     * Union of both association as employee and as manager
     * */
    public List<Company> getAssociations () {
        //TODO does this cast work?
        return (List<Company>) CollectionUtils.union(associationsAsEmployee,associationsAsManager);
    }

    /**
     * Currently using the id to compare if both companies passed as parameter are the same.
     * @param first to be compared
     * @param second to be compared
     * @return whether both companies passed as parameter as equal to one another
     * */
    private boolean areBothTheSameCompany(Company first, Company second) {
        return first.getId().equals(second.getId());
    }

    private boolean isCompanyInList(Company company, List<Company> companies) {

        return companies.stream()
                        .anyMatch(current -> areBothTheSameCompany(company,current));
    }

    public List<Company> getAssociationsAsEmployee() {
        return this.associationsAsEmployee;
    }

    public List<Company> getAssociationsAsManager() {
        return this.associationsAsManager;
    }

    public void setAssociationsAsEmployee(List<Company> associationsAsEmployee) {
        this.associationsAsEmployee = associationsAsEmployee;
    }

    public void setAssociationsAsManager(List<Company> associationsAsManager) {
        this.associationsAsManager = associationsAsManager;
    }
}

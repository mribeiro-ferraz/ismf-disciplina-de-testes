package com.mackleaps.formium.service.survey_application;

import com.mackleaps.formium.exceptions.InvalidTokenException;
import com.mackleaps.formium.model.auth.VerificationToken;
import com.mackleaps.formium.model.survey_application.Company;
import com.mackleaps.formium.model.survey_application.Employee;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import java.util.List;

/**
 * Operations related to the association of new employees to ISMF, and their association to companies
 * */
public interface IEmployeeService {

    /**
     * Creates an inactive user with the information from the given employee.
     * Generates token and sends email to user
     * @param employee to be added
     * @return the resulting employee from the persistence operation
     * */
    Employee requestAssociation(Employee employee);

    /**
     * Change user status, invalidate token
     * @param token used to verify user's email
     * @return employee corresponding to the action
     * */
    Employee confirmEmployeeAssociationRequest(String token) throws InvalidTokenException;

    /**
     * Creates an inactive employee/user just with the email given, and invites him to specified company.
     * Generates token and sends email to user
     * @param email to which the token will be sent
     * @param company to which the user will be invited to
     * */
    void inviteUserAssociateCompany(String email, Company company);

    /**
     * Update employee info. Change user status. Invalidate token
     * @param employee with the information to be use to update registered user
     * @param token used to confirm email
     * @return the resulting employee from the persistence operation
     * */
    Employee confirmEmployeeAssociationByInvitation(Employee employee, VerificationToken token);


    /**
     * Get a list of all the companies to which the user is associated,
     * @param userEmployeeId of the user associated to the user
     * @return a list of the companies to which the user is associated to, whether as a manager or as normal employee
     * Considers the possibility of the user not being associated to any company, returning a empty list in that case
     * @throws ResourceNotFoundException if specified employee is not registered
     */
    List<Company> getCompaniesToWhichEmployeeIsAssociatedTo (Long userEmployeeId) throws ResourceNotFoundException;

}

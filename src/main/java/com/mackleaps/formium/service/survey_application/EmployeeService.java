package com.mackleaps.formium.service.survey_application;

import com.mackleaps.formium.exceptions.InvalidTokenException;
import com.mackleaps.formium.model.auth.VerificationToken;
import com.mackleaps.formium.model.survey_application.Company;
import com.mackleaps.formium.model.survey_application.Employee;
import com.mackleaps.formium.repository.auth.TokenRepository;
import com.mackleaps.formium.repository.survey_application.EmployeeRepository;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService implements IEmployeeService {

    private EmployeeRepository employeeRepo;
    private TokenRepository tokenRepo;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepo,
                           TokenRepository tokenRepo) {

        this.employeeRepo = employeeRepo;
        this.tokenRepo = tokenRepo;
    }

    @Override
    public Employee requestAssociation(Employee employee) {

        Validate.notNull(employee);

        if(employeeRepo.existsByEmail(employee.getUser().getEmailAddress()) ||
           employeeRepo.existsByCpf(employee.getCpf()))

            throw new IllegalArgumentException("Employee already registered");

        //TODO generate token, send email
        return employeeRepo.saveAndFlush(employee);
    }

    @Override
    public Employee confirmEmployeeAssociationRequest(String token) throws InvalidTokenException {

        Validate.notNull(token);

        VerificationToken validatedToken = validateTokenExistence(token);
        validatedToken.useToken();

        return employeeRepo.findEmployeeByUserId(validatedToken.getUser().getId());
    }

    @Override
    public void inviteUserAssociateCompany(String email, Company company) {

    }

    @Override
    public Employee confirmEmployeeAssociationByInvitation(Employee employee, VerificationToken token) {
        return null;
    }

    @Override
    public List<Company> getCompaniesToWhichEmployeeIsAssociatedTo(Long userEmployeeId) throws ResourceNotFoundException {

        Employee employee = employeeRepo.findEmployeeByUserId(userEmployeeId);
        if(employee == null)
            throw new ResourceNotFoundException();

        return employee.getAssociations();
    }


    public void disaffiliateEmployee(Employee employee) {

        Validate.notNull(employee);
        Validate.notNull(employee.getId());

        if(!employeeRepo.exists(employee.getId()))
            throw new IllegalArgumentException("Employee with id informed does not exist");

        if(employee.isUniqueManagerOfAnyCompany())
            return; //TODO find better way to do this

        employeeRepo.delete(employee.getId());
    }

    public Employee getEmployee(Long id) {
        Validate.notNull(id);
        return employeeRepo.findOne(id);
    }

    public Employee updateEmployee(Employee employee) {

        Validate.notNull(employee);
        Validate.notNull(employee.getId());

        if(!employeeRepo.exists(employee.getId()))
            throw new IllegalArgumentException("Employee with id informed does not exist");

        return employeeRepo.saveAndFlush(employee);
    }

    private VerificationToken validateTokenExistence(String token) throws InvalidTokenException {

        VerificationToken fromRepo = tokenRepo.findByToken(token);
        if(fromRepo == null)
            throw new InvalidTokenException("Token not found");

        return fromRepo;
    }
}

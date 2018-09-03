package com.mackleaps.formium.service.survey_application;

import com.mackleaps.formium.model.survey_application.Company;
import com.mackleaps.formium.model.survey_application.Employee;
import com.mackleaps.formium.repository.survey_application.CompanyRepository;
import com.mackleaps.formium.repository.survey_application.EmployeeRepository;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyAssociationService implements ICompanyAssociationService {

    private CompanyRepository companyRepo;

    private EmployeeRepository employeeRepo;

    @Autowired
    public CompanyAssociationService(CompanyRepository companyRepo, EmployeeRepository employeeRepo) {
        this.companyRepo = companyRepo;
        this.employeeRepo = employeeRepo;
    }

    @Override
    public Company associateCompany(Company company, Employee initialManager) {

        Validate.notNull(company);
        Validate.notNull(initialManager);
        Validate.notNull(initialManager.getId());

        if(!employeeRepo.exists(initialManager.getId()))
            throw new IllegalArgumentException("Employee with id informed does not exist");

        companyRepo.saveAndFlush(company);
        company.associatedEmployeeToCompanyAsManager(initialManager);
        return company;
    }

    @Override
    public void disassociateCompany(Company company) {
        throw new UnsupportedOperationException();
    }

}

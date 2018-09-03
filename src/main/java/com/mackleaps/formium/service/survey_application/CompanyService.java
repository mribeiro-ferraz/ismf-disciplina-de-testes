package com.mackleaps.formium.service.survey_application;

import com.mackleaps.formium.model.survey_application.Company;
import com.mackleaps.formium.repository.survey_application.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CompanyService implements ICompanyService {

    private CompanyRepository companyRepository;

    @Autowired
    public CompanyService (CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public Company getCompanyForUse(Long id) {

        Company company = companyRepository.findOne(id);
        if(company == null)
            throw new ResourceNotFoundException();

        return company;
    }

    @Override
    public Company getCompany(Long id) {
        return companyRepository.findOne(id);
    }
}

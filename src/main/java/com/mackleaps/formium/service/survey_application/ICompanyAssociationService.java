package com.mackleaps.formium.service.survey_application;

import com.mackleaps.formium.model.survey_application.Company;
import com.mackleaps.formium.model.survey_application.Employee;

/**
 * Operations related to the association of a new company
 * */
public interface ICompanyAssociationService {

    /**
     * Create a new company. Associate employee passed as parameter as its initial manager
     * @param company to be associated to the system
     * @param initialManager to be initially associated to company
     * @return the resulting company from the persistence operation
     *
     * TODO this will be incorporated by the affiliation process; emails will be sent, status will be used, and etc.
     * TODO for now this will be a simple CRUD operation, where the company will be created and the initial manager associated to it
     * */
    Company associateCompany(Company company, Employee initialManager);

    /**
     * Not to be dealt with for now
     * */
    void disassociateCompany(Company company);

}

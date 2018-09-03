package com.mackleaps.formium.service.survey_application;

import com.mackleaps.formium.model.survey_application.Company;

public interface ICompanyService {

    Company getCompanyForUse(Long id);
    Company getCompany (Long id);

}

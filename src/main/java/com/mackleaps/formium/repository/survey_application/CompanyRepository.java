package com.mackleaps.formium.repository.survey_application;

import com.mackleaps.formium.model.survey_application.Company;
import com.mackleaps.formium.model.survey_application.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company,Long> {

    List<Company> findCompaniesByEmployeesIn(Employee employee);
    List<Company> findCompaniesByManagersIn(Employee employee);

}

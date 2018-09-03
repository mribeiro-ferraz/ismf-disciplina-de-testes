package com.mackleaps.formium.repository.survey_application;

import com.mackleaps.formium.model.survey_application.SurveyResults;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SurveyResultsRepository extends JpaRepository<SurveyResults, Long> {

    SurveyResults findByCompanyIdAndAndCorrespondingSurveyId(Long companyId, Long correspondingSurveyId);
    boolean existsSurveyResultsByCompanyIdAndCorrespondingSurveyId(Long companyId, Long correspondingSurveyId);
    List<SurveyResults> findByCorrespondingSurveyId(Long surveyId);
}

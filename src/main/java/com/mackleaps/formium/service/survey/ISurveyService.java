package com.mackleaps.formium.service.survey;

import com.mackleaps.formium.exceptions.ComponentNotFoundException;
import com.mackleaps.formium.model.survey.Survey;

import java.util.List;

public interface ISurveyService {

    List<Survey> getAllSurveys();
    Survey addSurvey(Survey newSurvey);
    Survey editSurvey (Survey survey);
    Survey getSurvey (Long surveyId);
    Survey getSurveyForUse(Long surveyId) throws ComponentNotFoundException;
    void deleteSurvey(Long surveyId);

}

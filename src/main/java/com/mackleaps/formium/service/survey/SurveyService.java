package com.mackleaps.formium.service.survey;

import com.mackleaps.formium.exceptions.ComponentNotFoundException;
import com.mackleaps.formium.model.survey.Survey;
import com.mackleaps.formium.model.survey_application.SurveyResults;
import com.mackleaps.formium.repository.survey.SurveyRepository;
import com.mackleaps.formium.repository.survey_application.SurveyResultsRepository;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SurveyService implements ISurveyService {

    private SurveyRepository surveyRepository;
    private SurveyResultsRepository surveyResultsRepository;

    @Autowired
    public SurveyService (SurveyRepository surveyRepository, SurveyResultsRepository surveyResultsRepository) {
        this.surveyRepository = surveyRepository;
        this.surveyResultsRepository = surveyResultsRepository;
    }

    @Override
    public List<Survey> getAllSurveys() {
        return this.surveyRepository.findAll();
    }

    @Override
    public Survey addSurvey(Survey newSurvey) {

        Validate.notNull(newSurvey);
        Validate.isTrue(newSurvey.getId() == null);

        return surveyRepository.saveAndFlush(newSurvey);
    }

    @Override
    public Survey editSurvey(Survey survey) {

        Validate.notNull(survey);
        Validate.notNull(survey.getId());

        if(!surveyRepository.exists(survey.getId()))
            throw new ComponentNotFoundException();

        return surveyRepository.saveAndFlush(survey);
    }

    @Override
    public Survey getSurvey(Long surveyId) {
        Validate.notNull(surveyId);
        return surveyRepository.findOne(surveyId);
    }

    @Override
    public Survey getSurveyForUse(Long surveyId) throws ComponentNotFoundException {

        Validate.notNull(surveyId);

        Survey fromRepo = surveyRepository.findOne(surveyId);
        if(fromRepo == null)
            throw new ComponentNotFoundException();

        return fromRepo;
    }

    @Override
    public void deleteSurvey(Long surveyId) {

        Validate.notNull(surveyId);

        if(!surveyRepository.exists(surveyId))
            throw new ComponentNotFoundException();

        //TODO provisional, while we do not have a publication
        List<SurveyResults> results = surveyResultsRepository.findByCorrespondingSurveyId(surveyId);
        for(SurveyResults current : results) {
            current.setCorrespondingSurvey(null);
        }

        surveyRepository.delete(surveyId);
    }
}

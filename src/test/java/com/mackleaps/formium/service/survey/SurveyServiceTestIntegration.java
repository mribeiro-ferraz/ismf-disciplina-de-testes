package com.mackleaps.formium.service.survey;

import com.mackleaps.formium.Application;
import com.mackleaps.formium.exceptions.ComponentNotFoundException;
import com.mackleaps.formium.model.survey.Survey;
import com.mackleaps.formium.repository.survey.SurveyRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WithUserDetails("testing_researcher@test.com.br")
@ActiveProfiles({"test"})
public class SurveyServiceTestIntegration {

    @Autowired
    private ISurveyService surveyService;

    @Autowired
    private SurveyRepository surveyRepository;

    private static final Long NOT_EXISTING_SURVEY = 5L;

    @Test
    public void shouldReturnNullIfSurveyDoesNotExist () {
        Survey survey = surveyService.getSurvey(NOT_EXISTING_SURVEY);
        assertNull(survey);
    }

    @Test(expected = ComponentNotFoundException.class)
    public void shouldThrowExceptionIfSurveyDoesNotExistWhenRetrievingForUse () {
        surveyService.getSurveyForUse(NOT_EXISTING_SURVEY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfSurveyHasIdWhenAdding () {

        Survey survey = new Survey();
        survey.setTitle("Title");
        survey.setDescription("Description");
        survey.setId(NOT_EXISTING_SURVEY);

        surveyService.addSurvey(survey);
    }

    @Test
    public void shouldAddSurveyIfEverythingWentOk () {

        long beforeAdding = surveyRepository.count();

        Survey survey = new Survey();
        survey.setTitle("Title");
        survey.setDescription("Description");

        surveyService.addSurvey(survey);

        //after adding, these values should be filled
        assertNotNull(survey.getId());
        assertNotNull(survey.getCreatedDate());
        assertNotNull(survey.getLastEditedDate());
        assertNotNull(survey.getCreatedBy());
        assertNotNull(survey.getLastModifiedBy());

        assertEquals(beforeAdding + 1, surveyRepository.count());
    }

}
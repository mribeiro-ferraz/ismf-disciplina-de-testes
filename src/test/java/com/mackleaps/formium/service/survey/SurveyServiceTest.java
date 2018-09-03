package com.mackleaps.formium.service.survey;

import com.mackleaps.formium.Application;
import com.mackleaps.formium.exceptions.ComponentNotFoundException;
import com.mackleaps.formium.model.survey.Survey;
import com.mackleaps.formium.repository.survey.SurveyRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class SurveyServiceTest {

    @Mock
    private SurveyRepository surveyRepository;

    private SurveyService surveyService;

    @Before
    public void setup () {
        //SurveyResultsRepository is not used in the current test suite, so its dependency is passed as null
        surveyService = new SurveyService(surveyRepository,null);
    }

    @Test(expected = ComponentNotFoundException.class)
    public void shouldThrowExceptionWhenEditingAndSurveyDoesNotExist () {

        final Long NOT_EXISTING_ID = 5L;

        when(surveyRepository.findOne(NOT_EXISTING_ID)).thenReturn(null);

        Survey survey = new Survey();
        survey.setId(NOT_EXISTING_ID);
        survey.setTitle("New title");
        survey.setDescription("New description");
        survey.setPrefix("New prefix");

        surveyService.editSurvey(survey);
    }

    @Test
    public void shouldReturnSurveyWithEditedValuesIfEverythingWentOkWhenEditing () {

        Long EXISTING_ID = 1L;

        Survey existing = new Survey();
        existing.setPrefix("Prefix");
        existing.setTitle("Title");
        existing.setDescription("Description");
        existing.setId(EXISTING_ID);

        when(surveyRepository.exists(EXISTING_ID)).thenReturn(true);
        when(surveyRepository.saveAndFlush(existing)).thenReturn(existing);

        Survey editedSurvey = surveyService.editSurvey(existing);

        assertEquals(existing.getTitle(), editedSurvey.getTitle());
        assertEquals(existing.getPrefix(), editedSurvey.getPrefix());
        assertEquals(existing.getDescription(), editedSurvey.getDescription());
    }

    @Test(expected = ComponentNotFoundException.class)
    public void shouldThrowExceptionWhenTryingToDeleteANonExistingComponent () {

        Long NOT_EXISTING_SURVEY_ID = 5L;
        when(surveyRepository.exists(NOT_EXISTING_SURVEY_ID)).thenThrow(new ComponentNotFoundException());

        surveyService.deleteSurvey(NOT_EXISTING_SURVEY_ID);
    }

}
package com.mackleaps.formium.model.survey;

import com.mackleaps.formium.exceptions.DuplicatedComponentException;
import org.junit.Test;

import static org.junit.Assert.*;

public class SurveyTest {

    @Test
    public void returnSelfWhenAskedForSurvey () {
        Survey survey = new Survey();
        assertEquals(survey,survey.getSurveyForCurrentContainer());
    }

    @Test
    public void returnTrueWhenAskedIfSurvey () {
        Survey survey = new Survey();
        assertTrue(survey.isSurvey());
    }

    @Test
    public void shouldBeCreatedWithoutSubcomponents () {

        int empty = 0;

        Survey survey = new Survey();
        assertEquals(empty, survey.getQuestions().size());
        assertEquals(empty, survey.getSubcategories().size());
        assertEquals(empty, survey.getAllComponents().size());
    }

    @Test(expected = DuplicatedComponentException.class)
    public void shouldAddQuestionAndThrowExceptionIfDuplicatedHeaderFound () {

        int initialNumberComponents = 0;
        int initialPosition = 0;

        LikertQuestion question = new LikertQuestion();
        question.setHeader("Header");
        question.setLeftText("Left");
        question.setRightText("Right");
        question.setLeftValue(1);
        question.setRightValue(7);

        Survey survey = new Survey();
        survey.setPrefix("Prefix");
        survey.setTitle("Title");
        survey.setDescription("Description");
        assertEquals(survey.getAllComponents().size(), initialNumberComponents);

        survey.addQuestion(question);
        assertEquals(initialNumberComponents + 1, survey.getAllComponents().size());
        assertEquals(initialNumberComponents + 1, survey.getQuestions().size());
        assertEquals(initialNumberComponents, survey.getSubcategories().size());

        LikertQuestion addedQuestion = survey.getQuestionByType(LikertQuestion.class).get(initialPosition);
        assertNotNull(addedQuestion.getParent());
        assertEquals(survey, addedQuestion.getParent());
        assertEquals(question.getHeader(), addedQuestion.getHeader());

        LikertQuestion duplicatedHeader = new LikertQuestion();
        duplicatedHeader.setHeader(question.getHeader());

        survey.addQuestion(question); //should throw DuplicatedComponentException
    }

    @Test(expected = DuplicatedComponentException.class)
    public void shouldAddCategoryAndThrowExceptionIfDuplicatedTitleFound () {

        int initialNumberComponents = 0;
        int initialPosition = 0;

        Category category = new Category();
        category.setTitle("Title category");
        category.setDescription("Category description");

        Survey survey = new Survey();
        survey.setPrefix("Prefix");
        survey.setTitle("Title");
        survey.setDescription("Description");
        assertEquals(initialNumberComponents, survey.getAllComponents().size());

        survey.addCategory(category);
        assertEquals(initialNumberComponents + 1, survey.getAllComponents().size());
        assertEquals(initialNumberComponents, survey.getQuestions().size());
        assertEquals(initialNumberComponents + 1, survey.getSubcategories().size());

        Category addedCategory = survey.getSubcategories().get(initialPosition);
        assertNotNull(addedCategory.getParent());
        assertEquals(survey, addedCategory.getParent());
        assertEquals(category.getTitle(), addedCategory.getTitle());
        assertEquals(category.getDescription(), addedCategory.getDescription());

        Category duplicatedTitle = new Category();
        duplicatedTitle.setTitle(addedCategory.getTitle());

        survey.addCategory(duplicatedTitle); //should throw DuplicatedComponentException
    }

}
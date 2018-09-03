package com.mackleaps.formium.service.survey;

import com.mackleaps.formium.exceptions.ComponentNotFoundException;
import com.mackleaps.formium.model.survey.Container;
import com.mackleaps.formium.model.survey.Question;

public interface IQuestionService {

    <T extends Question> T addQuestion(T newQuestion, Long parentContainerId);
    <T extends Question> T editQuestion(T question, Class<T> questionType);
    <T extends Question> T getQuestion(Long questionId, Class<T> questionType);
    Question getQuestion(Long questionId);
    <T extends Question> T getQuestionForUse(Long questionId, Class<T> questionType) throws ComponentNotFoundException;
    Question getQuestionForUse(Long questionId) throws ComponentNotFoundException;
    void deleteQuestion(Long questionId);
    Container getParent(Long questionId);

}

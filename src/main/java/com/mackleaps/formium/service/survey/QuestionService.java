package com.mackleaps.formium.service.survey;

import com.mackleaps.formium.exceptions.ComponentNotFoundException;
import com.mackleaps.formium.model.survey.Container;
import com.mackleaps.formium.model.survey.Question;
import com.mackleaps.formium.repository.survey.ContainerRepository;
import com.mackleaps.formium.repository.survey.QuestionRepository;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionService implements IQuestionService {

    private QuestionRepository questionRepository;
    private ContainerRepository containerRepository;

    @Autowired
    public QuestionService (QuestionRepository questionRepository, ContainerRepository containerRepository) {
        this.questionRepository = questionRepository;
        this.containerRepository = containerRepository;
    }

    @Override
    public <T extends Question> T addQuestion(T newQuestion, Long parentContainerId) {

        Validate.notNull(newQuestion);
        Validate.notNull(parentContainerId);

        //if parent does not exist
        Container parentFromRepo = containerRepository.findOne(parentContainerId);
        if(parentFromRepo == null)
            throw new ComponentNotFoundException();

        //adding to parent
        parentFromRepo.addQuestion(newQuestion);
        return questionRepository.saveAndFlush(newQuestion);
    }

    @Override
    public <T extends Question> T editQuestion(T question, Class<T> questionType) {

        Validate.notNull(question);
        Validate.notNull(question.getId());
        Validate.notNull(question.getParent());
        Validate.notNull(question.getParent().getId());

        //if not persisted
        if(!questionRepository.existsByIdAndType(question.getId(), questionType))
            throw new ComponentNotFoundException();

        //if parent does not exist
        Long parentId = question.getParent().getId();
        Container parent = containerRepository.findOne(parentId);
        if(parent == null)
            throw new ComponentNotFoundException();

        //editing question and persisting values
        parent.editQuestion(question);
        return questionRepository.saveAndFlush(question);
    }

    @Override
    public <T extends Question> T getQuestion(Long questionId, Class<T> questionType) {

        Validate.notNull(questionId);
        Validate.notNull(questionType);

        return questionRepository.findByIdAndType(questionId, questionType);
    }

    @Override
    public Question getQuestion(Long questionId) {
        Validate.notNull(questionId);
        return questionRepository.findOne(questionId);
    }

    @Override
    public <T extends Question> T getQuestionForUse(Long questionId, Class<T> questionType) throws ComponentNotFoundException {

        Validate.notNull(questionId);
        Validate.notNull(questionType);

        T question = getQuestion(questionId,questionType);
        if(question == null)
            throw new ComponentNotFoundException();

        return question;
    }

    @Override
    public Question getQuestionForUse(Long questionId) throws ComponentNotFoundException {

        Validate.notNull(questionId);

        Question question = getQuestion(questionId);
        if(question == null)
            throw new ComponentNotFoundException();

        return question;
    }


    @Override
    public void deleteQuestion(Long questionId) {

        Validate.notNull(questionId);

        if(!questionRepository.exists(questionId))
            throw new ComponentNotFoundException();

        questionRepository.delete(questionId);
    }

    @Override
    public Container getParent(Long questionId) {

        Validate.notNull(questionId);

        Question question = getQuestionForUse(questionId);
        return question.getParent();
    }
}

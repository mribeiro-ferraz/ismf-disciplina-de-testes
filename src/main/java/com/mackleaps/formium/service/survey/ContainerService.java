package com.mackleaps.formium.service.survey;

import com.mackleaps.formium.exceptions.ComponentNotFoundException;
import com.mackleaps.formium.model.survey.Category;
import com.mackleaps.formium.model.survey.Container;
import com.mackleaps.formium.model.survey.Question;
import com.mackleaps.formium.model.survey.SurveyComponent;
import com.mackleaps.formium.repository.survey.ContainerRepository;
import com.mackleaps.formium.repository.survey.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ContainerService implements IContainerService {

    private ContainerRepository containerRepository;
    private QuestionRepository questionRepository;

    @Autowired
    public ContainerService (ContainerRepository containerRepository,
                             QuestionRepository questionRepository) {

        this.containerRepository = containerRepository;
        this.questionRepository = questionRepository;

    }

    @Override
    public List<Question> getQuestionsForContainer(Long containerId) {
        Container container = getContainerForUse(containerId);
        return container.getQuestions();
    }

    @Override
    public List<Category> getCategoriesForContainer(Long containerId) {
        Container container = getContainerForUse(containerId);
        return container.getSubcategories();
    }

    @Override
    public <T extends Question> List<T> getQuestionsForContainer(Long containerId, Class<T> questionType) {

        Container container = containerRepository.findOne(containerId);

        List<T> wantedQuestions = new ArrayList<>();
        for(Question currentQuestion : container.getQuestions()) {
            if(currentQuestion.getClass().isAssignableFrom(questionType))
                wantedQuestions.add(questionType.cast(currentQuestion));
        }

        return wantedQuestions;
    }

    @Override
    public List<SurveyComponent> getComponentsForContainer(Long containerId) {
        Container container = getContainerForUse(containerId);
        return container.getAllComponents();
    }

    @Override
    public boolean doesContainerExist(Long containerId) {
        return containerRepository.exists(containerId);
    }

    @Override
    public Container getContainerForUse(Long containerId) {

        Container container = containerRepository.findOne(containerId);
        if(container == null)
            throw new ComponentNotFoundException();

        return container;
    }

    @Override
    public Container getContainer(Long containerId) {
        return containerRepository.findOne(containerId);
    }

    @Override
    public List<Container> pathToSurvey(Long currentContainerId) {

        Container currentContainer = containerRepository.findOne(currentContainerId);

        List<Container> path = currentContainer.pathToSurvey();
        Collections.reverse(path);

        return path;
    }
}

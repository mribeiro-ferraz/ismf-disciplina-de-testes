package com.mackleaps.formium.service.survey;

import com.mackleaps.formium.model.survey.Category;
import com.mackleaps.formium.model.survey.Container;
import com.mackleaps.formium.model.survey.Question;
import com.mackleaps.formium.model.survey.SurveyComponent;

import java.util.List;

public interface IContainerService {

    List<Question> getQuestionsForContainer(Long containerId);
    List<Category> getCategoriesForContainer(Long containerId);
    <T extends Question> List<T> getQuestionsForContainer(Long containerId, Class<T> questionType);
    List<SurveyComponent> getComponentsForContainer(Long containerId);
    boolean doesContainerExist(Long containerId);
    Container getContainerForUse(Long containerId);
    Container getContainer(Long containerId);
    List<Container> pathToSurvey (Long currentContainerId);

}

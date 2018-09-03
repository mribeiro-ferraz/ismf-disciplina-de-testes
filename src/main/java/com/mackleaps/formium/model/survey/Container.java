package com.mackleaps.formium.model.survey;

import com.mackleaps.formium.exceptions.DuplicatedComponentException;
import org.apache.commons.lang3.Validate;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Container extends AuditableEntity {

    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<Category> subcategories;

    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<Question> questions;

    public Container() {
        super();
        subcategories = new ArrayList<>();
        questions = new ArrayList<>();
    }

    /**
     * @return a List of SurveyComponent, which is the union of both subcategories and questions
     */
    public List<SurveyComponent> getAllComponents() {
        List<SurveyComponent> components = new ArrayList<>();
        components.addAll(this.getSubcategories());
        components.addAll(this.getQuestions());
        return components;
    }

    /**
     * @param category to be added to subcategories
     * @throws DuplicatedComponentException in case category cannot be added
     * in current Container because it's not unique
     */
    public void addCategory(Category category) throws DuplicatedComponentException {
        Validate.notNull(category);
        this.addComponent(category);
    }

    /**
     * @param category that is going to edited
     * @throws DuplicatedComponentException if component has the same title as another
     * component's title inside the same collection
     * */
    public void editCategory (Category category) throws DuplicatedComponentException {

        Validate.notNull(category);
        Validate.notNull(category.getId());

        this.editComponent(category);
    }

    /**
     * @param question to be added
     * @throws DuplicatedComponentException if question passed to be added to question is not unique in current Container
     */
    public void addQuestion(Question question) throws DuplicatedComponentException {
        Validate.notNull(question);
        this.addComponent(question);
    }

    /**
     * @param question to be edited
     * @throws DuplicatedComponentException if question passed to be edited is not unique in current Container
     */
    public void editQuestion(Question question) throws DuplicatedComponentException {

        Validate.notNull(question);
        Validate.notNull(question.getId());

        this.editComponent(question);
    }

    /**
     * @param component to be added
     * @param <T> type of the component that is going to be added
     * @throws DuplicatedComponentException in case component is not unique on the list that is going to be added.
     *
     * Corresponding method {@link SurveyComponent#isUniqueComparedTo(SurveyComponent)} is used to define whether there
     * is another equivalent component on appropriated list
     */
    private <T extends SurveyComponent> void addComponent (T component) throws DuplicatedComponentException {

        //list to be used when comparing for uniqueness on container
        List<T> components = (List<T>) (component.isLeaf() ? questions : subcategories);

        //validating uniqueness of component on current Container
        for(SurveyComponent current : components) {
            if(!component.isUniqueComparedTo(current))
                throw new DuplicatedComponentException();
        }

        components.add(component);
        component.associateToParent(this);
    }


    /**
     * @param component that will replace equivalent item on list
     * @param <T> type of <code>component</code>
     * @throws DuplicatedComponentException in case a equal component is found on the list,
     * even though it is not the same entity as another component on equivalent list
     *
     * {@link SurveyComponent#isUniqueComparedTo(SurveyComponent)} to compare elements' attributes
     * {@link SurveyComponent#sameEntity(SurveyComponent)} to compare if they are equivalent entities
     */
    private <T extends SurveyComponent> void editComponent (T component) throws DuplicatedComponentException {

        //list to be used when comparing components
        List <T> components = (List<T>) (component.isLeaf() ? questions : subcategories);

        int currentComponentIndex = -1;

        for(int i = 0; i < components.size(); i++) {

            //found position of component on equivalent list
            if (components.get(i).sameEntity(component))
                currentComponentIndex = i;

            //found a different entity with the same attributes
            else if (!components.get(i).isUniqueComparedTo(component))
                throw new DuplicatedComponentException();

        }

        if(currentComponentIndex == -1)
            throw new IllegalStateException("Component passed to edit is not present in current container");

        //adding component to appropriated list
        components.set(currentComponentIndex,component);
    }

    public <T extends Question> List<T> getQuestionByType(Class<T> questionType) {

        //I hope this makes you proud, @LeoFuso :D
        return questions
                        .stream()
                        .filter(questionType::isInstance)
                        .map(questionType::cast)
                        .collect(Collectors.toList());

    }

    public Survey getSurveyForCurrentContainer () {

        if(this.isSurvey())
            return (Survey) this;

        Category categoryForm = (Category) this;
        return categoryForm.getParent().getSurveyForCurrentContainer();
    }

    public List<Container> pathToSurvey () {
        List<Container> items = new ArrayList<>();
        return pathToSurvey(items);
    }

    public abstract boolean isSurvey();
    public abstract String descriptor();

    private List<Container> addSelfToList(List<Container> currentPath) {
        currentPath.add(this);
        return currentPath;
    }

    private List<Container> pathToSurvey (List<Container> currentPath) {

        addSelfToList(currentPath);

        if(this.isSurvey())
            return currentPath;

        else {
            Category currentCategory = (Category) this;
            return currentCategory.getParent().pathToSurvey(currentPath);
        }
    }

    public boolean canQuestionBeInserted (String header) {

        for(Question question : questions) {
            if(!question.isUniqueComparedTo(header))
                return false;
        }

        return true;
    }

    public boolean canCategoryBeInserted (String title) {

        for(Category category : subcategories) {
            if(!category.isUniqueComparedTo(title))
                return false;
        }

        return true;
    }

    public List<Category> getSubcategories() {
        return this.subcategories;
    }

    public List<Question> getQuestions() {
        return this.questions;
    }

    public void setSubcategories(List<Category> subcategories) {
        this.subcategories = subcategories;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
